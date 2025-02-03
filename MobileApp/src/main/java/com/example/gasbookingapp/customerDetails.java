package com.example.gasbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class customerDetails extends AppCompatActivity {

    private Button cdNxtBtn;
    private EditText nameET, emailET, phoneET, addressOneET, addressTwoET, postalCodeET;
    private RadioGroup radioGroup;
    private RadioButton radioButtonYes, radioButtonNo;

    private String gasPrice, gasType;
    private int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        cdNxtBtn = findViewById(R.id.cdNxtBtn);
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        phoneET = findViewById(R.id.phoneET);
        addressOneET = findViewById(R.id.addressoneET);
        addressTwoET = findViewById(R.id.addresstwoET);
        postalCodeET = findViewById(R.id.postalcodeET);
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonYes = findViewById(R.id.radioButtonYes);
        radioButtonNo = findViewById(R.id.radioButtonNo);

        // Retrieve the gas price and type from the intent
        gasPrice = getIntent().getStringExtra("gasPrice");
        gasType = getIntent().getStringExtra("gasType");

        if (gasPrice == null || gasType == null) {
            Toast.makeText(this, "Gas price or type is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cdNxtBtn.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select if you have an empty gas cylinder", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean hasEmptyCylinder = selectedId == radioButtonYes.getId();
            int basePrice;
            try {
                basePrice = Integer.parseInt(gasPrice);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid gas price!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate total price
            int cylinderPrice = hasEmptyCylinder ? 0 : getAdditionalPrice();
            totalPrice = basePrice + cylinderPrice;

            // Navigate to beforePayment
            Intent intent = new Intent(customerDetails.this, beforePayment.class);
            intent.putExtra("name", nameET.getText().toString());
            intent.putExtra("address", addressOneET.getText().toString() + ", " + addressTwoET.getText().toString());
            intent.putExtra("totalPrice", totalPrice);
            intent.putExtra("cylinderPrice", cylinderPrice); // Pass the cylinder price
            startActivity(intent);
        });
    }

    private int getAdditionalPrice() {
        switch (gasType) {
            case "medium":
                return 3000;
            case "large":
                return 5000;
            default:
                return 2000; // Small cylinder
        }
    }
}
