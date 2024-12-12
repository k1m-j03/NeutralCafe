package com.example.neutralcafe;

/*import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditMyReservationActivity extends AppCompatActivity {

    private EditText customerNameEditText, phoneEditText, dateEditText, mealEditText, seatingAreaEditText, tableSizeEditText;
    private Button updateButton, cancelButton;
    private int reservationId; // Store the reservation ID
    private ReservationApi reservationApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editmyreservation_layout);

        // Initialize Views
        customerNameEditText = findViewById(R.id.customer_name);
        phoneEditText = findViewById(R.id.customer_phone);
        dateEditText = findViewById(R.id.date);
        mealEditText = findViewById(R.id.meal);
        seatingAreaEditText = findViewById(R.id.seating_area);
        tableSizeEditText = findViewById(R.id.table_size);
        updateButton = findViewById(R.id.update_button);
        cancelButton = findViewById(R.id.cancel_button);

        // Get Reservation ID from Intent
        reservationId = getIntent().getIntExtra("reservation_id", -1);

        // Initialize API Client
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        reservationApi = retrofit.create(ReservationApi.class);

        // Fetch and Display Existing Data
        fetchReservationDetails();

        // Handle Update Button Click
        updateButton.setOnClickListener(v -> updateReservation());

        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(EditMyReservationActivity.this, MyReserveDataActivity.class);
            intent.putExtra("reservation_id", reservationId);
            startActivity(intent);
        });
    }

    private void fetchReservationDetails() {
        Call<Reservation> call = reservationApi.getReservationById(reservationId);
        call.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Reservation reservation = response.body();

                    // Populate fields with existing data
                    customerNameEditText.setText(reservation.getCustomerName());
                    phoneEditText.setText(reservation.getCustomerPhoneNumber());
                    dateEditText.setText(reservation.getDate());
                    mealEditText.setText(reservation.getMeal());
                    seatingAreaEditText.setText(reservation.getSeatingArea());
                    tableSizeEditText.setText(String.valueOf(reservation.getTableSize()));
                } else {
                    Toast.makeText(EditMyReservationActivity.this, "Failed to load reservation details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                Toast.makeText(EditMyReservationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        // Validate if any field is empty
        if (customerNameEditText.getText().toString().trim().isEmpty() ||
                phoneEditText.getText().toString().trim().isEmpty() ||
                dateEditText.getText().toString().trim().isEmpty() ||
                mealEditText.getText().toString().trim().isEmpty() ||
                seatingAreaEditText.getText().toString().trim().isEmpty() ||
                tableSizeEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate date format (e.g., YYYY-MM-DD)
        String date = dateEditText.getText().toString();
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            Toast.makeText(this, "Date must be in the format YYYY-MM-DD!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate phone number
        String phone = phoneEditText.getText().toString();

        return true;
    }

    private void updateReservation() {
        // Extract input data
        String customerName = customerNameEditText.getText().toString();
        String customerPhone = phoneEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String meal = mealEditText.getText().toString();
        String seatingArea = seatingAreaEditText.getText().toString();
        int tableSize = Integer.parseInt(tableSizeEditText.getText().toString());

        // Get reservationId from the intent
        //int reservationId = getIntent().getIntExtra("reservation_id", -1);

        /*if (reservationId == -1) {
            Toast.makeText(this, "Error: Reservation ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }*/

/*          // Create ReservationData object without the ID
        ReservationData updatedReservation = new ReservationData(
                customerName,
                customerPhone,
                date,
                meal,
                seatingArea,
                tableSize
        );

      // Call the API
        Call<Void> call = reservationApi.updateReservation(updatedReservation);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditMyReservationActivity.this, "Reservation updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditMyReservationActivity.this, "Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditMyReservationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


*/
