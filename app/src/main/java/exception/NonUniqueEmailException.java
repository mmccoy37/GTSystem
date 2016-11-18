package exception;

/**
 * Created by hungdo on 11/17/16.
 */

public class NonUniqueEmailException extends Exception {
    public NonUniqueEmailException() {
        super("Attempted to create a user with a username that was taken");
    }
}