package com.pioneer.nanaiv3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pioneer.nanaiv3.adapter.SalesAdapter;
import com.pioneer.nanaiv3.adapter.RecyclerViewInterface;
import com.pioneer.nanaiv3.objects.MemberInfo;
import com.pioneer.nanaiv3.objects.PolicyInfo;
import com.pioneer.nanaiv3.util.SharedPreferencesUtility;
import com.pioneer.nanaiv3.util.Statics;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesActivity extends AppCompatActivity implements RecyclerViewInterface {



    String accessToken = null;

    SharedPreferences sharedPreferences = null;
    DatabaseHelper databaseHelper;
    SearchView searchView;
    RecyclerView recyclerView;
    SalesAdapter adapter;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        ImageView leftIcon = findViewById(R.id.left_icon);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);


        progressBar = findViewById(R.id.progressBar_tran);

        recyclerView = findViewById(R.id.recyclerview);
        searchView = findViewById(R.id.searchView);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });
        List<MemberInfo> items = null;
        try {
            items = databaseHelper.getMembers("Y");
        }catch (Exception e){
            items = new ArrayList<>();
            Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        for (MemberInfo member:items){
//            Log.d("items", "policy :" + member.getPremium());
            member.setCurrentstat(null);
        }
        adapter = new SalesAdapter(getApplicationContext(),items, SalesActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Call a method in your adapter to filter the data
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        Button btnSync = findViewById(R.id.btnSync);
        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String temp = SharedPreferencesUtility.getTempReference(sharedPreferences);
                String agentid = SharedPreferencesUtility.getAgentId(sharedPreferences);

                if (temp != null && temp.length() > 10){
                    agentid = temp;
                }
                syncPolicy(agentid);
            }

        });

    }

    void reload(){
        List<MemberInfo> items = null;
        try {
            items = databaseHelper.getMembers("Y");
            for (MemberInfo member:items){

                Log.d("items", "policy :" + member.getPremium());
                member.setCurrentstat(null);
            }
        }catch (Exception e){
            items = new ArrayList<>();
        }
        adapter = new SalesAdapter(getApplicationContext(),items, SalesActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onItemClick(MemberInfo member) {
        Intent intent = new Intent(SalesActivity.this, ViewActivity.class);
        intent.putExtra("id", member.getId().toString());
        startActivity(intent);
    }


    public void syncPolicy(final String agentid){

        progressBar.setVisibility(View.VISIBLE);
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
                                Log.d("policies", "Inserting policy records");

                                databaseHelper.addPolicyList("Y", List.of(policies));
//                                for (PolicyInfo policy : policies){
////                                    if (policy.getPoc().equalsIgnoreCase("3476013"))
////                                        Log.d("items", "policy :" + policy.getPremium());
////                                    messageView.setText("Processing :" + policy.getPoc() + " : " + policy.getProduct());
//                                    databaseHelper.addPolicy("Y", policy);
//                                }

                                Log.d("policies", "DOne inserting policy records");
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

//                        Log.d("items", "response :" + response);
                        try {
                            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            Gson gson = new Gson();
                            String message = "Processing member records";


                            Log.d("members", "Statics.SYNC_URL :" + Statics.SYNC_URL+"members/"+agentid);

                            MemberInfo[] members = new Gson().fromJson(response, MemberInfo[].class);

                            //Toast.makeText(context,  "Total members : " + members.length,Toast.LENGTH_LONG).show();
                            if (members != null && members.length > 0){
                                databaseHelper.deleteMembers();
//                                for (MemberInfo member : members){
////                                    Log.d("items", "member :" + member.getLname());
//                                    databaseHelper.addMember(member, "Y");
//                                }
                                databaseHelper.addMemberList(List.of(members), "Y");
                            }
                        } catch (Exception e) {
                            //Toast.makeText(context,  e.getMessage() + "\nPlease contact admin",Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                        showAlert("Done downloading old records.");
                        reload();
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
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
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
                progressBar.setVisibility(View.GONE);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}