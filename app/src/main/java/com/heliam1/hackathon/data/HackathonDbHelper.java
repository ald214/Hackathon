package com.heliam1.hackathon.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.heliam1.hackathon.data.HackathonContract.GroupEntry;
import com.heliam1.hackathon.data.HackathonContract.UserEntry;
import com.heliam1.hackathon.models.Group;
import com.heliam1.hackathon.models.User;

public class HackathonDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = HackathonDbHelper.class.getSimpleName();

    // .db file name
    private static final String DATABASE_NAME = "heliam1.Hackathon.db";

    // Database version. If you change the database schema, increment the database version.
    private static final int DATABASE_VERSION = 1;

    // Constructs a new instance of {@link HowtobefitDbHelper}.
    // @param context of the app
    public HackathonDbHelper(Context context) {
        super(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(LOG_TAG, "Database created");
        // Create a String that contains the SQL statement to create the tasks table
        String SQL_CREATE_GROUPS_TABLE = "CREATE TABLE " + GroupEntry.TABLE_NAME + " ("
                + GroupEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GroupEntry.COLUMN_GROUP_NAME + " TEXT NOT NULL, "
                + GroupEntry.COLUMN_GROUP_SUBJECT_CODE + " INTEGER NOT NULL, "
                + GroupEntry.COLUMN_GROUP_USER_COUNT + " INTEGER NOT NULL, "
                + GroupEntry.COLUMN_GROUP_LOCATION + "TEXT NOT NULL, "
                + GroupEntry.COLUMN_GROUP_RATING + " INTEGER NOT NULL DEFAULT 0);";

        String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " ("
                + UserEntry._ID_STUDENT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserEntry.COLUMN_USER_PSEUDONAME + " TEXT NOT NULL, "
                + UserEntry.COLUMN_USER_LOCATION + " TEXT NOT NULL);";

        // Execute the SQL statements
        db.execSQL(SQL_CREATE_GROUPS_TABLE);
        db.execSQL(SQL_CREATE_USER_TABLE);

        insertDummyData(db);

        if (db.getVersion() < DATABASE_VERSION)
            onUpgrade(db, db.getVersion(), DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(LOG_TAG, "Upgrading database...");
        int upgradeTo = oldVersion + 1;

        // This while loop ensures we incrementally update the database to the current version.
        while (upgradeTo <= newVersion)
        {
            switch (upgradeTo)
            {
                case 2:
                    /* Chang the database
                    Log.d(LOG_TAG, "Adding steps table");
                    String SQL_CREATE_STEPS_TABLE = "CREATE TABLE " + StepEntry.TABLE_NAME + " ("
                            + StepEntry._TASK_ID + " INTEGER NOT NULL, "
                            + StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + StepEntry.COLUMN_STEP_STRING + " TEXT, "
                            + StepEntry.COLUMN_STEP_COMPLETED + " INTEGER NOT NULL DEFAULT 0, "
                            + StepEntry.COLUMN_STEP_ORDER + " INTEGER NOT NULL);";

                    // Execute the SQL statement
                    db.execSQL(SQL_CREATE_STEPS_TABLE);
                    */
                    break;
            }
            upgradeTo++;
        }
    }

    private void insertDummyData(SQLiteDatabase db) {
        
    }
}
