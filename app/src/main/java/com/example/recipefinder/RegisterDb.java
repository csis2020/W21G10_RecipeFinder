package com.example.recipefinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RegisterDb extends SQLiteOpenHelper {
    // define all strings required for database
    private static final String DBNAME = "Usersdb";
    private static final String TABNAME = "users";
    private static final int DBVER = 1;
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    // constructor
    RegisterDb(Context context) {
        super(context, DBNAME, null, DBVER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // query to create table
        String sql = "CREATE TABLE " + TABNAME + "(" + COL_USERNAME + " TEXT NOT NULL PRIMARY KEY, " + COL_EMAIL + " TEXT NOT NULL, " + COL_PASSWORD + " TEXT NOT NULL);";
        // execute string to create table
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // drop and create table if exists
        String sql = "DROP TABLE IF EXISTS " + TABNAME + ";";
        db.execSQL(sql);
        onCreate(db);
    }

    // add user
    boolean addUser(String username, String email, String password) {
        ContentValues vals = new ContentValues();
        vals.put(COL_USERNAME, username);
        vals.put(COL_EMAIL, email);
        vals.put(COL_PASSWORD, password);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABNAME, null, vals) != -1;
    }

    // read user
    Cursor getAllUsers() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABNAME, null);
    }
}
