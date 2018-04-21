package com.heliam1.hackathon.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.heliam1.hackathon.BuildConfig;

public class HackathonContract {
    // prevent accidental instantiation
    private HackathonContract() {}

    // Allows both release and debug to be installed
    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;
    // com.heliam1.Hackathon or com.heliam1.Hackathon.debug

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Paths to each table
    public static final String PATH_GROUPS = "groups";
    public static final String PATH_USERS = "users";
    public static final String PATH_GROUPMESSAGES = "groupmessages";

    // Inner class for group table
    public static final class GroupEntry implements BaseColumns {
        // Content uri to access workout data via the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GROUPS);

        // MIME type of the {@link #CONTENT_URI} for a list of groups.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GROUPS;

        // MIME type of the {@link #CONTENT_URI} for a single group.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GROUPS;

        public final static String TABLE_NAME = "groups";

        // Columns
        public final static String _ID = BaseColumns._ID;                       // INTEGER
        public final static String COLUMN_GROUP_NAME = "name";                  // TEXT
        public final static String COLUMN_GROUP_SUBJECT_CODE = "subject_code";  // INTEGER
        public final static String COLUMN_GROUP_USER_COUNT = "user_count";      // INTEGER
        public final static String COLUMN_GROUP_LOCATION = "location";          // TEXT
        public final static String COLUMN_GROUP_RATING = "rating";              // INTEGER
    }

    // Inner class for exercise_set table
    public static final class UserEntry implements BaseColumns {
        // Content uri to access exercise_set data via the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USERS);

        // MIME type of the {@link #CONTENT_URI} for a list of users.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;

        // MIME type of the {@link #CONTENT_URI} for a single user.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;

        public final static String TABLE_NAME = "users";

        // Columns
        public final static String _ID_STUDENT = BaseColumns._ID;                       // INTEGER
        public final static String COLUMN_USER_PSEUDONAME = "pseudoname";
        public final static String COLUMN_USER_LOCATION = "location";
    }

    public static final class GroupMessageEntry implements BaseColumns {
        // Content uri to access exercise_set data via the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GROUPMESSAGES);

        // MIME type of the {@link #CONTENT_URI} for a list of users.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GROUPMESSAGES;

        // MIME type of the {@link #CONTENT_URI} for a single user.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GROUPMESSAGES;

        public final static String TABLE_NAME = "groupmessages";

        // Columns
        public final static String _ID = BaseColumns._ID;                       // INTEGER
        public final static String COLUMN_GROUP_ID = "group";
        public final static String COLUMN_USER_ID = "fromUserId";
        public final static String COLUMN_USER_NAME = "fromUserName";
        public final static String COLUMN_TIME_STAMP = "zonedDateTime";
        public final static String COLUMN_MESSAGE_TEXT = "messageText";
    }
}
