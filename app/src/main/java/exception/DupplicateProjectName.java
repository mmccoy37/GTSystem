package exception;

/**
 * Created by hungdo on 12/2/16.
 */

public class DupplicateProjectName extends Exception {
    public DupplicateProjectName() {
        super("This project is already exist!");
    }
}
