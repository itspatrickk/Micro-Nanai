package com.pioneer.microhmo;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.pioneer.microhmo.objects.Address_;
import com.pioneer.microhmo.util.JsonUtils;
import com.pioneer.microhmo.util.SharedPreferencesUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends AppCompatActivity {

      DatabaseHelper databaseHelper;
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

        databaseHelper = new DatabaseHelper(this);
        HandlerThread handlerThread = new HandlerThread("MyHandlerThread");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());


//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
//            activityResultLauncher.launch(Manifest.permission.READ_SMS);
//        }
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED) {
//            activityResultLauncher.launch(Manifest.permission.BROADCAST_SMS);
//        }



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.RECEIVE_SMS);
        }

        if (isRootedDevice()) {
            // Show alert dialog or toast
            Toast.makeText(SplashActivity.this, "This app does not run on rooted devices.", Toast.LENGTH_LONG).show();
            // Optionally, exit the app
            System.exit(0);
        }
        handler.post(() -> {
            Log.d("TAG", "onCreate: dumaan");
            List<String> addressList = null;
            try {
                addressList = databaseHelper.getAllProvinces();
            }catch (Exception e) {
                e.printStackTrace();
            }
            if (addressList == null || addressList.isEmpty()){
                String json = JsonUtils.loadJSONFromAsset(this);
                //Log.d("JSONContent", "Loaded JSON: " + json);
                if (json != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray addressArray = jsonObject.getJSONArray("address_list");
                        List<Address_> addrList = new ArrayList<Address_>();
                        for (int i = 0; i < addressArray.length(); i++) {
                            JSONObject addressObject = addressArray.getJSONObject(i);
                            Address_ address = new Address_();
                            address.city = addressObject.getString("city");
                            address.prov = addressObject.getString("prov");
                            address.brgy = addressObject.getString("brgy");
//                            address.setCity(addressObject.getString("city"));
//                            address.setProvince(addressObject.getString("prov"));
//                            address.setBarangay(addressObject.getString("brgy"));
                            addrList.add(address);
                        }
                        databaseHelper.addAddressList(addrList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            List<String> provinces = databaseHelper.getAllProvinces();
            Log.d("provinces", ""+provinces.size());
            Toast.makeText(this, "UAT VERSION 1.17", Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "TOTAL PROV SIZE " + provinces.size(), Toast.LENGTH_SHORT).show();
        });

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
//            activityResultLauncher.launch(Manifest.permission.RECEIVE_SMS);
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(SplashActivity.this, CameraActivity.class));
                Boolean isUserExist = false;
                String status = "";

                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);
                    isUserExist = SharedPreferencesUtility.isUserExist(sharedPreferences);
                    status = SharedPreferencesUtility.getStatus(sharedPreferences);

                    Log.d("AGENT STATUS" , "statss: " + status);

                }catch (Exception e){
                    isUserExist = false;
                }
               //startActivity(new Intent(SplashActivity.this, AccountActivity.class));//MainActivity.class))



                if (isUserExist){
                    Log.d(">>>>>>>>>>>AGENT STATUS" , "statss: " + status);
                    if (status.equalsIgnoreCase("INACTIVE")){
                        Log.d("HEREEE status>>>>>>>", "run:+ INCATIVe " );
                        showInactiveUserDialog();
                        return;
                    }
                    startActivity(new Intent(SplashActivity.this, AccountActivity.class));//MainActivity.class))
                }else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));//MainActivity.class));
                }
                finish();
            }
        }, 3000);





//



    }


    public boolean isRootedDevice() {
        return isDeviceRooted() || canExecuteSu() || isRunningTestKeys();
    }
    public boolean isDeviceRooted() {
        String[] paths = {
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su"
        };

        for (String path : paths) {
            if (new File(path).exists()) {
                return true;
            }
        }
        return false;
    }

    public boolean canExecuteSu() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("which su");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine() != null;
        } catch (Exception e) {
            return false;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    // Check if the device is running with test keys
    public boolean isRunningTestKeys() {
        String buildTags = Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }
    private void showInactiveUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Account Restricted")
                .setMessage("Your account is inactive or resigned. Please contact support.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                    databaseHelper.deleteAllData();
                    Log.d("ALL DATA DELETED", "deleted all data to this");
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                })
                .show();
    }
    public void showAlert3(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteAllData();
                Log.d("ALL DATA DELETED", "deleted all data to this");
                finishAffinity();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}