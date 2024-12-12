package com.example.neutralcafe;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditDetailActivity extends AppCompatActivity {

    private static final String TAG = "EditDetailActivity";

    private EditText customerNameEditText;
    private EditText customerPhoneEditText;
    private TextView dateTextView;
    private Spinner tableSizeSpinner;
    private EditText seatingAreaEditText;
    private LinearLayout breakfastLayout;
    private LinearLayout lunchLayout;
    private LinearLayout dinnerLayout;
    private Button cancelButton;
    private Button updateButton;
    private String username;
    private String selectedMeal = "Breakfast"; // Default value
    private int reservationId; // Store the reservation ID
    private ReservationApi reservationApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editdetail);

        // Retrieve username and reservationId
        username = getIntent().getStringExtra("USERNAME");
        reservationId = getIntent().getIntExtra("reservation_id", -1);

        if (reservationId == -1) {
            Toast.makeText(this, "Invalid reservation ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        initializeViews();

        // Initialize API Client
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        reservationApi = retrofit.create(ReservationApi.class);

        // Fetch existing reservation details
        fetchReservationDetails();

        // Handle date picker
        ImageView dateIcon = findViewById(R.id.date_icon);
        dateIcon.setOnClickListener(v -> showDatePickerDialog());

        // Meal selection logic
        setupMealSelection();
        breakfastLayout.performClick(); // Highlight Breakfast by default

        // Button actions
        cancelButton.setOnClickListener(v -> navigateToMyReservations());
        updateButton.setOnClickListener(v -> updateReservation());
    }

    private void initializeViews() {
        customerNameEditText = findViewById(R.id.customer_name);
        customerPhoneEditText = findViewById(R.id.customer_phone);
        dateTextView = findViewById(R.id.date);
        tableSizeSpinner = findViewById(R.id.table_size);
        seatingAreaEditText = findViewById(R.id.seating_area);
        breakfastLayout = findViewById(R.id.breakfast);
        lunchLayout = findViewById(R.id.Lunch);
        dinnerLayout = findViewById(R.id.dinner);
        cancelButton = findViewById(R.id.cancel_button);
        updateButton = findViewById(R.id.update_button);
    }

    private void fetchReservationDetails() {
        Call<Reservation> call = reservationApi.getReservationById(reservationId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Reservation reservation = response.body();
                    populateFields(reservation);
                } else {
                    Log.e(TAG, "Failed to fetch reservation details: " + response.message());
                    Toast.makeText(EditDetailActivity.this, "Failed to load reservation details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Reservation> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching reservation details: " + t.getMessage(), t);
                Toast.makeText(EditDetailActivity.this, "Network error, please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateFields(Reservation reservation) {
        customerNameEditText.setText(reservation.getCustomerName());
        customerPhoneEditText.setText(reservation.getCustomerPhoneNumber());
        dateTextView.setText(reservation.getDate());

        // Set table size spinner
        if (tableSizeSpinner.getAdapter() != null) {
            tableSizeSpinner.setSelection(getSpinnerIndex(tableSizeSpinner, String.valueOf(reservation.getTableSize())));
        }

        seatingAreaEditText.setText(reservation.getSeatingArea());

        // Meal selection
        switch (reservation.getMeal()) {
            case "Breakfast":
                breakfastLayout.performClick();
                break;
            case "Lunch":
                lunchLayout.performClick();
                break;
            case "Dinner":
                dinnerLayout.performClick();
                break;
        }
    }

    private int getSpinnerIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0; // Default to the first item
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            dateTextView.setText(selectedDate);
        }, year, month, day).show();
    }

    private void setupMealSelection() {
        View.OnClickListener mealClickListener = v -> {
            clearMealSelection();
            v.setBackgroundResource(R.drawable.selected_bg);

            if (v.getId() == R.id.breakfast) {
                selectedMeal = "Breakfast";
            } else if (v.getId() == R.id.Lunch) {
                selectedMeal = "Lunch";
            } else if (v.getId() == R.id.dinner) {
                selectedMeal = "Dinner";
            }
        };

        breakfastLayout.setOnClickListener(mealClickListener);
        lunchLayout.setOnClickListener(mealClickListener);
        dinnerLayout.setOnClickListener(mealClickListener);
    }

    private void clearMealSelection() {
        breakfastLayout.setBackgroundResource(R.drawable.rounded_bg);
        lunchLayout.setBackgroundResource(R.drawable.rounded_bg);
        dinnerLayout.setBackgroundResource(R.drawable.rounded_bg);
    }

    private void updateReservation() {
        String name = customerNameEditText.getText().toString().trim();
        String phone = customerPhoneEditText.getText().toString().trim();
        String date = dateTextView.getText().toString().trim();
        String seatingArea = seatingAreaEditText.getText().toString().trim();
        int tableSize = Integer.parseInt(tableSizeSpinner.getSelectedItem().toString().trim());

        if (name.isEmpty() || phone.isEmpty() || date.isEmpty() || seatingArea.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the Reservation object and set all fields
        Reservation updatedData = new Reservation();
        updatedData.setId(reservationId); // Ensure the ID matches the path parameter
        updatedData.setCustomerName(name);
        updatedData.setCustomerPhoneNumber(phone);
        updatedData.setDate(date);
        updatedData.setMeal(selectedMeal);
        updatedData.setSeatingArea(seatingArea);
        updatedData.setTableSize(tableSize);

        // Make the PUT request
        Call<Reservation> call = reservationApi.updateReservation(reservationId, updatedData);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditDetailActivity.this, "Reservation updated successfully!", Toast.LENGTH_SHORT).show();
                    navigateToMyReservations();
                } else {
                    Log.e(TAG, "Update failed: " + response.message());
                    Toast.makeText(EditDetailActivity.this, "Update failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Reservation> call, @NonNull Throwable t) {
                Log.e(TAG, "Update Error: " + t.getMessage(), t);
                Toast.makeText(EditDetailActivity.this, "Network error, please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void navigateToMyReservations() {
        Intent intent = new Intent(EditDetailActivity.this, MyReserveDataActivity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("reservation_id", reservationId);
        startActivity(intent);
        finish();
    }
}
