package exceptions;

public class FileMoveException extends RuntimeException {
    public FileMoveException(String message, Throwable cause) {
        super(message, cause);
    }
}
