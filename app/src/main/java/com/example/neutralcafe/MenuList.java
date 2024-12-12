package com.example.neutralcafe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MenuList extends AppCompatActivity {

    private final DrawerLayout drawerLayout;
    private final ImageView menuIcon;
    private final NavigationView navigationView;
    private ImageView menuImage;
    private final int[] menuImages = {
            R.drawable.me1,
            R.drawable.me2,
            R.drawable.me3,
            R.drawable.me4
    };
    private int currentImageIndex = 0;

    public MenuList(DrawerLayout drawerLayout, ImageView menuIcon, NavigationView navigationView) {
        this.drawerLayout = drawerLayout;
        this.menuIcon = menuIcon;
        this.navigationView = navigationView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulist_layout);

        // Handle Menu Icon Click
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Handle Navigation Drawer Item Clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Initialize Views
        menuImage = findViewById(R.id.menuImage);
        ImageButton prevButton = findViewById(R.id.prevButton);
        ImageButton nextButton = findViewById(R.id.nextButton);

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


