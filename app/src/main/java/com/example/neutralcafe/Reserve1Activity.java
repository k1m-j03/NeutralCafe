package com.example.neutralcafe;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class Reserve1Activity extends AppCompatActivity {
    private static final String TAG = "Reserve1Activity";
    private DrawerLayout drawerLayout;
    private TextView dateText;
    private String selectedMeal;
    private LinearLayout lastSelectedMealButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve1_layout);

        // Retrieve username passed from HomePageActivity
        username = getIntent().getStringExtra("USERNAME");

        if (username == null || username.isEmpty()) {
            Log.e(TAG, "USERNAME is null or empty. Redirecting to LoginActivity.");
            Toast.makeText(this, "User session expired. Please log in again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class)); // Redirect to LoginActivity
            finish();
            return;
        }

        // Initialize Navigation Drawer
        setupNavigationDrawer();

        // Initialize Views
        dateText = findViewById(R.id.date_text);
        LinearLayout breakfastButton = findViewById(R.id.breakfast_button);
        LinearLayout lunchButton = findViewById(R.id.lunch_button);
        LinearLayout dinnerButton = findViewById(R.id.dinner_button);

        // Setup Meal Button Clicks
        breakfastButton.setOnClickListener(v -> handleMealSelection("Breakfast", breakfastButton));
        lunchButton.setOnClickListener(v -> handleMealSelection("Lunch", lunchButton));
        dinnerButton.setOnClickListener(v -> handleMealSelection("Dinner", dinnerButton));

        // Date Picker
        findViewById(R.id.date_icon).setOnClickListener(v -> showDatePickerDialog());

        // Navigate to Reserve2Activity
        findViewById(R.id.select_table_button).setOnClickListener(v -> navigateToReserve2Activity());
    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            handleNavigationItemSelected(id);
            return true;
        });
    }

    private void handleNavigationItemSelected(int id) {
        Intent intent;
        if (id == R.id.home) {
            intent = new Intent(Reserve1Activity.this, HomePageActivity.class);
        } else if (id == R.id.menu) {
            intent = new Intent(Reserve1Activity.this, MenuListActivity.class);
        } else if (id == R.id.reserve) {
            intent = new Intent(Reserve1Activity.this, Reserve1Activity.class);
        } else if (id == R.id.logout) {
            intent = new Intent(Reserve1Activity.this, LoginActivity.class);
            finish();
            return;
        } else {
            return;
        }
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            dateText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }, year, month, day).show();
    }

    private void handleMealSelection(String mealType, LinearLayout selectedButton) {
        selectedMeal = mealType;

        // Reset the previous button's background color
        if (lastSelectedMealButton != null) {
            lastSelectedMealButton.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.default_button_background)
            );
        }

        // Highlight the currently selected button
        selectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_button_background));
        lastSelectedMealButton = selectedButton;

        Toast.makeText(this, mealType + " selected", Toast.LENGTH_SHORT).show();
    }

    private void navigateToReserve2Activity() {
        if (dateText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedMeal == null || selectedMeal.isEmpty()) {
            Toast.makeText(this, "Please select a meal type.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, Reserve2Activity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("date", dateText.getText().toString());
        intent.putExtra("meal", selectedMeal);
        startActivity(intent);
    }
}
