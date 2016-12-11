package com.batynchuk.endlesslistview.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.batynchuk.endlesslistview.models.User;

import java.util.List;

/**
 * Created by Батинчук on 07.12.2016.
 */

public class DbHandler {

    private static DbHandler mInstance = null;

    private UsersDbHelper dbHelper;
    private SQLiteDatabase database;

    private DbHandler(Context context) {
        dbHelper = new UsersDbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public static DbHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DbHandler(context);
        }
        return mInstance;
    }

    public List<User> getUserList(int page) {
        return dbHelper.selectPartUsers(database, page);
    }

    public int getUserCount() {
        return dbHelper.getUserCount(database);
    }
}
