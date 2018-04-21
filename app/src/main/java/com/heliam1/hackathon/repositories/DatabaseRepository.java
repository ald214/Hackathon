package com.heliam1.hackathon.repositories;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.heliam1.hackathon.models.Group;
import com.heliam1.hackathon.models.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class DatabaseRepository implements GroupsRepository, UserRepository {
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

    private List<Workout> queryWorkouts() {
        String[] projection = {
                WorkoutEntry._ID,
                WorkoutEntry.COLUMN_WORKOUT_NAME,
                WorkoutEntry.COLUMN_WORKOUT_IMAGE,
                WorkoutEntry.COLUMN_WORKOUT_LAST_DATE_COMPLETED,
                WorkoutEntry.COLUMN_WORKOUT_DURATION};

        String sortOrder = WorkoutEntry._ID;

        Cursor cursor = contentResolver.query(ExerciseSetEntry.CONTENT_URI,
                projection, null, null, sortOrder);

        List<Workout> workouts = new ArrayList<Workout>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            workouts.add(new Workout(
                    cursor.getLong(cursor.getColumnIndex(WorkoutEntry._ID)),
                    cursor.getString(cursor.getColumnIndex(WorkoutEntry.COLUMN_WORKOUT_NAME)),
                    cursor.getInt(cursor.getColumnIndex(WorkoutEntry.COLUMN_WORKOUT_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(
                            WorkoutEntry.COLUMN_WORKOUT_LAST_DATE_COMPLETED)),
                    cursor.getInt(cursor.getColumnIndex(WorkoutEntry.COLUMN_WORKOUT_DURATION))));
            cursor.moveToNext();
        }
        cursor.close();

        return workouts;
    }

    private List<ExerciseSet> queryExerciseSetsByWorkoutId(long workoutId) {
        String[] projection = {
                ExerciseSetEntry._ID,
                ExerciseSetEntry._WORKOUT_ID,
                ExerciseSetEntry.COLUMN_EXERCISE_NAME,
                ExerciseSetEntry.COLUMN_SET_NUMBER,
                ExerciseSetEntry.COLUMN_SET_DURATION,
                ExerciseSetEntry.COLUMN_SET_REST,
                ExerciseSetEntry.COLUMN_SET_WEIGHT,
                ExerciseSetEntry.COLUMN_SET_REPS,
                ExerciseSetEntry.COLUMN_SET_DATE,
                ExerciseSetEntry.COLUMN_SET_ORDER,
                ExerciseSetEntry.COLUMN_PB_WEIGHT,
                ExerciseSetEntry.COLUMN_PB_REPS};

        String selection = ExerciseSetEntry._WORKOUT_ID + "=?";

        String[] selectionArgs = {Long.toString(workoutId)};

        // String sortOrder = ExerciseSetEntry.COLUMN_SET_DATE + " DESC";
        String sortOrder = ExerciseSetEntry.COLUMN_SET_ORDER + " ASC";

        Cursor cursor = contentResolver.query(ExerciseSetEntry.CONTENT_URI,
                projection, selection, selectionArgs, sortOrder);

        List<ExerciseSet> exerciseSets = new ArrayList<ExerciseSet>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            exerciseSets.add(new ExerciseSet(
                    cursor.getLong(cursor.getColumnIndex(ExerciseSetEntry._ID)),
                    cursor.getLong(cursor.getColumnIndex(ExerciseSetEntry._WORKOUT_ID)),
                    cursor.getString(cursor.getColumnIndex(ExerciseSetEntry.COLUMN_EXERCISE_NAME)),
                    cursor.getInt(cursor.getColumnIndex(ExerciseSetEntry.COLUMN_SET_NUMBER)),
                    cursor.getInt(cursor.getColumnIndex(ExerciseSetEntry.COLUMN_SET_DURATION)),
                    cursor.getInt(cursor.getColumnIndex(ExerciseSetEntry.COLUMN_SET_REST)),
                    cursor.getDouble(cursor.getColumnIndex(ExerciseSetEntry.COLUMN_SET_WEIGHT)),
                    cursor.getInt(cursor.getColumnIndex(ExerciseSetEntry.COLUMN_SET_REPS)),
                    cursor.getString(cursor.getColumnIndex(ExerciseSetEntry.COLUMN_SET_DATE)),
                    cursor.getInt(cursor.getColumnIndex(ExerciseSetEntry.COLUMN_SET_ORDER)),
                    cursor.getInt(cursor.getColumnIndex(ExerciseSetEntry.COLUMN_PB_WEIGHT)),
                    cursor.getInt(cursor.getColumnIndex(ExerciseSetEntry.COLUMN_PB_REPS))));

            cursor.moveToNext();
        }
        cursor.close();

        return exerciseSets;
    }

    private Long upsertWorkout(Workout workout) {
        ContentValues values = new ContentValues();
        values.put(WorkoutEntry.COLUMN_WORKOUT_NAME, workout.getName());
        values.put(WorkoutEntry.COLUMN_WORKOUT_IMAGE, workout.getImage());
        values.put(WorkoutEntry.COLUMN_WORKOUT_LAST_DATE_COMPLETED, workout.getDate());
        values.put(WorkoutEntry.COLUMN_WORKOUT_DURATION, workout.getDuration());

        // uri or Long savedWorkoutId; deffs want long but convert from uri?
        Uri uri;

        if (workout.hasId()) {
            contentResolver.update(
                    ContentUris.withAppendedId(WorkoutEntry.CONTENT_URI, workout.getId()),
                    values, null, null);

            // TODO:
            uri = ContentUris.withAppendedId(WorkoutEntry.CONTENT_URI, workout.getId());
        } else {
            uri = contentResolver.insert(WorkoutEntry.CONTENT_URI, values);
        }
        return parseUriToId(uri);
    }

    private Long upsertExerciseSet(ExerciseSet exerciseSet) {
        ContentValues values = new ContentValues();
        values.put(ExerciseSetEntry._WORKOUT_ID, exerciseSet.getWorkoutId());
        values.put(ExerciseSetEntry.COLUMN_EXERCISE_NAME, exerciseSet.getExerciseName());
        values.put(ExerciseSetEntry.COLUMN_SET_NUMBER, exerciseSet.getSetNumber());
        values.put(ExerciseSetEntry.COLUMN_SET_DURATION, exerciseSet.getSetDuration());
        values.put(ExerciseSetEntry.COLUMN_SET_REST, exerciseSet.getSetRest());
        values.put(ExerciseSetEntry.COLUMN_SET_WEIGHT, exerciseSet.getSetWeight());
        values.put(ExerciseSetEntry.COLUMN_SET_REPS, exerciseSet.getSetReps());
        values.put(ExerciseSetEntry.COLUMN_SET_DATE, exerciseSet.getSetDate());
        values.put(ExerciseSetEntry.COLUMN_SET_ORDER, exerciseSet.getSetOrder());
        values.put(ExerciseSetEntry.COLUMN_PB_WEIGHT, exerciseSet.getPbWeight());
        values.put(ExerciseSetEntry.COLUMN_PB_REPS, exerciseSet.getPbReps());

        // uri or Long savedWorkoutId; deffs want long but convert from uri?
        Uri uri;

        if (exerciseSet.hasId()) {
            contentResolver.update(
                    ContentUris.withAppendedId(ExerciseSetEntry.CONTENT_URI, exerciseSet.getId()),
                    values, null, null);

            // TODO:
            uri = ContentUris.withAppendedId(ExerciseSetEntry.CONTENT_URI, exerciseSet.getId());
        } else {
            uri = contentResolver.insert(ExerciseSetEntry.CONTENT_URI, values);
        }
        return parseUriToId(uri);
    }
}
