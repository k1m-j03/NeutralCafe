package com.example.neutralcafe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        Button loginButton = findViewById(R.id.loginButton);
        TextView newAccountLink = findViewById(R.id.newAccountLink);

        loginButton.setOnClickListener(v ->
                Toast.makeText(LoginActivity.this, "Login Button Clicked!", Toast.LENGTH_SHORT).show()
        );


        newAccountLink.setOnClickListener(v ->
                Toast.makeText(LoginActivity.this, "Navigate to New Account Page", Toast.LENGTH_SHORT).show()
        );
    }
}
