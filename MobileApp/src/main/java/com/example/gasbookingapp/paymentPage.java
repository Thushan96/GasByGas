package com.example.gasbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class paymentPage extends AppCompatActivity {

    private RelativeLayout backBtn;
    private Button payNowButton;
    private EditText nameET, phoneET, dateET, cvcET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        backBtn = findViewById(R.id.backLayout);
        payNowButton = findViewById(R.id.payyBtn);

        // Initialize EditText fields
        nameET = findViewById(R.id.nameET);
        phoneET = findViewById(R.id.cardET);
        dateET = findViewById(R.id.dateET);
        cvcET = findViewById(R.id.cvcET);

        // Set a length filter for the date field
        dateET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});

        // Handle Back Button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(paymentPage.this, customerDetails.class);
                startActivity(intent);
            }
        });

        // Handle Pay Now Button
        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input fields
                if (validateInputs()) {
                    // Proceed with payment logic
                    Toast.makeText(paymentPage.this, "Payment Successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to confirmation or success page
                    Intent intent = new Intent(paymentPage.this, ConfirmationPage.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateInputs() {
        // Get input values
        String name = nameET.getText().toString().trim();
        String cardNumber = phoneET.getText().toString().trim();
        String expiryDate = dateET.getText().toString().trim();
        String cvc = cvcET.getText().toString().trim();

        // Check if any field is empty
        if (name.isEmpty()) {
            nameET.setError("Full Name is required");
            nameET.requestFocus();
            return false;
        }

        if (cardNumber.isEmpty()) {
            phoneET.setError("Card Number is required");
            phoneET.requestFocus();
            return false;
        }

        if (cardNumber.length() != 16) {
            phoneET.setError("Card Number must be 16 digits");
            phoneET.requestFocus();
            return false;
        }

        if (expiryDate.isEmpty()) {
            dateET.setError("Expiry Date is required");
            dateET.requestFocus();
            return false;
        }

        if (!expiryDate.matches("\\d{2}/\\d{2}")) { // Format: MM/YY
            dateET.setError("Expiry Date must be in MM/YY format");
            dateET.requestFocus();
            return false;
        }

        if (cvc.isEmpty()) {
            cvcET.setError("CVC is required");
            cvcET.requestFocus();
            return false;
        }

        if (cvc.length() != 3) {
            cvcET.setError("CVC must be 3 digits");
            cvcET.requestFocus();
            return false;
        }

        // Validation passed
        return true;
    }
}
