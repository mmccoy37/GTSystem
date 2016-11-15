package model;

/*
 * Class for users of the application
 * @author HungDo
 */
public class User {

    //attributes of User
    private String username;
    private String password;
    private String major;
    private String email;
    private int year;
    private AccountType accountType;

    /*
     * Creates a new user
     * @param username the username for the user
     * @param password the password for the user
     * @param name the name of the user
     * @param accountType the type of account that this user has
     */
    public User(String username, String password, String email, String major, int year,
                AccountType accountType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.year = year;
        this.major = major;
        this.accountType = accountType;
    }

    /*
     * Creates a new user
     * @param username the username for the user
     * @param password the password for the user
     * @param name the name of the user
     */
    public User(String username, String password, String email, String major, int year) {
        this(username, password, email, major, year, AccountType.STUDENT);
    }

    public User(String username, String password) {
        this(username, password, null, null, 0, AccountType.ADMIN);
    }

    /*
     * Getters + Setters for user fields
     */
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getMajor() {
        return major;
    }
    public String getEmail() {
        return email;
    }
    public int getYear() {
        return year;
    }
    public AccountType getAccountType() {
        return accountType;
    }

    public void setUsername(String newUsername) {
        username = newUsername;
    }
    public void setPassword(String newPassword) {
        password = newPassword;
    }
    public void setMajor(String newMjor) {
        major = newMjor;
    }
    public void setEmail(String newEmail) {
        email = newEmail;
    }
    public void serYear(int newYear) {
        year = newYear;
    }
    public void setAccountType(AccountType newAccountType) {
        accountType = newAccountType;
    }

    /*
     * Set equals to compare usernames.
     * @param obj the user instance
     * @return whether the object is a person and their username matches
     */
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User usr = (User) obj;
        return this.username.equals(usr.username);
    }

    /*
     * Set hash code equal to the username's hashcode.
     * @return the username's hashcode
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }

    /**
     *
     * @return the username's string
     */
    @Override
    public String toString() {
        return username;
    }
}

