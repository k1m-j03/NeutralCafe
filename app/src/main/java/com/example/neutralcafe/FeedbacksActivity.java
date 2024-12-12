package com.example.neutralcafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FeedbacksActivity extends AppCompatActivity {

    private String username; // To store the username passed to this activity
    private EditText commentEditText;
    private RadioGroup foodQualityGroup, environmentGroup, serviceGroup;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feadbacks_layout);

        // Retrieve the username from the intent
        username = getIntent().getStringExtra("USERNAME");

        // Initialize UI components
        commentEditText = findViewById(R.id.commentEditText); // EditText for comment
        foodQualityGroup = findViewById(R.id.interface_group); // RadioGroup for interface
        environmentGroup = findViewById(R.id.function_group); // RadioGroup for function
        serviceGroup = findViewById(R.id.quality_group); // RadioGroup for quality
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
                Toast.makeText(FeedbacksActivity.this,
                        "Feedback Submitted\nFood Quality: " + foodQuality +
                                "\nEnvironment: " + environment +
                                "\nService: " + service +
                                "\nComment: " + comment,
                        Toast.LENGTH_LONG).show();

                // Navigate to HomePageActivity and pass the username
                Intent intent = new Intent(FeedbacksActivity.this, HomePageActivity.class);
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
