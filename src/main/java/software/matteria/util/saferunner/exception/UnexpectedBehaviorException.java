package software.matteria.util.saferunner.exception;

public class UnexpectedBehaviorException extends RuntimeException {
    public UnexpectedBehaviorException(String message, Throwable cause) {
        super(message, cause);
    }
}
