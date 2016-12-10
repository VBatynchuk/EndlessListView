package com.batynchuk.endlesslistview.data;

import android.content.Context;

import com.batynchuk.endlesslistview.models.User;

import java.util.List;

/**
 * Created by Батинчук on 07.12.2016.
 */

public class DbHandler {

    private static DbHandler mInstance = null;

    private UsersDbHelper mHelper;

    private DbHandler(Context context) {
        this.mHelper = new UsersDbHelper(context);
    }

    public static DbHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DbHandler(context);
        }
        return mInstance;
    }

    public List<User> getUserList(int page) {
        return mHelper.selectPartUsers(mHelper.getWritableDatabase(), page);
    }
}
