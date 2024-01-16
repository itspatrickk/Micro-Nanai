package com.pioneer.nanaiv3;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.pioneer.nanaiv3.util.SharedPreferencesUtility;


public class SplashActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                Toast.makeText(SplashActivity.this, "Permission granted.",Toast.LENGTH_LONG).show();
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            activityResultLauncher.launch(Manifest.permission.READ_PHONE_STATE);
//        }
//        try{
//            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//            String deviceId = telephonyManager.getImei();
//
//            Log.d("deviceId", deviceId);
//        }catch (Exception e){
//            e.printStackTrace();
//            try {
//                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//
//                Log.d("deviceId", deviceId);
//            }catch (Exception es){
//
//            }
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(SplashActivity.this, CameraActivity.class));
                Boolean isUserExist = false;

                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);
                    isUserExist = SharedPreferencesUtility.isUserExist(sharedPreferences);
                }catch (Exception e){
                    isUserExist = false;
                }
//                startActivity(new Intent(SplashActivity.this, AccountActivity.class));//MainActivity.class))

                if (isUserExist){
                    startActivity(new Intent(SplashActivity.this, AccountActivity.class));//MainActivity.class))
                }else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));//MainActivity.class));
                }
                finish();
            }
        }, 3000);

    }
}