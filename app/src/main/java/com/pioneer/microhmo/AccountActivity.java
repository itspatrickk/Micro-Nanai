package com.pioneer.microhmo;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pioneer.microhmo.objects.AgentInfo;
import com.pioneer.microhmo.objects.Matrix;
import com.pioneer.microhmo.util.SharedPreferencesUtility;
import com.pioneer.microhmo.util.Statics;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    Button btnLogin, startButton;

    Button validatePinButton, forgotPin;

    LinearLayout validatePin;
    FrameLayout startDiv;

    TextView loginMessage;

    String deviceId = "";

    EditText pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        loginMessage = findViewById(R.id.login_message);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);

        Intent intent = getIntent();
        String otp = intent.getStringExtra("otp");
        if (otp != null) {
            SharedPreferencesUtility.saveOtp(sharedPreferences, otp);
        }

        pin = findViewById(R.id.pin);
        btnLogin = findViewById(R.id.btnLogin);
        startButton = findViewById(R.id.btnLogin);
        validatePinButton = findViewById(R.id.validatePinButton);
        forgotPin  = findViewById(R.id.forgotPin);

        pin.requestFocus();

        validatePin = findViewById(R.id.validatePin);
        startDiv = findViewById(R.id.startDiv);

        validatePin.setVisibility(View.GONE);
        startDiv.setVisibility(View.GONE);


        try{
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            deviceId = telephonyManager.getImei();
        }catch (Exception e){
            //e.printStackTrace();
            try {
                deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }catch (Exception es){

            }
        }

        SharedPreferencesUtility.saveAndroidId(sharedPreferences, deviceId);

        TextView androidId = findViewById(R.id.deviceId);
        androidId.setText(deviceId);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePin.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.GONE);

                pin.setFocusable(true);
                pin.requestFocus();
                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                InputMethodManager imm = getSystemService(InputMethodManager.class);
                imm.showSoftInput(pin, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        forgotPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferencesUtility.removeOtp(sharedPreferences);
                Intent intent = new Intent(AccountActivity.this, ActivateActivity.class);
                startActivity(intent);
            }
        });

        validatePin.setVisibility(View.GONE);
        startDiv.setVisibility(View.GONE);

        validatePinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinText = pin.getText().toString();
                String savedPin = SharedPreferencesUtility.getOtp(sharedPreferences);
//                if (pinText.equalsIgnoreCase("0000") || savedPin.equalsIgnoreCase(pinText)){

                String mobile = SharedPreferencesUtility.getMobileNo(sharedPreferences);
                    //TODO to delete this after update
                //String mobile="0998821111";
                Log.d("mobilemobilemobilemobile", mobile);
            //  if (pinText.equalsIgnoreCase("0000") || savedPin.equalsIgnoreCase(pinText)){
              if (savedPin != null && savedPin.equalsIgnoreCase(pinText)){
                    updateLimits();
                    if (mobile != null && mobile.length() > 10)
                        getAgentUpdate(mobile);
                    Intent intent = new Intent(AccountActivity.this, MenuActivity.class);
                    startActivity(intent);
                }else{
                    int attempt = SharedPreferencesUtility.getAttemptCount(sharedPreferences);
                  Log.d("+++++ATEMPT: ", "onClick: " + attempt);
                    if (attempt > 2){
                        showAlert1("Account locked, please reset your account");
                    }
                    else{
                        showAlert( "Please check your PIN");
                        SharedPreferencesUtility.addAttemptCount(sharedPreferences);
                    }

                }
            }
        });

        String message = "Magandang araw Tita "+SharedPreferencesUtility.getAgentName(sharedPreferences) +", pindutin ang LOGIN at ilagay ang inyong PIN";

        loginMessage.setText(message);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(android.Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            activityResultLauncher.launch(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE);
//        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.INTERNET);
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        // Get device model
        String deviceModel = Build.MODEL;
        // Get Android version
        String androidVersion = Build.VERSION.RELEASE;
        // Get device brand
        String deviceBrand = Build.BRAND;
        // Get device ID
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String deviceId = telephonyManager.getImei();
            Log.d("deviceid", deviceId);
        }catch (Exception e){

        }
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long totalRam = memoryInfo.totalMem;
        String ram =  (totalRam / (1024 * 1024)) + " MB";

        Log.d("version", androidVersion);
        Log.d("brand", deviceBrand);
        Log.d("model", deviceModel);
        Log.d("ram", ram);

    }

    final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                Toast.makeText(AccountActivity.this, "Permission granted.",Toast.LENGTH_LONG).show();
            }
        }
    });

    @Override
    public void onBackPressed() {
        // Disable the back button functionality
        // Remove the super.onBackPressed() line to disable the back button completely
    }


    SharedPreferences sharedPreferences;

    String accessToken;
    public void syncMatrix(){
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Statics.LOV_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Gson gson = new Gson();
                            String message = "Processing member records";

                            Matrix[] lovs = new Gson().fromJson(response, Matrix[].class);

                            //Toast.makeText(context,  "Total members : " + lovs.length,Toast.LENGTH_LONG).show();
                            if (lovs != null && lovs.length > 0){
                                for (Matrix lov : lovs){
                                    SharedPreferencesUtility.saveString(sharedPreferences, lov.getCode(), lov.getDesc() );
                                }
                            }
                        } catch (Exception e) {
                            //Toast.makeText(context,  e.getMessage() + "\nPlease contact admin",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+accessToken);
                return headers;
            }
        };

        queue.add(stringRequest);
    }
    public void updateLimits(){
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Statics.CRED_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            accessToken = json.getString("access_token");
                            Log.d("accessToken", accessToken);
                            SharedPreferencesUtility.saveToken(sharedPreferences, accessToken);
                            syncMatrix();
                        } catch (Exception e) {
                            //throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Statics.headerValue);
                return headers;
            }
        };

        queue.add(stringRequest);
    }


    public void getAgentUpdate(String mobile){
        Context context = getApplicationContext();

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Statics.AGENTINFO_URL+mobile ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            AgentInfo agent = gson.fromJson(response, AgentInfo.class);
                            SharedPreferencesUtility.saveCenters(sharedPreferences, agent.getCenters());
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+accessToken);
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    public void showAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void showAlert1(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> forgotPin.performClick())
                .create()
                .show();
    }
}