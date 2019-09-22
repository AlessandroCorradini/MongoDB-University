# Quiz

## The $out Stage

Which of the following statements is true regarding the $out stage?

- If a pipeline with $out errors, you must delete the collection specified to the $out stage.
- **$out will overwrite an existing collection if specified.**
- $out removes all indexes when it overwrites a collection.
- Using $out within many sub-piplines of a $facet stage is a quick way to generate many differently shaped collections.

## Views

Which of the following statements are true regarding MongoDB Views?

- Inserting data into a view is slow because MongoDB must perform the pipeline in reverse.
- A view cannot be created that contains both horizontal and vertical slices.
- Views should be used cautiously because the documents they contain can grow incredibly large.
- **View performance can be increased by creating the appropriate indexes on the source collection.**
