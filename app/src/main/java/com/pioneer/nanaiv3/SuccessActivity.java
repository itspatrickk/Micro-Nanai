package com.pioneer.nanaiv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Intent intent = getIntent();
        String products = intent.getStringExtra("products");
        String insured = intent.getStringExtra("insured");

        TextView successMessage = findViewById(R.id.success_message);
        String message = "Ayos, si " +insured + " ay nakaseguro na sa " + products +".";

        successMessage.setText(message);

        Button menuButton = findViewById(R.id.menuButton);
        Button enrollButton = findViewById(R.id.enrollButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable the back button functionality
        // Remove the super.onBackPressed() line to disable the back button completely
    }
}