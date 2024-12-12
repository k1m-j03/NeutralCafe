package com.example.neutralcafe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MenuListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private NavigationView navigationView;
    private ImageView menuImage;
    private String username;

    private final int[] menuImages = {
            R.drawable.me1,
            R.drawable.me2,
            R.drawable.me3,
            R.drawable.me4
    };
    private int currentImageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulist_layout);

        username = getIntent().getStringExtra("USERNAME");

        // Initialize Views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        ImageView profileIcon = findViewById(R.id.profile_icon);
        ImageButton prevButton = findViewById(R.id.prevButton);
        ImageButton nextButton = findViewById(R.id.nextButton);

        // Handle Menu Icon Click
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Handle Profile Icon Click
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MenuListActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Set up Navigation Drawer Item Clicks
        navigationView.setNavigationItemSelectedListener(this::handleNavigationItemSelected);

        // Set initial image
        menuImage.setImageResource(menuImages[currentImageIndex]);

        // Previous Button Click Listener
        prevButton.setOnClickListener(v -> {
            if (currentImageIndex > 0) {
                currentImageIndex--;
                menuImage.setImageResource(menuImages[currentImageIndex]);
            }
        });

        // Next Button Click Listener
        nextButton.setOnClickListener(v -> {
            if (currentImageIndex < menuImages.length - 1) {
                currentImageIndex++;
                menuImage.setImageResource(menuImages[currentImageIndex]);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private boolean handleNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent homeIntent = new Intent(MenuListActivity.this, HomePageActivity.class);
            homeIntent.putExtra("USERNAME", username);
            startActivity(homeIntent);
        } else if (id == R.id.menu) {
            Intent menuIntent = new Intent(MenuListActivity.this, MenuListActivity.class);
            menuIntent.putExtra("USERNAME", username);
            startActivity(menuIntent);
        } else if (id == R.id.reserve) {
            Intent reserveIntent = new Intent(MenuListActivity.this, Reserve1Activity.class);
            reserveIntent.putExtra("USERNAME", username);
            startActivity(reserveIntent);
        } else if (id == R.id.logout) {
            startActivity(new Intent(MenuListActivity.this, LoginActivity.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
