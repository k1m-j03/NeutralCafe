package com.example.neutralcafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reserve4Activity extends AppCompatActivity {
    private static final String TAG = "Reserve4Activity";
    private DrawerLayout drawerLayout;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve4_layout);

        // Retrieve the reservation ID and username
        int reservationId = getIntent().getIntExtra("reservationId", -1);
        username = getIntent().getStringExtra("USERNAME");
        TextView detailsTextView = findViewById(R.id.reservation_details);

        Log.d(TAG, "Reservation ID received: " + reservationId);
        Log.d(TAG, "Username received: " + username);

        if (reservationId == -1) {
            Log.e(TAG, "No reservation ID found in intent extras.");
            detailsTextView.setText("No reservation ID found. Please try again.");
            return; // Exit early
        }
        Log.d(TAG, "Fetched reservationId: " + reservationId);

        if (username == null || username.isEmpty()) {
            Log.e(TAG, "No username found in intent extras.");
            detailsTextView.setText("No username found. Please try again.");
            return; // Exit early
        }

        // Initialize Navigation Drawer
        setupNavigationDrawer();

        Log.d(TAG, "Username received: " + username);
        fetchReservationDetails(reservationId, detailsTextView);

        Button homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            finish();
        });

        Button reservation = findViewById(R.id.my_reservation_button);
        reservation.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyReserveActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            finish();
        });

    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        if (drawerLayout == null || navigationView == null) {
            Log.e(TAG, "DrawerLayout or NavigationView not found in the layout.");
            return;
        }

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
            intent = new Intent(Reserve4Activity.this, HomePageActivity.class);
        } else if (id == R.id.menu) {
            intent = new Intent(Reserve4Activity.this, MenuListActivity.class);
        } else if (id == R.id.reserve) {
            intent = new Intent(Reserve4Activity.this, Reserve1Activity.class);
        } else if (id == R.id.logout) {
            intent = new Intent(Reserve4Activity.this, LoginActivity.class);
            finish();
            return;
        } else {
            return;
        }
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
    }


    private void fetchReservationDetails(int reservationId, TextView detailsTextView) {
        ReservationApi apiService = ApiClient.getRetrofitInstance().create(ReservationApi.class);

        apiService.getReservationById(reservationId).enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Reservation reservation = response.body();
                    String details = "Reservation Details:\n\n"
                            + "Reserve ID: " + reservation.getId() + "\n"
                            + "Name: " + reservation.getCustomerName() + "\n"
                            + "Phone: " + reservation.getCustomerPhoneNumber() + "\n"
                            + "Date: " + reservation.getDate() + "\n"
                            + "Meal: " + reservation.getMeal() + "\n"
                            + "Table Size: " + reservation.getTableSize() + "\n"
                            + "Seating Area: " + reservation.getSeatingArea();
                    detailsTextView.setText(details);
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Log.e(TAG, "API Error: " + response.errorBody().string());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading errorBody", e);
                    }
                    detailsTextView.setText("Failed to fetch reservation details. Please try again.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Reservation> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching reservation details: " + t.getMessage(), t);
                detailsTextView.setText("Error fetching reservation details. Please check your connection and try again.");
                Toast.makeText(Reserve4Activity.this, "Failed to load reservation details.", Toast.LENGTH_LONG).show();
            }
        });
    }


}
