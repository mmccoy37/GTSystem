package database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.hungdo.team44phase3.UserScreen;

import java.sql.Connection;;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exception.NonUniqueEmailException;
import exception.NonUniqueUserNameException;
import model.Course;
import model.Project;
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
        String username = user.getUsername();
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
                        "VALUES ('" + username +
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
                        "'" + username + "', " +
                        "'" + major + "', " +
                        "'" + year + "'" +
                        ");";
                statement = connection.createStatement();
                statement.execute(query);
                connection.commit();
            } catch (SQLException s) {
                System.out.println(s.getMessage());
                try {
                    connection.rollback();
                    Log.i("SQL", "rolled back due to SQL query error.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                throw new NonUniqueEmailException();

            }
        }
    }

    /**
     * adds a new course to the database as well as the course_requirements
     * @param cnum course number
     * @param cname course name
     * @param cstudents estimated # of students
     * @param cinstr course instructor
     * @param designation course designation
     * @param ccategory course categories
     * @return result
     */
    public String addNewCourse(String cnum, String cname, String cstudents, String cinstr,
                               String designation, List<String> ccategory) {
        cnum = cnum.toUpperCase();
        try {
            String query = "INSERT INTO COURSE (" +
                    "CourseNumber, CourseName, EstimatedStudentNum, Instructor, DesignationName)" +
                    "VALUES (" +
                    "'" +cnum + "', " +
                    "'" + cname + "', " +
                    "'" + cstudents + "'," +
                    "'" + cinstr + "', " +
                    "'" + designation +"');";
            Statement statement = connection.createStatement();
            statement.execute(query);
            for (String s : ccategory) {
                query = "INSERT INTO COURSE_CATEGORY (CategoryName, CourseNumber) VALUES (" +
                        "'" + s + "', '" + cnum + "');";
                statement = connection.createStatement();
                statement.execute(query);
            }
            connection.commit();
        } catch (SQLException s) {
            Log.e("SQL", s.getMessage());
            try {
                connection.rollback();
                Log.i("SQL", "rolled back due to SQL query error.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return s.getMessage();
        } catch (Exception e) {
            Log.e("DATA", e.getMessage());
            return e.getMessage();
        }
        return "Course '" + cname + "' added!";
    }

    public String addNewProject(
            String pname,String pEstStudent, String pdesc, String padv,
            String pemail, String pdesignation, String pyear, String pmajor,
            String pdept, List<String> pcategories) {
        try {
            //project basic info
            String query = "INSERT INTO PROJECTS (" +
                    "PName, EstimatedStudents, Description, AdvisorName, AdvisorEmail, Designation)" +
                    "VALUES (" +
                    "'" +pname + "', " +
                    "'" + pEstStudent + "', " +
                    "'" + pdesc + "'," +
                    "'" + padv + "', " +
                    "'" + pemail + "', " +
                    "'" + pdesignation + "');";
            Statement statement = connection.createStatement();
            statement.execute(query);
            //categories
            for (String s : pcategories) {
                query = "INSERT INTO PROJ_CATEGORY (CategoryName, ProjectName) VALUES (" +
                        "'" + s + "', '" + pname + "');";
                statement = connection.createStatement();
                statement.execute(query);
            }
            //year requirement
            if (!pyear.equals("")) {
                query = "INSERT INTO PROJ_REQUIREMENTS (PName, PRequirements) VALUES(" +
                        "'" + pname + "', " +
                        "'" + pyear + "');";
                statement = connection.createStatement();
                statement.execute(query);
            }
            //major reuirement
            if (!pmajor.equals("")) {
                query = "INSERT INTO PROJ_REQUIREMENTS (PName, PRequirements) VALUES(" +
                        "'" + pname + "', " +
                        "'" + pmajor + "');";
                statement = connection.createStatement();
                statement.execute(query);
            }
            //department requirement
            if (!pdept.equals("")) {
                query = "INSERT INTO PROJ_REQUIREMENTS (PName, PRequirements) VALUES(" +
                        "'" + pname + "', " +
                        "'" + pdept + "');";
                statement = connection.createStatement();
                statement.execute(query);
            }
            connection.commit();
        } catch (SQLException s) {
            Log.e("SQL", s.getMessage());
            try {
                connection.rollback();
                Log.i("SQL", "rolled back due to SQL query error.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return s.getMessage();
        } catch (Exception e) {
            Log.e("DATA", e.getMessage());
            return e.getMessage();
        }
        return "Project '" + pname + "' added!";
    }

    /**
     * Get department name from major
     * @param major major input
     * @return Name of department
     */
    public String getDeptNameFromMajor(String major) {
        try {
            String query = "SELECT MAJORS.DeptName " +
                    "FROM MAJORS " +
                    "WHERE MAJORS.MajorName='" + major + "' ";
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet statementResults = statement.executeQuery(query);

            if (statementResults.next()) {
                String deptName = statementResults.getString(1);
                return deptName;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
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


    public ArrayList<String> getDepartments() {
        ArrayList<String> depts = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM DEPARTMENT");
            while (rs.next()) {
                depts.add(rs.getString("Name"));
            }
        } catch (SQLException s) {
            Log.e("QUERT", s.getMessage());
        }
        return depts;
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

    public List<Object> getMainPageResults(String Title, List<String> Category, String Designation,
                                                String Major, String Year, int Type) {
        List<Object> res = new ArrayList<>();
        if (!Title.equals("")) {
            res.addAll(getListProjectsByTitle(Title));
            res.addAll(getListCoursesByTitle(Title));
            if (res.size() == 0) {
                Toast error = Toast.makeText(context, "Title is not exist!", Toast.LENGTH_SHORT);
                error.show();
            }
        } else {
            if (Type == UserScreen.TYPE_IS_BOTH) {
                res.addAll(getListProjectByFilter(Category, Designation, Major, Year));
                res.addAll(getListCourseByFilter(Category, Designation));
            } else if (Type == UserScreen.TYPE_IS_COURSE) {
                res.addAll(getListCourseByFilter(Category, Designation));
            } else if (Type == UserScreen.TYPE_IS_PROJECT) {
                res.addAll(getListProjectByFilter(Category, Designation, Major, Year));
            }
            if (res.size() == 0) {
                Toast error = Toast.makeText(context, "Nothing match filters!", Toast.LENGTH_SHORT);
                error.show();
            }
        }
        Log.d("QUERY", "MainPage query results: " + res.toString());
        return res;
    }

    /**
     * Get a list of all courses and projects
     * @return
     */
    public List<Object> getAllCourseAndProject() {
        List<Object> listAll = new ArrayList<>();
        try {
            String query = "SELECT * FROM COURSE;";
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet statementResults = statement.executeQuery(query);

            while (statementResults.next()) {
                Course course = new Course(statementResults.getString("CourseNumber"),
                        statementResults.getString("CourseName"),
                        statementResults.getInt("EstimatedStudentNum"),
                        statementResults.getString("Instructor"),
                        statementResults.getString("DesignationName"));
                listAll.add(course);
            }
            query = "SELECT * FROM PROJECTS;";
            statement = connection.createStatement();
            statement.execute(query);
            statementResults = statement.executeQuery(query);

            while (statementResults.next()) {
                Project project = new Project(statementResults.getString("PName"),
                        statementResults.getInt("EstimatedStudents"),
                        statementResults.getString("Description"),
                        statementResults.getString("AdvisorName"),
                        statementResults.getString("AdvisorEmail"),
                        statementResults.getString("Designation"));
                listAll.add(project);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listAll;
    }

    /**
     * Get courses by filter
     * @param cats list of categories
     * @param des designation
     * @return a list of courses
     */
    public List<Course> getListCourseByFilter(List<String> cats, String des) {
        List<Course> res = new ArrayList<>();
        try {
            for (String cat: cats) {
                String query = "SELECT Co.* " +
                        "FROM COURSE as Co " +
                        "LEFT JOIN COURSE_CATEGORY as Ca " +
                        "ON Ca.CourseNumber = Co.CourseNumber " +
                        "WHERE Ca.CategoryName='" + cat + "' AND Co.DesignationName='" + des + "';";
                Statement statement = connection.createStatement();
                statement.execute(query);
                ResultSet statementResults = statement.executeQuery(query);
                while (statementResults.next()) {
                    Course c = new Course(statementResults.getString("CourseNumber"),
                            statementResults.getString("CourseName"),
                            statementResults.getInt("EstimatedStudentNum"),
                            statementResults.getString("Instructor"),
                            statementResults.getString("DesignationName"));
                    res.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Here " + e.getMessage());
        }
        return res;
    }


    /**
     * Get list of projects by filters
     * @param cats list of categories
     * @param des designation
     * @param maj major
     * @param ye year
     * @return list of projects
     */
    public List<Project> getListProjectByFilter(List<String> cats, String des, String maj, String ye) {
        List<Project> res = new ArrayList<>();
        try {
            String dept = getDeptNameFromMajor(maj);
            for (String cat: cats) {
                String query = "SELECT P.*, P_R.PRequirements FROM PROJECTS AS P " +
                        "LEFT JOIN PROJ_REQUIREMENTS AS P_R " +
                        "ON P.PName = P_R.PName " +
                        "WHERE P.PName IN " +
                        "   (SELECT PRO.PName " +
                        "   FROM PROJECTS AS PRO " +
                        "   LEFT JOIN PROJ_CATEGORY AS PC " +
                        "   ON PRO.PName = PC.ProjectName " +
                        "   LEFT JOIN PROJ_REQUIREMENTS as PR " +
                        "   ON PRO.PName = PR.PName " +
                        "   WHERE PRO.Designation = '" + des + "' " +
                        "   AND PC.CategoryName = '" + cat + "' " +
                        "   AND (PR.PRequirements = '" + ye + "' " +
                        "       OR PR.PRequirements = '" + maj + "' " +
                        "       OR PR.PRequirements = '" + dept + "') " +
                        "   GROUP BY PRO.PName HAVING COUNT(*)>1) " +
                        "AND P_R.PRequirements = '" + ye + "';";
                Statement statement = connection.createStatement();
                statement.execute(query);
                ResultSet statementResults = statement.executeQuery(query);
                while (statementResults.next()) {
                    Project p = new Project(statementResults.getString("PName"),
                            statementResults.getInt("EstimatedStudents"),
                            statementResults.getString("Description"),
                            statementResults.getString("AdvisorName"),
                            statementResults.getString("AdvisorEmail"),
                            statementResults.getString("Designation"));
                    res.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Here " + e.getMessage());
        }
        return res;
    }

    /**
     * Get list project by title
     * @param title name of project
     * @return list of project match title
     */
    public List<Project> getListProjectsByTitle(String title) {
        List<Project> listP = new ArrayList<>();
        try {
            String query = "SELECT * FROM PROJECTS AS P WHERE P.PName = '" + title + "';";
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet statementResults = statement.executeQuery(query);

            while (statementResults.next()) {
                Project project = new Project(statementResults.getString("PName"),
                        statementResults.getInt("EstimatedStudents"),
                        statementResults.getString("Description"),
                        statementResults.getString("AdvisorName"),
                        statementResults.getString("AdvisorEmail"),
                        statementResults.getString("Designation"));
                listP.add(project);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listP;
    }

    /**
     * Get list course by title
     * @param title name of course
     * @return list of course match title
     */
    public List<Course> getListCoursesByTitle(String title) {
        List<Course> listC = new ArrayList<>();
        try {
            String query = "SELECT * FROM COURSE AS C WHERE C.CourseName = '" + title + "';";
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet statementResults = statement.executeQuery(query);

            while (statementResults.next()) {
                Course course = new Course(statementResults.getString("CourseNumber"),
                        statementResults.getString("CourseName"),
                        statementResults.getInt("EstimatedStudentNum"),
                        statementResults.getString("Instructor"),
                        statementResults.getString("DesignationName"));
                listC.add(course);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listC;
    }

    /**
     * get list categories of course name
     * @param name name of course
     * @return list categories
     */
    public List<String> getCategoriesOfCourseName(String name) {
        List<String> list = new ArrayList<>();
        try {
            String query = "SELECT Ca.CategoryName FROM COURSE AS C " +
                    "LEFT JOIN COURSE_CATEGORY AS Ca " +
                    "ON C.CourseNumber = Ca.CourseNumber " +
                    "WHERE C.CourseName = '" + name + "';";
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet statementResults = statement.executeQuery(query);

            while (statementResults.next()) {
                list.add(statementResults.getString("CategoryName"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * Get list of categories of project name
     * @param name name of project
     * @return
     */
    public List<String> getCategoriesOfProjectName(String name) {
        List<String> list = new ArrayList<>();
        try {
            String query = "SELECT Pc.CategoryName FROM PROJECTS AS P " +
                    "LEFT JOIN PROJ_CATEGORY AS Pc " +
                    "ON P.PName = Pc.ProjectName " +
                    "WHERE P.PName = '" + name + "';";
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet statementResults = statement.executeQuery(query);

            while (statementResults.next()) {
                list.add(statementResults.getString("CategoryName"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * Get list requirements of project
     * @param name name of project
     * @return list of requirements
     */
    public List<String> getListRequireByProjectName(String name) {
        List<String> list = new ArrayList<>();
        try {
            String query = "SELECT Pr.PRequirements FROM PROJECTS AS P " +
                    "LEFT JOIN  PROJ_REQUIREMENTS AS Pr " +
                    "ON P.PName = Pr.PName " +
                    "WHERE P.PName = '" + name + "';";
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet statementResults = statement.executeQuery(query);

            while (statementResults.next()) {
                list.add(statementResults.getString("PRequirements"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
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
                connection.setAutoCommit(false);
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
