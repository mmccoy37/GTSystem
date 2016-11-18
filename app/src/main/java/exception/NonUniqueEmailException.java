package exception;

/**
 * Created by hungdo on 11/17/16.
 */

public class NonUniqueEmailException extends Exception {
    public NonUniqueEmailException(String message) {
        super(message);
    }
}