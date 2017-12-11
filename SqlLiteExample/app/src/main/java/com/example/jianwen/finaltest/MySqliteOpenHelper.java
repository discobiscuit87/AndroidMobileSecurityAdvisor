package com.example.jianwen.finaltest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianwen on 3/11/17.
 */

public class MySqliteOpenHelper extends SQLiteOpenHelper {

    private static final String database_name = "company.db";

    private static final String table_name = "employee";

    private static final int database_version = 1;

    private static final String column_id = "id",column_name="name";

    private String database_create_statement = "Create table " + table_name+ "(" + column_id +
            " integer primary key autoincrement," + column_name + " text not null);";

    public MySqliteOpenHelper(Context context)
    {
        super(context,database_name,null,database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

        sqLiteDatabase.execSQL(database_create_statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlLiteDatabase, int i, int i1)
    {

    }

    public void insertTest(String text)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(column_name, text);

        // insert row
        long todo_id = db.insert(table_name, null, values);
    }

    public List<String> getAllTest(){
        List<String> allText = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + table_name;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                allText.add(c.getString(c.getColumnIndex(column_name)));
            } while (c.moveToNext());
        }
        return allText;
    }

}
