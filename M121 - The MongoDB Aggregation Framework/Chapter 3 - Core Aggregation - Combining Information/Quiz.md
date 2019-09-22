# Quiz

## The $lookup Stage

Which of the following statements is true about the $lookup stage?

- **Specifying an existing field name to as will overwrite the the existing field**
- You can specify a collection in another database to from
- **$lookup matches between localField and foreignField with an equality match**
- **The collection specified in from cannot be sharded**

## $graphLookup Introduction

Which of the following statements apply to $graphLookup operator? check all that apply

- $lookup and $graphLookup stages require the exact same fields in their specification.
- **$graphLookup provides MongoDB a transitive closure implementation**
- $graphLookup is a new stage of the aggregation pipeline introduced in MongoDB 3.2
- **Provides MongoDB with graph or graph-like capabilities**
- $graphLookup depends on $lookup operator. Cannot be used without $lookup

## $graphLookup: Simple Lookup

Which of the following statements is/are correct? Check all that apply.

- as determines a collection where $graphLookup will store the stage results
- **connectToField will be used on recursive find operations**
- startWith indicates the index that should be use to execute the recursive match
- **connectFromField value will be use to match connectToField in a recursive match**

## $graphLookup: maxDepth and depthField

Which of the following statements are incorrect? Check all that apply

- depthField determines a field, in the result document, which specifies the number of recursive lookup needed to reach that document
- **maxDepth only takes $long values**
- maxDepth allows to specify the number of recursive lookups
- **depthField determines a field, which contains the value number of documents matched by the recursive lookup**

## $graphLookup: General Considerations

Consider the following statement:

```
``$graphLookup`` is required to be the last element on the pipeline.
```

Which of the following is true about the statement?

- This is correct because of the recursive nature of $graphLookup we want to save resources for last.
- This is incorrect. graphLookup needs to be the first element of the pipeline, regardless of other stages needed to perform the desired query.
- **This is incorrect. $graphLookup can be used in any position of the pipeline and acts in the same way as a regular $lookup.**
- This is correct because $graphLookup pipes out the results of recursive search into a collection, similar to $out stage.