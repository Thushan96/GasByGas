package com.example.gasbookingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class smallGasScreen extends AppCompatActivity {

    private TextView priceTxt;
    private Button buyBtn;
    private String smallGasPrice;

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_gas_screen);

        priceTxt = findViewById(R.id.priceTxt);
        buyBtn = findViewById(R.id.buyBtn);
        backButton = findViewById(R.id.imageView2);

        // Load the small gas price from SharedPreferences
        loadSmallGasPrice();

        buyBtn.setOnClickListener(v -> {
            // Pass the gas price to the customerDetails activity
            Intent intent = new Intent(smallGasScreen.this, customerDetails.class);
            intent.putExtra("gasPrice", smallGasPrice);
            intent.putExtra("gasType", "small");
            startActivity(intent);
        });

        // Set an OnClickListener on the backButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to MainMenuActivity
                Intent intent = new Intent(smallGasScreen.this, DashboardActivity.class);
                startActivity(intent);  // Start the MainMenuActivity
            }
        });
    }

    private void loadSmallGasPrice() {
        SharedPreferences sharedPreferences = getSharedPreferences("GasPrices", MODE_PRIVATE);
        smallGasPrice = sharedPreferences.getString("smallGasPrice", "694"); // Default price
        priceTxt.setText(String.format("%s", smallGasPrice));
    }
}
