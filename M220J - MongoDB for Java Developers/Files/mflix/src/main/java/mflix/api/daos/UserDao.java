package mflix.api.daos;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import mflix.api.models.Session;
import mflix.api.models.User;

import org.bson.BSON;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.awt.List;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

@Configuration
public class UserDao extends AbstractMFlixDao {

  private final MongoCollection<User> usersCollection;

  private final MongoCollection<Session> sessionsCollection;

  private CodecRegistry pojoCodecRegistry;

  private final Logger log;

  @Autowired
  public UserDao(
      MongoClient mongoClient, @Value("${spring.mongodb.database}") String databaseName) {
    super(mongoClient, databaseName);
    log = LoggerFactory.getLogger(this.getClass());
    this.db = this.mongoClient.getDatabase(MFLIX_DATABASE);
    this.pojoCodecRegistry =
        fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    usersCollection = db.getCollection("users", User.class).withCodecRegistry(pojoCodecRegistry);
    
    sessionsCollection = db.getCollection("sessions", Session.class).withCodecRegistry(pojoCodecRegistry);
  }

  /**
   * Inserts the `user` object in the `users` collection.
   *
   * @param user - User object to be added
   * @return True if successful, throw IncorrectDaoOperation otherwise
   */
  public boolean addUser(User user) {
    try {
      usersCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(user);
    } catch (MongoException e) {
      log.error("An error ocurred while trying to insert a User.");
      if (ErrorCategory.fromErrorCode( e.getCode() ) == ErrorCategory.DUPLICATE_KEY) {
        throw new IncorrectDaoOperation("The User is already in the database.");
      }
      return false;
    }
    
    return true;
    //TODO > Ticket: Handling Errors - make sure to only add new users
    // and not users that already exist.

  }

  /**
   * Creates session using userId and jwt token.
   *
   * @param userId - user string identifier
   * @param jwt - jwt string token
   * @return true if successful
   */
  public boolean createUserSession(String userId, String jwt) {
    Session session = new Session();
    session.setUserId(userId);
    session.setJwt(jwt);

    try {
      if (Optional.ofNullable(sessionsCollection.find( eq("user_id", userId) ).first()).isPresent()) {
        sessionsCollection.updateOne(eq("user_id", userId), set("jwt", jwt));
      } else {
        sessionsCollection.insertOne(session);  
      }
    } catch (MongoException e) {
      log.error("An error ocurred while trying to insert/update a Session.");
      return false;
    }

    return true;
    //TODO > Ticket: Handling Errors - implement a safeguard against
    // creating a session with the same jwt token.
  }

  /**
   * Returns the User object matching the an email string value.
   *
   * @param email - email string to be matched.
   * @return User object or null.
   */
  public User getUser(String email) {
    User user = new User();
    user = usersCollection.find(eq("email", email)).first();
    return user;
  }

  /**
   * Given the userId, returns a Session object.
   *
   * @param userId - user string identifier.
   * @return Session object or null.
   */
  public Session getUserSession(String userId) {
    return sessionsCollection.find(eq("user_id", userId)).first();
  }

  public boolean deleteUserSessions(String userId) {
    sessionsCollection.deleteMany(eq("user_id", userId));
    return true;
  }

  /**
   * Removes the user document that match the provided email.
   *
   * @param email - of the user to be deleted.
   * @return true if user successfully removed
   */
  public boolean deleteUser(String email) {
    // remove user sessions
    //TODO > Ticket: Handling Errors - make this method more robust by
    // handling potential exceptions.
    sessionsCollection.deleteMany(eq("user_id", email));
    usersCollection.deleteMany(eq("email", email));
    
    try {
      sessionsCollection.deleteMany(eq("user_id", email));
      usersCollection.deleteMany(eq("email", email));
    } catch (MongoException e) {
      log.error("An error ocurred while trying to delete a User.");
      return false;
    }
    
    return true;
  }

  /**
   * Updates the preferences of an user identified by `email` parameter.
   *
   * @param email - user to be updated email
   * @param userPreferences - set of preferences that should be stored and replace the existing
   *     ones. Cannot be set to null value
   * @return User object that just been updated.
   */
  public boolean updateUserPreferences(String email, Map<String, ?> userPreferences) {
    //TODO > Ticket: Handling Errors - make this method more robust by
    // handling potential exceptions when updating an entry.

    try {
      usersCollection
      .updateOne( 
        eq("email", email), 
        set("preferences", 
          Optional.ofNullable(userPreferences).orElseThrow( () -> 
            new IncorrectDaoOperation("user preferences cannot be null") ) ) );
    } catch (MongoException e) {
      log.error("An error ocurred while trying to update User preferences.");
      return false;
    }

    return true;
  }
}
