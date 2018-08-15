package com.lorem.simpledictionary.db;

import android.provider.BaseColumns;

/**
 * Created by CHEVALIER-11 on 09-Aug-18.
 */

public class DatabaseContract {

    public static String TABLE_EN = "ENGLISH";
    public static String TABLE_ID = "BAHASA";
    static final class WordColumns implements BaseColumns {
        static String WORD = "WORD";
        static String TRANSLATION = "TRANSLATION";
    }

}
