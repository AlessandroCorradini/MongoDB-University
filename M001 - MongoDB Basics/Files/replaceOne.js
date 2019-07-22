let filter = {title: "House, M.D., Season Four: New Beginnings"}

let doc = db.movieDetails.findOne(filter);

doc.poster;

doc.poster = "https://www.imdb.com/title/tt1329164/mediaviewer/rm2619416576";

doc.genres;

doc.genres.push("TV Series");

db.movieDetails.replaceOne(filter, doc);
