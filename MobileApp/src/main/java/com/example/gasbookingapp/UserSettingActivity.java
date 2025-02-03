package com.example.gasbookingapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

public class UserSettingActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView userNameTextView;
    private TextView userNameTextView2;
    private ImageView profileImageView; // ImageView to display selected profile photo
    private TextView deleteAccountTextView; // TextView for delete account option
    private ImageView backButton;

    private LinearLayout logoutBtn;
    private TextView userMobileTextView;
    private TextView userEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        // Initialize the backButton ImageView
        backButton = findViewById(R.id.backButton);


        // Initialize the TextViews and ImageView after setContentView
        userNameTextView = findViewById(R.id.userNameTextView);
        userNameTextView2 = findViewById(R.id.userNameTextView2);
        profileImageView = findViewById(R.id.profileImageView); // Make sure you have an ImageView in your layout
        deleteAccountTextView = findViewById(R.id.deleteAccountTextView); // Initialize the delete account button
        logoutBtn = findViewById(R.id.logoutBtn);
        userMobileTextView = findViewById(R.id.userMobileTextView);
        userEmailTextView = findViewById(R.id.userEmailTextView);



        // Retrieve the saved name from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData",  MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "Email Address");
        String savedName = sharedPreferences.getString("fullName", "Guest");
        String savedPhone = sharedPreferences.getString("phone", "Not available");// Default to "Guest" if not set
        String savedImageUri = sharedPreferences.getString("userProfileImageUri", null); // Retrieve the image URI

        // Display full name on the profile
        userNameTextView.setText(savedName);
        userEmailTextView.setText(savedEmail);
        userNameTextView2.setText(savedName);
        userMobileTextView.setText(savedPhone);

        // Load the saved profile image if it exists
        if (savedImageUri != null) {
            Uri savedUri = Uri.parse(savedImageUri);
            Glide.with(this)
                    .load(savedUri)
                    .transform(new CircleCrop()) // Apply circular crop transformation
                    .into(profileImageView);
        }

        logoutBtn.setOnClickListener(v -> {
            // Clear the isLoggedIn flag
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            // Navigate back to MainActivity
            Intent intent = new Intent(UserSettingActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close DashboardActivity
        });

        // Set an OnClickListener on the backButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to MainMenuActivity
                Intent intent = new Intent(UserSettingActivity.this, DashboardActivity.class);
                startActivity(intent);  // Start the MainMenuActivity
            }
        });

        // Set the listener to open the dialog when the username is clicked
        userNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateNameDialog();
            }
        });

        // Set the listener to open the image picker when the "Update Profile Photo" is clicked
        TextView updateProfilePhotoTextView = findViewById(R.id.updateProfilePhotoTextView);
        updateProfilePhotoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker(); // Open image picker when clicked
            }
        });

        // Set the listener for the "Delete Account" option
        deleteAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAccountConfirmationDialog();
            }
        });
    }

    // Method to open image picker
    private void openImagePicker() {
        // Intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST); // Start the activity to pick an image
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from the image picker
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData(); // Get the URI of the selected image
            try {
                // Use Glide to load the image with circular cropping
                Glide.with(this)
                        .load(selectedImageUri) // Load the image URI
                        .transform(new CircleCrop()) // Apply circular crop transformation
                        .into(profileImageView); // Set the image to the ImageView

                // Optionally, save the selected image URI in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userProfileImageUri", selectedImageUri.toString()); // Save the image URI
                editor.apply();

                // Pass the image URI to the MainMenuActivity
                Intent intent = new Intent(UserSettingActivity.this, DashboardActivity.class);
                intent.putExtra("userProfileImageUri", selectedImageUri.toString());
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Method to show the update name dialog
    private void showUpdateNameDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_name);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        EditText nameEditText = dialog.findViewById(R.id.nameEditText);
        Button saveButton = dialog.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = nameEditText.getText().toString().trim();
                if (!newName.isEmpty()) {
                    // Update the TextViews in the current activity
                    userNameTextView.setText(newName);
                    userNameTextView2.setText(newName);

                    // Save the updated name in SharedPreferences
                    saveNameToPreferences(newName);

                    // Pass the updated name to the MainMenuActivity
                    Intent intent = new Intent(UserSettingActivity.this, DashboardActivity.class);
                    intent.putExtra("updatedName", newName);
                    startActivity(intent);

                    dialog.dismiss();
                } else {
                    nameEditText.setError("Name cannot be empty");
                }
            }
        });

        dialog.show();
    }

    // Method to save the updated name in SharedPreferences
    private void saveNameToPreferences(String newName) {
        // Get SharedPreferences editor
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullName", newName); // Save the updated name
        editor.apply(); // Apply changes asynchronously
    }

    // Method to show the delete account confirmation dialog
    private void showDeleteAccountConfirmationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm_delete_account);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        Button confirmButton = dialog.findViewById(R.id.confirmButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccountData();
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Close the dialog without deleting the account
            }
        });

        dialog.show();
    }

    // Method to delete user data
    private void deleteAccountData() {
        // Clear the SharedPreferences where user data is stored
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all saved data
        editor.apply(); // Apply changes asynchronously

        // Optionally, you can navigate to a login or home screen
        Intent intent = new Intent(UserSettingActivity.this, RegisterActivity.class); // Or any other activity you prefer
        startActivity(intent);
        finish(); // Close the current activity
    }
}