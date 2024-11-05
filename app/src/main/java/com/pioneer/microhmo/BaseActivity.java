package com.pioneer.microhmo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private static final long TIMEOUT = 15 * 60 * 1000; // 15 minutes in milliseconds
    // For testing purposes, you can use the following line for 5 seconds timeout
    //private static final long TIMEOUT = 5 * 60 * 100; // 5 seconds
    private Handler handler = new Handler();

    private Runnable logoutRunnable = new Runnable() {
        @Override
        public void run() {
            logoutUser();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("----------------AutoLogout", "Initiated");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AutoLogout", "onResume");
        resetLogoutTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AutoLogout", "onPause");
        handler.removeCallbacks(logoutRunnable);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        Log.d("AutoLogout", "User interaction detected");
        resetLogoutTimer();
    }

    private void resetLogoutTimer() {
        handler.removeCallbacks(logoutRunnable);
        handler.postDelayed(logoutRunnable, TIMEOUT);
    }

    private void logoutUser() {
        Log.d("AutoLogout", "Logging out user");
        // Implement your logout logic here
        Intent intent = new Intent(this, AccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close current activity
    }

    @Override
    public void onBackPressed() {
        // Disable the back button functionality
        // Remove the super.onBackPressed() line to disable the back button completely
    }
}