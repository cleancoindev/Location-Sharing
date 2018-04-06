package com.classify.locationsharing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ADMIN on 06-04-2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LocationSharing";
    private static final String table_name = "Location_value";

    private static final String id = "id";
    private static final String global_uid = "uid";
    private static final String global_grant = "grant";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String Create_table = "CREATE TABLE " + table_name + "("
                + id + " INTEGER PRIMARY KEY AUTOINCREMENT," + global_uid + " TEXT,"
                + global_grant + " TEXT)";
        sqLiteDatabase.execSQL(Create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(sqLiteDatabase);
    }


    public void globaladdData(String uid,String grant) {
        SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(global_uid, uid);
            values.put(global_grant, grant);
            db.insert(table_name, null, values);
        db.close();
    }

    public String getUids()
    {
        String value = "";
        String query = "SELECT "+global_uid+" FROM "+ table_name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                value = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return value;
    }

    public String getGrant()
    {
        String value = "";
        String query = "SELECT "+global_grant+" FROM "+ table_name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                value = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return value;
    }


    public void updateGrant(String val){
        String query = "UPDATE "+table_name+" SET "+ global_grant +"= '" +val+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }
}
