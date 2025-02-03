package com.example.gasbookingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

public class DashboardActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private ConstraintLayout constraintTwoLayout;
    private ConstraintLayout constraintThreeLayout;
    private ConstraintLayout constraintLayoutAllInOne;

    private ImageView settingIcon;
    private TextView userNameTextViewInAnotherActivity;
    private ImageView profileImageView;

    private Button rateAppButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        constraintLayout = findViewById(R.id.constraintLayout);
        constraintTwoLayout = findViewById(R.id.constraintTwoLayout);
        constraintThreeLayout = findViewById(R.id.constraintThreeLayout);


        // Initialize views
        settingIcon = findViewById(R.id.settingIcon);
        userNameTextViewInAnotherActivity = findViewById(R.id.userNameTextViewInAnotherActivity);
        profileImageView = findViewById(R.id.profileImageView);  // Initialize profile image view
        rateAppButton = findViewById(R.id.RateApp);

        // Retrieve the updated name or saved name from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String savedName = sharedPreferences.getString("fullName", "Guest"); // Default to "Guest"
        userNameTextViewInAnotherActivity.setText(savedName);

        // Retrieve the saved profile image URI from SharedPreferences
        String imageUriString = sharedPreferences.getString("userProfileImageUri", null);

        // Display the profile image if a URI is saved
        if (imageUriString != null && !imageUriString.isEmpty()) {
            Uri imageUri = Uri.parse(imageUriString);

            // Load the image with Glide, applying a circular crop
            Glide.with(this)
                    .load(imageUri) // Load the image from the URI
                    .transform(new CircleCrop()) // Apply circular crop transformation
                    .into(profileImageView); // Set the image in the ImageView
        }

        // Set up the setting icon click listener to navigate to the settings screen
        settingIcon.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(DashboardActivity.this, UserSettingActivity.class);
            startActivity(settingsIntent);
        });

        // Set an OnClickListener on the ConstraintLayout
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to MainActivity
                Intent intent = new Intent(DashboardActivity.this, smallGasScreen.class);
                startActivity(intent);  // Start MainActivity
            }
        });

        // Set an OnClickListener on the ConstraintLayout
        constraintTwoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to MainActivity
                Intent intent = new Intent(DashboardActivity.this, mediumGasScreen.class);
                startActivity(intent);  // Start MainActivity
            }
        });

        // Set an OnClickListener on the ConstraintLayout
        constraintThreeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to MainActivity
                Intent intent = new Intent(DashboardActivity.this, largeGasScreen.class);
                startActivity(intent);  // Start MainActivity
            }
        });
    }
}