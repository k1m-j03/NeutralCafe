package com.example.neutralcafe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyReserveDataActivity extends AppCompatActivity {

    private TextView customerNameTextView, customerPhoneTextView, mealTextView, seatingAreaTextView, tableSizeTextView, dateTextView;
    private Button cancelButton, editButton, backButton;
    private int reservationId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myreservedata_layout);

        username = getIntent().getStringExtra("USERNAME");

        // Initialize views
        customerNameTextView = findViewById(R.id.customer_name_textview);
        customerPhoneTextView = findViewById(R.id.customer_phone_textview);
        mealTextView = findViewById(R.id.meal_textview);
        seatingAreaTextView = findViewById(R.id.seating_area_textview);
        tableSizeTextView = findViewById(R.id.table_size_textview);
        dateTextView = findViewById(R.id.date_textview);

        cancelButton = findViewById(R.id.cancel_button);
        editButton = findViewById(R.id.edit_button);
        backButton = findViewById(R.id.back_button);

        // Retrieve reservation ID passed from the previous activity
        reservationId = getIntent().getIntExtra("reservation_id", -1);

        if (reservationId > 0) {
            fetchReservationDetails(reservationId);
        } else {
            Toast.makeText(this, "Invalid reservation ID!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no valid ID is passed
            return;
        }

        // Cancel button listener
        cancelButton.setOnClickListener(v -> {
            new AlertDialog.Builder(MyReserveDataActivity.this)
                    .setTitle("Delete Reservation")
                    .setMessage("Are you sure you want to delete this reservation?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteReservation(reservationId))
                    .setNegativeButton("No", null)
                    .show();
        });

        // Edit button listener
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyReserveDataActivity.this, EditDetailActivity.class);
            intent.putExtra("reservation_id", reservationId);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });

        // Edit button listener
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyReserveDataActivity.this, MyReserveActivity.class);
            intent.putExtra("reservation_id", reservationId);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });
    }

    private void fetchReservationDetails(int reservationId) {
        ReservationApi api = ApiClient.getRetrofitInstance().create(ReservationApi.class);
        Call<Reservation> call = api.getReservationById(reservationId);

        call.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Reservation reservation = response.body();
                    displayReservationDetails(reservation);
                } else {
                    Toast.makeText(MyReserveDataActivity.this, "Failed to fetch reservation details!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                Toast.makeText(MyReserveDataActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayReservationDetails(Reservation reservation) {
        customerNameTextView.setText("Customer Name: " + reservation.getCustomerName());
        customerPhoneTextView.setText("Customer Phone Number: " + reservation.getCustomerPhoneNumber());
        mealTextView.setText("Meal: " + reservation.getMeal());
        seatingAreaTextView.setText("Seating Area: " + reservation.getSeatingArea());
        tableSizeTextView.setText("Seating Table Size: " + reservation.getTableSize());
        dateTextView.setText("Date: " + reservation.getDate());
    }

    private void deleteReservation(int reservationId) {
        ReservationApi api = ApiClient.getRetrofitInstance().create(ReservationApi.class);
        Call<Void> call = api.deleteReservation(reservationId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyReserveDataActivity.this, "Reservation deleted successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyReserveDataActivity.this, MyReserveActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MyReserveDataActivity.this, "Failed to delete reservation!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyReserveDataActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
