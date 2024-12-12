package com.example.neutralcafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class FeedbackrActivity extends AppCompatActivity {

    private String username; // To store the username passed to this activity
    private EditText commentEditText;
    private RadioGroup foodQualityGroup, environmentGroup, serviceGroup;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feadbackr_layout);

        // Retrieve the username from the intent
        username = getIntent().getStringExtra("USERNAME");

        // Initialize UI components
        commentEditText = findViewById(R.id.commentEditText); // EditText for comment
        foodQualityGroup = findViewById(R.id.food_quality_group); // RadioGroup for food quality
        environmentGroup = findViewById(R.id.environment_group); // RadioGroup for environment
        serviceGroup = findViewById(R.id.service_group); // RadioGroup for service
        submitButton = findViewById(R.id.submit_button);

        // Handle submit button click
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collect user input
                String comment = commentEditText.getText().toString();
                String foodQuality = getSelectedRadioButtonText(foodQualityGroup);
                String environment = getSelectedRadioButtonText(environmentGroup);
                String service = getSelectedRadioButtonText(serviceGroup);

                // Display a Toast message with the inputs
                Toast.makeText(FeedbackrActivity.this,
                        "Feedback Submitted\nFood Quality: " + foodQuality +
                                "\nEnvironment: " + environment +
                                "\nService: " + service +
                                "\nComment: " + comment,
                        Toast.LENGTH_LONG).show();

                // Navigate to HomePageActivity and pass the username
                Intent intent = new Intent(FeedbackrActivity.this, HomePageActivity.class);
                intent.putExtra("USERNAME", username); // Pass username
                startActivity(intent);
            }
        });
    }

    // Helper method to get the selected RadioButton's text
    private String getSelectedRadioButtonText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return selectedRadioButton.getText().toString();
        } else {
            return "Not Selected";
        }
    }
}
