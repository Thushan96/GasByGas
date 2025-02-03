package com.example.gasbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class locationSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);

        RadioGroup branchRadioGroup = findViewById(R.id.branchRadioGroup);
        Button btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = branchRadioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    // No radio button selected
                    Toast.makeText(locationSelect.this, "Please select a branch", Toast.LENGTH_SHORT).show();
                } else {
                    // Get selected radio button
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String branchName = selectedRadioButton.getText().toString();

                    // Display selected branch (optional)
                    Toast.makeText(locationSelect.this, "Selected Branch: " + branchName, Toast.LENGTH_SHORT).show();

                    // Navigate to dashboard
                    Intent intent = new Intent(locationSelect.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
