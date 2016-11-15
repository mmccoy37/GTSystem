package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.AccountType;
import model.User;

/**
 * Created by hungdo on 11/10/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database.db";

    private static final String CREATE_USER_TABLE = "CREATE TABLE 'USERS' " +
            "(username VARCHAR(50) NOT NULL, " +
            "password VARCHAR(25) NOT NULL, " +
            "type INTEGER DEFAULT 0 NOT NULL, " +
            "PRIMARY KEY(Username));";

    private static final String CREATE_STUDENT_TABLE = "CREATE TABLE 'STUDENTS' " +
            "(GTechEmail VARCHAR(100) NOT NULL, " +
            "username VARCHAR(50) NOT NULL, " +
            "majorName VARCHAR(50) NOT NULL, " +
            "year INTEGER NOT NULL,  " +
            "PRIMARY KEY (GTechEmail), " +
            "FOREIGN KEY (username) REFERENCES USERS (username), " +
            "FOREIGN KEY (majorName) REFERENCES MAJORS (majorName), " +
            "UNIQUE (username));";

    private static final String INSERT_INITIAL_USERS = "INSERT INTO 'USERS' VALUES " +
            "('hungdo', '123456789', 1), " +
            "('nhunguyen', '123456789', 1), " +
            "('matthew', '123456789', 1), " +
            "('tringuyen', '123456789', 1), " +
            "('user1', 'password1', 0), " +
            "('user2', 'password2', 0), " +
            "('user3', 'password3', 0), " +
            "('user4', 'password4', 0);";

    private static final String INSERT_INITIAL_STUDENTS = "INSERT INTO 'STUDENTS' VALUES " +
            "('user1@gatech.edu', 'user1', 'CS', 1), " +
            "('user2@gatech.edu', 'user2', 'CM', 2), " +
            "('user3@gatech.edu', 'user3', 'EE', 3), " +
            "('user4@gatech.edu', 'user4', 'MATH', 4);";


    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(INSERT_INITIAL_USERS);
        db.execSQL(INSERT_INITIAL_STUDENTS);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXIST USERS, STUDENTS";
        db.execSQL(query);
        onCreate(db);
    }

//    /**
//     * Use only for testing
//     */
//    public void deleteAllData() {
//        db = this.getWritableDatabase();
//        String delete = "DELETE FROM " + TABLE_NAME;
//        db.execSQL(delete);
//    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
