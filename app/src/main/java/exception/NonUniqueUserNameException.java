package exception;

/**
 * Created by hungdo on 11/17/16.
 */

public class NonUniqueUserNameException extends Exception {
    public NonUniqueUserNameException(String message) {
        super(message);
    }
}
