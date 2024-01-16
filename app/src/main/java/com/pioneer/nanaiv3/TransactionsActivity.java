package com.pioneer.nanaiv3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pioneer.nanaiv3.adapter.RecyclerViewInterface;
import com.pioneer.nanaiv3.adapter.SalesAdapter;
import com.pioneer.nanaiv3.client.API;
import com.pioneer.nanaiv3.objects.MemberInfo;
import com.pioneer.nanaiv3.objects.PolicyInfo;
import com.pioneer.nanaiv3.objects.Transaction;
import com.pioneer.nanaiv3.util.SharedPreferencesUtility;
import com.pioneer.nanaiv3.util.Statics;
import com.pioneer.nanaiv3.util.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransactionsActivity extends AppCompatActivity implements RecyclerViewInterface {


    DatabaseHelper databaseHelper;
    SearchView searchView;
//    MemberAdapterPending adapter;
    SalesAdapter adapter;

    List<PolicyInfo> imageList;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String accessToken;

    private Button btn;
    private ImageView imageView;
    private final int GALLERY = 1;
    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;

    Boolean sending = false;

    String errorMsg = "Siguraduhing may internet connection wifi/mobile data bago mag upload ng mga policies.";
    Button btnSend ;
//    Button sendImagesBtn ;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        sending = false;
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);
        imageList = databaseHelper.getPendingPolicyImages();

//        List<PolicyInfo> pendingList = new ArrayList<PolicyInfo>();//Uploading
//        if (imageList != null && imageList.size() > 0) {
//            PolicyInfo p;
//            for (Iterator<PolicyInfo> iter = imageList.iterator(); iter.hasNext(); ) {
//                p = iter.next();
//                if ("Uploading".equalsIgnoreCase(p.getCurrentstatus())) {
//                    pendingList.add(p);
//                    iter.remove();
//                }
//            }
//        }
        progressBar = findViewById(R.id.progressBar_tran);

        btnSend = findViewById(R.id.send_Tran);
//        sendImagesBtn = findViewById(R.id.send_Images);

        ImageView leftIcon = findViewById(R.id.left_icon);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionsActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.recyclerview_Tran);
        searchView = findViewById(R.id.searchView_Tran);
        databaseHelper = new DatabaseHelper(this);


        loadPending();
    }

    private void loadPending(){
        List<MemberInfo> items = null;
        try {
            items = databaseHelper.getMembers("N");
            Log.d("items", "getMembers count :" + items.size());
            for (MemberInfo member : items){
                Log.d("items", "getProduct1 :" + member.getPoc());
                Log.d("items", "getProduct1 :" + member.getProduct1());
                Log.d("items", "getProduct2 :" + member.getProduct2());
                Log.d("items", "getProduct3 :" + member.getProduct3());
            }
        }catch (Exception e){
            items = new ArrayList<>();
//            Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        String filepath = "MyFileStorage";

//        adapter = new MemberAdapterPending(getApplicationContext(),items, TransactionsActivity.this);
        adapter = new SalesAdapter(getApplicationContext(),items, TransactionsActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (items == null || items.isEmpty()){
            File mediaStorageDir = new File(getExternalFilesDir(filepath), "Nanai-compressed");

            if (mediaStorageDir.isDirectory())
            {
                String[] children = mediaStorageDir.list();
                for (int i = 0; i < children.length; i++)
                {
                    new File(mediaStorageDir, children[i]).delete();
                }
            }
        }

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

        if (imageList.size() == 0){
            btnSend.setVisibility(View.GONE);
//            sendImagesBtn.setVisibility(View.GONE);
        }else {
            btnSend.setVisibility(View.VISIBLE);
//            sendImagesBtn.setVisibility(View.VISIBLE);
            btnSend.setText("SEND " + imageList.size() + " RECORDS ");
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnSend.setVisibility(View.GONE);

                    sendPolicy();

                    sendImages();
                }
            });

            //sendImagesBtn.setText("SEND " + imageList.size() + " RECORDS IMAGES");
//            sendImagesBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    resendPendingImages();
//                }
//            });
        }
    }

    public void sendPolicy(){
        setToken();
        if (accessToken == null){
            setToken();
        }
        if (accessToken == null){
            setToken();
        }
        if (accessToken == null){
            setToken();
        }
        if (accessToken == null){
            setToken();
        }

        List<Transaction> transList = generatePayload();
        if (!sending) {
            try {
                sendJsonArray(Statics.SEND_URL, transList);
            }catch (Exception e){

            }
        }

//        Context context = getApplicationContext();
//        progressBar.setVisibility(View.VISIBLE);
//        RequestQueue queue = Volley.newRequestQueue(context);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Statics.CRED_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject json = new JSONObject(response);
//                            accessToken = json.getString("access_token");
//
//                            Log.d("accessToken", accessToken);
//                            SharedPreferencesUtility.saveToken(sharedPreferences, accessToken);
//                            List<Transaction> transList = generatePayload();
//                            if (!sending)
//                                sendJsonArray(Statics.SEND_URL, transList);
//
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
////                        Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();
//                        showAlert(errorMsg);
//                        btnSend.setVisibility(View.VISIBLE);
//                    }
//                })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", Statics.headerValue);
//                return headers;
//            }
//        };
//
//        queue.add(stringRequest);
    }

    public void resendPendingImages(){
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

                            Log.d("accessToken", accessToken);
                            SharedPreferencesUtility.saveToken(sharedPreferences, accessToken);
                            sendImages();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();
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

    public void sendImages(){
        imageList = databaseHelper.getPendingPolicyImages();
        setToken();
        if (accessToken == null) setToken();
        if (accessToken == null) setToken();
        if (accessToken == null) setToken();
        if (accessToken == null) setToken();
        for (PolicyInfo pol:imageList){

            Log.d("items", "getImage1stat :" + pol.getImage1stat() );
            Log.d("items", "getImage1 :" + pol.getImage1() );

            Log.d("items", "getImage2stat :" + pol.getImage2stat() );
            Log.d("items", "getImage2 :" + pol.getImage2() );


            Log.d("items", "url :" + Statics.uploadURL+"uploadFront/"+pol.getId()+"/"+pol.getPoc());

            databaseHelper.updateCurrStat(pol.getId(), "Uploading");

            if (pol.getImage1stat().equalsIgnoreCase("N")){
                try {
                    File file = new File(pol.getImage1());
                    if (!file.exists())
                        databaseHelper.updateImage1Stat(pol.getId());
                    if (file.exists())
                        uploadImageFile(pol.getImage1(),
                             "uploadFront", pol.getId(), pol.getPoc() );
//                    Bitmap photo = BitmapFactory.decodeFile(pol.getImage1());
//                    uploadImage(photo, uploadURL + "uploadFront/" + pol.getId() + "/" + pol.getPoc());
                }catch (Exception e){
                    e.printStackTrace();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            if (pol.getImage2stat().equalsIgnoreCase("N")){
                try {
                    File file = new File(pol.getImage2());
                    if (!file.exists())
                        databaseHelper.updateImage2Stat(pol.getId());
                    if (file.exists())
                        uploadImageFile(pol.getImage2(),
                            "uploadBack", pol.getId() , pol.getPoc());
//                    Bitmap photo = BitmapFactory.decodeFile(pol.getImage2());
//                    uploadImage(photo, uploadURL+"uploadBack/"+pol.getId()+"/"+pol.getPoc());
                }catch (Exception e){
                    progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }

            }
        }

        loadPending();

        showAlertAction("Done sending transactions.");

    }

    List<Transaction> generatePayload(){

        List<PolicyInfo> items = null;
        try {
            items = databaseHelper.getPendingPolicies();

            if (items != null && items.size() > 0) {
                PolicyInfo p;
                for (Iterator<PolicyInfo> iter = items.iterator(); iter.hasNext(); ) {
                    p = iter.next();
                    if ("Uploading".equalsIgnoreCase(p.getCurrentstatus())) {
                        iter.remove();
                    }
                }
            }
        }catch (Exception e){
            items = new ArrayList<>();
//            Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        List<Transaction> transactionList = new ArrayList<Transaction>();
        if (items != null && items.size() > 0) {

            Transaction tran;
            for (PolicyInfo policy:items){
                tran = new Transaction();
                tran.setAgentid(policy.getAgentid());
                tran.setPolicy(policy);
                List<MemberInfo> members = databaseHelper.getMembersId(policy.getId());
                tran.setMembers(members);
                transactionList.add(tran);
            }

        }
        return transactionList;
    }


    public void sendJsonArray( String url, List<Transaction> list) throws JSONException {
        sending = true;
        Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        Gson gson = new Gson();

        String json = new Gson().toJson(list);
        JSONArray array = new JSONArray(json);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, array,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle response
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject json = response.getJSONObject(i);
                                String oldid = json.getString("id");
                                String retval = json.getString(oldid);

                                Log.d("items", "oldid :" + oldid);
                                Log.d("items", "retval :" + retval);

                                databaseHelper.updateId(oldid, retval);

                            } catch ( Exception e) {
                                throw new RuntimeException(e);
                            }

                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        sendImages();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
//                        showAlert(errorMsg);
                        for (Transaction pol:list) {
                            databaseHelper.updateCurrStat(pol.getPolicy().getId(), "Queue");
                        }
                        showAlert("Transaction(s) added to queue, please check connection then resend.");
                    }
                })  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+accessToken);
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    private void uploadImage(final Bitmap bitmap, String uploadURL){

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadURL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
//                        Toast.makeText(getApplicationContext(), "Sending done.", Toast.LENGTH_SHORT).show();

                        Log.d("ressssssoo",new String(response.data));
                        rQueue.getCache().clear();
//                        try {
//                            JSONObject jsonObject = new JSONObject(new String(response.data));
//                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//
//                            jsonObject.toString().replace("\\\\","");
//
//                            if (jsonObject.getString("status").equals("true")) {
//
//                                arraylist = new ArrayList<HashMap<String, String>>();
//                                JSONArray dataArray = jsonObject.getJSONArray("data");
//
//                                String url = "";
//                                for (int i = 0; i < dataArray.length(); i++) {
//                                    JSONObject dataobj = dataArray.getJSONObject(i);
//                                    url = dataobj.optString("pathToFile");
//                                }
//                                //Picasso.get().load(url).into(imageView);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {

//            /*
//             * If you want to add more parameters with the image
//             * you can do it here
//             * here we have only one parameter with the image
//             * which is tags
//             * */
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                // params.put("tags", "ccccc");  add string parameters
//                return params;
//            }
//
//            /*
//             *pass files using below method
//             * */
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                long imagename = System.currentTimeMillis();
//                String filename = imagename + ".png";
//                params.put("filename", new DataPart(filename, getFileDataFromDrawable(bitmap)));
//                return params;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+accessToken);
                return headers;
            }
        };


        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(TransactionsActivity.this);
        rQueue.add(volleyMultipartRequest);
    }


    public void uploadImageFile(String filePath, String path, String id, final String coc) {

//        if (coc == null || coc.length() == 0){
//            coc = "no-poc";
//        }
        progressBar.setVisibility(View.VISIBLE);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
//                httpClientBuilder.addInterceptor(loggingInterceptor);
                httpClientBuilder.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request originalRequest = chain.request();

                        // Add your authorization header here
                        String authorizationHeaderValue = "Bearer " + accessToken;

                        // Create a new request builder with the authorization header
                        okhttp3.Request.Builder requestBuilder = originalRequest.newBuilder()
                                .header("Authorization", authorizationHeaderValue);

                        okhttp3.Request newRequest = requestBuilder.build();
                        return chain.proceed(newRequest);
                    }
                });

                OkHttpClient client = httpClientBuilder
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .build();


// Create Retrofit instance
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Statics.uploadURL) // Replace with your base URL
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

// Create ApiService instance
                API apiService = retrofit.create(API.class);

                File file = new File(filePath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
//                RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

                retrofit2.Call<ResponseBody> call = apiService.uploadImage(imagePart, path, id, coc);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        progressBar.setVisibility(View.INVISIBLE);


                        Log.d("retrofit2", "response.isSuccessful() :" +response.isSuccessful());

                        if (response.isSuccessful()) {

                            // Image uploaded successfully, use the imageUrl as needed
                            if (path.equalsIgnoreCase("uploadFront")){
                                databaseHelper.updateImage1Stat(id);
                            }else{
                                databaseHelper.updateImage2Stat(id);
                            }
                            try {
                                file.delete();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
//                    Toast.makeText(getApplicationContext(), "Upload rs image successfully.", Toast.LENGTH_LONG).show();
                        } else {
                            // Handle error
//                    Toast.makeText(getApplicationContext(), "Upload rs image failed.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//                showAlert(t.getMessage());
//                        showAlert("Siguraduhing may internet connection wifi/mobile data bago mag upload ng mga policies.");
//                        progressBar.setVisibility(View.INVISIBLE);
                    }

                });
            }
        };
        new Thread(runnable).start();
        progressBar.setVisibility(View.INVISIBLE);

    }



    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    @Override
    public void onItemClick(MemberInfo member) {
//        Intent intent = new Intent(SalesActivity.this, ViewActivity.class);
//        intent.putExtra("id", member.getId().toString());
//        startActivity(intent);
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

    public void showAlertAction(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(TransactionsActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void setToken(){
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

                            Log.d("accessToken", accessToken);
                            SharedPreferencesUtility.saveToken(sharedPreferences, accessToken);

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
}