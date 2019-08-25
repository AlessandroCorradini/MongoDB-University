import { ObjectId } from "bson"

let movies
let mflix
const DEFAULT_SORT = [["tomatoes.viewer.numReviews", -1]]

export default class MoviesDAO {
  static async injectDB(conn) {
    if (movies) {
      return
    }
    try {
      mflix = await conn.db(process.env.MFLIX_NS)
      movies = await conn.db(process.env.MFLIX_NS).collection("movies")
      this.movies = movies // this is only for testing
    } catch (e) {
      console.error(
        `Unable to establish a collection handle in moviesDAO: ${e}`,
      )
    }
  }

  /**
   * Retrieves the connection pool size, write concern and user roles on the
   * current client.
   * @returns {Promise<ConfigurationResult>} An object with configuration details.
   */
  static async getConfiguration() {
    const roleInfo = await mflix.command({ connectionStatus: 1 })
    const authInfo = roleInfo.authInfo.authenticatedUserRoles[0]
    const { poolSize, wtimeout } = movies.s.db.serverConfig.s.options
    let response = {
      poolSize,
      wtimeout,
      authInfo,
    }
    return response
  }

  /**
   * Finds and returns movies originating from one or more countries.
   * Returns a list of objects, each object contains a title and an _id.
   * @param {string[]} countries - The list of countries.
   * @returns {Promise<CountryResult>} A promise that will resolve to a list of CountryResults.
   */
  static async getMoviesByCountry(countries) {
    /**
    Ticket: Projection

    Write a query that matches movies with the countries in the "countries"
    list, but only returns the title and _id of each movie.

    Remember that in MongoDB, the $in operator can be used with a list to
    match one or more values of a specific field.
    */

    let cursor
    const query = { countries: { $in: countries } }
    const projection = { title: 1 } // _id is returned by default unless explicitly excluded
    const opts = { projection }

    try {
      // Find movies matching the "countries" list, but only return the title
      // and _id. Do not put a limit in your own implementation, the limit
      // here is only included to avoid sending 46000 documents down the
      // wire.
      cursor = await movies.find(query, opts)
    } catch (e) {
      console.error(`Unable to issue find command, ${e}`)
      return []
    }

    return cursor.toArray()
  }

  /**
   * Finds and returns movies matching a given text in their title or description.
   * @param {string} text - The text to match with.
   * @returns {QueryParams} The QueryParams for text search
   */
  static textSearchQuery(text) {
    const query = { $text: { $search: text } }
    const meta_score = { $meta: "textScore" }
    const sort = [["score", meta_score]]
    const project = { score: meta_score }

    return { query, project, sort }
  }

  /**
   * Finds and returns movies including one or more cast members.
   * @param {string[]} cast - The cast members to match with.
   * @returns {QueryParams} The QueryParams for cast search
   */
  static castSearchQuery(cast) {
    const searchCast = Array.isArray(cast) ? cast : Array(cast)

    const query = { cast: { $in: searchCast } }
    const project = {}
    const sort = DEFAULT_SORT

    return { query, project, sort }
  }

  /**
   * Finds and returns movies matching a one or more genres.
   * @param {string[]} genre - The genres to match with.
   * @returns {QueryParams} The QueryParams for genre search
   */
  static genreSearchQuery(genre) {
    /**
    Ticket: Text and Subfield Search

    Given an array of one or more genres, construct a query that searches
    MongoDB for movies with that genre.
    */

    const searchGenre = Array.isArray(genre) ? genre : Array(genre)

    // TODO Ticket: Text and Subfield Search
    // Construct a query that will search for the chosen genre.
    const query = { genres: { $in: searchGenre } }
    const project = {}
    const sort = DEFAULT_SORT

    return { query, project, sort }
  }

  /**
   *
   * @param {Object} filters - The search parameter to use in the query. Comes
   * in the form of `{cast: { $in: [...castMembers]}}`
   * @param {number} page - The page of movies to retrieve.
   * @param {number} moviesPerPage - The number of movies to display per page.
   * @returns {FacetedSearchReturn} FacetedSearchReturn
   */
  static async facetedSearch({
    filters = null,
    page = 0,
    moviesPerPage = 20,
  } = {}) {
    if (!filters || !filters.cast) {
      throw new Error("Must specify cast members to filter by.")
    }
    const matchStage = { $match: filters }
    const sortStage = { $sort: { "tomatoes.viewer.rating": -1 } }
    const countingPipeline = [matchStage, sortStage, { $count: "count" }]
    const skipStage = { $skip: moviesPerPage * page }
    const limitStage = { $limit: moviesPerPage }
    const facetStage = {
      $facet: {
        runtime: [
          {
            $bucket: {
              groupBy: "$runtime",
              boundaries: [0, 60, 90, 120, 180],
              default: "other",
              output: {
                count: { $sum: 1 },
              },
            },
          },
        ],
        rating: [
          {
            $bucket: {
              groupBy: "$metacritic",
              boundaries: [0, 50, 70, 90, 100],
              default: "other",
              output: {
                count: { $sum: 1 },
              },
            },
          },
        ],
        movies: [
          {
            $addFields: {
              title: "$title",
            },
          },
        ],
      },
    }

    /**
    Ticket: Faceted Search

    Please append the skipStage, limitStage, and facetStage to the queryPipeline
    (in that order). You can accomplish this by adding the stages directly to
    the queryPipeline.

    The queryPipeline is a Javascript array, so you can use push() or concat()
    to complete this task, but you might have to do something about `const`.
    */

    let queryPipeline = [
      matchStage,
      sortStage,
      // TODO Ticket: Faceted Search
      // Add the stages to queryPipeline in the correct order.
    ]
    // Clearly we could have just added these to the original const queryPipeline.
    queryPipeline.push(skipStage)
    queryPipeline.push(limitStage)
    queryPipeline.push(facetStage)

    try {
      const results = await (await movies.aggregate(queryPipeline)).next()
      const count = await (await movies.aggregate(countingPipeline)).next()
      return {
        ...results,
        ...count,
      }
    } catch (e) {
      return { error: "Results too large, be more restrictive in filter" }
    }
  }

  /**
   * Finds and returns movies by country.
   * Returns a list of objects, each object contains a title and an _id.
   * @param {Object} filters - The search parameters to use in the query.
   * @param {number} page - The page of movies to retrieve.
   * @param {number} moviesPerPage - The number of movies to display per page.
   * @returns {GetMoviesResult} An object with movie results and total results
   * that would match this query
   */
  static async getMovies({
    // here's where the default parameters are set for the getMovies method
    filters = null,
    page = 0,
    moviesPerPage = 20,
  } = {}) {
    let queryParams = {}
    if (filters) {
      if ("text" in filters) {
        queryParams = this.textSearchQuery(filters["text"])
      } else if ("cast" in filters) {
        queryParams = this.castSearchQuery(filters["cast"])
      } else if ("genre" in filters) {
        queryParams = this.genreSearchQuery(filters["genre"])
      }
    }

    let { query = {}, project = {}, sort = DEFAULT_SORT } = queryParams
    let cursor
    try {
      cursor = await movies
        .find(query)
        .project(project)
        .sort(sort)
    } catch (e) {
      console.error(`Unable to issue find command, ${e}`)
      return { moviesList: [], totalNumMovies: 0 }
    }

    /**
    Ticket: Paging

    Before this method returns back to the API, use the "moviesPerPage" and
    "page" arguments to decide the movies to display.

    Paging can be implemented by using the skip() and limit() cursor methods.
    */

    // TODO Ticket: Paging
    const MOVIES_TO_SKIP = page * moviesPerPage // Skip over movies from previous pages
    // Use the cursor to only return the movies that belong on the current page
    const displayCursor = cursor.skip(MOVIES_TO_SKIP).limit(moviesPerPage)

    try {
      const moviesList = await displayCursor.toArray()
      const totalNumMovies = page === 0 ? await movies.countDocuments(query) : 0

      return { moviesList, totalNumMovies }
    } catch (e) {
      console.error(
        `Unable to convert cursor to array or problem counting documents, ${e}`,
      )
      return { moviesList: [], totalNumMovies: 0 }
    }
  }

  /**
   * Gets a movie by its id
   * @param {string} id - The desired movie id, the _id in Mongo
   * @returns {MflixMovie | null} Returns either a single movie or nothing
   */
  static async getMovieByID(id) {
    try {
      /**
      Ticket: Get Comments

      Given a movie ID, build an Aggregation Pipeline to retrieve the comments
      matching that movie's ID.

      The $match stage is already completed. You will need to add a $lookup
      stage that searches the `comments` collection for the correct comments.
      */

      // TODO Ticket: Get Comments
      /*
        See https://docs.mongodb.com/manual/reference/operator/aggregation/lookup/#join-conditions-and-uncorrelated-sub-queries:

          {
            $lookup:
              {
                from: <collection to join>,
                let: { <var_1>: <expression>, …, <var_n>: <expression> },
                pipeline: [ <pipeline to execute on the collection to join> ],
                as: <output array field>
              }
          }

      */
      // Implement the required pipeline.
      const pipeline = [
        {
          // Find the _id from our movies collection
          $match: {
            _id: ObjectId(id),
          },
        },
        {
          $lookup: {
            from: "comments", // We want to find all comments that have the same ID as our movie
            let: { id: "$_id" }, // Create a local variable "id" which is set to our original "_id" variable in our $match stage
            pipeline: [
              {
                // Find all documents in the comments collection where the movie_id matches our local variable "id"
                $match: {
                  $expr: {
                    $eq: ["$movie_id", "$$id"],
                  },
                },
              },
              {
                // Sort the comments so that the most recent ones are first (descending order)
                $sort: {
                  date: -1,
                },
              },
            ],
            as: "comments",
          },
        },
      ]
      return await movies.aggregate(pipeline).next()
    } catch (e) {
      /**
      Ticket: Error Handling

      Handle the error that occurs when an invalid ID is passed to this method.
      When this specific error is thrown, the method should return `null`.
      */

      // TODO Ticket: Error Handling
      // Catch the InvalidId error by string matching, and then handle it.
      if ((e.MongoError = "E11000 duplicate key error collection")) {
        return null
      }
      console.error(`Something went wrong in getMovieByID: ${e}`)
      throw e
    }
  }
}

/**
 * This is a parsed query, sort, and project bundle.
 * @typedef QueryParams
 * @property {Object} query - The specified query, transformed accordingly
 * @property {any[]} sort - The specified sort
 * @property {Object} project - The specified project, if any
 */

/**
 * Represents a single country result
 * @typedef CountryResult
 * @property {string} ObjectID - The ObjectID of the movie
 * @property {string} title - The title of the movie
 */

/**
 * A Movie from mflix
 * @typedef MflixMovie
 * @property {string} _id
 * @property {string} title
 * @property {number} year
 * @property {number} runtime
 * @property {Date} released
 * @property {string[]} cast
 * @property {number} metacriticd
 * @property {string} poster
 * @property {string} plot
 * @property {string} fullplot
 * @property {string|Date} lastupdated
 * @property {string} type
 * @property {string[]} languages
 * @property {string[]} directors
 * @property {string[]} writers
 * @property {IMDB} imdb
 * @property {string[]} countries
 * @property {string[]} rated
 * @property {string[]} genres
 * @property {string[]} comments
 */

/**
 * IMDB subdocument
 * @typedef IMDB
 * @property {number} rating
 * @property {number} votes
 * @property {number} id
 */

/**
 * Result set for getMovies method
 * @typedef GetMoviesResult
 * @property {MflixMovies[]} moviesList
 * @property {number} totalNumResults
 */

/**
 * Faceted Search Return
 *
 * The type of return from faceted search. It will be a single document with
 * 3 fields: rating, runtime, and movies.
 * @typedef FacetedSearchReturn
 * @property {object} rating
 * @property {object} runtime
 * @property {MFlixMovie[]}movies
 */
