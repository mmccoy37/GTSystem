package model;

/**
 * Account Type enum
 * Stores Types Student, Admin
 */
public enum AccountType {
    STUDENT ("Student"),
    ADMIN ("Admin");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}

