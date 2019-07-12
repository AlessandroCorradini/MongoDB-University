db.movies_initial.aggregate(
[
    {
        '$limit': 5
    },
    {'$addFields': {
        'lastupdated': {
            '$arrayElemAt': [
                {'$split': ["$lastupdated", "."]},
                0]}}
    },
    {'$project': {
        'title': 1,
        'year': 1,
        'directors': {'$split': ["$director", ", "]},
        'cast': {'$split': ["$cast", ", "]},
        'writers': {'$split': ["$writer", ", "]},
        'genres': {'$split': ["$genre", ", "]},
        'languages': {'$split': ["$language", ", "]},
        'countries': {'$split': ["$country", ", "]},
        'plot': 1,
        'fullPlot': "$fullplot",
        'rated': "$rating",
        'runtime': 1,
        'poster': 1,
        'imdb': {
            'id': "$imdbID",
            'rating': "$imdbRating",
            'votes': "$imdbVotes"},
        'metacritic': 1,
        'awards': 1,
        'type': 1,
        'released': {
            '$cond': {
                'if': {'$ne': ["$released", ""]},
                'then': {
                    '$dateFromString': {
                        'dateString': "$released"}},
                'else': ""}},
        'lastUpdated': {
            '$cond': {
                'if': {'$ne': ["$lastupdated", ""]},
                'then': {
                    '$dateFromString': {
                        'dateString': "$lastupdated",
                        'timezone': "America/New_York"
                    }
                },
                'else': ""
            }
        }
    }},
    {'$project': {
        'o': {
            '$filter': {
                'input': {'$objectToArray': "$$ROOT"},
                'as': "field",
                'cond': {'$and': [
                    {'$ne': ["$$field.v", ""]},
                    {'$ne': ["$$field.v", [""]]}
                ]}}}
    }},
    {'$replaceRoot': {
        'newRoot': {'$arrayToObject': "$o"}
    }},
    {
        '$out': "movies_demo"
    }
]);
