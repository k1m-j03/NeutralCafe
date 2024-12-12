package com.example.neutralcafe;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestApiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);

        TextView apiStatusText = findViewById(R.id.api_status_text);
        Button testApiButton = findViewById(R.id.test_api_button);

        testApiButton.setOnClickListener(v -> testApiConnection(apiStatusText));
    }

    private void testApiConnection(TextView apiStatusText) {
        ReservationApi apiService = ApiClient.getRetrofitInstance().create(ReservationApi.class);

        Call<List<Reservation>> call = apiService.getAllReservations();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Reservation>> call, @NonNull Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = "API Connected Successfully! Reservations: " + response.body().size();
                    apiStatusText.setText(message);
                    apiStatusText.setTextColor(Color.GREEN);
                    Log.d("API_TEST", message);
                } else {
                    String message = "API Response Error: " + response.code();
                    apiStatusText.setText(message);
                    apiStatusText.setTextColor(Color.RED);
                    Log.e("API_TEST", message);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Reservation>> call, @NonNull Throwable t) {
                String message = "API Connection Failed: " + t.getMessage();
                apiStatusText.setText(message);
                apiStatusText.setTextColor(Color.RED);
                Log.e("API_TEST", message);
            }
        });
    }
}
