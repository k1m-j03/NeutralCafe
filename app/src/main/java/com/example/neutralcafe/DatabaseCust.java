package com.example.neutralcafe;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class DatabaseCust extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Define the User table and columns
    public static final String TABLE_USER = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";


    public DatabaseCust(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PHONE + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    // Method to check if user exists with matching username and password
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_USER, null, selection, selectionArgs, null, null, null);

        boolean userExists = cursor.moveToFirst();
        cursor.close();
        db.close();

        return userExists;
    }

    // Fetch user by username
    public Cursor getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            Log.e("DatabaseCust", "getUserByUsername called with null or empty username");
            return null;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USERNAME + " = ?";
        return db.rawQuery(query, new String[]{username});
    }


    // Fetch user details as a HashMap
    public HashMap<String, String> getUserDetails(String username) {
        HashMap<String, String> userDetails = new HashMap<>();
        Cursor cursor = getUserByUsername(username);

        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);

            if (nameIndex != -1 && phoneIndex != -1) {
                userDetails.put("name", cursor.getString(nameIndex));
                userDetails.put("phone", cursor.getString(phoneIndex));
            } else {
                Log.e("DatabaseCust", "Column index not found for 'name' or 'phone'");
            }

            cursor.close();
        } else {
            Log.e("DatabaseCust", "Cursor is null or empty for username: " + username);
        }

        return userDetails;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = super.getReadableDatabase();
        if (!db.isOpen()) {
            Log.e("DatabaseCust", "Database is not open.");
        } else {
            Log.d("DatabaseCust", "Database opened successfully");
        }
        return db;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        if (!db.isOpen()) {
            Log.e("DatabaseCust", "Database is not open.");
        } else {
            Log.d("DatabaseCust", "Database is open.");
        }
        return db;
    }


    public HashMap<String, String> TABLE_USER(String username) {
        return null;
    }
}
