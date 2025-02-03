package com.example.gasbookingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    EditText etEmail, etPassword, etFullName, etPhone;
    Button btnRegister;

    TextView signInText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.emailET);
        etPassword = findViewById(R.id.passwordET);
        etFullName = findViewById(R.id.nameET);
        etPhone = findViewById(R.id.phoneET);
        btnRegister = findViewById(R.id.signInBtn);
        signInText = findViewById(R.id.signInText);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String fullName = etFullName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || fullName.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                } else if (phone.length() != 10 || !phone.matches("\\d+")) {
                    Toast.makeText(RegisterActivity.this, "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show();
                } else {
                    // Save to SharedPreferences
                    // Save to SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.putString("fullName", fullName);
                    editor.putString("phone", phone);
                    editor.apply();

                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    // Navigate to Login Screen
                    Intent intent = new Intent(RegisterActivity.this, locationSelect.class);
                    startActivity(intent);
                }
            }
        });

        // Sign in text click listener
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}