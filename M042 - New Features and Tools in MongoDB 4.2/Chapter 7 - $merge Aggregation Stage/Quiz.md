# Quiz

## $merge Overview

In MongoDB 4.2, the $merge Aggregation stage:



- **can output to a collection in the same or different database.**
- **can merge documents from an Aggregation and a target collection.**
- **can output to a sharded collection.**

## $merge Syntax

Consider an Aggregation Pipeline using the new $merge stage that outputs to the employee_data collection.

If we are not expecting to find any matching documents in the employee_data collection, which of the following stages should we use?



- 
    ```
    {
    $merge: {
        into: "employee_data",
        whenNotMatched: "fail",
        whenMatched: "replace"
    }
    }
    ```
- 
    ```
    {
    $merge: {
        into: "employee_data",
        whenNotMatched: "discard",
        whenMatched: "replace"
    }
    }
    ```
- [X]
    ```
    {
    $merge: {
        into: "employee_data",
        whenNotMatched: "insert",
        whenMatched: "fail"
    }
    }
    ```
- 
    ```
    {
    $merge: {
        into: "employee_data",
        whenNotMatched: "fail",
        whenMatched: "merge"
    }
    }
    ```
- 
    ```
    {
    $merge: {
        into: "employee_data",
        whenNotMatched: "insert",
        whenMatched: "merge"
    }
    }
    ```