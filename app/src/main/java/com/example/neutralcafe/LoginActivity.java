package com.example.neutralcafe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private DatabaseCust databaseCust;  // Declare your DatabaseHelper (or DatabaseCust)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout); // Ensure this layout exists

        // Initialize the DatabaseCust (or DatabaseHelper)
        databaseCust = new DatabaseCust(this);

        // Link UI elements to XML
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        Button loginButton = findViewById(R.id.loginButton);
        TextView newAccountLink = findViewById(R.id.newAccountLink);

        // Handle Login Button Click
        loginButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            // Validate input before proceeding with the login check
            if (validateInput(username, password)) {
                // Verify credentials using the DatabaseHelper
                if (databaseCust.checkUserCredentials(username, password)) {
                    // If credentials are correct, proceed to HomePageActivity
                    // Navigate to ProfileActivity with the username
                    String Username = "username"; // Replace this with the actual username from LoginActivity
                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    intent.putExtra("USERNAME", username); // Pass the actual username entered
                    startActivity(intent);
                    finish();

                } else {
                    // Show error message if credentials do not match
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle "New Account" link
        newAccountLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to validate input fields
    private boolean validateInput(String username, String password) {
        if (username.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Optionally, add more validation (like checking for username format or password length)
        return true;
    }
}
