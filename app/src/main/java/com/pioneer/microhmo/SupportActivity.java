package com.pioneer.microhmo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pioneer.microhmo.objects.Matrix;
import com.pioneer.microhmo.util.SharedPreferencesUtility;
import com.pioneer.microhmo.util.Statics;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SupportActivity extends AppCompatActivity {

    EditText agentRefId;
    Button agentRefIdBtn;

    SharedPreferences sharedPreferences;

    String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);

        ImageView leftIcon = findViewById(R.id.left_icon);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupportActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        agentRefId = findViewById(R.id.agentRefId);
        agentRefIdBtn = findViewById(R.id.agentRefIdBtn);

        agentRefIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ref = agentRefId.getText().toString();
                if (ref.length() < 20){
                    ref = "add9d7c7-f48b-41f6-8f13-8134388f65a2";
                }
                SharedPreferencesUtility.saveTempReference(sharedPreferences, ref);

                Toast.makeText(SupportActivity.this, "UPDATED",Toast.LENGTH_LONG).show();
            }
        });

        Button updateMatrix  = findViewById(R.id.updateMatrix);
        updateMatrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferencesUtility.setInitialValues(sharedPreferences);
                updateLimits();
                Toast.makeText(SupportActivity.this, "UPDATED",Toast.LENGTH_LONG).show();
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
                        try {
                            JSONObject json = new JSONObject(response);
                            accessToken = json.getString("access_token");
                            Log.d("accessToken", accessToken);
                            SharedPreferencesUtility.saveToken(sharedPreferences, accessToken);
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

                            if (lovs != null && lovs.length > 0){
                                for (Matrix lov : lovs){
                                    SharedPreferencesUtility.saveString(sharedPreferences, lov.getCode(), lov.getDesc() );
                                }
                            }

                            showAlert("Application updated.");

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


    public void showAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                progressBar.setVisibility(View.GONE);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}