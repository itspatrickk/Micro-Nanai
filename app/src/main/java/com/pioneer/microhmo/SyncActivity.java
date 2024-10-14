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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pioneer.microhmo.objects.MemberInfo;
import com.pioneer.microhmo.objects.PolicyInfo;
import com.pioneer.microhmo.util.SharedPreferencesUtility;
import com.pioneer.microhmo.util.Statics;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SyncActivity extends AppCompatActivity {



    Button btnSendTransaction, btnSync;
    ProgressBar progressBar;
    TextView messageView ,totalMembers;

    String accessToken = null;

    SharedPreferences sharedPreferences = null;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        ImageView leftIcon = findViewById(R.id.left_icon);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SyncActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);


        btnSendTransaction = findViewById(R.id.btnSendTransaction);

        messageView = findViewById(R.id.sync_status);
        progressBar = findViewById(R.id.syncProgressBar);
        totalMembers = findViewById(R.id.total_members);


        btnSendTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SyncActivity.this, TransactionsActivity.class);
                startActivity(intent);
            }
        });

        btnSync = findViewById(R.id.btnSync);
        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String temp = SharedPreferencesUtility.getTempReference(sharedPreferences);
                String agentid = SharedPreferencesUtility.getReference(sharedPreferences);

                if (temp != null && temp.length() > 10){
                    agentid = temp;
                }
                syncPolicy(agentid);
            }
        });

        totalMembers.setText("Total policy : " + databaseHelper.getTotalPolicies()+ "\nTotal members : " + databaseHelper.getTotalMembers());
    }

    public void syncPolicy(final String agentid){
        Context context = getApplicationContext();
        progressBar.setVisibility(View.VISIBLE);
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
                            throw new RuntimeException(e);
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
                            messageView.setText(message);


                            PolicyInfo[] policies = new Gson().fromJson(response, PolicyInfo[].class);

                            //Toast.makeText(context,  "Total policies : " + policies.length,Toast.LENGTH_LONG).show();
                            if (policies != null && policies.length > 0){
                                databaseHelper.deletePolicies();
                                for (PolicyInfo policy : policies){
                                    Log.d("items", "policy :" + policy.getPoc());
//                                    messageView.setText("Processing :" + policy.getPoc() + " : " + policy.getProduct());
                                    databaseHelper.addPolicy("Y", policy);
                                }

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
                            messageView.setText(message);

                            MemberInfo[] members = new Gson().fromJson(response, MemberInfo[].class);
                            totalMembers.setText("Total members : " + members.length);

                            //Toast.makeText(context,  "Total members : " + members.length,Toast.LENGTH_LONG).show();
                            if (members != null && members.length > 0){
                                databaseHelper.deleteMembers();
                                for (MemberInfo member : members){
                                    Log.d("items", "member :" + member.getPoc());
                                    databaseHelper.addMember(member, "Y");
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            messageView.setText("Processing done");

                            totalMembers.setText("Total policy : " + databaseHelper.getTotalPolicies()+ "\nTotal members : " + databaseHelper.getTotalMembers());


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
}