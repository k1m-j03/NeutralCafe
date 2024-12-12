package com.example.neutralcafe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    private ImageView newsImageView, mustTryImageView, reserveTableImageView;
    private int newsIndex = 0, mustTryIndex = 0, reserveTableIndex = 0;

    // Arrays for drawable resources
    private final int[] newsImages = {R.drawable.n1, R.drawable.n2, R.drawable.n3};
    private final int[] mustTryImages = {R.drawable.f1, R.drawable.f2, R.drawable.f3};
    private final int[] reserveTableImages = {R.drawable.v1, R.drawable.v2, R.drawable.v3};

    private final Handler imageChangeHandler = new Handler();
    private final int imageChangeInterval = 3000; // 3 seconds

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

        newsImageView = findViewById(R.id.news);
        mustTryImageView = findViewById(R.id.mustTry);
        reserveTableImageView = findViewById(R.id.reserveTable);

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

        // Start automatic image change
        startImageAutoChange();
    }

    private void handleNavigationItemSelected(int id) {
        if (id == R.id.home) {
            // No action needed; already on home page
        } else if (id == R.id.menu) {
            Intent intent = new Intent(HomePageActivity.this, MenuListActivity.class);
            intent.putExtra("USERNAME", username); // Pass username to MyReserveActivity
            startActivity(intent);
        } else if (id == R.id.reserve) {
            Intent intent = new Intent(HomePageActivity.this, Reserve1Activity.class);
            intent.putExtra("USERNAME", username); // Pass username to Reserve1Activity
            startActivity(intent);
        } else if (id == R.id.my_reservation) {
            Intent intent = new Intent(HomePageActivity.this, MyReserveActivity.class);
            intent.putExtra("USERNAME", username); // Pass username to MyReserveActivity
            startActivity(intent);
        } else if (id == R.id.feedback) {
            Intent intent = new Intent(HomePageActivity.this, FeedbacksActivity.class);
            intent.putExtra("USERNAME", username); // Pass username to MyReserveActivity
            startActivity(intent);
        } else if (id == R.id.logout) {
            startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START); // Close drawer after selection
    }

    private void startImageAutoChange() {
        Runnable imageChanger = new Runnable() {
            @Override
            public void run() {
                // Change images for each section
                newsImageView.setImageResource(newsImages[newsIndex]);
                mustTryImageView.setImageResource(mustTryImages[mustTryIndex]);
                reserveTableImageView.setImageResource(reserveTableImages[reserveTableIndex]);

                // Update indices and reset if necessary
                newsIndex = (newsIndex + 1) % newsImages.length;
                mustTryIndex = (mustTryIndex + 1) % mustTryImages.length;
                reserveTableIndex = (reserveTableIndex + 1) % reserveTableImages.length;

                // Schedule next image change
                imageChangeHandler.postDelayed(this, imageChangeInterval);
            }
        };

        imageChangeHandler.post(imageChanger);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop image change handler when activity is destroyed
        imageChangeHandler.removeCallbacksAndMessages(null);
    }
}
