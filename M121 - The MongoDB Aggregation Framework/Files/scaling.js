// general scaling
min + (max - min) * ((x - x_min) / (x_max - x_min))

// we will use 1 as the minimum value and 10 as the maximum value for scaling,
// so all scaled votes will fall into the range [1,10]

scaled_votes = 1 + 9 * ((x - x_min) / (x_max - x_min))

// NOTE: We CANNOT simply do 10 * ((x - x_min))..., results will be wrong
// Order of operations is important!

// use these values for scaling imdb.votes
x_max = 1521105
x_min = 5
min = 1
max = 10
x = imdb.votes

// within a pipeline, it should look something like the following
/*
  {
    $add: [
      1,
      {
        $multiply: [
          9,
          {
            $divide: [
              { $subtract: [<x>, <x_min>] },
              { $subtract: [<x_max>, <x_min>] }
            ]
          }
        ]
      }
    ]
  }
*/

// given we have the numbers, this is how to calculated normalized_rating
// yes, you can use $avg in $project and $addFields!
normalized_rating = average(scaled_votes, imdb.rating)
