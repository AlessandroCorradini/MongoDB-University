package mflix.api.models;

public class ViewerRating {

  private double rating;
  private int numReviews;

  public ViewerRating() {
    super();
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public int getNumReviews() {
    return numReviews;
  }

  public void setNumReviews(int numReviews) {
    this.numReviews = numReviews;
  }
}
