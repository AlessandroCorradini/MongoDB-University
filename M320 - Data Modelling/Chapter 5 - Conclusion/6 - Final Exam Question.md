# Final Exam: Question 6

Consider a system that collects census data on all countries in the world.

Which of the following implementations are recommended ways to model a One-to-Zillions relationship between a person entity and the country entity in which that person was born?



- **Referencing from the Zillions side.**

    In each person document, we reference the corresponding country document.

- Referencing from the One side.

    In the document for a country, we have an array of references to the documents of the people who are kept in a separate collection.

- Embedding as an array.

    In the document for a country, we embed all the people born in that country.