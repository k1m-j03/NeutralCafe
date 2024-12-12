package com.example.neutralcafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Reserve2Activity extends AppCompatActivity {

    private static final String TAG = "Reserve2Activity";

    private String date;
    private String meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve2_layout);

        // Retrieve USERNAME and validate it
        String username = getIntent().getStringExtra("USERNAME");

        if (username == null || username.isEmpty()) {
            Log.e("Reserve2Activity", "USERNAME is null or empty. Redirecting to LoginActivity.");
            Toast.makeText(this, "User session expired. Please log in again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class)); // Redirect to LoginActivity
            finish();
            return;
        }
        Log.d("Reserve2Activity", "USERNAME received: " + username);

        // Retrieve data passed from Reserve1Activity
        date = getIntent().getStringExtra("date");
        meal = getIntent().getStringExtra("meal");


        if (date == null || meal == null) {
            Log.e(TAG, "Date or Meal data is missing. Returning to Reserve1Activity.");
            Toast.makeText(this, "Missing reservation data. Returning to the previous screen.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Views
        Spinner spinnerPax = findViewById(R.id.spinner_pax);
        Button backButton = findViewById(R.id.button_back);
        Button reserveButton = findViewById(R.id.button_reserve);

        // Handle Back button
        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked.");
            finish();
        });

        // Handle Reserve button click
        reserveButton.setOnClickListener(v -> {
            // Get the selected number of pax
            int pax;
            try {
                pax = Integer.parseInt(spinnerPax.getSelectedItem().toString());
            } catch (NumberFormatException e) {
                Log.e(TAG, "Invalid pax selection.", e);
                Toast.makeText(this, "Please select the number of pax.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the selected table ID
            String selectedTableId = getSelectedTableId();
            if (selectedTableId == null) {
                Toast.makeText(this, "Please select a table.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Navigate to Reserve3Activity with the collected data
            navigateToReserve3Activity(selectedTableId, pax);
        });

        // Setup table button listeners
        setupTableButtons();
    }

    private void navigateToReserve3Activity(String tableId, int pax) {
        Log.d("Reserve2Activity", "Navigating to Reserve3Activity with tableId: " + tableId + ", pax: " + pax);
        Log.d("Reserve2Activity", "Date: " + date + ", Meal: " + meal);



        if (date == null || meal == null || tableId == null || pax <= 0) {
            Toast.makeText(this, "Invalid reservation data. Please select all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, Reserve3Activity.class);
        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
        intent.putExtra("date", date);
        intent.putExtra("meal", meal);
        intent.putExtra("tableId", tableId);
        intent.putExtra("pax", pax);
        startActivity(intent);
    }


    private void setupTableButtons() {
        int[] tableButtonIds = {
                R.id.table_tso1, R.id.table_tso2, R.id.table_tso3, R.id.table_tso4,
                R.id.table_tgo1, R.id.table_tgo2, R.id.table_tgo3, R.id.table_tgo4,
                R.id.table_tsi1, R.id.table_tsi2, R.id.table_tsi3, R.id.table_tsi4,
                R.id.table_tsi5, R.id.table_tsi6, R.id.table_tgi1, R.id.table_tgi2,
                R.id.table_tgi3, R.id.table_tgi4, R.id.table_tgi5, R.id.table_tgi6
        };

        for (int id : tableButtonIds) {
            Button tableButton = findViewById(id);
            if (tableButton != null) {
                tableButton.setOnClickListener(this::handleTableSelection);
            } else {
                Log.w(TAG, "Table button with ID " + id + " not found in layout.");
            }
        }
    }

    private void handleTableSelection(View view) {
        if (view instanceof Button) {
            Button button = (Button) view;

            // Deselect all table buttons
            resetTableSelections();

            // Highlight the selected button
            button.setBackgroundColor(getResources().getColor(R.color.selected_table));
            button.setTag("selected");

            Log.d(TAG, "Table selected: " + button.getText());
        }
    }

    private void resetTableSelections() {
        int[] tableButtonIds = {
                R.id.table_tso1, R.id.table_tso2, R.id.table_tso3, R.id.table_tso4,
                R.id.table_tgo1, R.id.table_tgo2, R.id.table_tgo3, R.id.table_tgo4,
                R.id.table_tsi1, R.id.table_tsi2, R.id.table_tsi3, R.id.table_tsi4,
                R.id.table_tsi5, R.id.table_tsi6, R.id.table_tgi1, R.id.table_tgi2,
                R.id.table_tgi3, R.id.table_tgi4, R.id.table_tgi5, R.id.table_tgi6
        };

        for (int id : tableButtonIds) {
            Button button = findViewById(id);
            if (button != null) {
                button.setBackgroundColor(getResources().getColor(R.color.default_table));
                button.setTag(null);
            }
        }
    }

    private String getSelectedTableId() {
        int[] tableButtonIds = {
                R.id.table_tso1, R.id.table_tso2, R.id.table_tso3, R.id.table_tso4,
                R.id.table_tgo1, R.id.table_tgo2, R.id.table_tgo3, R.id.table_tgo4,
                R.id.table_tsi1, R.id.table_tsi2, R.id.table_tsi3, R.id.table_tsi4,
                R.id.table_tsi5, R.id.table_tsi6, R.id.table_tgi1, R.id.table_tgi2,
                R.id.table_tgi3, R.id.table_tgi4, R.id.table_tgi5, R.id.table_tgi6
        };

        for (int id : tableButtonIds) {
            Button button = findViewById(id);
            if (button != null && "selected".equals(button.getTag())) {
                Log.d("Reserve2Activity", "Selected table: " + button.getText());
                return button.getText().toString(); // Return the text on the selected button
            }
        }

        Log.e("Reserve2Activity", "No table selected.");
        return null;
    }

}
