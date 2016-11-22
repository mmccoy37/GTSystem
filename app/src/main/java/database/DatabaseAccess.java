package database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hungdo.team44phase3.UserScreen;

import java.sql.Connection;;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import exception.NonUniqueEmailException;
import exception.NonUniqueUserNameException;
import model.User;

/**
 * Created by hungdo on 11/15/16.
 */

public class DatabaseAccess {
    private static DatabaseAccess databaseAccess;
    private static Connection connection;
    private Context context;
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
        this.context = context;
    }

    public void setConnection() {
        new PostTask().execute();
    }

    public Context getContext() {
        return context;
    }

    public Connection getConnection() {
        return connection;
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

            String query = "SELECT U.username, U.password, " +
                    "S.GTechEmail, S.majorName, S.year, U.type " +
                    "FROM USERS AS U " +
                    "LEFT JOIN STUDENTS AS S " +
                    "ON U.Username=S.Username " +
                    "WHERE U.username='" + username + "';";
            Statement statement = connection.createStatement();

            ResultSet statementResults = statement.executeQuery(query);

            if (statementResults.next()) {
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

            return null;

        } catch (SQLException e) {

            System.out.println("Could not connect to the database: " + e.getMessage());
        }

        return null;
    }


    /**
     * Insert new user into database
     * @param user new user
     */
    public void createUser(User user) throws NonUniqueUserNameException, NonUniqueEmailException {
        String usename = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        String major = user.getMajor();
        int year = user.getYear();
        User u = getUserByUserName(user.getUsername());
        if (u != null) {
            throw new NonUniqueUserNameException();
        } else {
            try {
                String query = "INSERT INTO USERS (" +
                        "Username, " +
                        "Password, " +
                        "Type) " +
                        "VALUES ('" + usename +
                        "', '" + password + "', '0'" +
                        ");";
                Statement statement = connection.createStatement();
                statement.execute(query);
                query = "INSERT INTO STUDENTS (" +
                        "GTechEmail, " +
                        "Username, " +
                        "MajorName, " +
                        "Year) " +
                        "VALUES (" +
                        "'" + email + "', " +
                        "'" + usename + "', " +
                        "'" + major + "', " +
                        "'" + year + "'" +
                        ");";
                statement = connection.createStatement();
                statement.execute(query);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                try {
                    String query = "DELETE FROM USERS " +
                            "WHERE Username = '" + usename + "';";
                    Statement statement = connection.createStatement();
                    statement.execute(query);
                } catch (SQLException s) {
                    System.out.println(s.getMessage());
                }
                throw new NonUniqueEmailException();

            }
        }
    }

    /**
     * get categories from db
     * @return
     */
    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM CATEGORY");
            while (rs.next()) {
                categories.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            Log.e("QUERY", e.getMessage());
        }
        return categories;
    }

    /**
     * get designations from db
     * @return
     */
    public ArrayList<String> getDesignations() {
        ArrayList<String> res = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM DESIGNATION");
            while (rs.next()) {
                res.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            Log.e("QUERY", e.getMessage());
        }
        return res;
    }

    /**
     * get majors from db
     * @return
     */
    public ArrayList<String> getMajors() {
        ArrayList<String> res = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT MajorName FROM MAJORS");
            while (rs.next()) {
                res.add(rs.getString("MajorName"));
            }
        } catch (SQLException e) {
            Log.e("QUERY", e.getMessage());
        }
        return res;
    }

    /**
     * get years from database
     * @return
     */
    public ArrayList<String> getYears() {
        ArrayList<String> res = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT DISTINCT Year FROM STUDENTS");
            while (rs.next()) {
                res.add(rs.getString("Year"));
            }
        } catch (SQLException e) {
            Log.e("QUERY", e.getMessage());
        }
        return res;
    }

    public ArrayList<String> getMainPageResults(String Title, String Category, String Designation,
                                                String Major, String Year, int Type) {
        //TODO: THIS IS BROKEN. AKA MUST FIX ONCE WE DO OUR SQL QUERIES FOR PHASE 3
        ArrayList<String> res = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = null;
            if (Type == UserScreen.TYPE_IS_BOTH) {
                rs = statement.executeQuery("SELECT DISTINCT * FROM COURSE,PROJECTS");
                while (rs.next()) {
                    res.add(rs.getString("CourseName") + rs.getString("PName"));
                }
            } else if (Type == UserScreen.TYPE_IS_COURSE) {
                rs = statement.executeQuery("SELECT DISTINCT * FROM COURSE");
                while (rs.next()) {
                    res.add(rs.getString("CourseName"));
                }
            } else if (Type == UserScreen.TYPE_IS_PROJECT) {
                rs = statement.executeQuery("SELECT DISTINCT * FROM PROJECTS");
                while (rs.next()) {
                    res.add(rs.getString("PName"));
                }
            }
            Log.d("QUERY", "MainPage query results: " + res.toString());
        } catch (SQLException e) {
            Log.e("QUERY", e.getMessage());
        }
        return res;
    }




















    /**
     * Class to run under background to connect to database
     */
    private class PostTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("PreExecute");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_44",
                        "cs4400_Team_44",
                        "eAO5XaBD");

                if(!connection.isClosed())
                    System.out.println("Successfully connected to " +
                            "MySQL server using TCP/IP...");
            } catch(Exception e) {
                System.err.println("Exception: " + e.getMessage());
                System.out.println("Successfully connected to " +
                        "MySQL server using TCP/IP...");
            }
            return "All Done!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            System.out.println("Doing");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Finished");
        }
    }

}
