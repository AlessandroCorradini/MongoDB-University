import MoviesDAO from "../src/dao/moviesDAO"

describe("Text and Subfield Search", async () => {
  beforeAll(async () => {
    await MoviesDAO.injectDB(global.mflixClient)
  })

  test("Can perform a text search", async () => {
    const filters = { text: "mongo" }
    const { moviesList, totalNumMovies } = await MoviesDAO.getMovies({
      filters,
    })
    expect(moviesList.length).toEqual(6)
    expect(totalNumMovies).toEqual(6)
    const firstMovie = moviesList[0]
    expect(firstMovie["title"]).toEqual("Flash Gordon")
  })

  test("Can perform a genre search with one genre", async () => {
    const filters = { genre: ["Action"] }
    const { moviesList, totalNumMovies } = await MoviesDAO.getMovies({
      filters,
    })
    expect(moviesList.length).toEqual(20)
    expect(totalNumMovies).toEqual(2539)
    const firstMovie = moviesList[0]
    expect(firstMovie["title"]).toEqual("Gladiator")
  })

  test("Can perform a genre search with multiple genres", async () => {
    const filters = { genre: ["Mystery", "Thriller"] }
    const { moviesList, totalNumMovies } = await MoviesDAO.getMovies({
      filters,
    })
    expect(moviesList.length).toEqual(20)
    expect(totalNumMovies).toEqual(3485)
    const firstMovie = moviesList[0]
    expect(firstMovie["title"]).toEqual("2 Fast 2 Furious")
  })

  test("Can perform a cast search with one cast member", async () => {
    const filters = { cast: ["Elon Musk"] }
    const { moviesList, totalNumMovies } = await MoviesDAO.getMovies({
      filters,
    })
    expect(moviesList.length).toEqual(1)
    expect(totalNumMovies).toEqual(1)
    const firstMovie = moviesList[0]
    expect(firstMovie["title"]).toEqual("Racing Extinction")
  })

  test("Can perform a cast search with multiple cast members", async () => {
    const filters = { cast: ["Robert Redford", "Julia Roberts"] }
    const { moviesList, totalNumMovies } = await MoviesDAO.getMovies({
      filters,
    })
    expect(moviesList.length).toEqual(20)
    expect(totalNumMovies).toEqual(61)
    const lastMovie = moviesList.slice(-1).pop()
    expect(lastMovie["title"]).toEqual("Eat Pray Love")
  })

  test("Can perform a search and return a non-default number of movies per page", async () => {
    const filters = { cast: ["Robert Redford", "Julia Roberts"] }
    const { moviesList, totalNumMovies } = await MoviesDAO.getMovies({
      filters,
      moviesPerPage: 33,
    })
    expect(moviesList.length).toEqual(33)
    expect(totalNumMovies).toEqual(61)
    const lastMovie = moviesList.slice(-1).pop()
    expect(lastMovie["title"]).toEqual("Sneakers")
  })
})
