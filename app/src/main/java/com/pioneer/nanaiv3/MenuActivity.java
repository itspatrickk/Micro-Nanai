package com.pioneer.nanaiv3;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pioneer.nanaiv3.objects.AgentInfo;
import com.pioneer.nanaiv3.objects.Matrix;
import com.pioneer.nanaiv3.util.SharedPreferencesUtility;
import com.pioneer.nanaiv3.util.Statics;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    ImageButton enrollButton, supportButton, salesButton, syncButton;


    SharedPreferences sharedPreferences;

    String accessToken;

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                Toast.makeText(MenuActivity.this, "Permission granted.",Toast.LENGTH_LONG).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        }


        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);

        enrollButton = findViewById(R.id.enrollButton);
        supportButton = findViewById(R.id.supportButton);
        salesButton = findViewById(R.id.salesButton);
        syncButton = findViewById(R.id.syncButton);

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

//        supportButton.setVisibility(View.INVISIBLE);
        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MenuActivity.this, SupportActivity.class);
//                startActivity(intent);
                updateLimits();
                updateCenter();
            }
        });
        salesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SalesActivity.class);
                startActivity(intent);
            }
        });
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TransactionsActivity.class);
                startActivity(intent);
            }
        });

        Button btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }

    public void updateLimits(){
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Statics.CRED_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("accessToken", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            accessToken = json.getString("access_token");
                            Log.d("accessToken", accessToken);
                            SharedPreferencesUtility.saveToken(sharedPreferences, accessToken);
                            //getAgentUpdate();
                            syncMatrix();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();
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


    public void updateCenter(){
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Statics.CRED_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("accessToken", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            accessToken = json.getString("access_token");
                            Log.d("accessToken", accessToken);
                            SharedPreferencesUtility.saveToken(sharedPreferences, accessToken);
                            getAgentUpdate();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();
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




    public void syncMatrix(){

        Log.d("Statics.LOV_URL", Statics.LOV_URL);
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

                            Toast.makeText(context,  "Total members : " + lovs.length,Toast.LENGTH_LONG).show();
                            if (lovs != null && lovs.length > 0){
                                for (Matrix lov : lovs){
                                    SharedPreferencesUtility.saveString(sharedPreferences, lov.getCode(), lov.getDesc() );
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(context,  e.getMessage() + "\nPlease contact admin",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();
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



    public void getAgentUpdate(){
        Context context = getApplicationContext();

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Statics.AGENTINFO_URL+
                SharedPreferencesUtility.getMobileNo(sharedPreferences),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            AgentInfo agent = gson.fromJson(response, AgentInfo.class);
                            Log.d("agent.getCenters()", agent.getCenters());
                            SharedPreferencesUtility.saveCenters(sharedPreferences, agent.getCenters());
                        } catch (Exception e) {
                            e.printStackTrace();
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
    @Override
    public void onBackPressed() {
        // Disable the back button functionality
        // Remove the super.onBackPressed() line to disable the back button completely
    }

}