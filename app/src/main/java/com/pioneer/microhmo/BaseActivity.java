package com.pioneer.microhmo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pioneer.microhmo.util.Statics;

public class BaseActivity extends AppCompatActivity {

    private static final int INACTIVITY_MINUTES = 1;
    private static final long TIMEOUT = INACTIVITY_MINUTES * 60 * 1000;

    private Handler handler;
    private Runnable logoutRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AutoLogout", "onCreate");

        handler = new Handler();
        logoutRunnable = () -> {
            if (Statics.isLoggedIn) {
                logoutUser();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AutoLogout", "onResume");
        resetLogoutTimer();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("AutoLogout", "Touch detected");
        resetLogoutTimer();
        return super.onTouchEvent(event);
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
        resetLogoutTimer();
    }

    private void resetLogoutTimer() {
        handler.removeCallbacks(logoutRunnable);
        handler.postDelayed(logoutRunnable, TIMEOUT);
    }

    private void logoutUser() {
        Toast.makeText(this, "You have been inactive for " + INACTIVITY_MINUTES + " minutes", Toast.LENGTH_SHORT).show();
        Log.d("AutoLogout", "User logged out due to inactivity");

        if (!isScreenActive(this)) {
            Intent intent = new Intent(this, AccountActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private boolean isScreenActive(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return powerManager != null && (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
                ? powerManager.isInteractive()
                : powerManager.isScreenOn());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(logoutRunnable);
    }

    @Override
    public void onBackPressed() {
        // Back button disabled
    }
}