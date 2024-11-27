package com.pioneer.microhmo;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pioneer.microhmo.adapter.DependentAdapter;
import com.pioneer.microhmo.adapter.RecyclerViewInterface;
import com.pioneer.microhmo.objects.MemberInfo;
import com.pioneer.microhmo.objects.PolicyInfo;
import com.pioneer.microhmo.objects.Transaction;
import com.pioneer.microhmo.retrofit.RestEnpoint;
import com.pioneer.microhmo.util.CommonUtil;
import com.pioneer.microhmo.util.SharedPreferencesUtility;
import com.pioneer.microhmo.util.Statics;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends  RegisterCommon implements RecyclerViewInterface {

    LinearLayout dependentLinearLayout;
    DependentAdapter customAdapter;

    private RestEnpoint restEnpoint;

    private Retrofit retrofit;
    private AutoCompleteTextView province;
    // Spinner element
    Spinner spinner;// = (Spinner) findViewById(R.id.center);
    String username = "nanai";
    String password = "nanai@2021";
    String authToken = Base64.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusselected = "New";
        genderselected = "Babae";
        civilstatusselected ="Single";
        membershipselected = "No";
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_register);

        spinner = (Spinner) findViewById(R.id.center);
        province = findViewById(R.id.province);

        city = findViewById(R.id.city);

        age = findViewById(R.id.age);
        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(Statics.BASE_URL_HMO)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restEnpoint = retrofit.create(RestEnpoint.class);

//        loadProvinces();



        transaction = new Transaction();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            activityResultLauncher.launch(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
//        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        }


        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);
        cameraExecutor = Executors.newSingleThreadExecutor();

        ImageView leftIcon = findViewById(R.id.left_icon);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        List<String> provinces;

        setComponents();
        capturedRS1 = findViewById(R.id.capturedRS1);
       // capturedRS2 = findViewById(R.id.capturedRS2);

        //ToDo SET UP FOR PROVINCE OFFLINE MODE
        provinces = databaseHelper.getAllProvinces();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.list_item , provinces);
        province.setAdapter(adapter);

        province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                province.setError(null);
                city.setText("");
                brgy.setText("");
                String item = adapterView.getItemAtPosition(i).toString();
                setCity(item);
            }
        });

        city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                city.setError(null);
                String item = adapterView.getItemAtPosition(i).toString();
                brgy.setText("");
                setBrgy(item , province.getText().toString());
            }
        });

        messageView.setVisibility(View.GONE);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        String centersStr = SharedPreferencesUtility.getCenters(sharedPreferences);

        Log.d("agent.centersStr()", centersStr);
        if (centersStr != null){
            String [] aaa = centersStr.split("~");
            for (String s:aaa){
                if (!s.contains("null"))
                    categories.add(s);
            }
        }
        else{
            categories.add("DEFAULT CENTER");
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.list_item_center, categories);


        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.setDropDownViewResource(R.layout.list_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);



       // dependentLinearLayout = findViewById(R.id.dependentLinearLayout);

        TextInputLayout mobilePrefix = findViewById(R.id.mobilePrefix);
        mobilePrefix.setPrefixTextAppearance(R.style.PrefixTextAppearance);
//mike
//        checkCardCare.setEnabled(false);
//        checkKabuklod.setEnabled(false);
//        checkSagip.setEnabled(false);

//        addDependent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RegisterActivity.this, MemberActivity.class);
//                String data = "";
//                intent.putExtra("key", data);
//                String sagipType = "0";
//                String isSpouseExist = "N";
//                if (sagipIndividual.isChecked()){
//                    sagipType = sagipIndividualCode;
//                    intent.putExtra("reltype", "CHILD");
//                } else if (sagipFamily.isChecked()){
//                    sagipType = sagipFamilyCode;
//                    for (MemberInfo member:members){
//                        if (member.getReltype().equalsIgnoreCase("SPOUSE")){
//                            isSpouseExist = "Y";
//                            break;
//                        }
//                    }
//                } else if (sagipPlatinum.isChecked()){
//                    sagipType = sagipPlatinumCode;
//                }
//                intent.putExtra("isSpouseExist", isSpouseExist);
//                intent.putExtra("sagipType", sagipType);
//                intent.putExtra("regstatus", statusselected);
//                addDependentResultLauncher.launch(intent);
//            }
//        });


        //radioSagip.setVisibility(View.GONE);
    //    sagipDiv.setVisibility(View.GONE);

//        page1ImageView.setVisibility(View.GONE);
//        page2ImageView.setVisibility(View.GONE);

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

  //      customAdapter = new DependentAdapter(getApplicationContext(),members, this);

//        recyclerView.setAdapter(customAdapter);

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (frontphoto != null) {
    //                            Bitmap photo = BitmapFactory.decodeFile(frontphoto.getAbsolutePath());
                        String newpath = saveImage(RegisterActivity.this, frontphoto.getAbsolutePath());
                        if (newpath ==null) return;
                        frontphoto = new File(newpath);
                        Bitmap photo = BitmapFactory.decodeFile(frontphoto.getAbsolutePath());
                        try {
                            photo = modifyOrientation(photo, frontphoto.getAbsolutePath());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
//                        page1ImageView.setImageBitmap(photo);
//                        page1ImageView.setVisibility(View.VISIBLE);

//REFERENCE NO.:1234567PANUNTUNAN
//REFERENCE NO.:1234567PANGALAN

                        try {
                            textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                            Frame imageFrame = new Frame.Builder().setBitmap(photo).build();
                            SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
                            String s = "";
                            for (int i = 0; i < textBlocks.size(); i++) {
                                TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                                s = s+textBlock.getValue();
                            }
//                            isImage1Valid = isRSValid(pocNumber.getText().toString(), s, RS1_LEFT_KEY, RS1_RIGHT_KEY);
                            String refno = getReferenceNumber(s);
//                            showAlert("Info", "Ang inyong reference number ay " +refno);
//                            pocNumber.setText(refno);
//                            messageView.setText("Text : " + s);

                            Log.d("REFNO " , refno);
                            Log.d("register", "raw : " + s);
                            if (!s.contains("LLMCA") || !s.contains("LRMCA")){
                                isImage1Valid = false;
                                showAlert("Hindi tama ang paraan ng iyong pag-upload ng larawan. Kuhanan nang MALINAW ang form mula sa Reference No. hanggang sa LLMCA at LRMCA.");
                                //frontphoto.delete();
//                                page1ImageView.setVisibility(View.GONE);
                                capturedRS1.setText("");
                            }else {
                                PolicyInfo pol = getPolicyInfo();
                                if (databaseHelper.isPocExists(refno, pol.getProduct1(), pol.getProduct2(), pol.getProduct3())){
                                     showAlert("Ang Reference No: "+ refno+" ay na-enroll na. Siguraduhing walang kaparehong policy and naka-save,upload o synced.");
                                } else {
                                    isImage1Valid = true;
                                    messageView.setText("");
                                    capturedRS1.setText(refno);
//                                    if (capturedRS2.getText().toString().length() > 0 && capturedRS1.getText().toString().length() > 0 &&
//                                            capturedRS2.getText().toString().equalsIgnoreCase(capturedRS1.getText().toString())) {
//                                        pocNumber.setText(refno);
//                                    }
                                    if (!capturedRS1.getText().toString().isEmpty()) {
                                        pocNumber.setText(refno);
                                    }
                                }
                            }
                            textRecognizer.release();
                            totalPremium.setFocusable(true);
                            totalPremium.requestFocus();
                        }catch (Exception e){
                            e.printStackTrace();
//                            messageView.setText(e.getMessage());
                        }finally {
                            textRecognizer.release();
                        }
                    }
                }
            });

        ActivityResultLauncher<Intent> startActivityIntent2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Add same code that you want to add in onActivityResult method
                        // There are no request codes
                        Intent data = result.getData();
                        if (backphoto != null) {
                            String newpath = saveImage(RegisterActivity.this,backphoto.getAbsolutePath());
                            if (newpath ==null) return;
                            backphoto = new File(newpath);
//                            Bitmap photo = BitmapFactory.decodeFile(newpath);
                            Bitmap photo = BitmapFactory.decodeFile(backphoto.getAbsolutePath());
                            try {
                                photo = modifyOrientation(photo, backphoto.getAbsolutePath());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

//                            page2ImageView.setImageBitmap(photo);
//                            page2ImageView.setVisibility(View.GONE);

                            try {
                                textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                                Frame imageFrame = new Frame.Builder().setBitmap(photo).build();
                                SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
                                String s = "";
                                for (int i = 0; i < textBlocks.size(); i++) {
                                    TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                                    s = s+textBlock.getValue();
                                }
//                                isImage2Valid = isRSValid(pocNumber.getText().toString(), s, RS2_LEFT_KEY, RS2_RIGHT_KEY);
//                                messageView.setText("isImageValid : " + isImage2Valid);
                                String refno = getReferenceNumber(s);

//                                showAlert("Info", "Ang inyong reference number ay " +refno);
//                                messageView.setText(refno);
                                if (!s.contains("LLMCA") || !s.contains("LRMCA")){
                                    isImage2Valid = false;
                                    showAlert("Hindi tama ang paraan ng iyong pag-upload ng larawan. Kuhanan nang MALINAW ang form mula sa Reference No. hanggang sa LLXB at LRXB");
                                    //backphoto.delete();
//                                    page2ImageView.setVisibility(View.GONE);
                                    capturedRS2.setText("");
                                }else{
                                    PolicyInfo pol = getPolicyInfo();
                                    if (databaseHelper.isPocExists(refno, pol.getProduct1(), pol.getProduct2(), pol.getProduct3())){
                                        showAlert("Ang Reference No: "+ refno+" ay na-enroll na. Siguraduhing walang kaparehong policy and naka-save,upload o synced.");
                                    } else {
                                        isImage2Valid = true;
                                        messageView.setText("");
                                        capturedRS2.setText(refno);
                                        if (capturedRS2.getText().toString().length() > 0 && capturedRS1.getText().toString().length() > 0 &&
                                                capturedRS2.getText().toString().equalsIgnoreCase(capturedRS1.getText().toString())) {
                                            pocNumber.setText(refno);
                                        }
                                    }
                                }

                                textRecognizer.release();
                                totalPremium.setFocusable(true);
                                totalPremium.requestFocus();
                            }catch (Exception e){
//                                textView2.setText(e.getMessage());
                            } finally {
                                textRecognizer.release();
                            }
                        }
                    }
                });


        // Camera_open button is for open the camera and add the setOnClickListener in this button
        page1CameraButton.setOnClickListener(v -> {
            PolicyInfo pol = getPolicyInfo();
            Log.d("camera", "product" + pol.getProduct1() );
            Log.d("camera", "product" + pol.getProduct2() );
            Log.d("camera", "product" + pol.getProduct3() );
            if ((pol.getProduct1() != null && pol.getProduct1().length() > 0) ||
                    (pol.getProduct2() != null && pol.getProduct2().length() > 0)  ||
                    (pol.getProduct3() != null && pol.getProduct3().length() > 0) ) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                frontphoto = getOutputMediaFile();
                Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", frontphoto);
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                if (ContextCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    startActivityIntent.launch(camera_intent);
                } else {
                    startActivityIntent.launch(camera_intent);
                }
            } else {
                showAlert("Please select product");
            }
        });

//        page2CameraButton.setOnClickListener(v -> {
//            PolicyInfo pol = getPolicyInfo();
//            if ((pol.getProduct1() != null && pol.getProduct1().length() > 0) ||
//                    (pol.getProduct2() != null && pol.getProduct2().length() > 0)  ||
//                    (pol.getProduct3() != null && pol.getProduct3().length() > 0) ) {
//                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                backphoto = getOutputMediaFile();//new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
//                Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", backphoto);
//                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
////            camera_intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, fileSizeLimit);
//                if (ContextCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
////                activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                    startActivityIntent2.launch(camera_intent);
//                } else {
//                    startActivityIntent2.launch(camera_intent);
//                }
//            } else {
//                showAlert("Please select product");
//            }
//        });


//        sagipIndividual.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sagipIndividual.isChecked() && members != null && members.size() > 0){
//                    MemberInfo member;
//                    for (Iterator<MemberInfo> iter = members.iterator(); iter.hasNext();){
//                        member = iter.next();
//                        if (member.getReltype().equalsIgnoreCase("SPOUSE")){
//                             iter.remove();
//                        }
//                    }
//
//                    customAdapter = new DependentAdapter(getApplicationContext(),members, RegisterActivity.this);
//                    recyclerView.setAdapter(customAdapter);//new MemberAdapter(getApplicationContext(),members, RegisterActivity.this));
//                    customAdapter.notifyDataSetChanged();
//                    setTotalPremium();
//                }
//            }
//        });

        effectivityDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                effectivityDate.setError(null);
                hideKeyboard(v);
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month = month+1;
                        String monthstr = ""+month;
                        String dayOfMonthStr = ""+dayOfMonth;

                        if (monthstr.length()==1) monthstr = "0"+monthstr;
                        if (dayOfMonthStr.length()==1) dayOfMonthStr = "0"+dayOfMonthStr;
                        String date = monthstr+"/"+dayOfMonthStr+"/"+year;
                        effectivityDate.setText(date);

                        setAge();

                    }
                },year, month,day);
                dialog.getDatePicker().setMaxDate(CommonUtil.getEffMinDate().getTime());
                dialog.getDatePicker().setMinDate(CommonUtil.getEffMaxDate().getTime());
                dialog.show();

            }
        });

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateOfBirth.setError(null);
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                hideKeyboard(v);
                String selectedDate = dateOfBirth.getText().toString();
                try {
                    String[] dateArray = selectedDate.split("/");
                    year = Integer.parseInt(dateArray[2]);
                    month = Integer.parseInt(dateArray[0])-1;
                    day = Integer.parseInt(dateArray[1]);
                }catch (Exception e){
                }
                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month = month+1;
                        String monthstr = ""+month;
                        String dayOfMonthStr = ""+dayOfMonth;

                        if (monthstr.length()==1) monthstr = "0"+monthstr;
                        if (dayOfMonthStr.length()==1) dayOfMonthStr = "0"+dayOfMonthStr;
                        String date = monthstr+"/"+dayOfMonthStr+"/"+year;
                        dateOfBirth.setText(date);

//                        checkCardCare.setChecked(false);
//                        checkKabuklod.setChecked(false);
//                        checkSagip.setChecked(false);
//                        sagipIndividual.setChecked(false);
//                        sagipFamily.setChecked(false);
//                        sagipPlatinum.setChecked(false);

                        members = new ArrayList<>();
                       // customAdapter = new DependentAdapter(getApplicationContext(),members, RegisterActivity.this);
                        //recyclerView.setAdapter(customAdapter);//new MemberAdapter(getApplicationContext(),members, RegisterActivity.this));
                       // customAdapter.notifyDataSetChanged();

                        try {
                            Date birthDate = dateFormat.parse(date);
                            Date effDate = null;
                            try {
                                effDate = dateFormat.parse(effectivityDate.getText().toString());
                            }catch (Exception e){

                            }
//                            (SharedPreferences sharedPreferences, String code,String type, String status, Date birthdate)
//                            Boolean cardCareValid = CommonUtil.isProductValid(sharedPreferences, cardCareCode,  "PRINCIPAL",statusselected,birthDate,effDate);
//                            Boolean kabukloadValid = CommonUtil.isProductValid(sharedPreferences, kabuklodCode,  "PRINCIPAL",statusselected,birthDate,effDate);
//
//                            Boolean sagipFamilyValid = CommonUtil.isProductValid(sharedPreferences, sagipFamilyCode,  "PRINCIPAL",statusselected,birthDate,effDate);
//                            Boolean sagipIndividualValid = CommonUtil.isProductValid(sharedPreferences, sagipIndividualCode,  "PRINCIPAL",statusselected,birthDate,effDate);
//                            Boolean sagipPlatinumValid = CommonUtil.isProductValid(sharedPreferences, sagipPlatinumCode,  "PRINCIPAL",statusselected,birthDate,effDate);



//                            Log.d("register", "cardCareValid : " + cardCareValid);
//                            Log.d("register", "kabukloadValid : " + kabukloadValid);
//                            Log.d("register", "sagipFamilyValid : " + sagipFamilyValid);
//                            Log.d("register", "sagipIndividualValid : " + sagipIndividualValid);
//                            Log.d("register", "sagipPlatinumValid : " + sagipPlatinumValid);

//                            if (!cardCareValid)checkCardCare.setChecked(false);
//                            if (!kabukloadValid)checkKabuklod.setChecked(false);
//                            if (!sagipIndividualValid || !sagipFamilyValid || !sagipPlatinumValid)checkSagip.setChecked(false);

//                            checkCardCare.setEnabled(cardCareValid);
//                            checkKabuklod.setEnabled(kabukloadValid);
//                            checkSagip.setEnabled(sagipIndividualValid || sagipFamilyValid || sagipPlatinumValid);

//                            switchCardCare(cardCareValid);
//                            switchKabuklod(kabukloadValid);
//                            switchSagip(sagipIndividualValid || sagipFamilyValid || sagipPlatinumValid);

//                            if (cardCareValid){
//                                checkCardCare.setChecked(true);
//                            }
//                            if (kabukloadValid){
//                                checkKabuklod.setChecked(true);
//                            }
//                            if (sagipIndividualValid || sagipFamilyValid || sagipPlatinumValid){
//                                checkSagip.setChecked(true);
//                            }

//                            if (sagipIndividualValid || sagipFamilyValid || sagipPlatinumValid){
//                                sagipDiv.setVisibility(View.VISIBLE);
//                            }

//                            sagipIndividual.setEnabled(sagipIndividualValid);
//                            sagipFamily.setEnabled(sagipFamilyValid);
//                            sagipPlatinum.setEnabled(sagipPlatinumValid);
//                            if(sagipPlatinumValid) {
//                                setSagipCheck(sagipPlatinum,sagipIndividual,sagipFamily);
//                            }

                            setAge();
                        } catch (Exception e) {
                            showAlert(e.getMessage());
                        }
                    }
                },year, month,day);
                Date sysdate = new Date();
                dialog.getDatePicker().setMaxDate(CommonUtil.getMinDate().getTime());
                dialog.getDatePicker().setMinDate(CommonUtil.getMaxDate().getTime());
                dialog.show();
            }
        });
        newRadio.setVisibility(View.VISIBLE);
        members = new ArrayList<MemberInfo>();
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("id") != null  && intent.getStringExtra("id").length() > 0) {
            String id = intent.getStringExtra("id");
            newRadio.setVisibility(View.GONE);
            List<MemberInfo> memberlist = databaseHelper.getMembersId(id);
            policy = databaseHelper.getPolicyInfo(id);
            statusselected = "Renewal";
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            int amonth = month+1;
            String monthstr = ""+amonth;
            String dayOfMonthStr = ""+day;

            if (monthstr.length()==1) monthstr = "0"+monthstr;
            if (dayOfMonthStr.length()==1) dayOfMonthStr = "0"+dayOfMonthStr;
            String date = monthstr+"/"+dayOfMonthStr+"/"+year;
            effectivityDate.setText(date);
            renewalRadio.setChecked(true);
            renewalRadio.setTextColor(WHITE);
            renewalRadio.setBackgroundResource(R.drawable.blue_button);

            if (policy.getPaymode().equalsIgnoreCase("CASH")){
                radioCash.setChecked(true);
                switchColor(radioCash, radioLoan);
            }else if (policy.getPaymode().equalsIgnoreCase("LOAN")){
                radioLoan.setChecked(true);
                switchColor(radioLoan, radioCash);
            }


            String product = policy.getProduct() + policy.getProduct1() + policy.getProduct2() + policy.getProduct3() ;

            Log.d("register", "product : " + product);

            Date effDate = null;
            try {
                effDate = dateFormat.parse(effectivityDate.getText().toString());
            }catch (Exception e){

            }

            for (MemberInfo member:memberlist){
                member.setProduct(product);
                if (member.getPertype().equalsIgnoreCase("PRINCIPAL")){
                    firstname.setText(member.getFname());
                    middlename.setText(member.getMname());
                    lastname.setText(member.getLname());
                    suffix.setText(member.getSuffix());
                    if (member.getMobileno() != null && member.getMobileno().length() >=11){
                        mobileno.setText(member.getMobileno().substring(member.getMobileno().length() - 9));
                    }else {
                        mobileno.setText(member.getMobileno());
                    }
                    dateOfBirth.setText(member.getDob());
                    Date birthDate = null;
                    try {
                        birthDate = dateFormat.parse(member.getDob());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }


                    setAge();
                    Boolean cardCareValid = CommonUtil.isProductValid(sharedPreferences, cardCareCode,  "PRINCIPAL",statusselected,birthDate,effDate);
                    Boolean kabukloadValid = CommonUtil.isProductValid(sharedPreferences, kabuklodCode,  "PRINCIPAL",statusselected,birthDate,effDate);

                    Boolean sagipFamilyValid = CommonUtil.isProductValid(sharedPreferences, sagipFamilyCode,  "PRINCIPAL",statusselected,birthDate,effDate);
                    Boolean sagipIndividualValid = CommonUtil.isProductValid(sharedPreferences, sagipIndividualCode,  "PRINCIPAL",statusselected,birthDate,effDate);
                    Boolean sagipPlatinumValid = CommonUtil.isProductValid(sharedPreferences, sagipPlatinumCode,  "PRINCIPAL",statusselected,birthDate,effDate);
//virus_scan_time_table(pkgname,softname,time,event,category)

//                    if (!cardCareValid)checkCardCare.setChecked(false);
//                    if (!kabukloadValid)checkKabuklod.setChecked(false);
//                    if (!sagipIndividualValid || !sagipFamilyValid || !sagipPlatinumValid)checkSagip.setChecked(false);

                    Log.d("register", "cardCareValid : " + cardCareValid);
                    Log.d("register", "kabukloadValid : " + kabukloadValid);
                    Log.d("register", "sagipFamilyValid : " + sagipFamilyValid);
                    Log.d("register", "sagipIndividualValid : " + sagipIndividualValid);
                    Log.d("register", "sagipPlatinumValid : " + sagipPlatinumValid);

//                    checkCardCare.setEnabled(cardCareValid);
//                    checkKabuklod.setEnabled(kabukloadValid);
//                    checkSagip.setEnabled(sagipIndividualValid || sagipFamilyValid || sagipPlatinumValid);
                    switchCardCare(cardCareValid);
                    switchKabuklod(kabukloadValid);
                    switchSagip(sagipIndividualValid || sagipFamilyValid || sagipPlatinumValid);

//                    sagipIndividual.setEnabled(sagipIndividualValid);
//
//                    sagipFamily.setEnabled(sagipFamilyValid);
//
//                    sagipPlatinum.setEnabled(sagipPlatinumValid);
//                    if(sagipPlatinumValid) {
//                        setSagipCheck(sagipPlatinum,sagipIndividual,sagipFamily);
//                    }


                    if (member.getSuffix() != null)
                        suffix.setText(member.getSuffix(), false);

                    if (member.getGender() != null){
                        if (member.getGender().equalsIgnoreCase("Lalaki")){
                            genderselected = "Lalaki";
                            genderMale.setChecked(true);
                        } else  if (member.getGender().equalsIgnoreCase("Babae")){
                            genderselected = "Babae";
                            genderFemale.setChecked(true);
                        }
                    }
                    Log.d("key", "member.getCardmember() : " +member.getCardmember());
                    Log.d("key", "member.getCivilstat() : " +member.getCivilstat());
                    if (member.getCivilstat() != null){
                        if (member.getCivilstat().equalsIgnoreCase("Single")){
                            civilstatusselected = "New";
                            civilstatusSingle.setChecked(true);
                            switchColor(civilstatusSingle, civilstatusMarried);
                        }else if (member.getCivilstat().equalsIgnoreCase("Married")){
                            civilstatusselected ="Married";
                            civilstatusMarried.setChecked(true);
                            switchColor(civilstatusMarried, civilstatusSingle);
                        }
                    }
                    if (member.getCardmember() != null){

                        if (member.getCardmember().equalsIgnoreCase("Yes")){
                            membershipselected = "Yes";
                            cardmemberYes.setChecked(true);
                            switchColor(cardmemberYes, cardmemberNo);
                        }else if (member.getCardmember().equalsIgnoreCase("No")){
                            membershipselected = "No";
                            cardmemberNo.setChecked(true);
                            switchColor(cardmemberNo, cardmemberYes);
                        }
                    }
                }else{
                    members.add(member);
                }
            }

//            if (product != null){
//                Log.d("key", "product : " +product);
//                if (product.toUpperCase().contains("KABUK")){
//                    checkKabuklod.setChecked(true);
//                    checkKabuklod.setTextColor(WHITE);
//                    checkKabuklod.setBackgroundResource(R.drawable.blue_button);
//                }
//                if (product.toUpperCase().contains("CARD")){
//                    checkCardCare.setChecked(true);
//                    checkCardCare.setTextColor(WHITE);
//                    checkCardCare.setBackgroundResource(R.drawable.blue_button);
//                }
//                if (product.toUpperCase().contains("SAGIP")){
//                    isSagip = true;
//                    checkSagip.setChecked(true);
//                    checkSagip.setTextColor(WHITE);
//                    checkSagip.setBackgroundResource(R.drawable.blue_button);
//
//                    sagipDiv.setVisibility(View.VISIBLE);
//
//                    if (product.toUpperCase().contains("FAMILY")){
//                        setSagipCheck(sagipFamily, sagipPlatinum,sagipIndividual);
//                    } else if (product.toUpperCase().contains("INDI")){
//                        setSagipCheck(sagipIndividual, sagipFamily, sagipPlatinum);
//                    }else if (product.toUpperCase().contains("PLAT")){
//                        setSagipCheck(sagipPlatinum, sagipIndividual, sagipFamily);
//                    }
//                }
//            }

            customAdapter = new DependentAdapter(getApplicationContext(),members, RegisterActivity.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(customAdapter);

            setTotalPremium();
        }

        setListeners();

        if (intent != null && intent.getStringExtra("id") != null  && intent.getStringExtra("id").length() > 0) {
            String id = intent.getStringExtra("id");
            List<MemberInfo> memberlist = databaseHelper.getMembersId(id);
            policy = databaseHelper.getPolicyInfo(id);
            String product = policy.getProduct();

            Log.d("register", "product : " + product);

            Date effDate = null;
            try {
                effDate = dateFormat.parse(effectivityDate.getText().toString());
            } catch (Exception e) {

            }
            for (MemberInfo member : memberlist) {
                if (member.getPertype().equalsIgnoreCase("PRINCIPAL")) {
                    Date birthDate = null;
                    try {
                        birthDate = dateFormat.parse(member.getDob());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    setAge();
                    Boolean cardCareValid = CommonUtil.isProductValid(sharedPreferences, cardCareCode, "PRINCIPAL", statusselected, birthDate, effDate);
                    Boolean kabukloadValid = CommonUtil.isProductValid(sharedPreferences, kabuklodCode, "PRINCIPAL", statusselected, birthDate, effDate);

                    Boolean sagipFamilyValid = CommonUtil.isProductValid(sharedPreferences, sagipFamilyCode, "PRINCIPAL", statusselected, birthDate, effDate);
                    Boolean sagipIndividualValid = CommonUtil.isProductValid(sharedPreferences, sagipIndividualCode, "PRINCIPAL", statusselected, birthDate, effDate);
                    Boolean sagipPlatinumValid = CommonUtil.isProductValid(sharedPreferences, sagipPlatinumCode, "PRINCIPAL", statusselected, birthDate, effDate);

//                    if (!cardCareValid) checkCardCare.setChecked(false);
//                    if (!kabukloadValid) checkKabuklod.setChecked(false);
//                    if (!sagipIndividualValid || !sagipFamilyValid || !sagipPlatinumValid)
//                        checkSagip.setChecked(false);

                    switchCardCare(cardCareValid);
                    switchKabuklod(kabukloadValid);
                    switchSagip(sagipIndividualValid || sagipFamilyValid || sagipPlatinumValid);

//                    sagipIndividual.setEnabled(sagipIndividualValid);
//                    sagipFamily.setEnabled(sagipFamilyValid);
//                    sagipPlatinum.setEnabled(sagipPlatinumValid);
//                    if (sagipPlatinumValid) {
//                        setSagipCheck(sagipPlatinum, sagipIndividual, sagipFamily);
//                    }

                    if (product != null){
                        Log.d("key", "product : " +product);
//                        if (product.toUpperCase().contains("KABUK")){
//                            checkKabuklod.setChecked(true);
//                            checkKabuklod.setTextColor(WHITE);
//                            checkKabuklod.setBackgroundResource(R.drawable.blue_button);
//                        }
//                        if (product.toUpperCase().contains("CARD")){
//                            checkCardCare.setChecked(true);
//                            checkCardCare.setTextColor(WHITE);
//                            checkCardCare.setBackgroundResource(R.drawable.blue_button);
//                        }
//                        if (product.toUpperCase().contains("SAGIP")){
//                            isSagip = true;
//                            checkSagip.setChecked(true);
//                            checkSagip.setTextColor(WHITE);
//                            checkSagip.setBackgroundResource(R.drawable.blue_button);
//
//                            sagipDiv.setVisibility(View.VISIBLE);
//
//                            if (product.toUpperCase().contains("FAMILY")){
//                                setSagipCheck(sagipFamily, sagipPlatinum,sagipIndividual);
//                            } else if (product.toUpperCase().contains("INDI")){
//                                setSagipCheck(sagipIndividual, sagipFamily, sagipPlatinum);
//                            }else if (product.toUpperCase().contains("PLAT")){
//                                setSagipCheck(sagipPlatinum, sagipIndividual, sagipFamily);
//                            }
//                        }
                    }

                }

            }
        }

        radioStatus.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        System.out.println("checkedId : " + checkedId);
                        if (checkedId == R.id.radioStatusNew){
                            statusselected = "New";
                            newRadio.setTextColor(WHITE);
                            newRadio.setBackgroundResource(R.drawable.blue_button);
                            renewalRadio.setTextColor(BLACK);
                            renewalRadio.setBackgroundResource(R.drawable.gray_button);
                        } else if (checkedId == R.id.radioStatusRenewal){
                            statusselected = "Renewal";
                            renewalRadio.setTextColor(WHITE);
                            renewalRadio.setBackgroundResource(R.drawable.blue_button);
                            newRadio.setTextColor(BLACK);
                            newRadio.setBackgroundResource(R.drawable.gray_button);
                        }
                        dateOfBirth.setText("");
                        age.setText("");

//                        checkCardCare.setChecked(false);
//                        checkKabuklod.setChecked(false);
//                        checkSagip.setChecked(false);
//                        sagipIndividual.setChecked(false);
//                        sagipFamily.setChecked(false);
//                        sagipPlatinum.setChecked(false);

//                        members = new ArrayList<>();
//                        customAdapter = new DependentAdapter(getApplicationContext(),members, RegisterActivity.this);
//                        recyclerView.setAdapter(customAdapter);//new MemberAdapter(getApplicationContext(),members, RegisterActivity.this));
//                        customAdapter.notifyDataSetChanged();
                    }
                }
        );

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int amonth = month+1;
        String monthstr = ""+amonth;
        String dayOfMonthStr = ""+day;

        if (monthstr.length()==1) monthstr = "0"+monthstr;
        if (dayOfMonthStr.length()==1) dayOfMonthStr = "0"+dayOfMonthStr;
        String date = monthstr+"/"+dayOfMonthStr+"/"+year;
//        effectivityDate.setText(date); // np default date



        savePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    setTransaction(SharedPreferencesUtility.getAgentId(sharedPreferences),
                            spinner.getSelectedItem().toString());

                    String message = getErrorMessage();

                    if (message != null){

                        messageView.setText(message);
                        showAlert(message);
                        return;
                    }else{
                        messageView.setText("");
                    }

                    Log.d("FPHOTO" , frontphoto.toString() );
                    if (frontphoto != null){
                        if (message != null){
                            messageView.setText(message);
                            showAlert(message);
                        }else{
                            messageView.setText("");
                            //save record and go to success page
                            saveTransation();
                            Intent intent = new Intent(RegisterActivity.this, SuccessActivity.class);
                            PolicyInfo policy = transaction.getPolicy();
                            String products = policy.getProduct1();

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            String policyJson = gson.toJson(policy);
                            Log.d("POLICY MEMBER DETAILS: " , policyJson + "");
                            if ((policy.getProduct1() != null && policy.getProduct1().length() > 0) &&
                                    (policy.getProduct2() != null && policy.getProduct2().length() > 0) &&
                                    policy.getProduct3() != null && policy.getProduct3().length() > 0){
                                products = policy.getProduct1() + ", " + policy.getProduct2() + " at " + policy.getProduct3();
                            } else if (policy.getProduct1()  != null  && policy.getProduct2() != null &&
                                    policy.getProduct1().length() > 0 && policy.getProduct2().length() >  0  ){
                                products = policy.getProduct1() + " at " + policy.getProduct2();
                            } else if (policy.getProduct1() != null && policy.getProduct3() != null &&
                                    policy.getProduct1().length() > 0 && policy.getProduct3().length() >  0){
                                products = policy.getProduct1() + " at " + policy.getProduct3();
                            } else if (policy.getProduct2() != null && policy.getProduct3() != null &&
                                    policy.getProduct2().length() > 0 && policy.getProduct3().length() >  0){
                                products = policy.getProduct2() + " at " + policy.getProduct3();
                            } else if (policy.getProduct1() != null && policy.getProduct1().length() > 0){
                                products = policy.getProduct1();
                            } else if (policy.getProduct2() != null && policy.getProduct2().length() > 0){
                                products = policy.getProduct2();
                            } else if (policy.getProduct3() != null && policy.getProduct3().length() > 0){
                                products = policy.getProduct3();
                            }

                            intent.putExtra("products", products);
                            intent.putExtra("insured",
                                    (transaction.getPrincipal().getFname() + " " +
                                            (
                                                    (transaction.getPrincipal().getMname() !=null && transaction.getPrincipal().getMname().length() > 0)?
                                                            (transaction.getPrincipal().getMname() + " "):"" )+
                                            transaction.getPrincipal().getLname()) );

                            startActivity(intent);
                        }
                    }else{
                        Log.d("ERROR " , "ERROR");
                        message = "PLEASE CAPTURE RS";
                        messageView.setText(message);
                        messageView.setVisibility(View.VISIBLE);
                        showAlert(message);
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    if (Objects.equals(e.getMessage(), "println needs a message")){
                        messageView.setVisibility(View.VISIBLE);
                        messageView.setText("Siguraduhing lahat ng may "+"*"+" ay may laman");
                        showAlert("Siguraduhing lahat ng may "+"*"+" ay may laman");
                        return;
                    }
                    messageView.setText(e.getMessage());
                    Log.d("Error" , e.getLocalizedMessage()+"");
                }

            }
        });




    }

    ActivityResultLauncher<Intent> addDependentResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    Intent data = result.getData();
                    String afirstname ="";
                    String amiddlename ="";
                    String alastname ="";
                    String amobileno ="";
                    String asuffix ="";
                    String adateofbirth ="";
                    String areltype ="";
                    String acardmember ="";
                    String acivilstat ="";
                    String agender ="";
                    String astatus ="";
                    String aaccountOfficer ="";
                    String aunitManager ="";
                    String acenter = "";
                    String aauthrep = "";
                    String areltomember= "";
                    String aprovince = "";
                    String acity  = "";
                    String abrgy = "";
                    String astreet= "";
                    String aemail= "";
                    String aplaceofbirth= "";
                    String anationality= "";
                    String aclienttype = "";
                    String aINSTITUTION= "";
                    String aAuthRepFname = "";
                    String aAuthRepMname = "";
                    String aAuthRepLname = "";

                    try {
                        if (data != null && data.getStringExtra("firstname") != null
                                && data.getStringExtra("firstname").length() > 0) {
                            afirstname = data.getStringExtra("firstname");
                            amiddlename = data.getStringExtra("middlename");
                            alastname = data.getStringExtra("lastname");
                            amobileno = data.getStringExtra("mobileno");
                            adateofbirth = data.getStringExtra("dateofbirth");
                            areltype = data.getStringExtra("reltype");
                            acivilstat = data.getStringExtra("civilstat");

                            asuffix = data.getStringExtra("suffix");
                            acardmember = data.getStringExtra("cardmember");
                            agender = data.getStringExtra("gender");
                            astatus = data.getStringExtra("status");
                            aaccountOfficer = data.getStringExtra("ACCOUNT_OFFICER");
                            aunitManager = data.getStringExtra("UNIT_MANAGER");
                            acenter = data.getStringExtra("CENTER");
                            aauthrep  = data.getStringExtra("AUTH_REP");
                            areltomember = data.getStringExtra("REL_TO_MEMBER");
                            aprovince = data.getStringExtra("PROVINCE");
                            acity = data.getStringExtra("CITY");
                            abrgy = data.getStringExtra("BRGY");
                            astreet = data.getStringExtra("STREET");
                            aemail = data.getStringExtra("EMAIL");
                            anationality = data.getStringExtra("NATIONALITY");
                            aplaceofbirth = data.getStringExtra("PLACEOFBIRTH");
                            aclienttype = data.getStringExtra("CLIENTTYPE");
                            aINSTITUTION = data.getStringExtra("INSTITUTION");
                            aAuthRepFname = data.getStringExtra("AUTH_REP_FIRST_NAME");
                            aAuthRepMname = data.getStringExtra("AUTH_REP_MNAME") ;
                            aAuthRepLname = data.getStringExtra("AUTH_REP_LNAME");


                            if (!isSpouseSelected){
                                if (areltype.equalsIgnoreCase("SPOUSE")){
                                    isSpouseSelected = true;
                                }
                            }

                        }
                        if (result.getResultCode() == REQUEST_CODE_ADD_MEMBER) {
                            Long id = Long.valueOf(members.size() + 1);
                            MemberInfo member = new MemberInfo(""+id, areltype, afirstname, amiddlename, alastname, asuffix,
                                    adateofbirth, agender, acivilstat, amobileno, acardmember, "",astatus, areltype , aaccountOfficer , aunitManager , acenter ,aauthrep , areltomember , aprovince , acity , abrgy , astreet , aemail , aplaceofbirth,anationality , aclienttype ,aINSTITUTION , aAuthRepFname , aAuthRepMname , aAuthRepLname);
                            if (areltype.equalsIgnoreCase("SPOUSE") && isSpouseExist()){
                                showAlert("Cannot add multiple spouse." );
                            }else {
                                int membersize = members.size();
                                if (isSpouseExist()){
                                    membersize = membersize-1;
                                }
                                if (membersize >= 5 && !areltype.equalsIgnoreCase("SPOUSE")) {
                                    showAlert("Umabot na sa lima ang naka encode na insured child para sa policy na ito, " +
                                            "Maaaring reviewhin ang kanilang mga detalye.");
//                                }else  if (members.size() >= 5 && areltype.equalsIgnoreCase("SPOUSE") ){
//                                    showAlert("Siguraduhin na ang Return Stub <# of Return Stub ex. 2> Reference # ay tugma sa Return Stub 1." +
//                                            " Maaaring makipag ugnayan sa ating PROs kapag tayo ay hindi makatuloy sa capturing.");
                                }else {
                                    members.add(member);
                                }
                            }
                        } else {
                            String key = data.getStringExtra("key");
                            Log.d("key", "key : " +key);
                            MemberInfo member = new MemberInfo(key, areltype, afirstname, amiddlename, alastname, asuffix,
                                    adateofbirth, agender, acivilstat, amobileno, acardmember, "",astatus, areltype , aaccountOfficer , aunitManager , acenter , aauthrep , areltomember , aprovince , acity, abrgy , astreet ,aemail , aplaceofbirth,anationality , aclienttype , aINSTITUTION , aAuthRepFname , aAuthRepMname , aAuthRepLname);

                            MemberInfo a;
                            for (Iterator<MemberInfo> iter = members.iterator(); iter.hasNext(); ) {
                                a = iter.next();
                                if (a.getId().equals(key)) {
                                    iter.remove();
                                    break;
                                }
                            }
                            members.add(member);
                        }
                    } catch ( Exception e){
                        //Toast.makeText(RegisterActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }


                    customAdapter = new DependentAdapter(getApplicationContext(),members, RegisterActivity.this);
                    recyclerView.setAdapter(customAdapter);//new MemberAdapter(getApplicationContext(),members, RegisterActivity.this));
                    customAdapter.notifyDataSetChanged();
                    setTotalPremium();
                    totalPremium.setFocusable(true);
                    totalPremium.requestFocus();
                }
            });

    boolean isSpouseExist(){
        if (members != null && members.size() > 0){
            for (MemberInfo m:members){
                if (m.getReltype().equalsIgnoreCase("SPOUSE")) return true;
            }
        }
        return false;
    }
    String getErrorMessage(){



        MemberInfo principal = transaction.getPrincipal();
        PolicyInfo policy = transaction.getPolicy();

//        if (checkSagip.isChecked() && sagipFamily.isChecked()){
//            if (members.size() > 0){
//                boolean isSpouseExist = false;
//                for (MemberInfo member:members){
//                    if (member.getReltype().equalsIgnoreCase("SPOUSE")){
//                        isSpouseExist = true;
//                        break;
//                    }
//                }
//                if (!isSpouseExist){
//                    return "Ilagay ang impormasyon ng asawa para magpatuloy.";
//                }
//            } else{
//                return "Ilagay ang impormasyon ng mga anak para magpatuloy.";
//            }
//        }F

        String sagipType = "0";
//        if (sagipIndividual.isChecked()){
//            sagipType = sagipIndividualCode;
//        } else if (sagipFamily.isChecked()){
//            sagipType = sagipFamilyCode;
//        } else if (sagipPlatinum.isChecked()){
//            sagipType = sagipPlatinumCode;
//        }
        String accountOfficer  = principal.getAcctOfficer();
        String unitManagers  = principal.getUnitManager();
        String centers  = principal.getCenter();
        String authRepFnames  = principal.getAuthRepFName();
        String authRepMnames = principal.getAuthRepMName();
        String authRepLNames = principal.getAuthRepLName();
        String relToMemberxxx  = principal.getRelToMember();
        if (principal.getClientType().isEmpty() || principal.getClientType() == null){
            radioCardChild.setError("Siguraduhin na ilagay ang Client Type ng Member");
            radioCardPrincipal.setError("Siguraduhin na ilagay ang Client Type ng Member");
            radioNonCardPrincipal.setError("Siguraduhin na ilagay ang Client Type ng Member");
            radioNonCardSpouse.setError("Siguraduhin na ilagay ang Client Type ng Member");
            radioGroupClientType.setFocusableInTouchMode(true);
            radioGroupClientType.requestFocus();
            return "Siguraduhin na ilagay ang Client Type ng Member";
        }

        if (principal.getInstitution().isEmpty()){
            institutions.setError("Siguraduhin na ilagay ang Institution sa Miyembro ng Member");
            institutions.requestFocus();
            return "Siguraduhin na ilagay ang Institution sa Miyembro ng Member";
        }
        if(policy.getEffdate().isEmpty()){
            effectivityDate.setError("Siguraduhin na ilagay ang Effective Date ng Member");
            effectivityDate.setFocusableInTouchMode(true);
            effectivityDate.setFocusable(true);
            effectivityDate.requestFocus();
            return "Siguraduhin na ilagay ang Effective Date ng Member";
        }




        try {
            for (MemberInfo member : members) {
                if (member.getReltype() != null && !member.getReltype().equalsIgnoreCase("SPOUSE")) {
                    Boolean isValid = CommonUtil.isProductValid(sharedPreferences, sagipType, "CHILD", "NEW",
                            new Date(member.getDob()), new Date(principal.getEffectivity()));
                    if (!isValid) {
                        return "Siguraduhing pasok ang edad ni " + member.getFname() + " " + member.getLname() + "bilang dependent.";
                    }
                }
            }
        }catch (Exception e){

        }

        if (genderselected.equalsIgnoreCase("")){
            return "Siguraduhin na ilagay ang kasarian ng Member.";
        }



        if (principal.getFname().isEmpty()){
            firstname.setError("Siguraduhin na ilagay ang First Name ng Member.");
            firstname.requestFocus();
            return "Siguraduhin na ilagay ang First Name ng Member.";//""First name is required.";
        }

        if (principal.getLname().isEmpty()){
            lastname.setError("Siguraduhin na ilagay ang Last Name ng Member.");
            lastname.requestFocus();
            return "Siguraduhin na ilagay ang Last Name ng Member.";//""Last name is required.";
        }


        if (principal.getMobileno().length() != 11){
            mobileno.setError("Siguraduhin na ilagay ang Cellphone Number ng Member.");
            mobileno.requestFocus();
            return "Siguraduhin na ilagay ang Cellphone Number ng Member.";//"Mobile number is invalid.";
        }

        if (principal.getDob().isEmpty()){
            dateOfBirth.setError("Siguraduhin na ilagay ang Kapanganakan ng Member.");
            dateOfBirth.setFocusableInTouchMode(true);
            dateOfBirth.setFocusable(true);
            dateOfBirth.requestFocus();
            return "Siguraduhin na ilagay ang Kapanganakan ng Member.";
        }

//        if (principal.getPlaceOfBirth().isEmpty()){
//            placeofBirth.setError("Siguraduhin na ilagay ang Place of Birth ng Member");
//            placeofBirth.requestFocus();
//            return "Siguraduhin na ilagay ang Place of Birth ng Member";
//        }

//        if(principal.getNationality().isEmpty()){
//            nationality.setError("Siguraduhin na ilagay ang Nationality ng Member");
//            nationality.requestFocus();
//            return "Siguraduhin na ilagay ang Nationality ng Member";
//        }
        if (principal.getNationality().startsWith("-")||principal.getNationality().startsWith(".") ||
                principal.getNationality().endsWith("-") || principal.getNationality().endsWith(".")) {
            unitManager.setError("Ang Nationality ay hindi maaaring magsimula o magtapos sa '-' o '.'");
            unitManager.requestFocus();
            return "Ang Nationality ay hindi maaaring magsimula o magtapos sa '-' o '.'";
        }

        if (principal.getCivilstat().isEmpty() || principal.getCivilstat() == null){
            civilstatusMarried.setError("Siguraduhin na ilagay ang Civil Status ng Member");
            civilstatusSingle.setError("Siguraduhin na ilagay ang Civil Status ng Member");
            civilstatus.setFocusableInTouchMode(true);
            civilstatus.requestFocus();
             civilstatus.requestFocus();
            return "Siguraduhin na ilagay ang Civil Status ng Member";
        }

        if (principal.getProv().isEmpty()){
            province.setError("Siguraduhin na ilagay ang Province ng Member");
            province.requestFocus();
            return "Siguraduhin na ilagay ang Province ng Member";
        }
        if (principal.getCity().isEmpty()){
            city.setError("Siguraduhin na ilagay ang Province ng Member");
            city.requestFocus();
            return "Siguraduhin na ilagay ang City ng Member";
        }
        if (principal.getBrgy().isEmpty()){
            brgy.setError("Siguraduhin na ilagay ang Barangay ng Member");
            brgy.requestFocus();
            return "Siguraduhin na ilagay ang Barangay ng Member";
        }
//        if (principal.getStreet().isEmpty()){
//            edtStreet.setError("Siguraduhin na ilagay ang Street ng Member");
//            edtStreet.requestFocus();
//            return "Siguraduhin na ilagay ang Street ng Member";
//        }


        if (accountOfficer.isEmpty()){
            acctOfficer.setError("Siguraduhin na ilagay ang Account Officer ng Member");
            acctOfficer.requestFocus();
            return "Siguraduhin na ilagay ang Account Officer ng Member";
        }
        if (accountOfficer.startsWith("-")||accountOfficer.startsWith(".") ||
        accountOfficer.endsWith("-") || accountOfficer.endsWith(".")) {
            acctOfficer.setError("Ang Account Officer ay hindi maaaring magsimula o magtapos sa '-' o '.'");
            acctOfficer.requestFocus();
            return "Ang Account Officer ay hindi maaaring magsimula o magtapos sa '-' o '.'";
        }


        if (unitManagers.isEmpty()){
            unitManager.setError("Siguraduhin na ilagay ang Unit Manager ng Member");
            unitManager.requestFocus();
            return "Siguraduhin na ilagay ang Unit Manager ng Member";
        }
        if (unitManagers.startsWith("-")||unitManagers.startsWith(".") ||
                unitManagers.endsWith("-") || unitManagers.endsWith(".")) {
            unitManager.setError("Ang Unit Manager ay hindi maaaring magsimula o magtapos sa '-' o '.'");
            unitManager.requestFocus();
            return "Ang Unit Manager ay hindi maaaring magsimula o magtapos sa '-' o '.'";
        }

        if (centers.isEmpty()){
            Center.setError("Siguraduhin na ilagay ang Center ng Member");
            Center.requestFocus();
            return "Siguraduhin na ilagay ang Center ng Member";
        }
        if (centers.startsWith("-")||centers.startsWith(".") ||
                centers.endsWith("-") || centers.endsWith(".")) {
            Center.setError("Ang Center ay hindi maaaring magsimula o magtapos sa '-' o '.'");
            Center.requestFocus();
            return "Ang Center ay hindi maaaring magsimula o magtapos sa '-' o '.'";
        }

        if (authRepFnames.isEmpty()){
            authRepFname.setError("Siguraduhin na ilagay ang Authorized Representative  ng Member");
            authRepFname.requestFocus();
            return "Siguraduhin na ilagay ang Authorized Representative  ng Member";
        }
        if (authRepFnames.startsWith("-")||authRepFnames.startsWith(".") ||
                authRepFnames.endsWith("-") || authRepFnames.endsWith(".")) {

            authRepFname.setError("Ang Authorized Representative ay hindi maaaring magsimula o magtapos sa '-' o '.'");
            authRepFname.requestFocus();
            return "Ang Authorized Representative ay hindi maaaring magsimula o magtapos sa '-' o '.'";
        }

        if (authRepLNames.isEmpty()){
            authRepLname.setError("Siguraduhin na ilagay ang Authorized Representative  ng Member");
            authRepLname.requestFocus();
            return "Siguraduhin na ilagay ang Authorized Representative  ng Member";
        }
        if (authRepLNames.startsWith("-")||authRepLNames.startsWith(".") ||
                authRepLNames.endsWith("-") || authRepLNames.endsWith(".")) {

            authRepLname.setError("Ang Authorized Representative ay hindi maaaring magsimula o magtapos sa '-' o '.'");
            authRepLname.requestFocus();
            return "Ang Authorized Representative ay hindi maaaring magsimula o magtapos sa '-' o '.'";
        }


        if (relToMemberxxx.isEmpty()){
            relToMemberX.setError("Siguraduhin na ilagay ang Relasyon sa Miyembro ng Member");
            relToMemberX.requestFocus();
            return "Siguraduhin na ilagay ang Relasyon sa Miyembro ng Member";
        }
        if (relToMemberxxx.startsWith("-")||relToMemberxxx.startsWith(".") ||
                relToMemberxxx.endsWith("-") || relToMemberxxx.endsWith(".")) {
            relToMemberX.setError("Ang Relasyon sa Miyembro ay hindi maaaring magsimula o magtapos sa '-' o '.'");
            relToMemberX.requestFocus();

            return "Ang Relasyon sa Miyembro ay hindi maaaring magsimula o magtapos sa '-' o '.'";
        }
        //TOdo add the following Nationality province municipality barangay Street

        if(!isImage1Valid){
            return "Image for RS1 is invalid";
        }

        if (policy.getPoc().isEmpty() || policy.getPoc().length() < 7){
            capturedRS1.setError("Siguraduhin na ilagay ang Reference No. ng Member.");
            capturedRS1.requestFocus();
            return "Siguraduhin na ilagay ang Reference No. ng Member.";
        }

        if (pocMatched.isChecked() && !capturedRS1.getText().toString().equalsIgnoreCase(pocNumber.getText().toString())) {
            pocNumber.setError("Siguraduhin na magkaparehas ang reference number at capture poc from RS#1");
            pocNumber.requestFocus();
            capturedRS1.setError("Siguraduhin na magkaparehas ang reference number at capture poc from RS#1");
            capturedRS1.requestFocus();

            return "Siguraduhin na magkaparehas ang reference number at capture poc from RS#1";
        }


        if (policy.getProduct1() == null && policy.getProduct2() == null &&policy.getProduct3() == null ){
            //checkKabuklod.requestFocus();
            return "Please select product.";
        }

        if (databaseHelper.isPocExists(policy.getPoc(), policy.getProduct1(), policy.getProduct2(), policy.getProduct3())){
            pocNumber.setError("POM No. ay nakarehistro na.");
            pocNumber.requestFocus();
            return "POM No. ay nakarehistro na.";
        }

        if (policy.getEffdate().isEmpty()){
            effectivityDate.setError("Siguraduhin na ilagay ang Effectivity Date ng Member.");
            effectivityDate.requestFocus();
            return "Siguraduhin na ilagay ang Effectivity Date ng Member.";//"Effectivity date is required.";
        }

        if (!pocMatched.isChecked()){
            if (!isImage1Valid){
                return "Image for RS1 is invalid";
            }
        }

//        if (policy.getPoc() != capturedRS1.getText().toString()){
//            return "Siguraduhin na magkaparehas ang reference number at capture .";
//        }


        //DELETE THIS IF THEY want is ref no is not the same with the captured pom
//        if (!pocMatched.isChecked()){
//            return "Siguraduhing may check ang Reference No. is correct.";
//        }

        if (!dpaTag.isChecked()){
            return "Siguraduhing i-check ang Data Privacy Consent";
        }

        int userAge = Integer.parseInt(age.getText().toString());

        if (userAge >= 66 && userAge <= 70 && statusselected.equalsIgnoreCase("NEW")){

            return "Maaring Renewal ang gamitin sa iyong Status";
        }


        if (userAge >= 70){
            return "Ang iyong edad na " + userAge + " ay lagpas na ";
        }




        return null;
    }

    @Override
    public void onItemClick(MemberInfo memberInfo) {
        String sss = memberInfo.getFname() + " " + memberInfo.getLname();
        Intent intent = new Intent(RegisterActivity.this, MemberActivity.class);
        intent.putExtra("key", memberInfo.getId().toString());
        intent.putExtra("firstname", memberInfo.getFname());
        intent.putExtra("middlename", memberInfo.getMname());
        intent.putExtra("lastname", memberInfo.getLname());
        intent.putExtra("mobileno", memberInfo.getMobileno());
        intent.putExtra("dateofbirth", memberInfo.getDob());
        intent.putExtra("reltype", memberInfo.getReltype());

        intent.putExtra("suffix", memberInfo.getSuffix());
        intent.putExtra("gender", memberInfo.getGender());
        intent.putExtra("status", memberInfo.getStatus());
        intent.putExtra("cardmember", memberInfo.getCardmember());

        String sagipType = "0";
//        if (sagipIndividual.isChecked()){
//            sagipType = sagipIndividualCode;
//        } else if (sagipFamily.isChecked()){
//            sagipType = sagipFamilyCode;
//        } else if (sagipPlatinum.isChecked()){
//            sagipType = sagipPlatinumCode;
//        }
        intent.putExtra("sagipType", sagipType);
        intent.putExtra("regstatus", statusselected);
        addDependentResultLauncher.launch(intent);

    }


    final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                Toast.makeText(RegisterActivity.this, "Permission granted.",Toast.LENGTH_LONG).show();
            }
        }
    });

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

//            Snackbar snackbar = Snackbar.make(dependentLinearLayout, "Dependent deleted.", Snackbar.LENGTH_LONG);
//            snackbar.show();
//
//            members.remove(viewHolder.getAdapterPosition());
//            customAdapter.notifyDataSetChanged();

            showConfim(viewHolder.getAdapterPosition());
        }
    };



    public void showConfim(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm delete");
        String message = "Are you sure to delete this record?";
        builder.setMessage(message);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar snackbar = Snackbar.make(dependentLinearLayout, "Dependent deleted.", Snackbar.LENGTH_LONG);
                snackbar.show();
                members.remove(position);
                customAdapter.notifyDataSetChanged();
                setTotalPremium();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                customAdapter.notifyDataSetChanged();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    //ToDo : province setup


    //TODO: Create for CITY setup
    List<String> cityList;
    public  void setCity(String province){

      cityList =  databaseHelper.getCities(province);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this ,R.layout.list_item , cityList);
        city.setAdapter(adapter);
    }


    //TODO : Create for BARANGAY setup
    List<String> brgyList;
    public  void setBrgy(String city , String province){
        brgyList = databaseHelper.getBarangays(province,city);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.list_item , brgyList);
        brgy.setAdapter(adapter);
    }

}