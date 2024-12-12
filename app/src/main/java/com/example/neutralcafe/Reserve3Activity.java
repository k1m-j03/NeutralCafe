package com.example.neutralcafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reserve3Activity extends AppCompatActivity {
    private static final String TAG = "Reserve3Activity";

    private String date;
    private String tableId;
    private int pax;
    private String username;
    private String customerName;
    private String customerPhoneNumber;

    private TextView mealInput;
    private TextView seatingAreaInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve3_layout);

        // Retrieve data passed from the previous activity
        username = getIntent().getStringExtra("USERNAME");
        date = getIntent().getStringExtra("date");
        tableId = getIntent().getStringExtra("tableId");
        pax = getIntent().getIntExtra("pax", -1);

        fetchCustomerData();
        setupConfirmationViews();

        mealInput = findViewById(R.id.confirmation_meat);
        seatingAreaInput = findViewById(R.id.confirmation_sitting_area);
        seatingAreaInput.setText(determineSittingArea(tableId));

        Button confirmButton = findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(v -> submitReservation());
    }

    private void fetchCustomerData() {
        DatabaseCust db = new DatabaseCust(this);

        if (username == null || username.isEmpty()) {
            Log.e(TAG, "Username is null or empty.");
            showErrorAndFinish("Username is not available.");
            return;
        }

        HashMap<String, String> user = db.getUserDetails(username);

        if (user != null && !user.isEmpty()) {
            customerName = user.get("name");
            customerPhoneNumber = user.get("phone");
        } else {
            Log.e(TAG, "User data not found for username: " + username);
            showErrorAndFinish("Failed to retrieve user data.");
        }
    }

    private void setupConfirmationViews() {
        TextView confirmationDate = findViewById(R.id.confirmation_date);
        TextView confirmationMeal = findViewById(R.id.confirmation_meat);
        TextView confirmationTableNumber = findViewById(R.id.confirmation_table_number);
        TextView confirmationSittingArea = findViewById(R.id.confirmation_sitting_area);
        TextView confirmationPax = findViewById(R.id.confirmation_pax);

        confirmationDate.setText(date != null ? date : "N/A");
        confirmationTableNumber.setText(tableId != null ? tableId : "N/A");
        confirmationSittingArea.setText(determineSittingArea(tableId)); // Display combined value
        confirmationPax.setText(pax > 0 ? String.valueOf(pax) : "N/A");
    }


    private String determineSittingArea(String tableId) {
        if (tableId == null) return "Unknown";
        String area = tableId.startsWith("TGO") || tableId.startsWith("TSO") ? "Outside" : "Inside";
        return tableId + " (" + area + ") "; // Combine tableId and area
    }

    private void submitReservation() {
        String meal = mealInput.getText().toString().trim();
        String seatingArea = determineSittingArea(tableId); // Use the updated method

        if (customerName == null || customerPhoneNumber == null || meal.isEmpty() || seatingArea.equals("Unknown")) {
            Log.e(TAG, "Missing required fields.");
            Toast.makeText(this, "Please ensure all details are filled.", Toast.LENGTH_SHORT).show();
            return;
        }

        String formattedDate = formatDate(date);
        if (formattedDate == null) {
            Toast.makeText(this, "Invalid date format. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        ReservationData data = new ReservationData(
                customerName,
                customerPhoneNumber,
                formattedDate,
                meal,
                seatingArea, // Send the combined tableId and area
                pax
        );

        ReservationApi apiService = ApiClient.getRetrofitInstance().create(ReservationApi.class);
        apiService.submitReservation(data).enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Reservation confirmed. ID: " + response.body().getId());
                    navigateToReserve4(response.body().getId());
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Reservation> call, @NonNull Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage(), t);
                Toast.makeText(Reserve3Activity.this, "Submission failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String formatDate(String rawDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(rawDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            Log.e(TAG, "Error formatting date", e);
            return null;
        }
    }

    private void navigateToReserve4(int reservationId) {
        Intent intent = new Intent(this, Reserve4Activity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("reservationId", reservationId);
        startActivity(intent);
        finish();
    }

    private void handleApiError(Response<Reservation> response) {
        try {
            if (response.errorBody() != null) {
                Log.e(TAG, "API Error: " + response.errorBody().string());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading response", e);
        }
        Toast.makeText(this, "Submission failed. Please check your inputs.", Toast.LENGTH_SHORT).show();
    }

    private void showErrorAndFinish(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish();
    }
}
