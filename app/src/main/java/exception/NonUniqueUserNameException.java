package exception;

/**
 * Created by hungdo on 11/17/16.
 */

public class NonUniqueUserNameException extends Exception {
    public NonUniqueUserNameException() {
        super("Attempted to create a user with a username that was taken");
    }
}
