package com.pioneer.microhmo;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.pioneer.microhmo.util.SharedPreferencesUtility;
import com.pioneer.microhmo.util.TermsAndCondition;
import com.pioneer.microhmo.util.TimerViewModel;

public class MainActivity extends AppCompatActivity {

    private Button btnActivate, btnLogin,closebtn;

    FrameLayout closebtnlayout;
    LinearLayout mainView;

    CheckBox terms , checkDpa;

    public static String SHARED_PREFERENCE_ID = "MyAppPreferences";
    WebView mywebView;

    String deviceId = "";
    private TimerViewModel timerViewModel;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                Toast.makeText(MainActivity.this, "Permission granted.",Toast.LENGTH_LONG).show();
            }
        }
    });

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        //timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);

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
                    //onBrowseClick();
                    showTermsDialog();

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

    private void showTermsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.terms_dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Find the TextView
        TextView termsContent = dialogView.findViewById(R.id.terms_content);

        // Full text with clickable sections
        String fullText = "By clicking continue, you agree to our Terms and Condition and the Data Privacy Act.";
        String termsPart = "Terms and Condition";
        String privacyPart = "Data Privacy Act";

        // Create a SpannableString from the full text
        SpannableString spannableString = new SpannableString(fullText);

        // Set up the Terms and Condition clickable part
        int termsStartIndex = fullText.indexOf(termsPart);
        int termsEndIndex = termsStartIndex + termsPart.length();

        ClickableSpan termsClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showTermsDialogs(); // Show detailed Terms and Conditions dialog
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };
        spannableString.setSpan(termsClickableSpan, termsStartIndex, termsEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set up the Data Privacy Act clickable part
        int privacyStartIndex = fullText.indexOf(privacyPart);
        int privacyEndIndex = privacyStartIndex + privacyPart.length();

        ClickableSpan privacyClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                onBrowseClick();
                dialog.dismiss();// Open Data Privacy Act in WebView
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };
        spannableString.setSpan(privacyClickableSpan, privacyStartIndex, privacyEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the SpannableString to the TextView
        termsContent.setText(spannableString);
        termsContent.setMovementMethod(LinkMovementMethod.getInstance()); // Make links clickable
        termsContent.setHighlightColor(Color.TRANSPARENT); // Remove link highlight

        // Continue button to close the dialog
        Button btnContinue = dialogView.findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void showTermsDialogs() {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_terms_and_condition, null);

        // Set the custom view in the dialog
        builder.setView(dialogView);

        // Optionally, you can add "Close" button or other buttons to the dialog
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Show the dialog first to manipulate its size
        dialog.show();

        // Set custom width and height for the dialog
        dialog.getWindow().setLayout(900, 1200); // Width and Height in pixels

        // Alternatively, you can use percentage-based width & height:
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90); // 90% width
        layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.80); // 80% height
        dialog.getWindow().setAttributes(layoutParams);
    }
}