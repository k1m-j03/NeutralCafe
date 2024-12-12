package com.example.neutralcafe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ProfileActivity extends AppCompatActivity {
    private TextView nameText, phoneText, emailText;
    private DrawerLayout drawerLayout;
    private static final String TAG = "ProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        // Initialize Views
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        ImageView menuIcon = findViewById(R.id.menu_icon);

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        phoneText = findViewById(R.id.phoneText);

        // Set up Navigation Drawer Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle Menu Icon Click
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Handle Navigation Drawer Item Clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    startActivity(new Intent(ProfileActivity.this, HomePageActivity.class));
                } else if (id == R.id.menu) {
                    startActivity(new Intent(ProfileActivity.this, MenuListActivity.class));
                } else if (id == R.id.reserve) {
                    startActivity(new Intent(ProfileActivity.this, Reserve1Activity.class));
                } else if (id == R.id.logout) {
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish(); // Finish current activity after logout
                } else {
                    Log.w(TAG, "Unhandled navigation item selected");
                }

                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer after selection
                return true;
            }
        });

        // Fetch user data based on the Username passed via Intent
        String username = getIntent().getStringExtra("USERNAME"); // Updated key
        if (username != null) {
            Log.d(TAG, "Received Username: " + username);
            fetchUserData(username);
        } else {
            Log.e(TAG, "No Username provided in intent");
            Toast.makeText(this, "No Username provided", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchUserData(String username) {
        Log.d(TAG, "Fetching data for username: " + username);

        DatabaseCust databaseCust = new DatabaseCust(this);
        Cursor cursor = databaseCust.getUserByUsername(username);

        if (cursor != null) {
            Log.d(TAG, "Cursor count: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                // Fetch column indices
                int nameIndex = cursor.getColumnIndex(DatabaseCust.COLUMN_NAME);
                int emailIndex = cursor.getColumnIndex(DatabaseCust.COLUMN_EMAIL);
                int phoneIndex = cursor.getColumnIndex(DatabaseCust.COLUMN_PHONE);

                // Log column indices
                Log.d(TAG, "Column Indices - Name: " + nameIndex + ", Email: " + emailIndex + ", Phone: " + phoneIndex);

                if (nameIndex != -1 && emailIndex != -1 && phoneIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    String email = cursor.getString(emailIndex);
                    String phone = cursor.getString(phoneIndex);

                    Log.d(TAG, "Fetched Data - Name: " + name + ", Email: " + email + ", Phone: " + phone);

                    // Set data to TextViews
                    nameText.setText("Name: " + name);
                    emailText.setText("Email: " + email);
                    phoneText.setText("Phone number: " + phone);
                } else {
                    Log.e(TAG, "Invalid column indices");
                    Toast.makeText(this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "No data found for username: " + username);
                Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            Log.e(TAG, "Cursor is null for username: " + username);
            Toast.makeText(this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, HomePageActivity.class);
        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME")); // Pass back username
        startActivity(intent);
        finish();
    }

}
