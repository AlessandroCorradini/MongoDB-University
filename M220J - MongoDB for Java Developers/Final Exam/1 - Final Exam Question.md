# Final: Question 1

Assume a collection called elections that holds data about all United States Presidential Elections since 1789. All the documents in the elections collection look like this:

```
{
  "year" : 1828,
  "winner" : "Andrew Jackson",
  "winner_running_mate" : "John C. Calhoun",
  "winner_party" : "Democratic",
  "winner_electoral_votes" : 178,
  "total_electoral_votes" : 261
}
```

total_electoral_votes represents the total number of electoral votes that year, and winner_electoral_votes represents the number of electoral votes received by the winning candidates.

Which of the following queries will retrieve all the Republican winners with at least 160 electoral votes?



 - 
    ```
    Bson query = and(eq("winner_party", "Republican"),
                 lt("winner_electoral_votes", 160));
    ```
 - 
    ```
   Bson query = and(eq("winner_party", "Republican"),
                 lte("winner_electoral_votes", 160));
    ```
 - [X]
    ```
   Bson query = and(eq("winner_party", "Republican"),
                 gte("winner_electoral_votes", 160));
    ```
 - 
    ```
   Bson query5 = and(gte("total_electoral_votes", 160),
                  eq("winner_party", "Republican"));
    ```
 - 
    ```
   Bson query = gte("winner_electoral_votes", 160);
    ```
