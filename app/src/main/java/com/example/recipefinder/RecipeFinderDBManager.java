package com.example.recipefinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeFinderDBManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "RecipeFinderDB";
    private static final int DB_VERSION = 1;

    private static final String USERS_TABLE_NAME = "users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    private static final String RECIPES_TABLE_NAME = "recipes";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_IMAGE_ID = "imgDrawableId";
    private static final String COLUMN_INGREDIENTS = "ingredients";
    private static final String COLUMN_DIRECTIONS = "directions";
    private static final String COLUMN_CUISINE = "cuisine";
    private static final String COLUMN_SERVING = "serving";
    private static final String COLUMN_PREP_TIME = "prepTime";
    private static final String COLUMN_COOK_TIME = "cookTime";
    private static final String COLUMN_TOTAL_TIME = "totalTime";

    /*
    String dropRecipesTableCmd = "DROP TABLE IF EXISTS " + "recipes;";
    String createRecipesTableCmd = "CREATE TABLE recipes (title TEXT PRIMARY KEY, imgDrawableId INTEGER,  ingredients TEXT, " +
            "directions TEXT, "+
            "cuisine TEXT, serving TEXT, prepTime TEXT, cookTime TEXT, totalTime TEXT);";
    */

    //singleton Pattern ========================================================
    private static RecipeFinderDBManager mInstance = null;

    public static synchronized RecipeFinderDBManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new RecipeFinderDBManager(context.getApplicationContext());
        }

        return mInstance;
    }
    //==========================================================================

    private RecipeFinderDBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        //String setPRAGMAForeignKeysOn = "PRAGMA foreign_keys = ON;";

        // query to create table
        String createUsersQuery = "CREATE TABLE " + USERS_TABLE_NAME + "(" + COLUMN_USERNAME + " TEXT NOT NULL PRIMARY KEY, " +
                                                        COLUMN_EMAIL + " TEXT NOT NULL, " +
                                                        COLUMN_PASSWORD + " TEXT NOT NULL);";


        String createRecipesQuery = "CREATE TABLE " + RECIPES_TABLE_NAME + "(" + COLUMN_TITLE + " TEXT PRIMARY KEY, " +
                                                        COLUMN_IMAGE_ID + " INTEGER, " +
                                                        COLUMN_INGREDIENTS + " TEXT, " +
                                                        COLUMN_DIRECTIONS + " TEXT, " +
                                                        COLUMN_CUISINE + " TEXT, " +
                                                        COLUMN_SERVING + " TEXT, " +
                                                        COLUMN_PREP_TIME + " TEXT, " +
                                                        COLUMN_COOK_TIME + " TEXT, " +
                                                        COLUMN_TOTAL_TIME + " TEXT);";

        // execute string to create table

        //db.execSQL(setPRAGMAForeignKeysOn);
        db.execSQL(createUsersQuery);
        db.execSQL(createRecipesQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop and create table if exists
        String dropUsersTableCmd = "DROP TABLE IF EXISTS " + USERS_TABLE_NAME + ";";
        String dropRecipesTableCmd = "DROP TABLE IF EXISTS " + RECIPES_TABLE_NAME + ";";

        db.execSQL(dropUsersTableCmd);
        db.execSQL(dropRecipesTableCmd);
        onCreate(db);
    }

    // add user
    boolean addUser(String username, String email, String password) {
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_USERNAME, username);
        vals.put(COLUMN_EMAIL, email);
        vals.put(COLUMN_PASSWORD, password);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(USERS_TABLE_NAME, null, vals) != -1;
    }

    // read user
    Cursor getAllUsers() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + USERS_TABLE_NAME, null);
    }

    public boolean addRecipe(String title, int imgDrawableId, String ingredients, String directions,
                                String cuisine, String serving, String prepTime, String cookTime, String totalTime ){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_IMAGE_ID, imgDrawableId);
        contentValues.put(COLUMN_INGREDIENTS, ingredients);
        contentValues.put(COLUMN_DIRECTIONS, directions);
        contentValues.put(COLUMN_CUISINE, cuisine);
        contentValues.put(COLUMN_SERVING, serving);
        contentValues.put(COLUMN_PREP_TIME, prepTime);
        contentValues.put(COLUMN_COOK_TIME, cookTime);
        contentValues.put(COLUMN_TOTAL_TIME, totalTime);

        SQLiteDatabase db = getWritableDatabase();

        return db.insert(RECIPES_TABLE_NAME, null, contentValues) != -1;

    }

    public Cursor getAllRecipes(){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + RECIPES_TABLE_NAME, null);
    }

}
