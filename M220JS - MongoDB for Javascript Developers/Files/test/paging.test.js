import MoviesDAO from "../src/dao/moviesDAO"

describe("Paging", async () => {
  beforeAll(async () => {
    await MoviesDAO.injectDB(global.mflixClient)
  })

  test("Supports paging by cast", async () => {
    const filters = { cast: ["Natalie Portman"] }
    /**
     * Testing first page
     */
    const { moviesList: firstPage, totalNumMovies } = await MoviesDAO.getMovies(
      {
        filters,
      },
    )

    // check the total number of movies, including both pages
    expect(totalNumMovies).toEqual(23)

    // check the number of movies on the first page
    expect(firstPage.length).toEqual(20)

    // check some of the movies on the second page
    const firstMovie = firstPage[0]
    const twentiethMovie = firstPage.slice(-1).pop()
    expect(firstMovie.title).toEqual(
      "Star Wars: Episode III - Revenge of the Sith",
    )
    expect(twentiethMovie.title).toEqual("Knight of Cups")

    /**
     * Testing second page
     */
    const { moviesList: secondPage } = await MoviesDAO.getMovies({
      filters,
      page: 1,
    })

    // check the number of movies on the second page
    expect(secondPage.length).toEqual(3)
    // check some of the movies on the second page
    const twentyFirstMovie = secondPage[0]
    const lastMovie = secondPage.slice(-1).pop()
    expect(twentyFirstMovie.title).toEqual("A Tale of Love and Darkness")
    expect(lastMovie.title).toEqual("True")
  })

  test("Supports paging by genre", async () => {
    const filters = { genre: ["Comedy", "Drama"] }

    /**
     * Testing first page
     */
    const { moviesList: firstPage, totalNumMovies } = await MoviesDAO.getMovies(
      {
        filters,
      },
    )

    // check the total number of movies, including both pages
    expect(totalNumMovies).toEqual(17911)

    // check the number of movies on the first page
    expect(firstPage.length).toEqual(20)

    // check some of the movies on the second page
    const firstMovie = firstPage[0]
    const twentiethMovie = firstPage.slice(-1).pop()
    expect(firstMovie.title).toEqual("Titanic")
    expect(twentiethMovie.title).toEqual("Dègkeselyè")

    /**
     * Testing second page
     */
    const { moviesList: secondPage } = await MoviesDAO.getMovies({
      filters,
      page: 1,
    })

    // check the number of movies on the second page
    expect(secondPage.length).toEqual(20)
    // check some of the movies on the second page
    const twentyFirstMovie = secondPage[0]
    const fortiethMovie = secondPage.slice(-1).pop()
    expect(twentyFirstMovie.title).toEqual("8 Mile")
    expect(fortiethMovie.title).toEqual("Forrest Gump")
  })

  test("Supports paging by text", async () => {
    const filters = { text: "countdown" }

    /**
     * Testing first page
     */
    const { moviesList: firstPage, totalNumMovies } = await MoviesDAO.getMovies(
      {
        filters,
      },
    )

    // check the total number of movies, including both pages
    expect(totalNumMovies).toEqual(12)

    // check the number of movies on the first page
    expect(firstPage.length).toEqual(12)

    // check some of the movies on the second page
    const firstMovie = firstPage[0]
    const twentiethMovie = firstPage.slice(-1).pop()
    expect(firstMovie.title).toEqual("Countdown")
    expect(twentiethMovie.title).toEqual("The Front Line")

    /**
     * Testing second page
     */
    const { moviesList: secondPage } = await MoviesDAO.getMovies({
      filters,
      page: 1,
    })

    // check the number of movies on the second page
    expect(secondPage.length).toEqual(0)
  })
})
