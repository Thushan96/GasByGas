package com.example.gasbookingapp;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView smallGasPrice;
    private TextView mediumGasPrice;
    private TextView largeGasPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        smallGasPrice = findViewById(R.id.smallgasprice);
        mediumGasPrice = findViewById(R.id.mediumgasprice);
        largeGasPrice = findViewById(R.id.threegascylinderprice);

        // Load saved prices from SharedPreferences
        loadPricesFromPreferences();

        smallGasPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdatePriceDialog("smallGasPrice");
            }
        });

        mediumGasPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdatePriceDialog("mediumGasPrice");
            }
        });

        largeGasPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdatePriceDialog("largeGasPrice");
            }
        });
    }

    private void showUpdatePriceDialog(final String priceKey) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_price);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText priceEditText = dialog.findViewById(R.id.nameEditText);
        Button saveButton = dialog.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPrice = priceEditText.getText().toString().trim();
                if (!newPrice.isEmpty()) {
                    updatePrice(priceKey, newPrice);
                    dialog.dismiss();
                } else {
                    priceEditText.setError("Price cannot be empty");
                }
            }
        });

        dialog.show();
    }

    private void updatePrice(String priceKey, String newPrice) {
        // Update the relevant TextView
        switch (priceKey) {
            case "smallGasPrice":
                smallGasPrice.setText(newPrice);
                break;
            case "mediumGasPrice":
                mediumGasPrice.setText(newPrice);
                break;
            case "largeGasPrice":
                largeGasPrice.setText(newPrice);
                break;
        }

        // Save the updated price in SharedPreferences
        savePriceToPreferences(priceKey, newPrice);
    }

    private void savePriceToPreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("GasPrices", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void loadPricesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("GasPrices", MODE_PRIVATE);
        String smallPrice = sharedPreferences.getString("smallGasPrice", "694");
        String mediumPrice = sharedPreferences.getString("mediumGasPrice", "1482");
        String largePrice = sharedPreferences.getString("largeGasPrice", "3690");

        smallGasPrice.setText(smallPrice);
        mediumGasPrice.setText(mediumPrice);
        largeGasPrice.setText(largePrice);
    }
}
