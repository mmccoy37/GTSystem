package model;

/**
 * Created by hungdo on 11/22/16.
 */
public enum YearLevel {
    FR ("FRESHMAN", 1),
    SO ("SOPHOMORE", 2),
    JR ("JUNIOR", 3),
    SR ("SENIOR", 4);

    /** the full string representation of the class standing */
    private String name;
    private int code;

    /**
     * Constructor for the enumeration
     *
     * @param name   full name of the class standing
     */
    YearLevel(String name, int code) {
        this.name = name;
        this.code = code;
    }

    /**
     *
     * @return   the full class standing name
     */
    public String getClassStandingName() {
        return name;
    }

    public int getCode() {
        return code;
    }


    @Override
    public String toString() {
        return name;
    }
}
