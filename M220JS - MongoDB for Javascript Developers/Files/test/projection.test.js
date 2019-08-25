import MoviesDAO from "../src/dao/moviesDAO"

describe("Projection", async () => {
  beforeAll(async () => {
    await MoviesDAO.injectDB(global.mflixClient)
  })

  test("Can perform a country search for one country", async () => {
    const kosovoList = ["Kosovo"]
    const movies = await MoviesDAO.getMoviesByCountry(kosovoList)
    expect(movies.length).toEqual(2)
  })

  test("Can perform a country search for three countries", async () => {
    const countriesList = ["Russia", "Japan", "Mexico"]
    const movies = await MoviesDAO.getMoviesByCountry(countriesList)
    expect(movies.length).toEqual(1468)
    movies.map(movie => {
      const movieKeys = Object.keys(movie).sort()
      const expectedKeys = ["_id", "title"]
      expect(movieKeys).toEqual(expectedKeys)
    })
  })
})
