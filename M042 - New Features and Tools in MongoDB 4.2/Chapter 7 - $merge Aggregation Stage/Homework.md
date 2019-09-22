# Homework

## Homework: Using $merge

Consider a potential $merge stage that:

outputs results to a collection called analytics.
merges the results of the $merge stage with current analytics documents using the value of the name field.
updates existing analytics documents to include any modified information from the resulting $merge documents.
creates a new analytics document if an existing document with the resulting document's name does not exist.
Which of the following $merge stages will perform all of the above functionality?



- 
    ```
    {
    $merge: {
        into: "analytics"
    }
    }
    ```
- 
    ```
    {
    $merge: {
        into: "analytics",
        on: "name",
        whenNotMatched: "fail"
    }
    }
    ```
- [X]
    ```
    {
    $merge: {
        into: "analytics",
        on: "name",
        whenMatched: "merge",
        whenNotMatched: "insert"
    }
    }
    ```
- 
    ```
    {
    $merge: {
        on: "name"
    }
    }
    ```
- 
    ```
    {
    $merge: {
        into: "analytics",
        on: "name",
        whenMatched: "keepExisting",
        whenNotMatched: "insert"
    }
    }
    ```