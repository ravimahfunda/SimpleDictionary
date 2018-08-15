package com.lorem.simpledictionary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.lorem.simpledictionary.MainActivity;
import com.lorem.simpledictionary.Word;
import com.lorem.simpledictionary.db.DatabaseContract;

import java.sql.SQLException;
import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.lorem.simpledictionary.db.DatabaseContract.WordColumns.TRANSLATION;
import static com.lorem.simpledictionary.db.DatabaseContract.WordColumns.WORD;

/**
 * Created by CHEVALIER-11 on 09-Aug-18.
 */

public class OperationHelper {

    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public OperationHelper(Context context){
        this.context = context;
    }

    public OperationHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dataBaseHelper.close();
    }

    public ArrayList<Word> query(String tableName, String filter){
        ArrayList<Word> arrayList = new ArrayList<Word>();
        Cursor cursor;

        if (!filter.equalsIgnoreCase(""))
            cursor = database.query(tableName,null,WORD+" LIKE ?",new String[]{filter+"%"},null,null,_ID + " ASC",null);
        else
            cursor = database.query(tableName,null,null,null,null,null,WORD +" ASC",null);

        cursor.moveToFirst();
        Word word;
        if (cursor.getCount()>0) {
            do {
                word = new Word();
                word.setWord(cursor.getString(cursor.getColumnIndexOrThrow(WORD)));
                word.setTranslation(cursor.getString(cursor.getColumnIndexOrThrow(TRANSLATION)));
                arrayList.add(word);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public void beginTransaction(){
        database.beginTransaction();
    }

    public void setTransactionSuccess(){
        database.setTransactionSuccessful();
    }

    public void endTransaction(){
        database.endTransaction();
    }

    public void insertTransaction(String tableName, Word word){
        String sql = "INSERT INTO "+tableName+" ("+WORD+", "+TRANSLATION
                +") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, word.getWord());
        stmt.bindString(2, word.getTranslation());
        stmt.execute();
        stmt.clearBindings();
    }

}