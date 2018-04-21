package com.heliam1.hackathon.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.heliam1.hackathon.data.HackathonContract.GroupEntry;
import com.heliam1.hackathon.data.HackathonContract.UserEntry;
import com.heliam1.hackathon.data.HackathonContract.GroupMessageEntry;

public class HackathonProvider extends ContentProvider {
    public static final String LOG_TAG = HackathonProvider.class.getSimpleName();

    private static final int GROUPS = 100;        // URI matcher code for workouts table
    private static final int GROUP_ID = 101;      // URI matcher code for the content URI for a single task in the workouts table
    private static final int USERS = 200;   // URI matcher code for the content URI for the exercisesets table
    private static final int USER_ID = 201; // URI matcher code for the content URI for a single step in the exercise sets table
    private static final int GROUPMESSAGES = 300;
    private static final int GROUPMESSAGE_ID = 301;

    /**
     * UriMatcher object to match a content URI to a corresponding code. The input passed into the
     * constructor represents the code to return for the root URI.It's common to use NO_MATCH as the
     * input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(HackathonContract.CONTENT_AUTHORITY,
                HackathonContract.PATH_GROUPS, GROUPS);

        sUriMatcher.addURI(HackathonContract.CONTENT_AUTHORITY,
                HackathonContract.PATH_GROUPS + "/#", GROUP_ID);

        sUriMatcher.addURI(HackathonContract.CONTENT_AUTHORITY,
                HackathonContract.PATH_USERS, USERS);

        sUriMatcher.addURI(HackathonContract.CONTENT_AUTHORITY,
                HackathonContract.PATH_USERS + "/#",
                USER_ID);

        sUriMatcher.addURI(HackathonContract.CONTENT_AUTHORITY,
                HackathonContract.PATH_GROUPMESSAGES, GROUPMESSAGES);

        sUriMatcher.addURI(HackathonContract.CONTENT_AUTHORITY,
                HackathonContract.PATH_GROUPMESSAGES + "/#",
                GROUPMESSAGE_ID);
    }

    private HackathonDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new HackathonDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case GROUPS:
                cursor = database.query(GroupEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case GROUP_ID:
                selection = GroupEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(GroupEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case USERS:
                cursor = database.query(UserEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case USER_ID:
                selection = UserEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(UserEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case GROUPMESSAGES:
                cursor = database.query(GroupMessageEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case GROUPMESSAGE_ID:
                selection = GroupMessageEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(GroupMessageEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GROUPS:
                return insertGroup(uri, contentValues);
            case USERS:
                return insertUser(uri, contentValues);
            case GROUPMESSAGES:
                return insertGroupMessage(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertGroup(Uri uri, ContentValues values) {
        values = sanitiseGroup(values);
        // If values were not sanitary
        if (values == null) {
            return null;
        }

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(GroupEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the task content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertUser(Uri uri, ContentValues values) {
        values = sanitiseUser(values);
        // If values were not sanitary
        if (values == null) {
            return null;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(UserEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the task content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertGroupMessage(Uri uri, ContentValues values) {
        values = sanitiseGroupMessage(values);
        // If values were not sanitary
        if (values == null) {
            return null;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(GroupMessageEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the task content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GROUPS:
                return updateGroup(uri, contentValues, selection, selectionArgs);
            case GROUP_ID:
                // For the TASK_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = GroupEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateGroup(uri, contentValues, selection, selectionArgs);
            case USERS:
                return updateUser(uri, contentValues, selection, selectionArgs);
            case USER_ID:
                selection = UserEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateUser(uri, contentValues, selection, selectionArgs);
            case GROUPMESSAGES:
                return updateGroupMessage(uri, contentValues, selection, selectionArgs);
            case GROUPMESSAGE_ID:
                selection = UserEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateGroupMessage(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateGroup(Uri uri, ContentValues values, String selection,
                              String[] selectionArgs) {

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        values = sanitiseGroup(values);
        // if values bad sanitised update failed
        if (values == null) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(GroupEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    private int updateUser(Uri uri, ContentValues values, String selection,
                                  String[] selectionArgs) {

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        values = sanitiseUser(values);
        // if values bad sanitised update failed
        if (values == null) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(UserEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    private int updateGroupMessage(Uri uri, ContentValues values, String selection,
                           String[] selectionArgs) {

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        values = sanitiseGroupMessage(values);
        // if values bad sanitised update failed
        if (values == null) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(GroupMessageEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GROUPS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(GroupEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case GROUP_ID:
                // Delete a single row given by the ID in the URI
                selection = GroupEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(GroupEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USERS:
                rowsDeleted = database.delete(UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER_ID:
                // Delete a single row given by the ID in the URI
                selection = UserEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case GROUPMESSAGES:
                rowsDeleted = database.delete(GroupMessageEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case GROUPMESSAGE_ID:
                // Delete a single row given by the ID in the URI
                selection = GroupMessageEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(GroupMessageEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GROUPS:
                return GroupEntry.CONTENT_LIST_TYPE;
            case GROUP_ID:
                return GroupEntry.CONTENT_ITEM_TYPE;
            case USERS:
                return UserEntry.CONTENT_LIST_TYPE;
            case USER_ID:
                return UserEntry.CONTENT_ITEM_TYPE;
            case GROUPMESSAGES:
                return GroupMessageEntry.CONTENT_LIST_TYPE;
            case GROUPMESSAGE_ID:
                return GroupMessageEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    private ContentValues sanitiseGroup(ContentValues values) {
        return values;
    }

    private ContentValues sanitiseUser(ContentValues values) {
        return values;
    }

    private ContentValues sanitiseGroupMessage(ContentValues values) {
        return values;
    }
}
