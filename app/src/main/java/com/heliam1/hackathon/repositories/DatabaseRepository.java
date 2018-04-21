package com.heliam1.hackathon.repositories;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.heliam1.hackathon.models.Group;
import com.heliam1.hackathon.models.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import com.heliam1.hackathon.data.HackathonContract.GroupEntry;
import com.heliam1.hackathon.data.HackathonContract.UserEntry;

public class DatabaseRepository implements GroupsRepository, UserRepository {
    public static final String LOG_TAG = DatabaseRepository.class.getSimpleName();

    private final ContentResolver contentResolver;

    public DatabaseRepository(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    @Override
    public Single<List<Group>> getGroups() {
        return Single.fromCallable(() -> {
            try {
                return queryGroups();
            } catch (Exception e) {
                Log.v(LOG_TAG, "error querying for groups");
                throw new RuntimeException("Something wrong with db");
            }
        });
    }

    @Override
    public Single<List<User>> getUser() {
        return Single.fromCallable(() -> {
            try {
                return queryUser();
            } catch (Exception e) {
                throw new RuntimeException("Something wrong with db");
            }
        });
    }

    private List<Group> queryGroups() {
        String[] projection = {
                GroupEntry._ID,
                GroupEntry.COLUMN_GROUP_NAME,
                GroupEntry.COLUMN_GROUP_SUBJECT_CODE,
                GroupEntry.COLUMN_GROUP_USER_COUNT,
                GroupEntry.COLUMN_GROUP_LOCATION,
                GroupEntry.COLUMN_GROUP_RATING};

        Log.v(LOG_TAG, "projection reached");

        Cursor cursor = contentResolver.query(GroupEntry.CONTENT_URI,
                projection, null, null, null);

        List<Group> groups = new ArrayList<Group>();

        Log.v(LOG_TAG, "just about to begin converting cursor to group list");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            groups.add(new Group(
                    cursor.getLong(cursor.getColumnIndex(GroupEntry._ID)),
                    new Location(cursor.getString(cursor.getColumnIndex(GroupEntry.COLUMN_GROUP_LOCATION))),
                    cursor.getString(cursor.getColumnIndex(GroupEntry.COLUMN_GROUP_NAME)),
                    cursor.getInt(cursor.getColumnIndex(GroupEntry.COLUMN_GROUP_SUBJECT_CODE)),
                    cursor.getInt(cursor.getColumnIndex(GroupEntry.COLUMN_GROUP_USER_COUNT)),
                    cursor.getInt(cursor.getColumnIndex(GroupEntry.COLUMN_GROUP_RATING))));

            Log.v(LOG_TAG, "group added to list");

            cursor.moveToNext();
        }
        cursor.close();

        return groups;
    }

    private List<User> queryUser() {
        String[] projection = {
                UserEntry._ID_STUDENT,
                UserEntry.COLUMN_USER_PSEUDONAME,
                UserEntry.COLUMN_USER_LOCATION};

        Cursor cursor = contentResolver.query(UserEntry.CONTENT_URI,
                projection, null, null, null);

        List<User> users = new ArrayList<User>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            users.add(new User(
                    cursor.getLong(cursor.getColumnIndex(UserEntry._ID_STUDENT)),
                    cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_USER_PSEUDONAME)),
                    new Location(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_USER_LOCATION)))));

            cursor.moveToNext();
        }
        cursor.close();

        return users;
    }

    @Override
    public Single<Long> saveGroup(Group group) {
        return Single.fromCallable(() -> {
            try {
                return upsertGroup(group);
            } catch (Exception e) {
                throw new RuntimeException("Something wrong with db");
            }
        });
    }

    @Override
    public Single<Long> saveUser(User user) {
        return Single.fromCallable(() -> {
            try {
                return upsertUser(user);
            } catch (Exception e) {
                throw new RuntimeException("Something wrong with db");
            }
        });
    }

    private Long upsertGroup(Group group) {
        ContentValues values = new ContentValues();
        values.put(GroupEntry.COLUMN_GROUP_NAME, group.getName());
        values.put(GroupEntry.COLUMN_GROUP_SUBJECT_CODE, group.getSubjectCode());
        values.put(GroupEntry.COLUMN_GROUP_USER_COUNT, group.getUserCount());
        values.put(GroupEntry.COLUMN_GROUP_LOCATION, group.getLocation().toString());
        values.put(GroupEntry.COLUMN_GROUP_RATING, group.getRating());

        // uri or Long savedWorkoutId; deffs want long but convert from uri?
        Uri uri;

        if (group.hasId()) {
            contentResolver.update(
                    ContentUris.withAppendedId(GroupEntry.CONTENT_URI, group.getId()),
                    values, null, null);

            // TODO:
            uri = ContentUris.withAppendedId(GroupEntry.CONTENT_URI, group.getId());
        } else {
            uri = contentResolver.insert(GroupEntry.CONTENT_URI, values);
        }
        return parseUriToId(uri);
    }

    private Long upsertUser(User user) {
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_USER_PSEUDONAME, user.getPseudoname());
        values.put(UserEntry.COLUMN_USER_LOCATION, user.getLocation().toString());

        // uri or Long savedWorkoutId; deffs want long but convert from uri?
        Uri uri;

        if (user.hasId()) {
            contentResolver.update(
                    ContentUris.withAppendedId(UserEntry.CONTENT_URI, user.getStudentId()),
                    values, null, null);

            // TODO:
            uri = ContentUris.withAppendedId(UserEntry.CONTENT_URI, user.getStudentId());
        } else {
            uri = contentResolver.insert(UserEntry.CONTENT_URI, values);
        }
        return parseUriToId(uri);
    }

    @Override
    public Single<Long> deleteGroup(Group group) {
        return Single.fromCallable(() -> {
            try {
                Long id  = group.getId();
                contentResolver.delete(
                        ContentUris.withAppendedId(GroupEntry.CONTENT_URI, group.getId()),
                        null, null);
                return id;
            } catch (Exception e) {
                throw new RuntimeException("Something wrong with db");
            }
        });
    }

    @Override
    public Single<Long> deleteUser(User user) {
        return Single.fromCallable(() -> {
            try {
                Long id  = user.getStudentId();
                contentResolver.delete(
                        ContentUris.withAppendedId(UserEntry.CONTENT_URI, id),
                        null, null);
                return id;
            } catch (Exception e) {
                throw new RuntimeException("Something wrong with db");
            }
        });
    }

    Long parseUriToId(Uri uri) {
        String idString = uri.toString();
        idString = idString.replaceFirst("1","");
        String idValueString = idString.replaceAll("[^0-9]", "");
        Log.v("Task:", "extracted long id: " + idValueString);
        return Long.parseLong(idValueString);
    }
}
