# Quiz

## Relationship Types and Cardinality

Why did we introduce the one-to-zillions relationship in our modeling notation?

- **To highlight the fact that huge cardinalities may impact design choices.**
- To address the fact that a crow's foot has 5 fingers, not 3.
- **To address the fact that the concept of relationship linked to a huge number of entitites is missing in normal crow's foot notation.**

## One-to-Many Relationship

Consider a one-to-many relationship observed between a county and the cities in that county.

Which of the following are valid ways to represent this one-to-many relationship with the document model in MongoDB?

- **Embed the entities for the cities as an array of sub-documents in the corresponding county document.**
- Embed all the fields for a city as a subdocument in the corresponding county document.
- **Have a collection for the counties and a collection for the cities with each city document having a field to reference the document of its county.**

## Many-to-Many Relationship

Consider a many-to-many relationship observed between movies and the actors starring in these movies, for a system that could provide detailed information about either a movie or an actor.

![](https://university-courses.s3.amazonaws.com/M320/rst-images-prob_movies_actors.png)

Which of the following are true about modeling this many-to-many relationship with the document model in MongoDB?

- **Embedding actors in movies creates duplication of actor information.**
- When using one collection for movies and one collection for actors, all movie documents must have an array of references to the actors in that movie, and all actor documents must have an array of references to the movies they appear in.
- **Embedding actors in movies still requires a separate collection to store all actors.**

## One-to-One Relationship

Which of the following are valid ways to represent a one-to-one relationship with the document model in MongoDB?

- **Embed the fields in the document.**
- **Link to a single document in another collection.**
- **Embed the fields as a sub-document in the document.**

## One-to-Zillions Relationship

Which of the following statements are true about one-to-zillions relationships?

- **The relationship representations that embed documents are not recommended.**
- **It is a special case of the one-to-many relationship.**
- **We must take extra care when writing queries that retrieve data on the zillions side.**
