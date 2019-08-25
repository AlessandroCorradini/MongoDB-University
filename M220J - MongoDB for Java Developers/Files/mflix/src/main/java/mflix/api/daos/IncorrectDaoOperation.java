package mflix.api.daos;

/**
 * Runtime exception to be thrown in cases where a Dao layer operation cannot be successfully
 * performed, or as Database layer exception encapsulation.
 */
public class IncorrectDaoOperation extends RuntimeException {

  /**
   * Creates a exception for incorrect Dao layer operations.
   *
   * @param message - inflicting string message that originated the error.
   * @param exception - exception chain reference.
   */
  public IncorrectDaoOperation(final String message, Throwable exception) {
    super(message, exception);
  }

  /**
   * Creates a exception for incorrect Dao layer operations.
   *
   * @param message - inflicting string message that originated the error.
   */
  public IncorrectDaoOperation(final String message) {
    super(message);
  }
}
