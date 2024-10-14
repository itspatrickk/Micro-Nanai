package com.pioneer.microhmo;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.text.TextRecognizer;
import com.pioneer.microhmo.objects.MemberInfo;
import com.pioneer.microhmo.objects.PolicyInfo;
import com.pioneer.microhmo.objects.Transaction;
import com.pioneer.microhmo.util.CommonUtil;
import com.pioneer.microhmo.util.SharedPreferencesUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class RegisterCommon extends AppCompatActivity{

    private String filename = "SampleFile.txt";
    private String filepath = "MyFileStorage";
    long fileSizeLimit = 1 * 1024 * 1024; //1mb
    TextRecognizer textRecognizer;

    String premium = "";
    String cardCareCode = "CC";
    String kabuklodCode = "KP";
    String sagipFamilyCode = "SF";
    String sagipIndividualCode = "SI";
    String sagipPlatinumCode= "SP";

    TextView capturedRS1 ;
    TextView capturedRS2 ;
    public static String suffixlist[] = {"", "SR", "JR", "II", "III", "IV", "V"};
    public static String relList[] = {"SPOUSE","CHILD" , "PARENTS" , "GRANDPARENTS" , "OTHERS"};
    public static String institution[] = {"CARD BANK INC." , "CARD SME BANK INC." , "CARD RIZAL BANK"};
    AutoCompleteTextView suffix , province , city , brgy , relToMemberX , institutions;
    RadioGroup radioStatus ;
    RadioGroup radioPaymentMode,gender, civilstatus, cardmember , radioGroupClientType;

//    RadioGroup radioSagip;
//    CheckBox checkCardCare, checkSagip, checkKabuklod, dpaTag,pocMatched, checkCardCarex, checkSagipx, checkKabuklodx;
//    RadioButton radioCash ,radioLoan,sagipIndividual,sagipFamily, sagipPlatinum,newRadio,renewalRadio;;

    CheckBox dpaTag , pocMatched;
    RadioButton radioCash , newRadio , radioLoan , renewalRadio , radioCardPrincipal , radioNonCardPrincipal , radioNonCardSpouse ,radioCardChild ;
    RadioButton genderMale, civilstatusSingle, cardmemberYes,genderFemale, civilstatusMarried, cardmemberNo;
    TextView effectivityDate, totalPremium;
    TextView dateOfBirth, age;
    TextView messageView;

    Button page1CameraButton,page2CameraButton,addDependent, savePolicy;
//    ImageView page1ImageView,page2ImageView;

    EditText firstname, middlename,lastname, mobileno,pocNumber, acctOfficer , unitManager , Center , authRep ,relToMember , edtStreet , email , nationality , placeofBirth;
    //Spinner province ;

    Boolean isSagip = false;

    LinearLayout sagipDiv;

    Transaction transaction;
    List<MemberInfo> members = new ArrayList<MemberInfo>();

    RecyclerView recyclerView;

    public static int REQUEST_CODE_ADD_MEMBER = 1000;
    public static int REQUEST_CODE_UPDATE_MEMBER = 1001;


    DatabaseHelper databaseHelper;
//    MemberAdapter adapter;
    PolicyInfo policy;

    Boolean isSpouseSelected = false;

    String genderselected,civilstatusselected,membershipselected, statusselected = "";

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    ExecutorService cameraExecutor;
    SharedPreferences sharedPreferences;

            File backphoto;
    File frontphoto;


    void setListeners(){

        switchCardCare(false);
        switchKabuklod(false);
        switchSagip(false);

//        checkCardCarex, checkSagipx, checkKabuklodx
//        checkCardCarex.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAlert("Info", "Hindi pasok ang edad ng prinsipal sa produktong nais piliin");
//                checkKabuklodx.setChecked(false);
//                checkSagipx.setChecked(false);
//                checkCardCarex.setChecked(false);
//            }
//        });

//        checkSagipx.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAlert("Info", "Hindi pasok ang edad ng prinsipal sa produktong nais piliin");
//                checkKabuklodx.setChecked(false);
//                checkSagipx.setChecked(false);
//                checkCardCarex.setChecked(false);
//            }
//        });

//        checkKabuklodx.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAlert("Info", "Hindi pasok ang edad ng prinsipal sa produktong nais piliin");
//                checkKabuklodx.setChecked(false);
//                checkSagipx.setChecked(false);
//                checkCardCarex.setChecked(false);
//            }
//        });

//        checkCardCare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked) {
//                    checkCardCare.setTextColor(WHITE);
//                    checkCardCare.setBackgroundResource(R.drawable.blue_button);
//                } else {
//                    checkCardCare.setTextColor(BLACK);
//                    checkCardCare.setBackgroundResource(R.drawable.gray_button);
//                }
//                setTotalPremium();
//            }
//        });
        brgy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                brgy.setError(null);
            }
        });
        dpaTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dpaTag.setTextColor(WHITE);
                    dpaTag.setBackgroundResource(R.drawable.blue_button);
                } else {
                    dpaTag.setTextColor(BLACK);
                    dpaTag.setBackgroundResource(R.drawable.gray_button);
                }
            }
        });
        pocMatched.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pocMatched.setTextColor(WHITE);
                    pocMatched.setBackgroundResource(R.drawable.blue_button);
                } else {
                    pocMatched.setTextColor(BLACK);
                    pocMatched.setBackgroundResource(R.drawable.gray_button);
                }
            }
        });


//        checkKabuklod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    checkKabuklod.setTextColor(WHITE);
//                    checkKabuklod.setBackgroundResource(R.drawable.blue_button);
//                } else {
//                    checkKabuklod.setTextColor(BLACK);
//                    checkKabuklod.setBackgroundResource(R.drawable.gray_button);
//                }
//                setTotalPremium();
//            }
//        });
//        checkSagip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    isSagip = true;
//                    checkSagip.setTextColor(WHITE);
//                    checkSagip.setBackgroundResource(R.drawable.blue_button);
//                    radioSagip.setVisibility(View.VISIBLE);
//                    sagipDiv.setVisibility(View.VISIBLE);
//                } else {
//                    checkSagip.setTextColor(BLACK);
//                    checkSagip.setBackgroundResource(R.drawable.gray_button);
//
////                    sagipIndividual.setChecked(true);
//
//                    radioSagip.setVisibility(View.GONE);
//                    sagipDiv.setVisibility(View.GONE);
//                }
//                setTotalPremium();
//            }
//        });

        //newRadio.setChecked(true);
        radioPaymentMode.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        System.out.println("checkedId : " + checkedId);
                        if (checkedId == R.id.radioPaymentModeCash){
                            radioCash.setTextColor(WHITE);
                            radioCash.setBackgroundResource(R.drawable.blue_button);
                            radioLoan.setTextColor(BLACK);
                            radioLoan.setBackgroundResource(R.drawable.gray_button);
                        } else if (checkedId == R.id.radioPaymentModeLoan){
                            radioLoan.setTextColor(WHITE);
                            radioLoan.setBackgroundResource(R.drawable.blue_button);
                            radioCash.setTextColor(BLACK);
                            radioCash.setBackgroundResource(R.drawable.gray_button);
                        }
                    }
                }
        );

        // radioCash = findViewById(R.id.radioPaymentModeCash);
        //Todo FOR CLIENT TYPE radioCardPrincipal , radioNonCardPrincipal , radioNonCardSpouse ,radioCardChild
        radioGroupClientType.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        Log.d("CHECK PRINCIPAL " , checkedId+"");
                        radioCardChild.setError(null);
                        radioCardPrincipal.setError(null);
                        radioNonCardPrincipal.setError(null);
                        radioNonCardSpouse.setError(null);

                        if (checkedId == R.id.radioCardPrincipal) {
                            radioCardPrincipal.setTextColor(WHITE);
                            radioCardPrincipal.setBackgroundResource(R.drawable.blue_button);
                            radioNonCardPrincipal.setTextColor(BLACK);
                            radioNonCardPrincipal.setBackgroundResource(R.drawable.gray_button);
                            radioNonCardSpouse.setTextColor(BLACK);
                            radioNonCardSpouse.setBackgroundResource(R.drawable.gray_button);
                            radioCardChild.setTextColor(BLACK);
                            radioCardChild.setBackgroundResource(R.drawable.gray_button);


                        } else if (checkedId == R.id.radioNonCardPrincipal) {
                            radioNonCardPrincipal.setTextColor(WHITE);
                            radioNonCardPrincipal.setBackgroundResource(R.drawable.blue_button);
                            radioCardPrincipal.setTextColor(BLACK);
                            radioCardPrincipal.setBackgroundResource(R.drawable.gray_button);
                            radioNonCardSpouse.setTextColor(BLACK);
                            radioNonCardSpouse.setBackgroundResource(R.drawable.gray_button);
                            radioCardChild.setTextColor(BLACK);
                            radioCardChild.setBackgroundResource(R.drawable.gray_button);
                        } else if (checkedId == R.id.radioNonCardSpouse) {
                            radioNonCardSpouse.setTextColor(WHITE);
                            radioNonCardSpouse.setBackgroundResource(R.drawable.blue_button);
                            radioCardPrincipal.setTextColor(BLACK);
                            radioCardPrincipal.setBackgroundResource(R.drawable.gray_button);
                            radioNonCardPrincipal.setTextColor(BLACK);
                            radioNonCardPrincipal.setBackgroundResource(R.drawable.gray_button);
                            radioCardChild.setTextColor(BLACK);
                            radioCardChild.setBackgroundResource(R.drawable.gray_button);
                        } else if (checkedId == R.id.radioCardChild) {
                            radioCardChild.setTextColor(WHITE);
                            radioCardChild.setBackgroundResource(R.drawable.blue_button);
                            radioCardPrincipal.setTextColor(BLACK);
                            radioCardPrincipal.setBackgroundResource(R.drawable.gray_button);
                            radioNonCardPrincipal.setTextColor(BLACK);
                            radioNonCardPrincipal.setBackgroundResource(R.drawable.gray_button);
                            radioNonCardSpouse.setTextColor(BLACK);
                            radioNonCardSpouse.setBackgroundResource(R.drawable.gray_button);
                        }
                    }
                }
        );



//        radioSagip.setOnCheckedChangeListener(
//                new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                        System.out.println("checkedId : " + checkedId);
//                        if (checkedId == R.id.radioSagipIndividual){
//                            setSagipCheck(sagipIndividual, sagipFamily,sagipPlatinum);
//                        } else if (checkedId == R.id.radioSagipFamily){
//                            if (civilstatusSingle.isChecked()){
//                                showAlert("Selected civil status is SINGLE and not valid for this option.");
//                                setSagipCheck(sagipIndividual, sagipFamily,sagipPlatinum);
//                            }else{
//                                setSagipCheck(sagipFamily, sagipIndividual,sagipPlatinum);
//                            }
//
//                        }  else if (checkedId == R.id.radioSagipPlatinum){
//                            setSagipCheck(sagipPlatinum,sagipIndividual,sagipFamily);
//                        }
//                        setTotalPremium();
//                    }
//                }
//        );

        gender.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        System.out.println("checkedId : " + checkedId);
                        if (checkedId == R.id.genderLalakiPrincipal){
                            genderselected = "Lalaki";
                            genderMale.setTextColor(WHITE);
                            genderMale.setBackgroundResource(R.drawable.blue_button);
                            genderFemale.setTextColor(BLACK);
                            genderFemale.setBackgroundResource(R.drawable.gray_button);
                        } else if (checkedId == R.id.genderBabaePrincipal){
                            genderselected = "Babae";
                            genderFemale.setTextColor(WHITE);
                            genderFemale.setBackgroundResource(R.drawable.blue_button);
                            genderMale.setTextColor(BLACK);
                            genderMale.setBackgroundResource(R.drawable.gray_button);
                        }
                    }
                }
        );



        civilstatus.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        System.out.println("checkedId : " + checkedId);
                        civilstatusMarried.setError(null);
                        civilstatusSingle.setError(null);
                        if (checkedId == R.id.civilstatSingle){
                            civilstatusselected = "Single";
                            civilstatusSingle.setTextColor(WHITE);
                            civilstatusSingle.setBackgroundResource(R.drawable.blue_button);
                            civilstatusMarried.setTextColor(BLACK);
                            civilstatusMarried.setBackgroundResource(R.drawable.gray_button);
                        } else if (checkedId == R.id.civilstatMarried){
                            civilstatusselected = "Married";
                            civilstatusMarried.setTextColor(WHITE);
                            civilstatusMarried.setBackgroundResource(R.drawable.blue_button);
                            civilstatusSingle.setTextColor(BLACK);
                            civilstatusSingle.setBackgroundResource(R.drawable.gray_button);
                        }

//                        if (sagipFamily.isChecked()){
//                            sagipFamily.setChecked(false);
//                            sagipIndividual.setChecked(true);
//                            setSagipCheck(sagipIndividual, sagipFamily, sagipPlatinum);
//                        }
                    }
                }
        );


//        cardmember.setOnCheckedChangeListener(
//                new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup group, int checkedId) {
//                        System.out.println("checkedId : " + checkedId);
//                        if (checkedId == R.id.membershipYesPrincipal){
//                            membershipselected = "Yes";
//                            cardmemberYes.setTextColor(WHITE);
//                            cardmemberYes.setBackgroundResource(R.drawable.blue_button);
//                            cardmemberNo.setTextColor(BLACK);
//                            cardmemberNo.setBackgroundResource(R.drawable.gray_button);
//                        } else if (checkedId == R.id.membershipNoPrincipal){
//                            membershipselected = "No";
//                            cardmemberNo.setTextColor(WHITE);
//                            cardmemberNo.setBackgroundResource(R.drawable.blue_button);
//                            cardmemberYes.setTextColor(BLACK);
//                            cardmemberYes.setBackgroundResource(R.drawable.gray_button);
//                        }
//                    }
//                }
//        );


        firstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        middlename.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        lastname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        acctOfficer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                hideKeyboard(view);
            }
        });
        unitManager.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                hideKeyboard(view);
            }
        });
        Center.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                hideKeyboard(view);
            }
        });
        authRep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                hideKeyboard(view);
            }
        });
        relToMemberX.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                hideKeyboard(view);
            }
        });
        institutions.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                hideKeyboard(view);
            }
        });


//        relToMember.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                hideKeyboard(view);
//            }
//        });

        mobileno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        suffix.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, suffixlist));
        suffix.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });

        relToMemberX.setAdapter(new ArrayAdapter<String>(this,R.layout.list_item,relList));
        relToMemberX.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                relToMemberX.setError(null);
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });
        institutions.setAdapter(new ArrayAdapter<String>(this,R.layout.list_item,institution));
        institutions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                institutions.setError(null);
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });
    }


    public void switchColor(RadioButton a, RadioButton b){
            a.setTextColor(WHITE);
            a.setBackgroundResource(R.drawable.blue_button);
            b.setTextColor(BLACK);
            b.setBackgroundResource(R.drawable.gray_button);
    }


    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    void setComponents(){
        messageView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerview);
        radioStatus = findViewById(R.id.radioStatus);
        radioPaymentMode = findViewById(R.id.radioPaymentMode);
//        radioSagip = findViewById(R.id.radioSagip);
//        checkCardCare = findViewById(R.id.checkCardCare);
//        checkSagip = findViewById(R.id.checkSagip);
//        checkKabuklod = findViewById(R.id.checkKabuklod);
//
//        checkCardCarex = findViewById(R.id.checkCardCarex);
//        checkSagipx = findViewById(R.id.checkSagipx);
//        checkKabuklodx = findViewById(R.id.checkKabuklodx);
// radioCardPrincipal , radioNonCardPrincipal , radioNonCardSpouse ,radioCardChild

        radioCardPrincipal = findViewById(R.id.radioCardPrincipal);
        radioNonCardPrincipal = findViewById(R.id.radioNonCardPrincipal);
        radioNonCardSpouse = findViewById(R.id.radioNonCardSpouse);
        radioCardChild = findViewById(R.id.radioCardChild);
        newRadio = findViewById(R.id.radioStatusNew);
        renewalRadio = findViewById(R.id.radioStatusRenewal);
        radioCash = findViewById(R.id.radioPaymentModeCash);
        radioGroupClientType = findViewById(R.id.radioGroupClientType);
        radioLoan = findViewById(R.id.radioPaymentModeLoan);
        genderMale = findViewById(R.id.genderLalakiPrincipal);
        genderFemale = findViewById(R.id.genderBabaePrincipal);
        civilstatusSingle = findViewById(R.id.civilstatSingle);
        civilstatusMarried = findViewById(R.id.civilstatMarried);
//        cardmemberYes = findViewById(R.id.membershipYesPrincipal);
//        cardmemberNo = findViewById(R.id.membershipNoPrincipal);
//        sagipIndividual = findViewById(R.id.radioSagipIndividual);
//        sagipFamily = findViewById(R.id.radioSagipFamily);
//        sagipPlatinum = findViewById(R.id.radioSagipPlatinum);
        page1CameraButton = findViewById(R.id.page1Camera_button);
        firstname = findViewById(R.id.firstname);
        dateOfBirth = findViewById(R.id.dob);
        middlename = findViewById(R.id.middlename);
        lastname = findViewById(R.id.lastname);
        suffix = findViewById(R.id.suffix);
        mobileno = findViewById(R.id.mobileNo);
        pocNumber = findViewById(R.id.poc_number);
        relToMemberX = findViewById(R.id.relToMember);
        institutions = findViewById(R.id.institution);
        acctOfficer = findViewById(R.id.edtAccountOfficer);
        unitManager = findViewById(R.id.edtUnitManager);
        Center = findViewById(R.id.edtCenter);
        authRep = findViewById(R.id.edtAuthRelative);
      //  relToMember = findViewById(R.id.edtRelToMember);

        email = findViewById(R.id.email);
        placeofBirth = findViewById(R.id.placeOfBirth);
        nationality = findViewById(R.id.nationality);

//        addDependent = findViewById(R.id.addDependent);
//        sagipDiv = findViewById(R.id.sagipDiv);
    //    page2CameraButton = findViewById(R.id.page2Camera_button);
//        page2ImageView = findViewById(R.id.page2_image);
//        page1ImageView = findViewById(R.id.page1_image);

        age = findViewById(R.id.age);
        effectivityDate = findViewById(R.id.effectivityDate);

        civilstatus = findViewById(R.id.civilstatRadioPrincipal);
//        cardmember = findViewById(R.id.membershipRadioPrincipal);
        gender = findViewById(R.id.genderRadioPrincipal);

//        totalPremium = findViewById(R.id.totalPremium);
        dpaTag = findViewById(R.id.dpaTag);
        pocMatched = findViewById(R.id.pocMatched);

        savePolicy = findViewById(R.id.savePolicy);;

        province = findViewById(R.id.province);
        city = findViewById(R.id.city);
        brgy = findViewById(R.id.brgy);
        edtStreet = findViewById(R.id.edtStreet);

//        addDependent.setVisibility(View.GONE);

        final String blockCharacterSet = "1234567890,;:<>~`!@#$%^&*()_+=\"'/|\\";
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            String chr = source + "";

//            for (int i = 0; i < blockCharacterSet.length(); i++) {
//                chr = chr.replace((""+blockCharacterSet.charAt(i)),"");
//            }
            if(chr.length() > 1) {
                chr = chr.substring((chr.length() - 1));
            }
            Log.d("chr", chr);
            if (source != null && blockCharacterSet.contains(chr)  ) {
                return "";
            }
            return null;
        };



        InputFilter filterFor = (source , start , end , dest , dstart , dend ) ->{
            String chr = source.toString();
            for (int i = 0; i < chr.length(); i++) {
                if (blockCharacterSet.contains(String.valueOf(chr.charAt(i)))) {
                    return "";
                }
            }

            return null;
        };


        InputFilter[] allCapsFilter = new InputFilter[] {new InputFilter.AllCaps(),filter, new InputFilter.LengthFilter(50) };
        InputFilter[] middle = new InputFilter[] {new InputFilter.AllCaps(),filter, new InputFilter.LengthFilter(5) };

        InputFilter[] allCaps = new InputFilter[]{new InputFilter.AllCaps(),filterFor};

        InputFilter[] allCAps = new InputFilter[]{new InputFilter.AllCaps()};
        firstname.setFilters(allCapsFilter);
        middlename.setFilters(middle);
        lastname.setFilters(allCapsFilter);
        suffix.setFilters(allCapsFilter);
        relToMemberX.setFilters(allCapsFilter);
        institutions.setFilters(allCapsFilter);
        acctOfficer.setFilters(allCaps);
        unitManager.setFilters(allCaps);
        Center.setFilters(allCaps);
        authRep.setFilters(allCaps);
//        relToMember.setFilters(allCaps);
        province.setFilters(allCAps);
        city.setFilters(allCAps);
        brgy.setFilters(allCAps);
        edtStreet.setFilters(allCAps);
        nationality.setFilters(allCaps);
        placeofBirth.setFilters(allCAps);
        email.setFilters(allCAps);

        addTextWatcher(acctOfficer);
        addTextWatcher(unitManager);
        addTextWatcher(Center);
        addTextWatcher(authRep);
//        addTextWatcher(relToMember);

//        pocNumber.setFilters(allCapsFilter);


        InputFilter[] length = new InputFilter[] {new InputFilter.LengthFilter(9)};
        mobileno.setFilters(length);

        kabuklodPrem = SharedPreferencesUtility.getProductPrem(sharedPreferences, kabuklodCode);
        cardCarePrem = SharedPreferencesUtility.getProductPrem(sharedPreferences, cardCareCode);
        sagipIndividualPrem = SharedPreferencesUtility.getProductPrem(sharedPreferences, sagipIndividualCode);
        sagipFamilyPrem = SharedPreferencesUtility.getProductPrem(sharedPreferences, sagipFamilyCode);
        sagipPlatinumPrem = SharedPreferencesUtility.getProductPrem(sharedPreferences, sagipPlatinumCode);

    }

    private  void  addTextWatcher(EditText editText ){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public  final String invalidName = "INVALID NAME";
    private  void  validateText(EditText editText){
        String text = editText.getText().toString();

        if (text.startsWith("-") || text.startsWith(".") || text.endsWith("-") || text.endsWith(".")){
            editText.setText(invalidName);
            editText.setSelection(invalidName.length());
        }
    }

    //TODO AGE COMP

    public void setAge(){
        try {
            Date birthDate = dateFormat.parse(dateOfBirth.getText().toString());
            Date effDate = null;
            try {
                effDate = dateFormat.parse(effectivityDate.getText().toString());
            }catch (Exception ee){
                ee.printStackTrace();
                if (effDate == null) effDate = new Date();
            }
            int attAge = CommonUtil.getAge(birthDate,effDate);
            Log.d("attAge", "attAge : " + attAge);
            age.setText(""+attAge);
        }catch (Exception e){

        }

    }

    File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

//        File backphoto = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");


//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOCUMENTS), "Nanai");

        File mediaStorageDir = new File(getExternalFilesDir(filepath), "Nanai");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Nanai", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }

    File getCompressedMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

//        File backphoto = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");


//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOCUMENTS), "Nanai_compressed");


        File mediaStorageDir = new File(getExternalFilesDir(filepath), "Nanai-compressed");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Nanai", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        Log.d("nanai", mediaFile.getPath());

        return mediaFile;
    }

    void setSagipCheck(RadioButton radio, RadioButton radio1, RadioButton radio2) {

        if (isSagip) {
            addDependent.setVisibility(View.VISIBLE);
//            checkSagip.setTextColor(WHITE);
//            checkSagip.setBackgroundResource(R.drawable.blue_button);
//            checkSagip.setChecked(true);

            radio.setChecked(true);
            radio.setTextColor(WHITE);
            radio.setBackgroundResource(R.drawable.blue_button);

            radio1.setChecked(false);
            radio2.setChecked(false);
            radio1.setTextColor(BLACK);
            radio1.setBackgroundResource(R.drawable.gray_button);
            radio2.setTextColor(BLACK);
            radio2.setBackgroundResource(R.drawable.gray_button);

        }else{
            addDependent.setVisibility(View.GONE);
            radio.setTextColor(BLACK);
            radio.setBackgroundResource(R.drawable.gray_button);
            radio1.setTextColor(BLACK);
            radio1.setBackgroundResource(R.drawable.gray_button);
            radio2.setTextColor(BLACK);
            radio2.setBackgroundResource(R.drawable.gray_button);

            radio.setChecked(false);
            radio1.setChecked(false);
            radio2.setChecked(false);
        }
    }
    int maxSize = 250000;

    String saveImage(Context context, String imagePath){
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        if (bitmap == null) return null;
        // Compress the bitmap to reduce its file size
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);

        // Check if the compressed file size is less than or equal to the maximum size
        if (byteArrayOutputStream.size() > maxSize) {
            // If the file size is greater than the maximum size, reduce the JPEG quality
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        }
        if (byteArrayOutputStream.size() > maxSize) {
            // If the file size is greater than the maximum size, reduce the JPEG quality
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        }
        if (byteArrayOutputStream.size() > maxSize) {
            // If the file size is greater than the maximum size, reduce the JPEG quality
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        }
        if (byteArrayOutputStream.size() > maxSize) {
            // If the file size is greater than the maximum size, reduce the JPEG quality
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        }
        if (byteArrayOutputStream.size() > maxSize) {
            // If the file size is greater than the maximum size, reduce the JPEG quality
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        }

        // Save the compressed bitmap to a file
//        File file = createImageFile();
        File file = getCompressedMediaFile();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
            //Toast.makeText(context, file.getAbsolutePath(),Toast.LENGTH_LONG).show();
        }catch (Exception e){
            //Toast.makeText(context, e.getMessage(),Toast.LENGTH_LONG).show();
        }

        new File(imagePath).delete();

        return file.getAbsolutePath();

    }



//Todo add new input fields for getter and setter
    void setTransaction(String agentid, String unit){
        if (members == null) members =new ArrayList<MemberInfo>();
        transaction = new Transaction();
        transaction.setAgentid(agentid);

        PolicyInfo policy = new PolicyInfo();
//        policy.setCenter(center);
        policy.setProduct1("MI CARE");
        policy.setUnit(unit);
        policy.setPoc(pocNumber.getText().toString().trim());
//        if (checkCardCare.isChecked()){
//            policy.setProduct1("CARD CARE PLUS");
//        } else{
//            policy.setProduct1(null);
//        }
//        if (checkKabuklod.isChecked()){
//            policy.setProduct2("KABUKLOD PLAN");
//        }else{
//            policy.setProduct2(null);
//        }
//        if (checkSagip.isChecked()){
//            if (sagipFamily.isChecked()){
//                policy.setProduct3("SAGIP PLAN FAMILY");
//            } else if (sagipIndividual.isChecked()){
//                policy.setProduct3("SAGIP PLAN INDIVIDUAL");
//            } else  if (sagipPlatinum.isChecked()){
//                policy.setProduct3("SAGIP PLAN PLATINUM");
//            } else{
//                policy.setProduct3(null);
//            }
//        } else {
//            policy.setProduct3(null);
//        }

        //IF THERE ANOTHER PRODUCT ADD HERE



        if (radioCash.isChecked()){
            policy.setPaymode("Cash");
        }else{
            policy.setPaymode("Loan");
        }

//        if (radioCardPrincipal)


        String effectiveDate = effectivityDate.getText().toString();

        String expDate = getExpDate(effectiveDate);
        // ToDo compute Exp date
        policy.setMistat(statusselected);
        policy.setEffdate(effectivityDate.getText().toString());
       // Log.d("exp Date : " , expDate);
        policy.setExpDate(expDate);

        transaction.setPolicy(policy);

//        int userAge = Integer.parseInt(age.getText().toString());
//
//        String setMember = "";
//        if (userAge >= 65 && userAge < 70){
//            setMember = "RENEWAL";
//        } else if (userAge < 65 && userAge >= 0) {
//            setMember = "NEW";
//        }


        MemberInfo principal = new MemberInfo();

       // principal.setCardmember(setMember);
        principal.setPertype("PRINCIPAL");
        principal.setGender(genderselected);
        principal.setCardmember(membershipselected);
       // principal.setCivilstat(civilstatusselected);
        principal.setDob(dateOfBirth.getText().toString());

        principal.setFname(firstname.getText().toString().trim());
        principal.setMname(middlename.getText().toString().trim());
        principal.setLname(lastname.getText().toString().trim());
        principal.setSuffix(suffix.getText().toString().trim());
        principal.setMobileno("09"+mobileno.getText().toString().trim());
        principal.setAcctOfficer(acctOfficer.getText().toString());
        principal.setUnitManager(unitManager.getText().toString());
        principal.setCenter(Center.getText().toString());
        principal.setAuthRepresentative(authRep.getText().toString());
        principal.setRelToMember(relToMemberX.getText().toString());
        principal.setInstitution(institutions.getText().toString());
        principal.setProv(province.getText().toString());
        principal.setCity(city.getText().toString());
        principal.setBrgy(brgy.getText().toString());
        principal.setStreet(edtStreet.getText().toString());
        principal.setNationality(nationality.getText().toString());
        principal.setEmail(email.getText().toString());
        principal.setPlaceOfBirth(placeofBirth.getText().toString());
        transaction.setPrincipal(principal);
        transaction.setMembers(members);

        if (civilstatusSingle.isChecked()){
            principal.setCivilstat("SINGLE");
        } else if (civilstatusMarried.isChecked()) {
            principal.setCivilstat("MARRIED");
        }else {
            principal.setCivilstat("");
        }

        if (radioCardPrincipal.isChecked()) {
            principal.setClientType("CARD PRINCIPAL");
        } else if (radioNonCardPrincipal.isChecked()) {
            principal.setClientType("NON CARD PRINCIPAL");
        } else if (radioNonCardSpouse.isChecked()){
            principal.setClientType("NON CARD SPOUSE");
        }else if (radioCardChild.isChecked()) {
            principal.setClientType("NON CARD CHILD");
        }else {
            principal.setClientType("");
        }

    }
    public String getExpDate(String effectivityDateStr) {
        try {
            // Define the date format as "MM/dd/yyyy" to match your input date layout
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // Example: "09/12/2024"

            // Parse the effectivity date
            Date effectivityDate = sdf.parse(effectivityDateStr);

            // Use Calendar to add one year
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(effectivityDate);
            calendar.add(Calendar.YEAR, 1);  // Add 1 year to the effectivity date

            // Get the expiration date and convert it back to string format
            Date expirationDate = calendar.getTime();
            return sdf.format(expirationDate);  // Return the expiration date as a string

        } catch (Exception e) {
            e.printStackTrace();  // Handle the error if date parsing fails
            return null;  // Return null or handle the error accordingly
        }
    }

    public PolicyInfo getPolicyInfo() {
        PolicyInfo policy = new PolicyInfo();


        policy.setProduct1("MI CARE");
        policy.setProduct2(null);
        policy.setProduct3(null);
//        if (checkCardCare.isChecked()) {
//            policy.setProduct1("CARD CARE PLUS");
//        }else{
//            policy.setProduct1(null);
//        }
//        if (checkKabuklod.isChecked()) {
//            policy.setProduct2("KABUKLOD PLAN");
//        }else{
//            policy.setProduct2(null);
//        }
//        if (checkSagip.isChecked()) {
//            if (sagipFamily.isChecked()) {
//                policy.setProduct3("SAGIP PLAN FAMILY");
//            } else if (sagipIndividual.isChecked()) {
//                policy.setProduct3("SAGIP PLAN INDIVIDUAL");
//            } else if (sagipPlatinum.isChecked()) {
//                policy.setProduct3("SAGIP PLAN PLATINUM");
//            }else{
//                policy.setProduct3(null);
//            }
//        }else{
//            policy.setProduct3(null);
//        }
        return policy;
    }

    void switchCardCare(Boolean enable){
//        if (enable) {
//            checkCardCarex.setVisibility(View.GONE);
//            checkCardCare.setVisibility(View.VISIBLE);
//        }else{
//            checkCardCare.setVisibility(View.GONE);
//            checkCardCarex.setVisibility(View.VISIBLE);
//        }
    }

    void switchSagip(Boolean enable){
//        if (enable) {
//            checkSagipx.setVisibility(View.GONE);
//            checkSagip.setVisibility(View.VISIBLE);
//        }else{
//            checkSagip.setVisibility(View.GONE);
//            checkSagipx.setVisibility(View.VISIBLE);
//        }
    }

    void switchKabuklod(Boolean enable){
//        if (enable) {
//            checkKabuklodx.setVisibility(View.GONE);
//            checkKabuklod.setVisibility(View.VISIBLE);
//        }else{
//            checkKabuklod.setVisibility(View.GONE);
//            checkKabuklodx.setVisibility(View.VISIBLE);
//        }
    }

    private String getBase64Image(String newpath){
//        File file = new File(newpath);
//        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//        ByteArrayOutputStream stream=new ByteArrayOutputStream();
//        // compress Bitmap
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
//        // Initialize byte array
//        byte[] bytes=stream.toByteArray();
//        // get base64 encoded string
//        String sImage= Base64.encodeToString(bytes,Base64.DEFAULT);
//        file.delete();
//        newpath = newpath.replace(".jpg", ".txt");
//        File gpxfile = new File(newpath);
//        try{
//            FileWriter writer = new FileWriter(gpxfile);
//            writer.append(sImage);
//            writer.flush();
//            writer.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        return newpath;
    }

    void saveTransation(){
        String agentId = SharedPreferencesUtility.getAgentId(sharedPreferences);
        String androidid = SharedPreferencesUtility.getAndroidId(sharedPreferences);


        Log.d("agentid", "agentId :"  +agentId);
        Log.d("androidid", "androidid :"  +androidid);

        PolicyInfo policy = transaction.getPolicy();
        policy.setAgentid(agentId);
        policy.setAndroidid(androidid);
        String id = policy.getPoc()+"_"+ System.currentTimeMillis()/1000;
        policy.setImage1(getBase64Image(frontphoto.getAbsolutePath()));
        //policy.setImage2(getBase64Image(backphoto.getAbsolutePath()));
        policy.setImage1stat("N");
       // policy.setImage2stat("N");
        policy.setTranstat("N");
        policy.setId(id);
        policy.setPremium("2,300.00");
        databaseHelper.addNewPolicy(policy);

        MemberInfo principal = transaction.getPrincipal();
        principal.setId(policy.getId());
        databaseHelper.addMember(principal, "N");

        if (transaction.getMembers().size() > 0){
            for (MemberInfo member : transaction.getMembers()){
                member.setId(policy.getId());
                member.setPoc(policy.getPoc());

                Log.d("CHECK", member.getClientType().toString());
                databaseHelper.addMember(member, "N");
            }
        }
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public String RS1_LEFT_KEY = "LLXA";
    public String RS1_RIGHT_KEY = "LRXA";

    public String RS2_LEFT_KEY = "LLXB";
    public String RS2_RIGHT_KEY = "LRXB";

    public String REFERENCE_NO = "REFERENCENO";

    Boolean isImage1Valid = false;
    Boolean isImage2Valid = false;

    public Boolean isPocCaptured(String pocno, String raw){

        raw = raw.replaceAll(" ", "");
        raw = raw.replaceAll("\n", "");
        raw = raw.toUpperCase();

        if (raw.contains(pocno)) return true;

        return false;
    }


    public Boolean isRSValid(String pocno, String raw, String leftkey, String rightkey){

        raw = raw.replaceAll(" ", "");
        raw = raw.replaceAll("\n", "");
        raw = raw.toUpperCase();

        Log.d("raw", raw);

        if (raw.contains(leftkey) && raw.contains(rightkey) && raw.contains(pocno)) return true;

        return false;
    }

    void setTotalPremium(){
        Double totalPrem = 2500.00;

//        if (checkKabuklod.isChecked()){
//            totalPrem = totalPrem + kabuklodPrem;
//        }
//        if (checkCardCare.isChecked()){
//            totalPrem = totalPrem + cardCarePrem;
//        }
//
//        if (checkSagip.isChecked()){
//            Boolean isSpouseExist = false;
//            Boolean isFamily = false;
//            if (sagipIndividual.isChecked()){
//                totalPrem = totalPrem + sagipIndividualPrem;
//            }
//            if (sagipFamily.isChecked()){
//                isFamily = true;
//                totalPrem = totalPrem + sagipFamilyPrem;
//            }
//            if (sagipPlatinum.isChecked()){
//                totalPrem = totalPrem + sagipPlatinumPrem;
//            }
//            if (members != null && members.size() > 0){
//                if (!isFamily){
//                    totalPrem = totalPrem + 125.00 * members.size();
//                }else{
//                    for (MemberInfo member:members){
//                        if (member.getReltype() != null && member.getReltype().equalsIgnoreCase("SPOUSE")){
//                            isSpouseExist = true;
//                            break;
//                        }
//                    }
//                    if (isSpouseExist) {
//                        if (members.size() >= 3) {
//                            totalPrem = totalPrem + 125.00 * (members.size() - 3);
//                        }
//                    }else{
//                        if (members.size() >= 2) {
//                            totalPrem = totalPrem + 125.00 * (members.size() - 2);
//                        }
//                    }
//                }
//            }
//        }
        //TODO set premium ill make static for now
        //premium = String.format("%,.2f",totalPrem);
        premium = "2300";
        totalPremium.setText("TOTAL PREMIUM : " + premium);

        frontphoto = null;
        backphoto  = null;

        capturedRS1.setText("");
        capturedRS2.setText("");
        pocNumber.setText("");

        pocMatched.setChecked(false);

    }

    //REFERENCE NO.:1234567PANUNTUNAN
    //REFERENCE NO.:1234567PANGALAN

    //na\nongl\n1\n1\n1\nREFERENCE NO.:\nPANUNTUNAN: Upang maging ganap na Nakaseguro sa
    public String getReferenceNumber(String raw){
        String ref = "";
//        try {
//            raw = raw.replaceAll(" ", "");
//            raw = raw.replaceAll("\n", "");
//            raw = raw.replaceAll("\r", "");
//            raw = raw.replaceAll(".", "");
//            raw = raw.replaceAll(":", "");
//            raw = raw.replaceAll("REFERENCE NO", "REFERENCENO");
//            raw = raw.replaceAll("PANUNTUNAN", "REFERENCENO");
//            raw = raw.replaceAll("PANGALAN", "REFERENCENO");
//            String[] refarray = raw.split("REFERENCENO");
//            ref = refarray[1].replace(".", "");
//            ref = ref.replace(":", "");
//        }catch (Exception e){
//
//        }
        raw = raw.replaceAll("#1", "");
        for (int i = 0; i < raw.length(); i++) {
            char a = raw.charAt(i);
            if (TextUtils.isDigitsOnly(String.valueOf(a))) {
                String refno = raw.substring(i, 7+i);
//				System.out.println("refno : " + refno);
                if (TextUtils.isDigitsOnly(refno)) {
                    ref = refno;
                    break;
                }
            }
        }

        return ref;
    }

    public Double kabuklodPrem = 0.00;
    public Double cardCarePrem = 0.00;
    public Double sagipIndividualPrem = 0.00;
    public Double sagipFamilyPrem = 0.00;
    public Double sagipPlatinumPrem = 0.00;


    public void showAlert(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(title);
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


//    File photoFile = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
//    File photoFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");


}
