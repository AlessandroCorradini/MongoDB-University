# Quiz

## Given the following MongoDB URI: 

```
mongodb+srv://brigitte:bardot@xyz-1234.srv.net/admin
```

which of the following statements is true ?



- There are 1234 nodes in the xyz Replica Set
- **The password used to authenticate is bardot**
- The server xyz-1234.srv.net is a member of the Replica Set the user is trying to connect to
- The user bardot is trying to authenticate on database admin

## MongoClient

Which of the following class names are part of the basic MongoDB Java driver objects ?



- **MongoCollection**
- **Document**
- **MongoClient**
- Database

## Driver Connection

What utility does the MongoClients class provide?



- **A builder method to generate a MongoClient instance.**
- An automatic factory of configuration settings to connect to our server.
- A List of MongoClient instances that are ready to be used

## Compass Basics

Use Compass to find which film title in the movies collection is 31st result document of all films that came out in 1991, when sorted by title in order 1 (ascending).

To complete this task check out the $skip option for your pipeline.



- "Bad Karma"
- "Autobus"
- **"Cape Fear"**
- "100 Days"

## Query Builders

Select the true statements below



- Filters and Projections are the only available Builders classes.
- **You can combine Filters to compose robust queries.**
- **Projections offers a convenience method, excludeId(), to remove the _id field in a projection.**
- Filters and Projections do not offer full query and projection functionality.

## Basic Reads

Which of the following statements is true regarding reads in MongoDB?



- **Field projection is specified as a JSON object.**
- The MongoDB Javascript driver can only find one document per query.
- **We must explicitly remove the _id field in a projection if we do not want to include this field.**

## Using POJO - Part 2

Imagine you have a date field in the Document that contains a Date data type in it and a Java Object where the date property is a String type. How would you read this Document into the Java Object while gaining the ability to also write to the Document form the Java Object.



- Using a POJO in conjunction with a Default Codec
- **Using a POJO in conjunction with a Custom Codec**
- **Using a POJO with a Default Codec and a custom field type conversion script**
- Using a manual mapping method
