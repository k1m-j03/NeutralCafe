package com.example.neutralcafe;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reserve3Activity extends AppCompatActivity {
    private static final String TAG = "Reserve3Activity";
    private static final int NOTIFICATION_PERMISSION_CODE = 101;
    private static final String CHANNEL_ID = "reservation_channel";

    private String date, meal, tableId, username, customerName, customerPhoneNumber;
    private int pax;

    private TextView mealInput, seatingAreaInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve3_layout);

        // Retrieve intent data
        username = getIntent().getStringExtra("USERNAME");
        date = getIntent().getStringExtra("date");
        meal = getIntent().getStringExtra("meal");
        tableId = getIntent().getStringExtra("tableId");
        pax = getIntent().getIntExtra("pax", -1);

        Log.d(TAG, "Received data - USERNAME: " + username + ", date: " + date + ", meal: " + meal + ", tableId: " + tableId + ", pax: " + pax);

        // Initialize views
        fetchCustomerData();
        setupConfirmationViews();

        mealInput = findViewById(R.id.confirmation_meat);
        seatingAreaInput = findViewById(R.id.confirmation_sitting_area);
        seatingAreaInput.setText(determineSittingArea(tableId));

        Button confirmButton = findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(v -> submitReservation());

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        }
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
            Log.d(TAG, "User data retrieved - Name: " + customerName + ", Phone: " + customerPhoneNumber);
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
        confirmationMeal.setText(meal != null ? meal : "N/A");
        confirmationTableNumber.setText(tableId != null ? tableId : "N/A");
        confirmationSittingArea.setText(determineSittingArea(tableId));
        confirmationPax.setText(pax > 0 ? String.valueOf(pax) : "N/A");
    }

    private String determineSittingArea(String tableId) {
        if (tableId == null) return "Unknown";
        String area = tableId.startsWith("TGO") || tableId.startsWith("TSO") ? "Outside" : "Inside";
        return tableId + " (" + area + ")";
    }

    private void submitReservation() {
        if (customerName == null || customerPhoneNumber == null || meal == null || meal.isEmpty()) {
            Log.e(TAG, "Missing required fields. Meal: " + meal);
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
                determineSittingArea(tableId),
                pax
        );

        ReservationApi apiService = ApiClient.getRetrofitInstance().create(ReservationApi.class);
        apiService.submitReservation(data).enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Reservation confirmed. ID: " + response.body().getId());
                    sendBookingConfirmedNotification();
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

    private void sendBookingConfirmedNotification() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        // Create channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Reservation Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Ensure this icon exists
                .setContentTitle("Booking Confirmed")
                .setContentText("Your reservation has been successfully confirmed!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1, builder.build());
    }

    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission granted.");
            } else {
                Toast.makeText(this, "Notification permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
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
