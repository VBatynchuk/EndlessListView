package com.batynchuk.endlesslistview.data;

import android.provider.BaseColumns;

/**
 * Created by Батинчук on 05.12.2016.
 */

class DataContract {

    private DataContract() {
    }

    static class DataEntry implements BaseColumns {
        static final String TABLE_NAME = "users";
        static final String COLUMN_FIRST_NAME = "first_name";
        static final String COLUMN_LAST_NAME = "last_name";

    }
}
