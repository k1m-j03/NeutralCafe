package com.example.neutralcafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyReserveActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private LinearLayout recordLayout;
    private ReservationApi reservationApi;
    private Button homeButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myreserve_layout);

        username = getIntent().getStringExtra("USERNAME");

        // Initialize Views
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        ImageView menuIcon = findViewById(R.id.menu_icon);
        ImageView profileIcon = findViewById(R.id.profile_icon);
        recordLayout = findViewById(R.id.record_layout);
        homeButton = findViewById(R.id.home_button);

        // Set up Navigation Drawer Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle Menu Icon Click
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Handle Profile Icon Click
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MyReserveActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Edit button listener
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyReserveActivity.this, HomePageActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });

        // Set up Navigation Drawer Item Clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            handleNavigationItemSelected(id);
            return true;
        });

        // Initialize API Client
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        reservationApi = retrofit.create(ReservationApi.class);

        // Fetch and display data from API
        fetchReservationData();
    }

    private void handleNavigationItemSelected(int id) {
        if (id == R.id.home) {
            Intent intent = new Intent(MyReserveActivity.this, HomePageActivity.class);
            intent.putExtra("USERNAME", username); // Pass username
            startActivity(intent);
        } else if (id == R.id.menu) {
            Intent intent = new Intent(MyReserveActivity.this, MenuListActivity.class);
            intent.putExtra("USERNAME", username); // Pass username
        } else if (id == R.id.reserve) {
            Intent intent = new Intent(MyReserveActivity.this, Reserve1Activity.class);
            intent.putExtra("USERNAME", username); // Pass username
            startActivity(intent);
        } else if (id == R.id.logout) {
            startActivity(new Intent(MyReserveActivity.this, LoginActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void fetchReservationData() {
        Call<List<Reservation>> call = reservationApi.getAllReservations();
        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayReservations(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void displayReservations(List<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            View recordView = getLayoutInflater().inflate(R.layout.record_item, null);

            TextView dateView = recordView.findViewById(R.id.record_date);
            TextView mealView = recordView.findViewById(R.id.record_meal);
            TextView seatingAreaView = recordView.findViewById(R.id.record_seating_area);
            TextView numberOfPaxView = recordView.findViewById(R.id.record_number_of_pax);

            dateView.setText("Date: " + reservation.getDate());
            mealView.setText("Meal: " + reservation.getMeal());
            seatingAreaView.setText("Seating Area: " + reservation.getSeatingArea());
            numberOfPaxView.setText("Number of Pax: " + reservation.getTableSize());

            // Set OnClickListener to navigate to ReservationDetailsActivity with reservation ID
            recordView.setOnClickListener(v -> {
                Intent intent = new Intent(MyReserveActivity.this, MyReserveDataActivity.class);
                intent.putExtra("reservation_id", reservation.getId()); // Pass the reservation ID
                startActivity(intent);
            });

            recordLayout.addView(recordView);
        }
    }
}
