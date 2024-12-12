package com.example.neutralcafe;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Reserve1Activity extends AppCompatActivity {

    private static final String TAG = "Reserve1Activity";
    private TextView dateText;
    private String selectedMeal;
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

        // Initialize Views
        dateText = findViewById(R.id.date_text);
        LinearLayout breakfastButton = findViewById(R.id.breakfast_button);
        LinearLayout lunchButton = findViewById(R.id.lunch_button);
        LinearLayout dinnerButton = findViewById(R.id.dinner_button);

        findViewById(R.id.date_icon).setOnClickListener(v -> showDatePickerDialog());
        breakfastButton.setOnClickListener(v -> handleMealSelection("Breakfast"));
        lunchButton.setOnClickListener(v -> handleMealSelection("Lunch"));
        dinnerButton.setOnClickListener(v -> handleMealSelection("Dinner"));

        findViewById(R.id.select_table_button).setOnClickListener(v -> navigateToReserve2Activity());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // DatePickerDialog starts with the current date
        new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            dateText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }, year, month, day).show();
    }

    private void handleMealSelection(String mealType) {
        selectedMeal = mealType;
        Toast.makeText(this, mealType + " selected", Toast.LENGTH_SHORT).show();
    }

    private void navigateToReserve2Activity() {
        // Validate date and meal before navigating
        if (dateText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedMeal == null || selectedMeal.isEmpty()) {
            Toast.makeText(this, "Please select a meal type.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Navigate to Reserve2Activity and pass data
        Intent intent = new Intent(this, Reserve2Activity.class);
        intent.putExtra("USERNAME", username); // Pass the username
        intent.putExtra("date", dateText.getText().toString());
        intent.putExtra("meal", selectedMeal);
        startActivity(intent);
    }
}
