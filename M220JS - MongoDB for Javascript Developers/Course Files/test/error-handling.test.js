import MoviesDAO from "../src/dao/moviesDAO"

const badObjectId = "helloworld"

describe("Get Comments", async () => {
  beforeAll(async () => {
    await MoviesDAO.injectDB(global.mflixClient)
  })

  test("Handles invalid ID error correctly", async () => {
    const response = await MoviesDAO.getMovieByID(badObjectId)
    expect(response).toBeNull()
  })
})
