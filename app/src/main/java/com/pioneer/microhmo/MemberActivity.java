package com.pioneer.microhmo;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pioneer.microhmo.util.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MemberActivity extends AppCompatActivity {

    int minAge = 0;
    int maxAge = 60;
    TextView dateOfBirth;

    RadioGroup radioReltype, genderGroup, membershipGroup, statusGroup;
    RadioButton spouse, child, genderMale, genderFemale,memeberYes,memberNo, statusNew, statusRenewal;

    LinearLayout spousediv, childdiv;

    Button addMember;
    EditText firstname, middlename,lastname, mobileno;

    AutoCompleteTextView suffix;//gender,suffix, status, cardmember;
    String key = null;
    String reltype = "SPOUSE";
    String sagipCode = "";
    String regstatus = "NEW";
    String gender, membership,status;
    private String suffixselected;//,genderselected, membershipselected,statusselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);

        addMember = findViewById(R.id.addMember);
        spousediv = findViewById(R.id.spouse_div);
        childdiv = findViewById(R.id.child_div);

        firstname = findViewById(R.id.member_firstname);
        dateOfBirth = findViewById(R.id.member_dob);
        middlename = findViewById(R.id.member_middlename);
        lastname = findViewById(R.id.member_lastname);

        mobileno = findViewById(R.id.member_mobileNo);

        suffix = findViewById(R.id.member_suffix);

//        gender = findViewById(R.id.member_gender);
//        cardmember = findViewById(R.id.spouse_membership);
//        status = findViewById(R.id.child_status);


        radioReltype = findViewById(R.id.radioReltype);
        genderGroup = findViewById(R.id.genderRadio);
        membershipGroup = findViewById(R.id.membershipRadio);
        statusGroup = findViewById(R.id.statusRadio);

        spouse = findViewById(R.id.radioSpouse);
        child = findViewById(R.id.radioChild);

        genderMale = findViewById(R.id.genderLalaki);
        genderFemale = findViewById(R.id.genderBabae);
        memeberYes = findViewById(R.id.membershipYes);
        memberNo = findViewById(R.id.membershipNo);
        statusNew = findViewById(R.id.statusNew);
        statusRenewal = findViewById(R.id.statusRenewal);

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        String date = "";//(month+1)+"/"+day+"/"+year;

        ImageView leftIcon = findViewById(R.id.left_icon);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        genderGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        System.out.println("checkedId : " + checkedId);
                        if (checkedId == R.id.genderLalaki){
                            gender = "Lalaki";
                            genderMale.setTextColor(WHITE);
                            genderMale.setBackgroundResource(R.drawable.blue_button);
                            genderFemale.setTextColor(BLACK);
                            genderFemale.setBackgroundResource(R.drawable.gray_button);
                        } else if (checkedId == R.id.genderBabae){
                            gender = "Babae";
                            genderFemale.setTextColor(WHITE);
                            genderFemale.setBackgroundResource(R.drawable.blue_button);
                            genderMale.setTextColor(BLACK);
                            genderMale.setBackgroundResource(R.drawable.gray_button);
                        }
                    }
                }
        );


        membershipGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        System.out.println("checkedId : " + checkedId);
                        if (checkedId == R.id.membershipYes){
                            membership = "Yes";
                            toggleRadio(memeberYes,memberNo);
                        } else if (checkedId == R.id.membershipNo){
                            membership = "No";
                            toggleRadio(memberNo, memeberYes);
                        }
                        System.out.println("membership : " + membership);
                    }
                }
        );


        statusGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        System.out.println("checkedId : " + checkedId);
                        if (checkedId == R.id.statusNew){
                            status = "New";
                            statusNew.setTextColor(WHITE);
                            statusNew.setBackgroundResource(R.drawable.blue_button);
                            statusRenewal.setTextColor(BLACK);
                            statusRenewal.setBackgroundResource(R.drawable.gray_button);
                        } else if (checkedId == R.id.statusRenewal){
                            status = "Renewal";
                            statusRenewal.setTextColor(WHITE);
                            statusRenewal.setBackgroundResource(R.drawable.blue_button);
                            statusNew.setTextColor(BLACK);
                            statusNew.setBackgroundResource(R.drawable.gray_button);
                        }
                    }
                }
        );

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(MemberActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month = month+1;
                        String monthstr = ""+month;
                        String dayOfMonthStr = ""+dayOfMonth;

                        if (monthstr.length()==1) monthstr = "0"+monthstr;
                        if (dayOfMonthStr.length()==1) dayOfMonthStr = "0"+dayOfMonthStr;
                        String date = monthstr+"/"+dayOfMonthStr+"/"+year;
                        dateOfBirth.setText(date);

                    }
                },year, month,day);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();

            }
        });

        //spousediv.setVisibility(View.GONE);
        childdiv.setVisibility(View.GONE);

        radioReltype.setOnCheckedChangeListener(
                        new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                System.out.println("checkedId : " + checkedId);
                                if (checkedId == R.id.radioSpouse){
                                    reltype = "SPOUSE";
                                    toggleRadio(spouse,child);
                                    spousediv.setVisibility(View.VISIBLE);
                                    childdiv.setVisibility(View.GONE);
                                } else if (checkedId == R.id.radioChild){
                                    reltype = "CHILD";
                                    toggleRadio(child,spouse);
                                    childdiv.setVisibility(View.VISIBLE);
                                    spousediv.setVisibility(View.GONE);
                                }
                            }
                        }
                );


        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int age = 0;

                Intent intent = new Intent();
                String dob = dateOfBirth.getText().toString();
                SimpleDateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy");
                Date bdate = null;


                Log.d("register", "dob : " + dob);

                try {
                    bdate= dateFormat.parse(dob);
                    Log.d("register", "Formated : " + dateFormat.format(bdate));

                    age = CommonUtil.getAge(bdate, new Date());
                } catch(Exception e) {
                    //java.text.ParseException: Unparseable date: Geting error
                    System.out.println("Excep"+e);
                    showAlert("Incomplete Information", "Siguraduhin na ilagay ang Kapanganakan ng dependent.");

                }

                Log.d("register", "reltype : " + reltype);
                gender = genderFemale.isChecked()?"Babae":"Lalaki";
                status = statusNew.isChecked()?"New":"Renewal";
                if (reltype == null) {
                    reltype = spouse.isChecked() ? "SPOUSE" : "CHILD";
                }
                membership = memeberYes.isChecked()?"YES":"NO";

                Boolean fnameValid = isValid(firstname.getText().toString());
                Boolean lnameValid = isValid(lastname.getText().toString());
                Boolean genderValid = isValid(gender);
                Boolean memberValid = reltype.equalsIgnoreCase("SPOUSE")?isValid( membership):true;
                Boolean statusValid = true;//reltype.equalsIgnoreCase("CHILD")?isValid( status):true;

                Log.d("register", "reltype : " + reltype);
                Log.d("register", "reltype : " + reltype);

                if (!fnameValid){
                    showAlert("Incomplete Information", "Siguraduhin na ilagay ang First Name ng dependent.");
                    return;
                }
                if (!lnameValid){
                    showAlert("Incomplete Information", "Siguraduhin na ilagay ang Last Name ng dependent.");
                    return;
                }
                if (!genderValid){
                    showAlert("Incomplete Information", "GENDER is Required");
                    return;
                }
                if (!memberValid){
                    showAlert("Incomplete Information", "MEMBERSHIP is Required");
                    return;
                }
                if (!statusValid){
                    showAlert("Incomplete Information",  "Status is Required");
                    return;
                }

                Boolean isValid = CommonUtil.isProductValid(sharedPreferences, sagipCode,  reltype, regstatus,bdate,  new Date());

                if (isValid) {
                    intent.putExtra("firstname", firstname.getText().toString());
                    intent.putExtra("middlename", middlename.getText().toString());
                    intent.putExtra("lastname", lastname.getText().toString());
                    intent.putExtra("mobileno", mobileno.getText().toString());
                    intent.putExtra("dateofbirth", dob);
                    intent.putExtra("reltype", reltype);

                    intent.putExtra("suffix", suffixselected);
                    intent.putExtra("cardmember", membership);
                    intent.putExtra("gender", gender);
                    intent.putExtra("status", status);

                    if (key != null && key.length() > 0) {
                        intent.putExtra("key", key);
                        setResult(RegisterActivity.REQUEST_CODE_UPDATE_MEMBER, intent);
                    } else {
                        setResult(RegisterActivity.REQUEST_CODE_ADD_MEMBER, intent);
                    }
                    finish();
                }else{
                    showAlert("Incomplete Information", "Siguraduhing tama ang edad ng dependent.");
                }

            }
        });

        final String blockCharacterSet = "1234567890,;:<>~`!@#$%^&*()_+=\"'/|\\";
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            Log.d("source", source.toString());
            if (source != null && blockCharacterSet.contains(("" + source))  ) {
                return "";
            }
            return null;
        };


        InputFilter[] allCapsFilter = new InputFilter[] {new InputFilter.AllCaps(), filter,new InputFilter.LengthFilter(50) };
        InputFilter[] middle = new InputFilter[] {new InputFilter.AllCaps(),filter, new InputFilter.LengthFilter(5) };

        firstname.setFilters(allCapsFilter);
        middlename.setFilters(middle);
        lastname.setFilters(allCapsFilter);
        mobileno.setFilters(allCapsFilter);

//        cardmember.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, RegisterActivity.cardmemberlist));
//        cardmember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                membershipselected = parent.getItemAtPosition(position).toString();
//            }
//        });
//
//        status.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, RegisterActivity.memberstatuslist));
//        status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                statusselected = parent.getItemAtPosition(position).toString();
//            }
//        });
//
//        gender.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, RegisterActivity.genderlist));
//        gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                genderselected = parent.getItemAtPosition(position).toString();
//            }
//        });
        suffix.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, RegisterActivity.suffixlist));
        suffix.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                suffixselected = parent.getItemAtPosition(position).toString();
            }
        });



        Intent intent = getIntent();
        sagipCode = intent.getStringExtra("sagipType");
        reltype = intent.getStringExtra("reltype");
        regstatus =  intent.getStringExtra("regstatus");
        String isSpouseExist = intent.getStringExtra("isSpouseExist");


        Log.d("dependents", "reltype : " + reltype);
        Log.d("dependents", "sagipCode : " + sagipCode);
        Log.d("dependents", "key : " + intent.getStringExtra("key"));

        if (intent != null && intent.getStringExtra("key") != null  && intent.getStringExtra("key").length() > 0) {
            key = intent.getStringExtra("key");
            String afirstname = intent.getStringExtra("firstname");
            String amiddlename = intent.getStringExtra("middlename");
            String alastname = intent.getStringExtra("lastname");
            String asuffix = intent.getStringExtra("suffix");
            String adateofbirth = intent.getStringExtra("dateofbirth");
            String agender = intent.getStringExtra("gender");
            String acardmember = intent.getStringExtra("cardmember");
            String astatus = intent.getStringExtra("status");

            firstname.setText(afirstname);
            middlename.setText(amiddlename);
            lastname.setText(alastname);
            dateOfBirth.setText(adateofbirth);

//            radioReltype.setVisibility(View.GONE);

            if ("SPOUSE".equalsIgnoreCase(reltype)) {
                spouse.setChecked(true);
                spouse.setTextColor(WHITE);
                spouse.setBackgroundResource(R.drawable.blue_button);
                child.setVisibility(View.GONE);
//                child.setChecked(false);
//                child.setTextColor(BLACK);
//                child.setBackgroundResource(R.drawable.gray_button);
            } else {
                child.setChecked(true);
                child.setTextColor(WHITE);
                child.setBackgroundResource(R.drawable.blue_button);
                spouse.setVisibility(View.GONE);
//                spouse.setChecked(false);
//                spouse.setTextColor(BLACK);
//                spouse.setBackgroundResource(R.drawable.gray_button);
            }



//            suffix.setText(asuffix, false);
//            gender.setText(agender, false);
//            status.setText(astatus, false);
//            cardmember.setText(acardmember, false);
            if (agender != null && agender.equalsIgnoreCase("Lalaki")){
                genderMale.setChecked(true);
                toggleRadio(genderMale, genderFemale);
            }
            if (agender != null && agender.equalsIgnoreCase("Babae")){
                genderFemale.setChecked(true);
                toggleRadio(genderFemale,genderMale);
            }

            if (astatus != null && astatus.equalsIgnoreCase("New")){
                statusNew.setChecked(true);
                toggleRadio(statusNew,statusRenewal);
            }
            if (astatus != null && astatus.equalsIgnoreCase("Renewal")){
                statusRenewal.setChecked(true);
                toggleRadio(statusRenewal,statusNew);
            }

            if (acardmember != null && acardmember.equalsIgnoreCase("Yes")){
                memeberYes.setChecked(true);
                toggleRadio(memeberYes,memberNo);
            }
            if (acardmember != null && acardmember.equalsIgnoreCase("No")){
                memberNo.setChecked(true);
                toggleRadio(memberNo,memeberYes);
            }

        }


        if (sagipCode.equalsIgnoreCase("SI")){
            child.setChecked(true);
            child.setVisibility(View.VISIBLE);
            spouse.setVisibility(View.INVISIBLE);
        } else {
            spouse.setVisibility(View.VISIBLE);
        }

        if (isSpouseExist != null && isSpouseExist.equalsIgnoreCase("Y")){
            spouse.setVisibility(View.INVISIBLE);
            child.setChecked(true);
            child.setVisibility(View.VISIBLE);
        }
        if (reltype != null){
            if (reltype.equalsIgnoreCase("CHILD")) {
                spouse.setVisibility(View.INVISIBLE);
                child.setChecked(true);
                child.setVisibility(View.VISIBLE);
            } else if (reltype.equalsIgnoreCase("SPOUSE")) {
                child.setVisibility(View.INVISIBLE);
                spouse.setChecked(true);
                spouse.setVisibility(View.VISIBLE);
            }
        }
    }

    public static Boolean isValid(String value){
        if (value == null) return false;
        if (value.isEmpty() || value.length()==0) return false;
        return true;
    }

    public void toggleRadio(RadioButton radio1, RadioButton radio2 ){
        radio1.setChecked(true);
        radio1.setTextColor(WHITE);
        radio1.setBackgroundResource(R.drawable.blue_button);

        radio2.setChecked(false);
        radio2.setTextColor(BLACK);
        radio2.setBackgroundResource(R.drawable.gray_button);
    }

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
}