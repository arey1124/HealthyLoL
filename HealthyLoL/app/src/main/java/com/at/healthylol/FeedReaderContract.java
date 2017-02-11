package com.at.healthylol;

import android.provider.BaseColumns;

/**
 * Created by Anirudh Trigunayat on 11-02-2017.
 */

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_TITLE = "doctor";
        
    }
}