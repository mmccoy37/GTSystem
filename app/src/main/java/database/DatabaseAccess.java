package database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.hungdo.team44phase3.UserScreen;

import java.sql.Connection;;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import exception.DupplicateProjectName;
import exception.NonUniqueEmailException;
import exception.NonUniqueUserNameException;
import model.Apply;
import model.Course;
import model.Project;
import model.Report;
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
     * returns the user with the given email in the system if one exists
     * Null otherwise
     * @param email the username of the User that will be returned
     * @return User the user that matches the given username
     */
    public User getUserByEmail(String email) {
        try {
            String query = "SELECT U.username, U.password, " +
                    "S.GTechEmail, S.majorName, S.year, U.type " +
                    "FROM USERS AS U " +
                    "LEFT JOIN STUDENTS AS S " +
                    "ON U.Username = S.Username " +
                    "WHERE S.GTechEmail ='" + email + "';";
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
                PreparedStatement statement = connection.prepareStatement(query);
                statement.executeUpdate();
                query = "INSERT INTO STUDENTS (" +
                        "GTechEmail, " +
                        "Username, " +
                        "MajorName, " +
                        "Year) " +
                        "VALUES (" +
                        "'" + email + "', " +
                        "'" + username + "', " +
                        "?, " +
                        "'" + year + "'" +
                        ");";
                statement= connection.prepareStatement(query);
                if (major != null) {
                    statement.setString(1, major);
                } else {
                    statement.setNull(1, Types.VARCHAR);
                }
                statement.executeUpdate();
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
        Collections.sort(res, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
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
        Collections.sort(listAll, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
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
                ResultSet statementResults = statement.executeQuery(query);
                while (statementResults.next()) {
                    Course c = new Course(statementResults.getString("CourseNumber"),
                            statementResults.getString("CourseName"),
                            statementResults.getInt("EstimatedStudentNum"),
                            statementResults.getString("Instructor"),
                            statementResults.getString("DesignationName"));
                    if (!res.contains(c)) {
                        res.add(c);
                    }
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
                String helper = "   (SELECT PRO.PName " +
                        "   FROM PROJECTS AS PRO " +
                        "   LEFT JOIN PROJ_CATEGORY AS PC " +
                        "   ON PRO.PName = PC.ProjectName " +
                        "   LEFT JOIN PROJ_REQUIREMENTS as PR " +
                        "   ON PRO.PName = PR.PName " +
                        "   WHERE PRO.Designation = '" + des + "' " +
                        "   AND PC.CategoryName = '" + cat + "' ";
                String extraTable = helper +
                        "   AND (PR.PRequirements = '" + ye + "' " +
                        "       OR PR.PRequirements = '" + maj + "' " +
                        "       OR PR.PRequirements = '" + dept + "') ";
                String query = "SELECT P.*, P_R.PRequirements FROM PROJECTS AS P " +
                        "LEFT JOIN PROJ_REQUIREMENTS AS P_R " +
                        "ON P.PName = P_R.PName " +
                        "WHERE P.PName IN " + extraTable +
                        "   GROUP BY PRO.PName HAVING COUNT(*)>1) " +
                        "AND P_R.PRequirements = '" + ye + "' " +

                        "UNION SELECT P.*, P_R.PRequirements FROM PROJECTS AS P " +
                        "LEFT JOIN PROJ_REQUIREMENTS AS P_R " +
                        "ON P.PName = P_R.PName " +
                        "WHERE P.PName IN " + helper +
                        "   GROUP BY PRO.PName HAVING COUNT(*)=1) " +
                        "AND P_R.PRequirements = '" + ye + "' " +

                        "UNION SELECT P.*, P_R.PRequirements FROM PROJECTS AS P " +
                        "LEFT JOIN PROJ_REQUIREMENTS AS P_R " +
                        "ON P.PName = P_R.PName " +
                        "WHERE P.PName IN " + helper +
                        "   GROUP BY PRO.PName HAVING COUNT(*)=1) " +
                        "AND P_R.PRequirements = '" + maj + "' " +

                        "UNION SELECT P.*, P_R.PRequirements FROM PROJECTS AS P " +
                        "LEFT JOIN PROJ_REQUIREMENTS AS P_R " +
                        "ON P.PName = P_R.PName " +
                        "WHERE P.PName IN " + helper +
                        "   GROUP BY PRO.PName HAVING COUNT(*)=1) " +
                        "AND P_R.PRequirements = '" + dept + "' " +

                        "UNION SELECT PRO.*, PR.PRequirements FROM PROJECTS AS PRO " +
                        "LEFT JOIN PROJ_CATEGORY AS PC " +
                        "ON PRO.PName = PC.ProjectName " +
                        "LEFT JOIN PROJ_REQUIREMENTS as PR " +
                        "ON PRO.PName = PR.PName " +
                        "WHERE PRO.Designation = '" + des + "' " +
                        "AND PC.CategoryName = '" + cat + "' " +
                        "AND PR.PRequirements IS NULL";
                Statement statement = connection.createStatement();
                ResultSet statementResults = statement.executeQuery(query);
                while (statementResults.next()) {
                    Project p = new Project(statementResults.getString("PName"),
                            statementResults.getInt("EstimatedStudents"),
                            statementResults.getString("Description"),
                            statementResults.getString("AdvisorName"),
                            statementResults.getString("AdvisorEmail"),
                            statementResults.getString("Designation"));
                    if (!res.contains(p)) {
                        res.add(p);
                    }
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
     * Update student information by email
     * @param email email
     * @param major major
     * @param year year
     */
    public void updateUserByEmail(String email, String major, int year) {
        try {
            String query = "UPDATE STUDENTS " +
                    "SET MajorName='" + major + "', Year = '" + year + "' " +
                    "WHERE GTechEmail='" + email + "';";
            Statement statement = connection.createStatement();
            statement.execute(query);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println(e.getMessage());
        }
    }

    /**
     * Apply new project
     * @param email email of student
     * @param projectName name of project
     * @param day day
     */
    public void applyProject(String email, String projectName, String day) throws DupplicateProjectName {
        try {
            String query = "INSERT INTO APPLY (" +
                    "GTechEmail, " +
                    "ProjName, " +
                    "Date, " +
                    "Status) " +
                    "VALUES (" +
                    "'" + email + "', " +
                    "'" + projectName + "', " +
                    "'" + day + "', " +
                    "'pending');";
            Statement statement = connection.createStatement();
            statement.execute(query);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new DupplicateProjectName();
        }
    }

    /**
     * get all applies from database
     * @return
     */
    public List<Apply> getALLApplies() {
        List<Apply> res = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM APPLY");
            while (rs.next()) {
                Apply ap = new Apply(rs.getString("GTechEmail"), rs.getString("ProjName"),
                        rs.getString("Date"), rs.getString("Status"));
                res.add(ap);
            }
        } catch (SQLException e) {
            Log.e("QUERY", e.getMessage());
        }
        return res;
    }

    /**
     * get applies from database by Email
     * @return list of application
     */
    public List<Apply> getAppliesByEmail(String email) {
        List<Apply> res = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM APPLY " +
                    "WHERE GTechEmail = '" + email + "';");
            while (rs.next()) {
                Apply ap = new Apply(rs.getString("GTechEmail"), rs.getString("ProjName"),
                        rs.getString("Date"), rs.getString("Status"));
                res.add(ap);
            }
        } catch (SQLException e) {
            Log.e("QUERY", e.getMessage());
        }
        return res;
    }

    /**
     * Update status for application
     * @param pName name of project
     * @param email email of student
     * @param status new status
     */
    public void updateApplyStatusByProjectNameAndEmail(String pName, String email, String status) {
        try {
            String query = "UPDATE APPLY " +
                    "SET Status = '" + status + "' " +
                    "WHERE GTechEmail = '" + email + "' " +
                    "AND ProjName = '" + pName + "';";
            Statement statement = connection.createStatement();
            statement.execute(query);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            Log.e("QUERY", e.getMessage());
        }
    }

    /**
     * get applies from database by Email
     * @return list of application
     */
    public List<Apply> getListApplyByProjectname(String pName) {
        List<Apply> res = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM APPLY " +
                    "WHERE ProjName = '" + pName + "';");
            while (rs.next()) {
                Apply ap = new Apply(rs.getString("GTechEmail"), rs.getString("ProjName"),
                        rs.getString("Date"), rs.getString("Status"));
                res.add(ap);
            }
        } catch (SQLException e) {
            Log.e("QUERY", e.getMessage());
        }
        return res;
    }

    /**
     * Get list of all reports
     * @return list of reports
     */
    public List<Report> getAllListReport() {
        List<Report> res = new ArrayList<>();
        Map<String, Integer> mapApplies = getListAppliesWithNumberOfApplicant();
        for (Map.Entry<String, Integer> m: mapApplies.entrySet()) {
            Report r = new Report(m.getKey());
            res.add(r);
        }
        Collections.sort(res, new Comparator<Report>() {
            @Override
            public int compare(Report o1, Report o2) {
                if (o1.getRate() > o2.getRate()) {
                    return -1;
                } else if (o1.getRate() < o2.getRate()) {
                    return 1;
                } else {
                    return o1.getProjectName().compareTo(o2.getProjectName());
                }
            }
        });
        return res;
    }

    /**
     * Get map list of prject name with #of applicant
     * @return Map
     */
    public Map<String, Integer> getListAppliesWithNumberOfApplicant() {
        HashMap<String, Integer> list = new HashMap<>();
        for (Apply apply: getALLApplies()) {
            if (!list.containsKey(apply.getProjectName())) {
                list.put(apply.getProjectName(), 1);
            } else {
                list.put(apply.getProjectName(), list.get(apply.getProjectName()) + 1);
            }
        }
        TreeMap<String, Integer> sortedMap = sortMapByValue(list);
        return sortedMap;
    }


    /**
     * Sort HashMap
     * @param map hashmap
     * @return TreeMap
     */
    public TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map){
        Comparator<String> comparator = new ValueComparator(map);
        //TreeMap is a map sorted by its keys.
        //The comparator is used to sort the TreeMap by keys.
        TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
        result.putAll(map);
        return result;
    }

    private class ValueComparator implements Comparator<String>{
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        public ValueComparator(HashMap<String, Integer> map){
            this.map.putAll(map);
        }
        @Override
        public int compare(String s1, String s2) {
            if(map.get(s1) > map.get(s2)){
                return -1;
            } else if (map.get(s1) < map.get(s2)){
                return 1;
            } else {
                return s1.compareTo(s2);
            }
        }
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
                Log.e("SQL", e.getMessage());
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
