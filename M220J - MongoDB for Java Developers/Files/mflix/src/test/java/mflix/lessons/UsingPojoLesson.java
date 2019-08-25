package mflix.lessons;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import mflix.lessons.utils.*;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.ClassModelBuilder;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.PropertyModelBuilder;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@SuppressWarnings("unchecked")
@SpringBootTest
public class UsingPojoLesson extends AbstractLesson {

  private ObjectId actor1Id;
  private ObjectId actor2Id;

  public UsingPojoLesson() {
    super();
  }

  /*
   * In this lesson we are going to discuss the options available to us
   * when reading and parsing through a document using the Java Driver,
   * We will contrast these varying approaches to find the one that
   * suits us best.
   *
   * For the purposes of this lesson we will create an actors collection,
   * that contains documents corresponding to each actor.
   *
   * In our first example we want to create a basic case where the
   * structure of the Actor class mimics a document from the actors
   * collection, so we will call this class ActorBasic.
   * @see ActorBasic
   *
   *
   * Before we start exploring, we need to set up our collection and throw
   * a couple of test documents in there.
   */

  @Before
  public void setUp() throws Exception {

    /*
     * To get a reference to the connection objects used here, check out
     * @see AbstractLesson
     */

    MongoCollection<Document> actors = testDb.getCollection("actors");

    /*
     * Now we can create two document objects and fill in each field
     * value.
     */

    Document actorDocument1 = new Document();
    actorDocument1.append("name", "Bruce Campbell");
    actorDocument1.append(
        "date_of_birth", new SimpleDateFormat("yyyy-MM" + "-dd").parse("1958-06-22"));
    actorDocument1.append("awards", Collections.EMPTY_LIST);
    actorDocument1.append("num_movies", 127);

    Document actorDocument2 = new Document();
    actorDocument2.append("name", "Natalie Portman");
    actorDocument2.append(
        "date_of_birth", new SimpleDateFormat("yyyy-MM" + "-dd").parse("1981-06-09"));
    actorDocument2.append("awards", Collections.EMPTY_LIST);
    actorDocument2.append("num_movies", 63);

    /* Great! We now have two Document Objects with data, that are
     * ready to be written to the actors collection.
     * For that we create an array of documents
     * and append both Bruce and Natalie to that array. Finally we can use
     * the insertMany method to add both documents to the actors database.
     */

    List<Document> listOfActors = new ArrayList<>();
    listOfActors.add(actorDocument1);
    listOfActors.add(actorDocument2);
    actors.insertMany(listOfActors);

    actor1Id = actorDocument1.getObjectId("_id");
    actor2Id = actorDocument2.getObjectId("_id");
  }

  /* The first method that we want to discuss implies a strict one-to-one
   * correspondence between the document field data types and the class
   * properties data types. This method uses Pojo code
   *
   * @http://mongodb.github.io/mongo-java-driver/3.5/bson/pojos/#annotations
   */

  @Test
  public void testReadUsingPojo() {

    /* The first thing that we need here is a pojoCodecRegistry. You can
     * think of the registry as a "factory" of Codecs, where Codecs are
     * an abstraction that determines how BSON data is converted and into
     * what type. There are default and custom codecs, so let's start
     * with a default one and see what it can do for us.
     *
     * The mechanism for POJO support is via the PojoCodecProvider which
     * provides a builder for configuring how and what POJOs to support.
     * The builder allows registering of classes and packages, with a
     * helpful setting that directs the provider to automatically try and
     * handle any POJO it sees.
     *
     * Here we are instantiating a codec registry and telling it to use
     * the default pojo provider, building it with an automatic setting,
     * which uses type introspection
     * @https://en.wikipedia.org/wiki/Type_introspection
     * to determine the properties of the given POJO and accurately apply
     * the default Codec.
     *
     */

    CodecRegistry pojoCodecRegistry =
        fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    // get a hold of our collection, using the codecRegistry, and
    // specifying the ActorBasic class as our POJO for the
    // pojoCodecRegistry.

    MongoCollection<ActorBasic> actors =
        testDb.getCollection("actors", ActorBasic.class).withCodecRegistry(pojoCodecRegistry);

    // create a  query to retrieve a document with a given actor id.
    Bson queryFilter = new Document("_id", actor1Id);

    // use our query to pipe our document into an ActorBasic object in one
    // quick line
    ActorBasic pojoActor = actors.find(queryFilter).iterator().tryNext();

    // make sure this worked and our object isn't empty
    Assert.assertNotNull(pojoActor);

    // let's also assert the type of Object, to make sure that we are
    // indeed creating an ActorBasic
    Assert.assertTrue(pojoActor instanceof ActorBasic);

    // now let's pull the same document from the collection and see if
    // the field values transferred accurately

    MongoCollection<Document> actorsCollectionDoc = testDb.getCollection("actors");

    Document docActor = actorsCollectionDoc.find(queryFilter).iterator().tryNext();

    Assert.assertEquals(docActor.getObjectId("_id"), pojoActor.getId());
    Assert.assertEquals(docActor.get("name"), pojoActor.getName());
    Assert.assertEquals(docActor.get("date_of_birth"), pojoActor.getDateOfBirth());
    Assert.assertEquals(docActor.get("awards"), pojoActor.getAwards());
    Assert.assertEquals(docActor.get("num_movies"), pojoActor.getNumMovies());
  }

  /* The first time we run this test it shouldn't work. Why is that?
   *
   *  Well, our ActorBasic object is the only point of reference our
   *  CodecRegistry has and it seems to me that it is missing some
   *  crucial information.
   *
   *  Let us add @Bson annotations in the ActorBasic class.
   *  These add the field name references as part of our ActorBasic
   *  class, and the CodecRegistry is now able to see the corresponding
   *  structure between the ActorBasic object and the document fields.
   *
   *  This basic case of reading from the database is not always close to
   *  reality. As you have learned in earlier lessons, a field in a
   *  database can have different data types, which need to be taken care
   *  of. Additionally, sometimes your app uses data types that are
   *  different from what is stored in the database.
   *
   *  For this example we will use ActorWithStringId, which as you may have
   *  guessed is an Actor class where the id field is not an ObjectId but a
   *  String instead.
   *
   *  In this case we have several different ways to reach the desired
   *  result. The first option is to write a method that will manually map
   *  each document field to the ActorWithStringId properties.
   *
   * Our fromDocument method helps us with that by mapping a Document
   * object into the ActorWithStringId object and returning the
   * ActorWithStringId object that is filled with new data.
   *
   */

  public ActorWithStringId fromDocument(Document doc) {
    ActorWithStringId actor = new ActorWithStringId(); // create an empty ActorWithStringId object
    actor.setId(doc.getObjectId("_id").toHexString()); // convert to String
    actor.setName(doc.getString("name"));
    actor.setDateOfBirth(doc.getDate("date_of_birth"));
    actor.setAwards((List<Document>) doc.get("awards"));
    actor.setNumMovies(doc.getInteger("num_movies"));
    return actor;
  }

  /* Let us test if our fromDocument mapper works and whether we were
   * able to successfully map values in each field to the values in the
   * ActorWithStringId class.
   */

  @Test
  public void testMappingDocumentsToActorClass() {
    // first we instantiate the collection object
    MongoCollection<Document> actors = testDb.getCollection("actors");
    // then we retrieve one of the documents that we added with our setUp
    // method. We use the find method to query the collection by its
    // unique _id value that we stored earlier. As you know from previous
    // lessons, find returns a cursor, so
    Document actorDocument = actors.find(new Document("_id", actor1Id)).iterator().tryNext();
    // tryNext will iterate over the iterable(cursor) and return the next
    // available Document, if there is no document, then the return value
    // is null.
    Assert.assertNotNull("We should be able to find the actor", actorDocument);
    // Now, we are going to create an ActorWithStringId object by mapping every value
    // in the Document that we just retrieved to the appropriate
    // attribute of the ActorWithStringId class using our helpful fromDocument method.
    ActorWithStringId actor = fromDocument(actorDocument);
    Assert.assertNotNull(actor);
    // to make sure that this was successful lets compare values field by
    // field
    Assert.assertEquals(actor.getId(), actorDocument.get("_id").toString());
    Assert.assertEquals(actor.getName(), actorDocument.getString("name"));
    Assert.assertEquals(actor.getDateOfBirth(), actorDocument.getDate("date_of_birth"));
    Assert.assertEquals(actor.getAwards(), actorDocument.get("awards"));
    Assert.assertEquals(actor.getNumMovies(), actorDocument.get("num_movies"));
  }

  /* Reading seems to work, but if we want to write to the database, we
       * will have to create a method that does the reverse of fromDocument
       * and manages data insertion from Actor Class to Document.
       * /

  /*  The above process doesn't seem too terrible because of our
   * simple use case, but as the complexity and size of our documents
   * grows, this can get more tedious than needed. There are easier
   * approaches to going from Bson to a Java Object, so let's take
   * advantage of the simplicity of our case and see what other options are
   * available to us, namely let's look at custom codec.
   *
   * As you may have noticed from the first example of using pojo and a
   * DefaultCodec, using a Codec allows us to write cleaner code.
   * Theoretically, we could use a Default Codec again, but that would
   * deprive us of the opportunity to have a value assigned to the id field
   * due to a type mismatch. Because now our id field is a String and not
   * an ObjectId.
   *
   * As you know _id is the document primary key, which therefore uniquely
   * identifies a MongoDB document, so it sounds like something we want to
   * have in our ActorWithStringId object as well. As of today, the default
   * codec doesn't automatically set the _id at insert time using PojoCodec.
   * So there is a little more work that needs doing to have a complete
   * transfer of data.
   *
   * To accommodate for the missing _id or any cross type conversions we will
   * use a custom codec, that will help handle our POJO
   * @see mflix.lessons.utils.ActorCodec
   * we provided you with this Codec for our specific use case. Feel free
   * to take a look and see how it works.
   *
   * Now with our Custom Codec in place we can encode a BSON ObjectId
   * straight into the ActorWithStringId String id value among other things.
   */

  @Test
  public void testReadObjectsWithCustomCodec() {

    // first we establish the use of our new custom codec
    ActorCodec actorCodec = new ActorCodec();
    // then create a codec registry with this codec
    CodecRegistry codecRegistry =
        fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromCodecs(actorCodec));
    // we can now access the actors collection with the use of our custom
    // codec that is specifically tailored for the actor documents.
    Bson queryFilter = new Document("_id", actor1Id);
    MongoCollection<ActorWithStringId> customCodecActors =
        testDb.getCollection("actors", ActorWithStringId.class).withCodecRegistry(codecRegistry);
    // we retrieve the first actor document
    ActorWithStringId actor = customCodecActors.find(Filters.eq("_id", actor1Id)).first();
    // and see if it worked!
    Assert.assertEquals(actor1Id.toHexString(), actor.getId());
    // the _id is not empty so that is good, but does it and other values
    // match our expected values?
    MongoCollection<Document> actorsCollectionDoc = testDb.getCollection("actors");
    ActorWithStringId mappedActor =
        fromDocument(actorsCollectionDoc.find(queryFilter).iterator().tryNext());

    Assert.assertNotNull(actor);
    Assert.assertNotNull(mappedActor);
    Assert.assertEquals(mappedActor.getId(), actor.getId());
    Assert.assertEquals(mappedActor.getName(), actor.getName());
    Assert.assertEquals(mappedActor.getDateOfBirth(), actor.getDateOfBirth());
    Assert.assertEquals(mappedActor.getAwards(), actor.getAwards());
    Assert.assertEquals(mappedActor.getNumMovies(), actor.getNumMovies());
    // looks like they do, which is great news.
  }

  /* The question now is: Why use POJO over manually mapping field
   * values into the object attributes? Well, the first reason for me is
   * that it is a more elegant implementation, the second is writing.
   * When you use Pojo with a custom codec, not only do you get to read
   * directly from Bson into your object in a cleaner way, but you also get
   * to write straight from an object into a Bson collection. While you can
   * do this with PojoCode, you will have to sacrifice any type of
   * mismatching between your Class objects and the supported Bson
   * types, like in our _id field case. So let's look at the CustomCodec
   * example below to demonstrate the write.
   */

  @Test
  public void testWriteObjectsWithCustomCodec() {
    // just like before we create a new ActorCodec object, which is our
    // custom codec
    ActorCodec actorCodec = new ActorCodec();
    // we now create a codecRegistry with the custom Codec
    CodecRegistry codecRegistry =
        fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromCodecs(actorCodec));
    // and get the "actors" collection using our new Registry
    MongoCollection<ActorWithStringId> customCodecActors =
        testDb.getCollection("actors", ActorWithStringId.class).withCodecRegistry(codecRegistry);
    // we can now create a new actor and insert it directly into our
    // collection with all required features present.
    ActorWithStringId actorNew = new ActorWithStringId();
    actorNew.setNumMovies(2);
    actorNew.setName("Norberto");
    customCodecActors.insertOne(actorNew);
    Assert.assertNotNull(actorNew.getId());
  }

  /* Finally, the last method that we wanted to show you, is the one that
   * allows us to use the default CodecRegistry while customizing the
   * fields that we know need special treatment. For example, if you have
   * a field that would need type conversion, or has various different
   * data types in it. This needs to be addressed when the documents are
   * read into your Java application, here is another approach to handling
   * it.
   *
   * In this approach we use the Default Codec, while applying a custom
   * approach to the field in question, which in our case is the id field.
   *
   * @see StringObjectIdCodec
   *
   *
   */

  @Test
  public void testReadObjectWithCustomFieldCodec() {
    // select a class that will be used as our POJO
    ClassModelBuilder<ActorWithStringId> classModelBuilder =
        ClassModel.builder(ActorWithStringId.class);
    // get the property that needs type conversion
    PropertyModelBuilder<String> idPropertyModelBuilder =
        (PropertyModelBuilder<String>) classModelBuilder.getProperty("id");
    // apply type conversion to the property of interest
    // StringObjectIdCodec describes specifically how to encode and decode
    // the ObjectId into a String and vice-versa.
    idPropertyModelBuilder.codec(new StringObjectIdCodec());
    // use the default CodecRegistry, with the changes implemented above
    // through registering the classModelBuilder with the PojoCodecProvider
    CodecRegistry stringIdCodecRegistry =
        fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(
                PojoCodecProvider.builder()
                    .register(classModelBuilder.build())
                    .automatic(true)
                    .build()));
    // we're done! Lets test if it worked! let us get the actors collection
    MongoCollection<ActorWithStringId> actors =
        testDb
            .getCollection("actors", ActorWithStringId.class)
            .withCodecRegistry(stringIdCodecRegistry);
    // build a search query
    Bson queryFilter = new Document("_id", actor1Id);
    // find the actor that we want to test for
    ActorWithStringId stringIdActor = actors.find(queryFilter).iterator().tryNext();
    // make sure we have a non empty object
    Assert.assertNotNull(stringIdActor);
    // make sure we have an instance of ActorWithStringId
    Assert.assertTrue(stringIdActor instanceof ActorWithStringId);
    // the id values match
    Assert.assertEquals(actor1Id.toHexString(), stringIdActor.getId());
    // now we can compare values across two different methods like we did
    // in our earlier test cases
    MongoCollection<Document> actorsCollectionDoc = testDb.getCollection("actors");
    ActorWithStringId mappedActor =
        fromDocument(actorsCollectionDoc.find(queryFilter).iterator().tryNext());

    Assert.assertNotNull(mappedActor);
    Assert.assertEquals(mappedActor.getId(), stringIdActor.getId());
    Assert.assertEquals(mappedActor.getName(), stringIdActor.getName());
    Assert.assertEquals(mappedActor.getDateOfBirth(), stringIdActor.getDateOfBirth());
    Assert.assertEquals(mappedActor.getAwards(), stringIdActor.getAwards());
    Assert.assertEquals(mappedActor.getNumMovies(), stringIdActor.getNumMovies());
    // looks like they do, which is great news.

  }

  @After
  public void tearDown() {
    testDb.getCollection("actors").drop();
    actor1Id = null;
    actor2Id = null;
  }

  /* Let us now recap the advantages and disadvantages of the methods
   * discussed in today's lesson
   *
   * 1. The Pojo implementation is much cleaner, it outsources translation
   * between BSON and object to a custom codec which makes it easier to
   * manage and maintain the code.
   *
   * 2. In either approach the field names don't have to match the object
   * attribute name. In case of the custom Codec this is easy to
   * accommodate in the code, via the @BsonProperty annotation only for the
   * fields that don't correspond to the attribute name one-to-one.
   *
   * 3. To map the Doc to an object manually you have to make use of
   * getters and setters, while using a POJO allows for automatic
   * introspection (a.k.a automatic process of analyzing a bean's design
   * patterns to reveal the bean's properties, events, and methods).
   *
   * 4. Handling of generics and subdocuments is cleaner and easier to
   * maintain with POJO, utilizing the Custom Codec or the field
   * customization approach instead of writing separate
   * methods to traverse sub-documents with getters and setters.
   *
   * 5. Finally, when writing a Document object to the DB, the document _id is
   * automatically generated and can be accessed in the Document object for
   * the subsequent use in the mapping method.
   *
   * This is the end of your Using Pojo Lesson.
   *
   * Enjoy your the next one!
   *
   *
   */

}
