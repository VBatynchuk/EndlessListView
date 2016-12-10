package com.batynchuk.endlesslistview.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.batynchuk.endlesslistview.MainActivity;
import com.batynchuk.endlesslistview.models.User;
import com.batynchuk.endlesslistview.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Батинчук on 05.12.2016.
 */

public class UsersDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database.db";

    public UsersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_DATA_ENTRIES =
                "CREATE TABLE " +
                        DataContract.DataEntry.TABLE_NAME +
                        " (" +
                        DataContract.DataEntry._ID + " INTEGER PRIMARY KEY," +
                        DataContract.DataEntry.COLUMN_FIRST_NAME + " TEXT," +
                        DataContract.DataEntry.COLUMN_LAST_NAME + " TEXT" +
                        " )";

        sqLiteDatabase.execSQL(SQL_CREATE_DATA_ENTRIES);
        insertDummyData(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void insertDummyData(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();

        for (int i = 0; i < 1000; i++) {
            contentValues.put(DataContract.DataEntry.COLUMN_FIRST_NAME, "firtst_name" + i);
            contentValues.put(DataContract.DataEntry.COLUMN_LAST_NAME, "last_name" + i);

            database.insert(DataContract.DataEntry.TABLE_NAME, null, contentValues);
        }
    }

    public List<User> selectPartUsers(SQLiteDatabase database, int page) {
        List<User> users = new ArrayList<>();

        Cursor cursor = getUsersFromDb(database, page);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    User user = getUserFromCursor(cursor);
                    users.add(user);
                } while (cursor.moveToNext());
            } else Log.e("TAG1", "Can't move cursor to first");
            cursor.close();
        } else {
            Log.e("TAG1", "Cursor is null");
        }
        return users;
    }

    private Cursor getUsersFromDb(SQLiteDatabase database, int page) {
        int limit = Constants.PAGE_LIMIT;
        int offset = page * limit;

        String query = "SELECT * FROM " +
                DataContract.DataEntry.TABLE_NAME +
                " LIMIT ?,?";

        String[] selectionArgs = new String[]{
                "" + offset,
                "" + limit
        };

        Cursor cursor = null;
        database.beginTransaction();
        try {
            cursor = database.rawQuery(query, selectionArgs);
            Log.v("TAG1", "load page:" + page);

        } catch (Exception e) {
            Log.e("TAG1", e.getMessage(), e);
        } finally {
            database.endTransaction();
        }
        return cursor;
    }

    private User getUserFromCursor(Cursor cursor) {
        User user = new User();
        user.setFirstName(cursor.getString(
                cursor.getColumnIndexOrThrow(DataContract.DataEntry.COLUMN_FIRST_NAME)));
        user.setLastName(cursor.getString(
                cursor.getColumnIndexOrThrow(DataContract.DataEntry.COLUMN_LAST_NAME)));
        return user;
    }

}
