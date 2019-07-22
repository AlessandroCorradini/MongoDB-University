db.movieDetails.find({genres: {$all: ["Comedy", "Crime", "Drama"]}}, 
                     {_id: 0, title: 1, genres: 1}).pretty()


