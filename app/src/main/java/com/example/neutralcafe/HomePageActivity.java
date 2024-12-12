package com.example.neutralcafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomePageActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private static final String TAG = "HomePageActivity";
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        // Retrieve USERNAME passed from LoginActivity or ProfileActivity
        username = getIntent().getStringExtra("USERNAME");
        Log.d(TAG, "USERNAME received: " + username);

        if (username == null || username.isEmpty()) {
            Log.e(TAG, "USERNAME is null or empty. Redirecting to LoginActivity.");
            Toast.makeText(this, "User session expired. Please log in again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize Views
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        ImageView menuIcon = findViewById(R.id.menu_icon);
        ImageView profileIcon = findViewById(R.id.profile_icon);
        EditText searchBar = findViewById(R.id.search_bar);
        Button reserveButton = findViewById(R.id.reserve_button);

        // Set up Navigation Drawer Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle Menu Icon Click
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Handle Profile Icon Click
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
            intent.putExtra("USERNAME", username); // Pass the username to ProfileActivity
            startActivity(intent);
        });

        // Set up Navigation Drawer Item Clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            handleNavigationItemSelected(id);
            return true;
        });

        // Handle Reserve Button Click
        reserveButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, Reserve1Activity.class);
            intent.putExtra("USERNAME", username); // Pass USERNAME to Reserve1Activity
            startActivity(intent);
        });

        // Optionally handle search bar (if search functionality is added later)
        searchBar.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Example: Navigate to a search results activity
                // Intent intent = new Intent(HomePageActivity.this, SearchResultsActivity.class);
                // startActivity(intent);
            }
        });
    }

    private void handleNavigationItemSelected(int id) {
        if (id == R.id.home) {
            // No action needed; already on home page
        } else if (id == R.id.menu) {
            startActivity(new Intent(HomePageActivity.this, MenuListActivity.class));
        } else if (id == R.id.reserve) {
            Intent intent = new Intent(HomePageActivity.this, Reserve1Activity.class);
            intent.putExtra("USERNAME", username); // Pass username to Reserve1Activity
            startActivity(intent);
        } else if (id == R.id.logout) {
            startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START); // Close drawer after selection
    }

    @Override
    public void onBackPressed() {
        // Close drawer if open; otherwise, handle back press normally
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
