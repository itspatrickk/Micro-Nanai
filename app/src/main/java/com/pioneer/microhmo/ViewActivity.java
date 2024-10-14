package com.pioneer.microhmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pioneer.microhmo.adapter.MemberAdapter;
import com.pioneer.microhmo.adapter.RecyclerViewInterface;
import com.pioneer.microhmo.objects.MemberInfo;
import com.pioneer.microhmo.objects.PolicyInfo;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity  implements RecyclerViewInterface  {

    DatabaseHelper databaseHelper;

    List<MemberInfo> dependents;


    MemberAdapter adapter;
    RecyclerView deprecyclerview;

    PolicyInfo policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        ImageView leftIcon = findViewById(R.id.left_icon);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseHelper = new DatabaseHelper(this);
        deprecyclerview = findViewById(R.id.deprecyclerview);



        TextView view_pocno = findViewById(R.id.view_pocno);
        TextView view_product = findViewById(R.id.view_product);
        TextView view_status = findViewById(R.id.view_status);
        TextView view_paymentmode = findViewById(R.id.view_paymentmode);
        TextView view_effectivedate = findViewById(R.id.view_effectivedate);
        TextView view_firstname = findViewById(R.id.view_firstname);
        TextView view_middlename = findViewById(R.id.view_middlename);
        TextView view_lastname = findViewById(R.id.view_lastname);
        TextView view_suffix = findViewById(R.id.view_suffix);
        TextView view_gender = findViewById(R.id.view_gender);
        TextView view_mobile = findViewById(R.id.view_mobile);
        TextView view_civilstatus = findViewById(R.id.view_civilstatus);
        TextView view_dob = findViewById(R.id.view_dob);




        dependents = new ArrayList<MemberInfo>();

        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("id") != null  && intent.getStringExtra("id").length() > 0) {
            String id = intent.getStringExtra("id");

            List<MemberInfo> members = databaseHelper.getMembersId(id);
            policy = databaseHelper.getPolicyInfo(id);

            String productDesc =  policy.getProduct() != null?policy.getProduct():"";

            productDesc = productDesc + (policy.getProduct1() != null?(","+policy.getProduct1()):"");
            productDesc = productDesc + (policy.getProduct2() != null?(","+policy.getProduct2()):"");
            productDesc = productDesc + (policy.getProduct3() != null?(","+policy.getProduct3()):"");
            if (productDesc.startsWith(","))
                productDesc = productDesc.substring(1);

            productDesc = productDesc.replace("CARDCARE PLUS", "CC+");
            productDesc = productDesc.replace("CARD CARE PLUS", "CC+");
            productDesc = productDesc.replace("CARDCARE", "CC+");
            productDesc = productDesc.replace("KABUKLOD PLAN", "K");
            productDesc = productDesc.replace("KABUKLOD", "K");

            productDesc = productDesc.replace("SAGIP PLAN INDIVIDUAL", "SPI");
            productDesc = productDesc.replace("SAGIP PLAN FAMILY PLUS", "SPF");
            productDesc = productDesc.replace("SAGIP PLAN FAMILY", "SPF");
            productDesc = productDesc.replace("SAGIP PLAN PLATINUM", "SPP");

            productDesc = productDesc.replace("SAGIP INDIVIDUAL", "SPI");
            productDesc = productDesc.replace("SAGIP FAMILY PLUS", "SPF");
            productDesc = productDesc.replace("SAGIP FAMILY", "SPF");

            productDesc = productDesc.replace(",,,", "");
            productDesc = productDesc.replace(",,", "");


            productDesc = productDesc.replace(", , ,", "");
            productDesc = productDesc.replace(", ,", "");
            productDesc = productDesc.replace("NULL", "");

            if (productDesc.startsWith(",")) {
                productDesc = productDesc.substring(1);
            }
            if (productDesc.endsWith(",")) {
                productDesc = productDesc.substring(0, productDesc.length()-1);
            }


            view_pocno.setText(policy.getPoc());
            //upperCase(policy.getProduct() +  policy.getProduct1() + policy.getProduct2() + policy.getProduct3())
            view_product.setText(productDesc);
            view_status.setText(upperCase(policy.getMistat()));
            view_paymentmode.setText(upperCase(policy.getPaymode()));
            view_effectivedate.setText(policy.getEffdate());

            for (MemberInfo member:members){
                Log.d("items", "member :" + member.getReltype());
                if (member.getPertype().equalsIgnoreCase("PRINCIPAL")){
                    view_firstname.setText(upperCase(member.getFname()));
                    view_middlename.setText(upperCase(member.getMname()));
                    view_lastname.setText(upperCase(member.getLname()));
                    view_suffix.setText(upperCase(member.getSuffix()));
                    view_gender.setText(upperCase(member.getGender()));
                    view_mobile.setText(upperCase(member.getMobileno()));
                    view_civilstatus.setText(member.getCivilstat());
                    view_dob.setText(member.getDob());
                }else{
                    dependents.add(member);
                }
            }
            LinearLayout sagipview = findViewById(R.id.sagip_view);
            RecyclerView recyclerView = findViewById(R.id.deprecyclerview);
            adapter = new MemberAdapter(getApplicationContext(),dependents, ViewActivity.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            if (dependents.size() > 0){
                sagipview.setVisibility(View.VISIBLE);
            }else{
                sagipview.setVisibility(View.GONE);
            }
        }

//TODO add this for Renew Policy

//        Button renewButton = findViewById(R.id.renewButton);
//        renewButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(ViewActivity.this, "ss"+policy.getId(),Toast.LENGTH_LONG).show();
//                if (policy != null) {
//                    Intent intent = new Intent(ViewActivity.this, RegisterActivity.class);
//                    intent.putExtra("id", policy.getId().toString());
//                    startActivity(intent);
//                }
//            }
//        });
    }


    private String upperCase(String val){
        if (val == null) return "";
        return val.toUpperCase();
    }
    @Override
    public void onItemClick(MemberInfo member) {

    }
}