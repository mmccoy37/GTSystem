package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.User;

/**
 * Created by hungdo on 11/15/16.
 */

public class DatabaseAccess {
    private static DatabaseAccess databaseAccess;
    private static Connection connection;
    private SQLiteDatabase db;
    private DatabaseHelper dh;
    private User user;

    /**
     * Call only once for setup Facade class
     */
    public static void Initialize() {
        databaseAccess = new DatabaseAccess();
    }

    /**
     * Setup the context for database
     * @param context the stage which user are at
     */
    public void setContext(Context context) {
        dh = new DatabaseHelper(context);
        db = dh.getDb();
    }

    public void setConnection(Connection c) {
        connection = c;
    }

    /**
     * get database
     * @return DatabaseAccess
     */
    public static DatabaseAccess getDatabaseAccess() {
        return databaseAccess;
    }

    /**
     * Get user
     * @return User who are loggin
     */
    public User getUser() {
        return user;
    }

    /**
     * Set user
     * @param user user who will loggin
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * returns the user with the given username in the system if one exists
     * Null otherwise
     * @param username the username of the User that will be returned
     * @return User the user that matches the given username
     */
    public User getUserByUserName(String username) {
        try {

            String query = "SELECT USERS.username, USERS.password, " +
                    "STUDENTS.GTechEmail, STUDENTS.majorName, STUDENTS.year, USERS.type " +
                    "FROM USERS " +
                    "JOIN STUDENTS";
            Statement statement
                    = connection.createStatement();

            ResultSet statementResults      = statement.executeQuery(query);

            while (statementResults.next()) {
                if (username.equals(statementResults.getString(1))) {
                    User user = new User(
                            statementResults.getString(1),
                            statementResults.getString(2),
                            statementResults.getString(3),
                            statementResults.getString(4),
                            statementResults.getInt(5),
                            statementResults.getInt(6)
                    );
                    return user;
                }

            }

            return null;

        } catch (SQLException e) {

            System.out.println("Could not connect to the database: "
                    + e.getMessage());


        }

        // this is needed for compilation
        // execution should never reach this line
        return null;
    }


    /**
     * Insert new user into database
     * @param u new user
     */
    public void createUser(User u) {
        db = dh.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put("username", u.getUsername());
        values1.put("password", u.getPassword());
        values1.put("type", 0);
        db.insertOrThrow("USERS", null, values1);
        ContentValues values2 = new ContentValues();
        values2.put("GTechEmail", u.getEmail());
        values2.put("username", u.getUsername());
        values2.put("majorName", u.getMajor());
        values2.put("year", u.getYear());
        db.insertOrThrow("STUDENTS", null, values2);
        db.close();
    }

    /**
     * Get the user by username
     * @param username username
     * @return return user match username
     */
    public User getUserByUsername(String username) {
        db = dh.getReadableDatabase();
        String query = "SELECT USERS.username, USERS.password, USERS.type, STUDENTS.GTechEmail, " +
                "STUDENTS.majorName, STUDENTS.year " +
                "FROM USERS " +
                "JOIN STUDENTS;";
        Cursor cursor = db.rawQuery(query, null);
        String a = "";
        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);
                if (a.equals(username)) {
                    String usernamestr = cursor.getString(0);
                    String passstr = cursor.getString(1);
                    int typeInt = cursor.getInt(2);
                    String emailstr = cursor.getString(3);
                    String majorstr = cursor.getString(4);
                    int yearInt = cursor.getInt(5);
                    return new User(usernamestr, passstr, emailstr, majorstr, yearInt, typeInt);
                }
            } while (cursor.moveToNext());
        }
        return null;
    }

}
