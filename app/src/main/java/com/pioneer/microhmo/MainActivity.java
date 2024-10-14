package com.pioneer.microhmo;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pioneer.microhmo.util.SharedPreferencesUtility;

public class MainActivity extends AppCompatActivity {

    private Button btnActivate, btnLogin,closebtn;

    FrameLayout closebtnlayout;
    LinearLayout mainView;

    CheckBox terms;

    public static String SHARED_PREFERENCE_ID = "MyAppPreferences";
    WebView mywebView;

    String deviceId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_main);
        closebtnlayout = findViewById(R.id.closebtnlayout);
        mainView = findViewById(R.id.main_layout);
        closebtn= findViewById(R.id.closebtn);
        btnActivate = findViewById(R.id.btnActivate);
        btnLogin = findViewById(R.id.btnLogin);
        terms = findViewById(R.id.terms);

        mywebView= findViewById(R.id.webview);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);

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

        Boolean isUserExist = false;

        try{
            isUserExist = SharedPreferencesUtility.isUserExist(sharedPreferences);
        }catch (Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
        }

        if (isUserExist){
            btnActivate.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }else{
            btnLogin.setVisibility(View.GONE);
            btnActivate.setVisibility(View.VISIBLE);
        }

        btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivateActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                terms.setChecked(true);
                if (isChecked) {
                    btnActivate.setTextColor(WHITE);
                    btnActivate.setBackgroundResource(R.drawable.blue_button);
                    btnActivate.setEnabled(true);
                    onBrowseClick();
                }else{
                    btnActivate.setTextColor(BLACK);
                    btnActivate.setBackgroundResource(R.drawable.gray_button);
                    btnActivate.setEnabled(false);
                }
            }
        });

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closebtnlayout.setVisibility(View.GONE);
                mywebView.setVisibility(View.GONE);
                mainView.setVisibility(View.VISIBLE);
            }
        });
    }


    public void onBrowseClick() {
        String url = "https://pioneer.com.ph/pioneer-data-privacy/";
//        Uri uri = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
//
        mywebView.setWebViewClient(new WebViewClient());
        mywebView.loadUrl(url);
        WebSettings webSettings=mywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mainView.setVisibility(View.GONE);
        closebtnlayout.setVisibility(View.VISIBLE);
        mywebView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        // Disable the back button functionality
        // Remove the super.onBackPressed() line to disable the back button completely
    }
}