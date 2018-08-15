package com.lorem.simpledictionary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lorem.simpledictionary.db.DatabaseContract.WordColumns;

import static com.lorem.simpledictionary.db.DatabaseContract.TABLE_EN;
import static com.lorem.simpledictionary.db.DatabaseContract.TABLE_ID;

/**
 * Created by CHEVALIER-11 on 09-Aug-18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "db_dict_app";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_EN = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_EN,
            WordColumns._ID,
            WordColumns.WORD,
            WordColumns.TRANSLATION
    );

    private static final String SQL_CREATE_TABLE_ID = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_ID,
            WordColumns._ID,
            WordColumns.WORD,
            WordColumns.TRANSLATION
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_EN);
        db.execSQL(SQL_CREATE_TABLE_ID);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ID);
        onCreate(db);
    }
}