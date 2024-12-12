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

        // Initialize Views
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu_icon);
        navigationView = findViewById(R.id.navigation_view);
        menuImage = findViewById(R.id.menuImage);
        ImageButton prevButton = findViewById(R.id.prevButton);
        ImageButton nextButton = findViewById(R.id.nextButton);

        // Handle Menu Icon Click
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Handle Navigation Drawer Item Clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId(); // Get the selected item's ID

                if (id == R.id.home) {
                    startActivity(new Intent(MenuListActivity.this, HomePageActivity.class));
                } else if (id == R.id.menu) {
                    startActivity(new Intent(MenuListActivity.this, MenuListActivity.class));
                } else if (id == R.id.reserve) {
                    startActivity(new Intent(MenuListActivity.this, Reserve1Activity.class));
                } else if (id == R.id.logout) {
                    startActivity(new Intent(MenuListActivity.this, LoginActivity.class));
                    finish(); // Finish current activity after logout
                } else {
                    // Handle other menu items (e.g., Feedback, My Reservation)
                }

                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer after selection
                return true;
            }
        });

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
}
