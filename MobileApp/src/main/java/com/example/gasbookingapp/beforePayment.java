package com.example.gasbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class beforePayment extends AppCompatActivity {

    private TextView nameTxt, addressTxt, totalPriceTxt, cylinderpTxt;

    private Button checkoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_payment);

        nameTxt = findViewById(R.id.textView6);
        addressTxt = findViewById(R.id.textView5);
        totalPriceTxt = findViewById(R.id.textView9);
        cylinderpTxt = findViewById(R.id.textView19); // Add this TextView in the layout
        checkoutBtn = findViewById(R.id.checkoutBtn);

        // Set an OnClickListener on the backButton
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to MainMenuActivity
                Intent intent = new Intent(beforePayment.this, paymentPage.class);
                startActivity(intent);  // Start the MainMenuActivity
            }
        });

        // Retrieve data from intent
        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        int totalPrice = getIntent().getIntExtra("totalPrice", -1);
        int cylinderPrice = getIntent().getIntExtra("cylinderPrice", -1);

        if (name == null || address == null || totalPrice == -1 || cylinderPrice == -1) {
            Toast.makeText(this, "Data missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Add 500 to total price
        totalPrice += 500;

        // Display data
        nameTxt.setText(String.format("%s", name));
        addressTxt.setText(String.format("%s", address));
        totalPriceTxt.setText(String.format("Rs : %d", totalPrice));
        cylinderpTxt.setText(String.format("Rs : %d", cylinderPrice)); // Display cylinder price
    }
}