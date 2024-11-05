package com.pioneer.microhmo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hcaptcha.sdk.HCaptcha;
import com.hcaptcha.sdk.HCaptchaConfig;
import com.hcaptcha.sdk.HCaptchaException;
import com.hcaptcha.sdk.HCaptchaSize;
import com.hcaptcha.sdk.HCaptchaTheme;
import com.hcaptcha.sdk.HCaptchaTokenResponse;
import com.hcaptcha.sdk.tasks.OnFailureListener;
import com.hcaptcha.sdk.tasks.OnSuccessListener;
import com.pioneer.microhmo.objects.AgentInfo;
import com.pioneer.microhmo.objects.Matrix;
import com.pioneer.microhmo.objects.MemberInfo;
import com.pioneer.microhmo.objects.PolicyInfo;
import com.pioneer.microhmo.util.SharedPreferencesUtility;
import com.pioneer.microhmo.util.Statics;
import com.pioneer.microhmo.util.TimerViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivateActivity extends AppCompatActivity {

    LinearLayout validateDiv, activateDiv,setPinDiv;

    Button btnActivate, validatePin,setPinBtn;

    EditText otp, mobileNumber,onetimepin, onetimepin2;
    TextView messageView, messageView1;
    private TimerViewModel timerViewModel;

    DatabaseHelper databaseHelper;

    private CountDownTimer countDownTimer;
    private long millisUntilFinished = 120000;

    String deviceId = "";

    static String accessToken = null;
    SharedPreferences sharedPreferences ;

    String mobile = "";


    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                Toast.makeText(ActivateActivity.this, "Permission granted.",Toast.LENGTH_LONG).show();
            }
        }
    });

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_activate);

        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);

//        progressBar = findViewById(R.id.progressBar);
        validateDiv = findViewById(R.id.validateDiv);
        activateDiv = findViewById(R.id.activateDiv);
        setPinDiv = findViewById(R.id.setPinDiv);

        setPinDiv.setVisibility(View.GONE);
        validateDiv.setVisibility(View.GONE);

        otp = findViewById(R.id.otp);
        onetimepin2 = findViewById(R.id.onetimepin2);
        onetimepin = findViewById(R.id.onetimepin);
        mobileNumber = findViewById(R.id.mobileNumber);


        btnActivate = findViewById(R.id.btnActivate);
        validatePin = findViewById(R.id.validatePin);
        setPinBtn = findViewById(R.id.setPin);

//        progressBar.setVisibility(View.GONE);


        messageView = findViewById(R.id.activate_message);
        messageView1 = findViewById(R.id.activate_message1);

        mobileNumber.requestFocus();
        InputMethodManager imm = getSystemService(InputMethodManager.class);
        imm.showSoftInput(mobileNumber, InputMethodManager.SHOW_IMPLICIT);
        syncMatrix();


//        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
//
//        timerViewModel.getRemainingTime().observe(this, remainingTime -> {
//            if (remainingTime > 0) {
//                btnActivate.setText("Wait " + remainingTime / 1000 + " seconds");
//            } else {
//                btnActivate.setEnabled(true);
//                btnActivate.setText("Activate");
//            }
//        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.READ_PHONE_STATE);
        }

        try{
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
              deviceId = telephonyManager.getImei();
        }catch (Exception e){
            e.printStackTrace();
            try {
                deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }catch (Exception es){

            }
        }

        TextView androidId = findViewById(R.id.deviceId);
        androidId.setText(deviceId);

        btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActivate.setVisibility(View.INVISIBLE);
                 mobile = mobileNumber.getText().toString();
                 if (mobile.length() == 9){
                     mobile = "639"+mobile;//.substring(mobile.length()-10);
                     activateAccount( mobile);

                 }else{
                     btnActivate.setVisibility(View.VISIBLE);
                     showAlert("Please check your mobile number.");
                 }
            }
        });

        validatePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = otp.getText().toString();
                if (pin.length() == 4){
                    validateOTP(pin);
                }else{
                    btnActivate.setVisibility(View.VISIBLE);
                    showAlert("Please check your OTP.");
                }
            }
        });

        setPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = onetimepin.getText().toString();
                String pin2 = onetimepin2.getText().toString();
                if (pin.length() == 4 && pin.equalsIgnoreCase(pin2)){
                    SharedPreferencesUtility.saveOtp(sharedPreferences, pin);

                    try{
                        Log.d("updating device information", mobile);
                        updateInfo(pin, mobile);
                        Log.d("updating device information done", mobile);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(ActivateActivity.this, AccountActivity.class);
                    startActivity(intent);
                }else{
                    showAlert("Please check your new OTP.");
                }
            }
        });


       //TODO CAPTCHA QUIZ

        final HCaptcha hCaptcha = HCaptcha.getClient(this);

        Log.d("----HCATPCHA" , "hCaptcha is now visible");

        hCaptcha
                .addOnSuccessListener(new OnSuccessListener<HCaptchaTokenResponse>() {
            @Override
            public void onSuccess(HCaptchaTokenResponse hCaptchaTokenResponse) {
                String userResponseTOken = hCaptchaTokenResponse.getTokenResult();

                Log.d("-----HCATPCHA " , "hCatpcha success token: " + userResponseTOken);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(HCaptchaException e) {
                        Log.d("hCaptcha", "hCaptcha failed: " + e.getMessage() + "(" + e.getStatusCode() + ")");
                    }
                });

        hCaptcha.setup();
        hCaptcha.verifyWithHCaptcha();
        final String SITE_KEY = "45f604dc-36b6-4e52-a1ca-27fcd504ed58";

        hCaptcha.setup(SITE_KEY).verifyWithHCaptcha();

        final HCaptchaConfig hCaptchaConfig = HCaptchaConfig.builder()
                .siteKey(SITE_KEY)
                .size(HCaptchaSize.NORMAL)
                .theme(HCaptchaTheme.LIGHT)
                .build();

        hCaptcha.setup(hCaptchaConfig).verifyWithHCaptcha();

    }

    public void validateOTP(final String mobileNo){
        Log.d("mobileNo", mobileNo);
        SharedPreferencesUtility.saveMobileNo(sharedPreferences, mobileNo);
        Context context = getApplicationContext();
//        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(context);
        Log.d("Statics.CRED_URL", Statics.CRED_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Statics.CRED_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            accessToken = json.getString("access_token");
                            Log.d("accessToken", accessToken);
                            SharedPreferencesUtility.saveToken(sharedPreferences, accessToken);
                            validateOTPx(mobileNo);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();
                        showAlert("Siguraduhing may internet connection wifi/mobile data bago mag activate sa app.");
                        btnActivate.setVisibility(View.VISIBLE);
                    }

                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // Calculate the progress based on the response data
                long contentLength = response.data.length;
                long transferredBytes = 0;

                // Update your progress bar with the calculated progress value
                int progress = (int) ((transferredBytes / (float) contentLength) * 100);
//                progressBar.setProgress(progress);

                // Return the response to be delivered
                return super.parseNetworkResponse(response);
            }

            @Override
            protected void deliverResponse(String response) {
                // Handle the response
                super.deliverResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Statics.headerValue);
                return headers;
            }
        };

        queue.add(stringRequest);
    }
    public void validateOTPx(final  String otp){
        Context context = getApplicationContext();
//        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("otp", Long.valueOf(otp));
            requestBody.put("reference", SharedPreferencesUtility.getReference(sharedPreferences));

        } catch ( Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Statics.VALIDATE_URL, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Boolean success = false;
                        try {
                            success = response.getBoolean("success");
                        } catch (JSONException e) {
                            showAlert("Please check your OTP");
//                            throw new RuntimeException(e);
                        }
                        if (success) {
//                            SharedPreferencesUtility.saveOtp(sharedPreferences, otp);
//                            Intent intent = new Intent(ActivateActivity.this, AccountActivity.class);
//                            startActivity(intent);
                            validateDiv.setVisibility(View.GONE);

                            String message = "Salamat "+SharedPreferencesUtility.getAgentName(sharedPreferences).trim()+ "!";
                            messageView.setText(message);
                            messageView1.setText("Ngayon ay i-set naman natin ang iyong 4 digit pin code para sa inyong password sa MIA App.");
                            setPinDiv.setVisibility(View.VISIBLE);


                            onetimepin.requestFocus();
                            InputMethodManager imm = getSystemService(InputMethodManager.class);
                            imm.showSoftInput(onetimepin, InputMethodManager.SHOW_IMPLICIT);
                            syncMatrix();

                            String temp = SharedPreferencesUtility.getTempReference(sharedPreferences);
                            String agentid = SharedPreferencesUtility.getAgentId(sharedPreferences);
                            if (temp != null && temp.length() > 10){
                                agentid = temp;
                            }

                            String finalAgentid = agentid;

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        syncPolicy(finalAgentid);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }else{
                            btnActivate.setVisibility(View.VISIBLE);
                            showAlert("Please check your OTP");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        showAlert(error.getMessage());
                        //Toast.makeText(ActivateActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                        showAlert("Please enter a valid OTP");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                //ToDo CHANGE CONNECTION VALIDATE OTP
                Map<String, String> headers = new HashMap<String, String>();
                String accessToken = SharedPreferencesUtility.getToken(sharedPreferences);
                headers.put("Authorization", "Bearer "+accessToken);
                //headers.put("Authorization", Statics.API_KEY_TEST);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void updateInfo(final  String pin, String mobileno){
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("pin", pin);
            requestBody.put("mobileno", mobileno);
            String agentid = SharedPreferencesUtility.getAgentSeqno(sharedPreferences);
            requestBody.put("agentid", agentid);
            // Get device model
            String deviceModel = Build.MODEL;
            // Get Android version
            String androidVersion = "Android "+ Build.VERSION.RELEASE;
            // Get device brand
            String deviceBrand = Build.BRAND;
            // Get device ID
            requestBody.put("deviceid", deviceId);
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            long totalRam = memoryInfo.totalMem;
            String ram =  (totalRam / (1024 * 1024)) + " MB";


            requestBody.put("version", androidVersion);
            requestBody.put("brand", deviceBrand);
            requestBody.put("model", deviceModel);
            requestBody.put("ram", ram);


        } catch ( Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Statics.UPDATE_URL, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String accessToken = SharedPreferencesUtility.getToken(sharedPreferences);
                headers.put("Authorization", "Bearer "+accessToken);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void showAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Please check error");
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle positive button click
                // ...
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void showAlert1(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Please check error");
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle positive button click
                // ...
                startButtonCooldown();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
//
//    public void validateOTPs(Context context, final  String otp){
//
//        progressBar.setVisibility(View.VISIBLE);
//        RequestQueue queue = Volley.newRequestQueue(context);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, VALIDATE_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            try {
//                                JSONObject json = new JSONObject(response);
//                                Boolean success = json.getBoolean("success");
//                                if (success) {
//                                    SharedPreferencesUtility.saveOtp(sharedPreferences, otp);
//
//                                    Intent intent = new Intent(ActivateActivity.this, AccountActivity.class);
//
//                                    intent.putExtra("otp", otp);
//
//                                    startActivity(intent);
//                                }
//
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();
//                    }
//                })  {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//
//                String accessToken = SharedPreferencesUtility.getToken(sharedPreferences);
//                headers.put("Authorization", "Bearer "+accessToken);
//                return headers;
//            }
//        };
//
//        queue.add(stringRequest);
//    }




    private void startTimer(long remainingTime) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        btnActivate.setEnabled(false);
        countDownTimer = new CountDownTimer(remainingTime, 1000) {
            public void onTick(long millisUntilFinished) {
                timerViewModel.setRemainingTime(millisUntilFinished); // Update remaining time in ViewModel
            }

            public void onFinish() {
                btnActivate.setEnabled(true);
                btnActivate.setText("Activate");
                timerViewModel.setRemainingTime(0); // Reset time in ViewModel
            }
        }.start();
    }



//    @Override
//    protected void onDestroy() {
//        Log.d("__GETTOME" , countDownTimer.toString());
//        if (countDownTimer != null) {
//            countDownTimer.cancel(); // Cancel the timer to avoid memory leaks
//        }
//
//        super.onDestroy();
//    }

    public void activateAccount(final String mobileNo){
        Log.d("mobileNo", mobileNo);
        SharedPreferencesUtility.saveMobileNo(sharedPreferences, mobileNo);
        Context context = getApplicationContext();
//        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(context);
        Log.d("Statics.CRED_URL", Statics.CRED_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Statics.CRED_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);



                            accessToken = json.getString("access_token");


                            Log.d("accessToken", accessToken);

                            SharedPreferencesUtility.saveToken(sharedPreferences, accessToken);


                            activateMobileno(mobileNo);

//                            syncMatrix();

                        } catch (Exception e) {
                           throw new RuntimeException(e);
                        }

//                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();

                        showAlert("Siguraduhing may internet connection wifi/mobile data bago mag activate sa app.");

                        btnActivate.setVisibility(View.VISIBLE);
                    }

                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // Calculate the progress based on the response data
                long contentLength = response.data.length;
                long transferredBytes = 0;

                // Update your progress bar with the calculated progress value
                int progress = (int) ((transferredBytes / (float) contentLength) * 100);
//                progressBar.setProgress(progress);

                // Return the response to be delivered
                return super.parseNetworkResponse(response);
            }

            @Override
            protected void deliverResponse(String response) {
                // Handle the response
                super.deliverResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Statics.headerValue);
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    public void activateMobileno(String mobileNo){
        Context context = getApplicationContext();

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Statics.OTP_URL+mobileNo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            // Check if OTP limit is exceeded

                            Gson gson = new Gson();
                            AgentInfo agent = gson.fromJson(response, AgentInfo.class);
                            String message = "Magandang araw "+agent.getFullname()+
                                    ", ilagay ang OTP na pinadala sa iyong numero";

                            messageView.setText(message);
                            messageView1.setText("");


                            SharedPreferencesUtility.saveAgentName(sharedPreferences, agent.getFullname());
                            SharedPreferencesUtility.saveReference(sharedPreferences, agent.getReference());
                            SharedPreferencesUtility.saveAgentId(sharedPreferences, agent.getKyid());
                            SharedPreferencesUtility.saveAgentSeqno(sharedPreferences, agent.getId());
                            SharedPreferencesUtility.saveCenters(sharedPreferences, agent.getCenters());

                            activateDiv.setVisibility(View.GONE);
                            validateDiv.setVisibility(View.VISIBLE);

                            otp.requestFocus();


                            InputMethodManager imm = getSystemService(InputMethodManager.class);
                            imm.showSoftInput(otp, InputMethodManager.SHOW_IMPLICIT);

                        } catch (Exception e) {
                            showAlert("Ang Mobile Number na iyong binigay ay wala sa aming records. Siguraduhin na ikaw ay isang authorized MIA user. Makipag-ugnayan sa iyong PRO kung hindi makapag activate.");

                            activateDiv.setVisibility(View.VISIBLE);
                            btnActivate.setVisibility(View.VISIBLE);
//                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String errorData = new String(error.networkResponse.data);  // Capture the raw error response data

                            Log.d("ERROR-------", "Status Code: " + statusCode);
                            Log.d("ERROR-------", "Error Data: " + errorData);

                            if (statusCode == 429) {
                                showAlert1("OTP request limit exceeded. Please try again in 2 minutes.");

                            } else {
                                showAlert("An error occurred. Please try again later.");
                                btnActivate.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.d("ERROR-------", "onErrorResponse: Network response is null");
                            showAlert("Siguraduhing may internet connection wifi/mobile data bago mag activate sa app.");
                            btnActivate.setVisibility(View.VISIBLE);
                        }

                        activateDiv.setVisibility(View.VISIBLE);
                    }


                })  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                //ToDo CHANGE TO OTP TO PROD UAT OR DEV TESTING
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+accessToken);
               // headers.put("Authorization", Statics.API_KEY_TEST);
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    private void startButtonCooldown() {
        btnActivate.setEnabled(false);  // Disable the button
        btnActivate.setVisibility(View.VISIBLE);  // Make sure it's visible

        new CountDownTimer(120000, 1000) {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                btnActivate.setText("Try Again in " + millisUntilFinished / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                btnActivate.setEnabled(true);
            }
        }.start();
    }


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

                            //Toast.makeText(context,  "Matrix updated : " + lovs.length,Toast.LENGTH_LONG).show();
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



    public void syncPolicy(final String agentid){

        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Statics.CRED_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            accessToken = json.getString("access_token");

                            SharedPreferencesUtility.saveToken(sharedPreferences, accessToken);
                            syncPolicyWithParam(agentid);
                        } catch (Exception e) {
                            //throw new RuntimeException(e);
                            showAlert("Siguraduhing may internet connection wifi/mobile data bago mag download ng mga policies.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showAlert("Siguraduhing may internet connection wifi/mobile data bago mag download ng mga policies.");
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

    public void syncPolicyWithParam(String agentid){
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Statics.SYNC_URL+"policies/"+agentid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            String message = "Processing policy records";

                            Log.d("Sales", "Statics.SYNC_URL :" + Statics.SYNC_URL+"policies/"+agentid);

                            PolicyInfo[] policies = new Gson().fromJson(response, PolicyInfo[].class);

                            //Toast.makeText(context,  "Total policies : " + policies.length,Toast.LENGTH_LONG).show();
                            if (policies != null && policies.length > 0){

                                databaseHelper.deletePolicies();
                                databaseHelper.addPolicyList("Y", List.of(policies));
//                                for (PolicyInfo policy : policies){
////                                    if (policy.getPoc().equalsIgnoreCase("3476013"))
////                                        Log.d("items", "policy :" + policy.getPremium());
////                                    messageView.setText("Processing :" + policy.getPoc() + " : " + policy.getProduct());
//                                    databaseHelper.addPolicy("Y", policy);
//                                }

                            }

                            syncMembers(agentid);
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

    public void syncMembers(String agentid){
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Statics.SYNC_URL+"members/"+agentid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Gson gson = new Gson();
                            String message = "Processing member records";


                            Log.d("members", "Statics.SYNC_URL :" + Statics.SYNC_URL+"members/"+agentid);

                            MemberInfo[] members = new Gson().fromJson(response, MemberInfo[].class);
                            databaseHelper.addMemberList(List.of(members), "Y");
//                            //Toast.makeText(context,  "Total members : " + members.length,Toast.LENGTH_LONG).show();
//                            if (members != null && members.length > 0){
//                                databaseHelper.deleteMembers();
//                                for (MemberInfo member : members){
//                                    Log.d("items", "member :" + member.getReltype());
//                                    databaseHelper.addMember(member, "Y");
//                                }
//                            }
                        } catch (Exception e) {
                            //Toast.makeText(context,  e.getMessage() + "\nPlease contact admin",Toast.LENGTH_LONG).show();
                        }

                        showAlert("Done downloading old records.");
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


}