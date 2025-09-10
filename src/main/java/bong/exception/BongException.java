package bong.exception;

/**
 * Represents an exception specific to the Bong application.
 * This custom exception is used to handle application-level errors,
 * providing more specific error messages to the user.
 */
public class BongException extends Exception {
    /**
     * Constructs a new BongException with the specified detail message.
     *
     * @param msg The detail message explaining the reason for the exception.
     */
    public BongException(String msg) {
        super(msg);
    }
}
