package com.example.neutralcafe;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseCust databaseCust;
    private EditText usernameField, nameField, emailField, passwordField, phoneNumberField;
    private TextView usernameError, emailError, passwordError, phoneError, haveAccountLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        // Initialize DatabaseHelper
        databaseCust = new DatabaseCust(this);

        // Link UI elements
        usernameField = findViewById(R.id.usernameField);
        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        phoneNumberField = findViewById(R.id.phoneNumberField);
        usernameError = findViewById(R.id.usernameError);
        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);
        phoneError = findViewById(R.id.phoneError);

        Button registerButton = findViewById(R.id.registerButton);
        TextView newAccountLink = findViewById(R.id.haveAccountLink);

        // Handle Register button click
        registerButton.setOnClickListener(v -> {
            // Reset error messages
            resetErrorMessages();

            // Get input data
            String username = usernameField.getText().toString().trim();
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String phoneNumber = phoneNumberField.getText().toString().trim();

            if (validateInput(username, name, email, password, phoneNumber)) {
                // Store user data in SQLite
                if (storeUserData(username, name, email, password, phoneNumber)) {
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    redirectToLoginPage();
                } else {
                    Toast.makeText(RegisterActivity.this, "Username, email, or phone already exists!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle "New Account" link
        newAccountLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });


    }

    private void resetErrorMessages() {
        usernameError.setText("");
        emailError.setText("");
        passwordError.setText("");
        phoneError.setText("");
    }

    private boolean validateInput(String username, String name, String email, String password, String phoneNumber) {
        boolean isValid = true;

        if (username.isEmpty()) {
            usernameError.setText("Cannot leave blank");
            isValid = false;
        }

        if (email.isEmpty()) {
            emailError.setText("Cannot leave blank");
            isValid = false;
        }

        if (password.isEmpty()) {
            passwordError.setText("Cannot leave blank");
            isValid = false;
        }

        if (phoneNumber.isEmpty()) {
            phoneError.setText("Cannot leave blank");
            isValid = false;
        }

        // Check if user data already exists
        if (isUserExists(username)) {
            usernameError.setText("Username already exists!");
            isValid = false;
        }

        if (isEmailExists(email)) {
            emailError.setText("Email already exists!");
            isValid = false;
        }

        if (isPhoneExists(phoneNumber)) {
            phoneError.setText("Phone already exists!");
            isValid = false;
        }

        return isValid;
    }

    private boolean isUserExists(String username) {
        SQLiteDatabase db = databaseCust.getReadableDatabase();
        String[] projection = {DatabaseCust.COLUMN_USERNAME};
        String selection = databaseCust.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(databaseCust.TABLE_USER, projection, selection, selectionArgs, null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    private boolean isEmailExists(String email) {
        SQLiteDatabase db = databaseCust.getReadableDatabase();
        String[] projection = {DatabaseCust.COLUMN_EMAIL};
        String selection = DatabaseCust.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(DatabaseCust.TABLE_USER, projection, selection, selectionArgs, null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    private boolean isPhoneExists(String phoneNumber) {
        SQLiteDatabase db = databaseCust.getReadableDatabase();
        String[] projection = {DatabaseCust.COLUMN_PHONE};
        String selection = DatabaseCust.COLUMN_PHONE + " = ?";
        String[] selectionArgs = {phoneNumber};
        Cursor cursor = db.query(DatabaseCust.TABLE_USER, projection, selection, selectionArgs, null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    private boolean storeUserData(String username, String name, String email, String password, String phoneNumber) {
        SQLiteDatabase db = databaseCust.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseCust.COLUMN_USERNAME, username);
        values.put(DatabaseCust.COLUMN_NAME, name);
        values.put(DatabaseCust.COLUMN_EMAIL, email);
        values.put(DatabaseCust.COLUMN_PASSWORD, password);
        values.put(DatabaseCust.COLUMN_PHONE, phoneNumber);

        long newRowId = db.insert(DatabaseCust.TABLE_USER, null, values);
        db.close();
        return newRowId != -1;
    }

    private void redirectToLoginPage() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}