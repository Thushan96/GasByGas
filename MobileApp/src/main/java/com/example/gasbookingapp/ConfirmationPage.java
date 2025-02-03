package com.example.gasbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConfirmationPage extends AppCompatActivity {

    private TextView mainMenuBtn;
    private TextView shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirmation_page);

        mainMenuBtn = findViewById(R.id.mainMenuBtn);

        // Set an OnClickListener on the backButton
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to MainMenuActivity
                Intent intent = new Intent(ConfirmationPage.this, DashboardActivity.class);
                startActivity(intent);  // Start the MainMenuActivity
            }
        });

    }
}