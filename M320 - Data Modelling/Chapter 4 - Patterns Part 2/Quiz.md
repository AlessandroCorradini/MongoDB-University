# Quiz

## Computed Pattern

Which one of the following scenarios is best suited for the application of the Computed Pattern?

- We need to group documents and sum on a field.
- We need to calculate a value that is displayed once per minute and is based on a field which updates 100 times per minute.
- **We need to calculate a value that is displayed 100 times a minute and is based on a field which updates once a minute.**
- We need to calculate a value that is displayed 100 times a minute and is based on a field which updates 100 times per minute.
- We have too much information to store in a single document.

## Bucket Pattern

Which one of the following requirements in our system is the best candidate to use the Bucket Pattern?

- Our system handles 1 million IOT devices.
- Our system must embed a one-to-many relationship in one of our models, however, some of the result documents would be too big.
- **Our system ingests thousands of log lines each day for each host it monitors.**
- Our system performs sums and averages over all elements of certain arrays.
- Our system ingests 10 million pieces of data per day from 1 million devices, with 20% coming from 10 devices.

## Schema Versioning Pattern

Which one of the following scenarios is the best candidate for the use of the Schema Versioning Pattern?

- **I want to avoid downtime when upgrading my schema.**
- I can schedule a window of downtime long enough to migrate the documents to the new version.
- I want to keep track of the changes to my documents.
- I have billions of documents.
- I have many obsolete documents.

## Tree Patterns

Which of the following scenarios would be ideal to use the Tree Pattern?

- **Company organization charts**
- **Product categories**
- Contact lists of users

## Polymorphic Pattern

Which one of the following scenarios is best suited for the use of the Polymorphic Pattern?

- **The organization acquired different companies over the years, serving the same markets with the same customers and there is a requirement to merge all systems into one.**
- There is a requirement to keep many versions of a document, and these versions may have different fields for each version.
- The application has a base class with some derived classes.
- There are billions of documents.
- There is a requirement to store addresses for my customers.

## Other Patterns

A pharmaceutical company needs to implement a design to model the relationships between the company and its partners, customers, and suppliers. A team came up with a design that works perfectly, meeting all performance requirements. However, when loading the real data in the system, they realized that one customer, the U.S. government had too many contacts to be stored into the designated array for this information.

Instead of redesigning the whole system to make this customer fit well into the new data model, which pattern can you use?

- The Schema Versioning Pattern.
- The Pharmaceutical Pattern.
- **The Outlier Pattern.**
- The Attribute Pattern.
- The Subset Pattern.
