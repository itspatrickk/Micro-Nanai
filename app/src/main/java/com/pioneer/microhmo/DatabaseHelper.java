package com.pioneer.microhmo;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.Nullable;

import com.pioneer.microhmo.objects.Address_;
import com.pioneer.microhmo.objects.MemberInfo;
import com.pioneer.microhmo.objects.PolicyInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String databaseName = "nanai3-7xyz121.db";
    //public static final String databaseName = "nanai3-7xyz121x.db";

    private static final String TABLE_MEMBERS = "member";
    private static final String TABLE_POLICY = "policy";
    public static final String  TABLE_ADDRESS = "address_table";


    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, 3);

    }


//                    contentValues.put("ACCOUNT_OFFICER", member.getAcctOfficer());
//                contentValues.put("UNIT_MANAGER", member.getUnitManager());
//                contentValues.put("CENTER", member.getCenter());
//                contentValues.put("AUTH_REP", member.getAuthRepresentative());
//                contentValues.put("REL_TO_MEMBER", member.getRelToMember());

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("create Table users(email TEXT primary key, password TEXT)");

        MyDatabase.execSQL("create Table " + TABLE_POLICY + "(GENID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id TEXT,product TEXT,product1 TEXT,product2 TEXT,product3 TEXT,product4 TEXT,product5 TEXT," +
                "poc TEXT,transdate TEXT,effdate TEXT, expdate TEXT,mistat TEXT,paymode TEXT," +
                "provoffice TEXT,unit TEXT,center TEXT, " +
                "image1 TEXT, image2 TEXT, image1stat TEXT, image2stat TEXT, " +
                "transtat TEXT, sendtag TEXT, timestamp TEXT, premium TEXT, currentstatus TEXT , agentid TEXT,  androidid TEXT)");

        MyDatabase.execSQL("create Table " + TABLE_MEMBERS + "(GENID INTEGER PRIMARY KEY AUTOINCREMENT, id TEXT, pertype TEXT, fname TEXT, mname TEXT, " +
                "lname TEXT, suffix TEXT, dob TEXT, gender TEXT, civilstat TEXT, mobileno TEXT, cardmember TEXT, ACCOUNT_OFFICER TEXT , UNIT_MANAGER TEXT , CENTER TEXT ,AUTH_REP TEXT ,REL_TO_MEMBER TEXT , " +
                " poc TEXT, status TEXT, transtat TEXT, sendtag TEXT, reltype TEXT , PROVINCE TEXT ,CITY TEXT , BRGY TEXT , STREET TEXT , EMAIL TEXT , PLACEOFBIRTH TEXT ,NATIONALITY TEXT , CLIENTTYPE TEXT , INSTITUTION TEXT)");
//PROVINCE TEXT ,CITY TEXT , BRGY TEXT , STREET TEXT , EMAIL TEXT , PLACEOFBIRTH TEXT ,NATIONALITY TEXT,
//        MyDatabase.execSQL("create Table " + TABLE_ADDRESS + "(GENID INTEGER PRIMARY KEY AUTOINCREMENT, province TEXT, city TEXT, barangay TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int version) {
//        MyDB.execSQL("drop Table if exists users");
////        MyDB.execSQL("drop Table if exists " + TABLE_POLICY);
////        MyDB.execSQL("drop Table if exists " + TABLE_MEMBERS);
////        MyDB.execSQL("drop Table if exists " + TABLE_ADDRESS);
        Log.d("SQL UPGRADE" , "Updating table from version " + i + " to version " + version);
        if (version > 1) {
            try{
                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN PROVINCE TEXT;");
                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN CITY TEXT;");
                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN BRGY TEXT;");
                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN STREET TEXT;");
                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN EMAIL TEXT;");
                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN PLACEOFBIRTH TEXT;");
                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN NATIONALITY TEXT;");
                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN CLIENTTYPE TEXT;");
                Log.d("SQLCREATE " , "SUCCESS");
//                MyDB.execSQL("create Table " + TABLE_ADDRESS + "(GENID INTEGER PRIMARY KEY AUTOINCREMENT, province TEXT, city TEXT, barangay TEXT)");
            }catch (Exception e){
                e.printStackTrace();
                Log.d("ERROR SQL" , e.getLocalizedMessage().toString());
            }
            MyDB.execSQL("ALTER TABLE " +TABLE_POLICY+" ADD COLUMN ao TEXT;");
        }
    }

    public void deletePolicies() {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        MyDatabase.delete(TABLE_POLICY, "transtat=?", new String[]{"Y"});
        MyDatabase.close();
    }

    public void deletePolicyById(String id) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        MyDatabase.delete(TABLE_POLICY, "ID=?", new String[]{id});
        MyDatabase.close();
    }

    public void deleteMembers() {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        MyDatabase.delete(TABLE_MEMBERS, "transtat=?", new String[]{"Y"});
        MyDatabase.close();
    }

    public void deleteMemberById(String id) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        MyDatabase.delete(TABLE_MEMBERS, "ID=?", new String[]{id});
        MyDatabase.close();
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public void addPolicyList(String transtat, List<PolicyInfo> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (PolicyInfo policy : list) {
//                deletePolicyById(policy.getId());
                db.delete(TABLE_POLICY, "ID=?", new String[]{policy.getId()});
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", policy.getId().toString());
                contentValues.put("product", policy.getProduct());
                contentValues.put("product1", policy.getProduct1());
                contentValues.put("product2", policy.getProduct2());
                contentValues.put("product3", policy.getProduct3());
                contentValues.put("product4", policy.getProduct4());
                contentValues.put("product5", policy.getProduct5());

                contentValues.put("poc", policy.getPoc());
                contentValues.put("transdate", policy.getTransdate());
                contentValues.put("effdate", policy.getEffdate());
                contentValues.put("mistat", policy.getMistat());
                contentValues.put("paymode", policy.getPaymode());
                contentValues.put("provoffice", policy.getProvoffice());
                contentValues.put("unit", policy.getUnit());
                contentValues.put("center", policy.getCenter());
                contentValues.put("premium", policy.getPremium());

                contentValues.put("agentid", policy.getAgentid());
                contentValues.put("androidid", policy.getAndroidid());

                contentValues.put("transtat", transtat);
                contentValues.putNull("image1");
                contentValues.putNull("image2");
                contentValues.putNull("image1stat");
                contentValues.putNull("image2stat");
                contentValues.putNull("sendtag");

                try {
                    Date date = dateFormat.parse(policy.getTransdate());
                    contentValues.put("timestamp", date.getTime());
                } catch (Exception e) {

                }
                db.insert(TABLE_POLICY, null, contentValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

//                    MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN EMAIL TEXT;");
//                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN PLACEOFBIRTH TEXT;");
//                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN NATIONALITY TEXT;");

    public void addMemberList(List<MemberInfo> members, String transtat) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (MemberInfo member : members) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", member.getId().toString());
                contentValues.put("pertype", member.getPertype());
                contentValues.put("fname", member.getFname());
                contentValues.put("mname", member.getMname());
                contentValues.put("lname", member.getLname());
                contentValues.put("suffix", member.getSuffix());
                contentValues.put("dob", member.getDob());
                contentValues.put("gender", member.getGender());
                contentValues.put("civilstat", member.getCivilstat());
                contentValues.put("mobileno", member.getMobileno());
                contentValues.put("cardmember", member.getCardmember());
                contentValues.put("poc", member.getPoc());
                contentValues.put("transtat", transtat);
                contentValues.put("reltype", member.getReltype());
                contentValues.put("ACCOUNT_OFFICER", member.getAcctOfficer());
                contentValues.put("UNIT_MANAGER", member.getUnitManager());
                contentValues.put("CENTER", member.getCenter());
                contentValues.put("AUTH_REP", member.getAuthRepresentative());
                contentValues.put("REL_TO_MEMBER", member.getRelToMember());
                contentValues.put("PROVINCE", member.getProv());
                contentValues.put("CITY" , member.getCity());
                contentValues.put("BRGY" , member.getBrgy());
                contentValues.put("STREET" , member.getStreet());
                contentValues.put("EMAIL" , member.getEmail());
                contentValues.put("PLACEOFBIRTH" , member.getPlaceOfBirth());
                contentValues.put("NATIONALITY" , member.getNationality());
                contentValues.put("CLIENTTYPE" , member.getClientType());
                db.insert(TABLE_MEMBERS, null, contentValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    public Boolean addPolicy(String transtat, PolicyInfo policy){
        deletePolicyById(policy.getId());

        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", policy.getId().toString());
        contentValues.put("product", policy.getProduct());
        contentValues.put("product1", policy.getProduct1());
        contentValues.put("product2", policy.getProduct2());
        contentValues.put("product3", policy.getProduct3());
        contentValues.put("product4", policy.getProduct4());
        contentValues.put("product5", policy.getProduct5());

        contentValues.put("poc", policy.getPoc());
        contentValues.put("transdate", policy.getTransdate());
        contentValues.put("effdate", policy.getEffdate());
        contentValues.put("mistat", policy.getMistat());
        contentValues.put("paymode", policy.getPaymode());
        contentValues.put("provoffice", policy.getProvoffice());
        contentValues.put("unit", policy.getUnit());
        contentValues.put("center", policy.getCenter());
        contentValues.put("premium", policy.getPremium());

        contentValues.put("agentid", policy.getAgentid());
        contentValues.put("androidid", policy.getAndroidid());

        contentValues.put("transtat",  transtat);
        contentValues.putNull("image1");
        contentValues.putNull("image2");
        contentValues.putNull("image1stat");
        contentValues.putNull("image2stat");
        contentValues.putNull("sendtag");

        try{
            Date date = dateFormat.parse(policy.getTransdate());
            contentValues.put("timestamp", date.getTime());
        }catch (Exception e){

        }


        long result = MyDatabase.insert(TABLE_POLICY, null, contentValues);
        MyDatabase.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean addNewPolicy(PolicyInfo policy){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", policy.getId().toString());

        contentValues.put("product", policy.getProduct());
        contentValues.put("product1", policy.getProduct1());
        contentValues.put("product2", policy.getProduct2());
        contentValues.put("product3", policy.getProduct3());
        contentValues.put("product4", policy.getProduct4());
        contentValues.put("product5", policy.getProduct5());

        contentValues.put("poc", policy.getPoc());
        contentValues.put("transdate", policy.getTransdate());
        contentValues.put("effdate", policy.getEffdate());
        contentValues.put("mistat", policy.getMistat());
        contentValues.put("paymode", policy.getPaymode());
        contentValues.put("provoffice", policy.getProvoffice());
        contentValues.put("unit", policy.getUnit());
        contentValues.put("center", policy.getCenter());

        contentValues.put("transtat",  policy.getTranstat());
        contentValues.put("image1", policy.getImage1());
        contentValues.put("image2", policy.getImage2());
        contentValues.put("image1stat", policy.getImage1stat());
        contentValues.put("image2stat", policy.getImage2stat());
        contentValues.put("sendtag", "N");
        contentValues.put("premium", policy.getPremium());


        contentValues.put("agentid", policy.getAgentid());
        contentValues.put("androidid", policy.getAndroidid());

        try{
            Date date = new Date();
            contentValues.put("timestamp", date.getTime());
        }catch (Exception e){

        }

        long result = MyDatabase.insert(TABLE_POLICY, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean addMember(MemberInfo member, String transtat){

        //deleteMemberById(member.getId());

        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", member.getId().toString());
        contentValues.put("pertype", member.getPertype());
        contentValues.put("fname", member.getFname());
        contentValues.put("mname", member.getMname());
        contentValues.put("lname", member.getLname());
        contentValues.put("suffix", member.getSuffix());
        contentValues.put("dob", member.getDob());
        contentValues.put("gender", member.getGender());
        contentValues.put("civilstat", member.getCivilstat());
        contentValues.put("mobileno", member.getMobileno());
        contentValues.put("cardmember", member.getCardmember());
        contentValues.put("poc", member.getPoc());
        contentValues.put("transtat", transtat);
        contentValues.put("reltype", member.getReltype());
        contentValues.put("ACCOUNT_OFFICER", member.getAcctOfficer());
        contentValues.put("UNIT_MANAGER", member.getUnitManager());
        contentValues.put("CENTER", member.getCenter());
        contentValues.put("AUTH_REP", member.getAuthRepresentative());
        contentValues.put("REL_TO_MEMBER", member.getRelToMember());
        contentValues.put("PROVINCE" , member.getProv());
        contentValues.put("CITY" , member.getCity());
        contentValues.put("brgy" , member.getBrgy());
        contentValues.put("STREET" , member.getStreet());
        contentValues.put("EMAIL",member.getEmail());
        contentValues.put("PLACEOFBIRTH",member.getPlaceOfBirth());
        contentValues.put("NATIONALITY",member.getNationality());
        contentValues.put("CLIENTTYPE" , member.getClientType());
        contentValues.put("INSTITUTION" , member.getInstitution());

        long result = MyDatabase.insert(TABLE_MEMBERS, null, contentValues);
        MyDatabase.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    PolicyInfo getPolicyInfo(String id) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery(
                "Select id ,product ,poc ,transdate ,effdate ,mistat ,paymode ,provoffice , " +
                        "unit ,center,product1,product2,product3, premium , expdate from "+TABLE_POLICY+ " where id = ? " +
                        "order by timestamp desc", new String[]{id});
        if (cursor != null)
            cursor.moveToFirst();
        //PolicyInfo(Long id, String product, String poc, String transdate, String effdate, String mistat, String paymode, String provoffice, String unit, String center)
        return new PolicyInfo(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getString(10),
                cursor.getString(11),
                cursor.getString(12),
                cursor.getString(13),
                cursor.getString(14),
                "",""
        );
    }




    List<PolicyInfo> getPendingPolicies() {
        List<PolicyInfo> list = new ArrayList<>();
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery(
                "Select id ,product ,poc ,transdate ,effdate ,mistat ,paymode ,provoffice , " +
                        "unit ,center,product1,product2,product3, premium, image1, image2, agentid, androidid , expdate  from "+TABLE_POLICY+ " where sendtag = 'N' " +
                        "order by timestamp desc", null);
        if (cursor.moveToFirst()) {
            do {
                PolicyInfo policy = new PolicyInfo(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getString(16)
                );
                policy.setAgentid(cursor.getString(16));
                policy.setAndroidid(cursor.getString(17));

                Log.d("agentid", "getAgentid :"  +policy.getAgentid());
                Log.d("agentid", "getAndroidid :"  +policy.getAndroidid());
                // Adding contact to list
                list.add(policy);
            } while (cursor.moveToNext());
        }
        return list;
    }
//    String id, String product, String poc,  String image1, String image2,  String image1stat, String image2stat
    List<PolicyInfo> getPendingPolicyImages() {
        List<PolicyInfo> list = new ArrayList<>();
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery(
                "Select id ,product ,poc ,image1 ,image2 ,image1stat ,image2stat, currentstatus   from "+TABLE_POLICY+ " where  image1stat = 'N' or  image2stat = 'N'  " +
                        "order by timestamp desc", null);
        if (cursor.moveToFirst()) {
            do {
                PolicyInfo policy = new PolicyInfo(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)
                );
                // Adding contact to list
                list.add(policy);
            } while (cursor.moveToNext());
        }
        return list;
    }

//                    MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN EMAIL TEXT;");
//                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN PLACEOFBIRTH TEXT;");
//                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN NATIONALITY TEXT;");

    public List<MemberInfo> getMembersId(String id) {
        List<MemberInfo> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  id , pertype , fname , mname , lname , suffix , dob , gender ," +
                " civilstat , mobileno , cardmember, PROVINCE , CITY , brgy,street , poc, status, reltype  , ACCOUNT_OFFICER , UNIT_MANAGER , CENTER, AUTH_REP ,REL_TO_MEMBER  , EMAIL , PLACEOFBIRTH , NATIONALITY , CLIENTTYPE , INSTITUTION from "+TABLE_MEMBERS + " where id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

//                String id,
//                String pertype,
//                String fname,
//                String mname,
//                String lname,
//                String suffix,
//                String dob,
//                String gender,
//                String civilstat,
//                String mobileno,
//                String cardmember,
//                String prov ,
//                String city ,
//                String brgy,
//                String street ,
//                String poc ,
//                String status ,
//                String areltype ,
//                String acctOfficer ,
//                String unitManager ,
//                String center ,
//                String authRepresentative ,
//                String relToMember ,
//                String email ,
//                String placeOfBirth ,
//                String nationality
                MemberInfo contact = new MemberInfo(
                        cursor.getString(0),  // id
                        cursor.getString(1),  // pertype
                        cursor.getString(2),  // fname
                        cursor.getString(3),  // mname
                        cursor.getString(4),  // lname
                        cursor.getString(5),  // suffix
                        cursor.getString(6),  // dob
                        cursor.getString(7),  // gender
                        cursor.getString(8),  // civilstat
                        cursor.getString(9),  // mobileno
                        cursor.getString(10), // cardmember
                        cursor.getString(11), // poc
                        cursor.getString(12), // status
                        cursor.getString(13), // reltype
                        cursor.getString(14), // ACCOUNT_OFFICER
                        cursor.getString(15), // UNIT_MANAGER
                        cursor.getString(16), // CENTER
                        cursor.getString(17), // AUTH_REP
                        cursor.getString(18), // REL_TO_MEMBER
                        cursor.getString(19), // PROVINCE
                        cursor.getString(20), // CITY
                        cursor.getString(21), // brgy
                        cursor.getString(22), // street
                        cursor.getString(23), // EMAIL
                        cursor.getString(24), // PLACEOFBIRTH
                        cursor.getString(25),  // NATIONALITY
                        cursor.getString(26),
                        cursor.getString(27)
                );

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }



    //            contentValues.put("PROVINCE" , member.getProv());
//        contentValues.put("CITY" , member.getCity());
//        contentValues.put("brgy" , member.getBrgy());
//        contentValues.put("street" , member.getStreet());
//                    MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN EMAIL TEXT;");
//                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN PLACEOFBIRTH TEXT;");
//                MyDB.execSQL("ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN NATIONALITY TEXT;");

        public List<MemberInfo> getMembers(String transtat) {
        List<MemberInfo> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  a.id , a.pertype , a.fname , a.mname , a.lname ," +
                " a.suffix , a.dob , a.gender , a.civilstat , a.mobileno , a.cardmember, b.poc," +
                " b.product, a.status, b.timestamp, b.product1,b.product2,b.product3, a.reltype, b.premium,b.effdate,b.currentstatus, b.transtat ,  A.ACCOUNT_OFFICER , A.UNIT_MANAGER , A.CENTER, A.AUTH_REP ,A.REL_TO_MEMBER , a.province , a.city , a.brgy , a.street , a.EMAIL , a.PLACEOFBIRTH , a.NATIONALITY , a.CLIENTTYPE , a.INSTITUTION from "+TABLE_MEMBERS + " a , " + TABLE_POLICY +
                " b where a.id = b.id and b.transtat = '"+transtat+"' and a.pertype = 'PRINCIPAL' " +
                " order by b.timestamp desc";

        if (transtat.equalsIgnoreCase("N")){
            selectQuery = "SELECT  a.id , a.pertype , a.fname , a.mname , a.lname ," +
                    " a.suffix , a.dob , a.gender , a.civilstat , a.mobileno , a.cardmember, b.poc," +
                    " b.product, a.status, b.timestamp, b.product1,b.product2,b.product3, a.reltype, b.premium,b.effdate,b.currentstatus, b.transtat ,  A.ACCOUNT_OFFICER , A.UNIT_MANAGER , A.CENTER, A.AUTH_REP ,A.REL_TO_MEMBER , a.province , a.city , a.brgy , a.street , a.EMAIL , a.PLACEOFBIRTH , a.NATIONALITY , a.CLIENTTYPE , a.INSTITUTION from "+TABLE_MEMBERS + " a , " + TABLE_POLICY +
                    " b where a.id = b.id and b.transtat = '"+transtat+"' and a.pertype = 'PRINCIPAL' " +
                    " order by b.timestamp desc";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                MemberInfo contact = new MemberInfo(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(29),  // province (a.province)
                        cursor.getString(30),  // city (a.city)
                        cursor.getString(31),  // brgy (a.brgy)
                        cursor.getString(32),  // street (a.street)
                        cursor.getString(11),
                        cursor.getString(13),
                        cursor.getString(18),
                        cursor.getString(23),
                        cursor.getString(24),
                        cursor.getString(25),
                        cursor.getString(26),
                        cursor.getString(27),
                        cursor.getString(28),
                        cursor.getString(33),
                        cursor.getString(34),
                        cursor.getString(35),
                        cursor.getString(36)
                );
                contact.setProduct(cursor.getString(12));
                contact.setProduct1(cursor.getString(15));
                contact.setProduct2(cursor.getString(16));
                contact.setProduct3(cursor.getString(17));
                contact.setPremium(cursor.getString(19));
                contact.setEffectivity(cursor.getString(20));
                contact.setCurrentstat(cursor.getString(21));
                contact.setTranstat(cursor.getString(22));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list

        Map<String,String> map = new HashMap<String,String>();
        MemberInfo m;
        for (Iterator<MemberInfo> iter = contactList.iterator();iter.hasNext();){
            m = iter.next();

            if (map.containsKey(m.getId())){
                iter.remove();
            }else {
                map.put(m.getId(), m.getId());
            }
        }

        return contactList;
    }


    public void updateId(String oldid, String newid){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", newid);
        contentValues.put("sendtag", "Y");
        MyDatabase.update(TABLE_POLICY, contentValues, "id=?", new String[]{oldid});
        MyDatabase.update(TABLE_MEMBERS, contentValues, "id=?", new String[]{oldid});
    }

    public void updateImage1Stat(String id){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("image1stat", "Y");
        MyDatabase.update(TABLE_POLICY, contentValues, "id=?", new String[]{id});
    }
    public void updateImage2Stat(String id){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("image2stat", "Y");
        contentValues.put("transtat", "Y");
        MyDatabase.update(TABLE_POLICY, contentValues, "id=?", new String[]{id});
    }

    public void updateTranStat(String id, String stat){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("transtat", "Y");
        MyDatabase.update(TABLE_POLICY, contentValues, "id=?", new String[]{id});
    }

    public void updateCurrStat(String id, String stat){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("currentstatus", stat);
        MyDatabase.update(TABLE_POLICY, contentValues, "id=?", new String[]{id});
    }

    public void updateImage2CurrStat(String id, String stat){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("image2stat", stat);
        MyDatabase.update(TABLE_POLICY, contentValues, "id=?", new String[]{id});
    }
    public int  getTotalPolicies(){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from "+ TABLE_POLICY, new String[]{});
        return cursor.getCount();
    }

    public Boolean isPocExists(String pocno){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from "+TABLE_POLICY+" where poc = ?", new String[]{pocno});
        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }

    List<PolicyInfo> getPolicyByPocNo(String pocno) {
        List<PolicyInfo> list = new ArrayList<>();
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery(
                "Select a.product ,a.product1 ,a.product2 ,a.product3 from "+TABLE_POLICY+ " a where poc = '"+pocno+"'", new String[]{});
        if (cursor.moveToFirst()) {
            do {
                PolicyInfo policy = new PolicyInfo(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                // Adding contact to list
                list.add(policy);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public Boolean isPocExists(String pocno, String pr1, String pr2, String pr3){

        List<PolicyInfo> pollist = getPolicyByPocNo(pocno);

        for (PolicyInfo pol:pollist){
            String product = pol.getProduct();
            String product1 = pol.getProduct1();
            String product2 = pol.getProduct2();
            String product3 = pol.getProduct3();

            Log.d("isPocExists", "product :"  +product);
            Log.d("isPocExists", "product1 :" +product1);
            Log.d("isPocExists", "product2 :" +product2);
            Log.d("isPocExists", "product3 :" +product3);
            Log.d("isPocExists", "pr1 :" +pr1);
            Log.d("isPocExists", "pr2 :" +pr2);
            Log.d("isPocExists", "pr3 :" +pr3);


            if (pr1 != null && pr1.length() > 5 && product1 != null && product1.length() > 5 && pr1.equalsIgnoreCase(product1)) return true;
            if (pr2 != null && pr2.length() > 5  && product2 != null && product2.length() > 5 && pr2.equalsIgnoreCase(product2)) return true;
            if (pr3 != null && pr3.length() > 5  && product3 != null && product3.length() > 5 ){
//                    pr3.equalsIgnoreCase(product3)
                if (pr3.contains("SAGIP") && product3.contains("SAGIP")){
                    return true;
                }
            }

            if (product != null && product.length() > 0 ){
                if ((pr1.length() > 0 && product.contains(pr1)) || (pr2.length() > 0 && product.contains(pr2)) ||  product.contains("SAGIP")){
                    return true;
                }
            }
        }
//
//        SQLiteDatabase MyDatabase = this.getWritableDatabase();
//        Cursor cursor = MyDatabase.rawQuery("Select poc, product1 ,product2, product3 from "+TABLE_POLICY+" where poc = ?", new String[]{pocno});
//
//        String product1 = "";
//        String product2 = "";
//        String product3 = "";
//
//        if (cursor.moveToFirst()) {
//            do {
//                //PolicyInfo(Long id, String product, String poc, String transdate, String effdate, String mistat, String paymode, String provoffice, String unit, String center)
//                product1 = cursor.getString(1);
//                product2 = cursor.getString(2);
//                product3 = cursor.getString(3);
//
//                Log.d("isPocExists", "pocno :" +pocno);
//                Log.d("isPocExists", "product1 :" +product1);
//                Log.d("isPocExists", "product2 :" +product2);
//                Log.d("isPocExists", "product3 :" +product3);
//                Log.d("isPocExists", "pr1 :" +pr1);
//                Log.d("isPocExists", "pr2 :" +pr2);
//                Log.d("isPocExists", "pr3 :" +pr3);
//
//                if (pr1 != null && product1 != null && product1.length() > 5 && pr1.equalsIgnoreCase(product1)) return true;
//                if (pr2 != null && product2 != null && product2.length() > 5 && pr2.equalsIgnoreCase(product2)) return true;
//                if (pr3 != null && product3 != null && product3.length() > 5 ){
////                    pr3.equalsIgnoreCase(product3)
//                    if (pr3.contains("SAGIP") && product3.contains("SAGIP")){
//                        return true;
//                    }
//                }
//
//            } while (cursor.moveToNext());
////        }
////            String product1 = "";
////            String product2 = "";
////            String product3 = "";
////
////            if (cursor != null)
////                cursor.moveToFirst();
////            //PolicyInfo(Long id, String product, String poc, String transdate, String effdate, String mistat, String paymode, String provoffice, String unit, String center)
////            product1 = cursor.getString(0);
////            product2 = cursor.getString(1);
////            product3 = cursor.getString(2);
////
////            if (pr1 != null && product1 != null && product1.length() > 5 && pr1.equalsIgnoreCase(product1)) return true;
////            if (pr2 != null && product2 != null && product2.length() > 5 && pr2.equalsIgnoreCase(product2)) return true;
////            if (pr3 != null && product3 != null && product3.length() > 5 && pr3.equalsIgnoreCase(product3)) return true;
//
////            return false;
//        }else {
//            return false;
//        }
        return false;
    }

    public int  getTotalMembers(){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from "+ TABLE_MEMBERS, new String[]{});
        return cursor.getCount();
    }

    public Boolean insertData(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDatabase.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ?", new String[]{email});
        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
//
//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//    Date startDate = dateFormat.parse("2023-01-01");
//    Date endDate = dateFormat.parse("2023-12-31");
//
//    String[] columns = {"id", "name", "date"};
//    String selection = "date BETWEEN ? AND ?";
//    String[] selectionArgs = {String.valueOf(startDate.getTime()), String.valueOf(endDate.getTime())};
//    String sortOrder = "date DESC";
//
//    Cursor cursor = db.query("my_table", columns, selection, selectionArgs, null, null, sortOrder);
//while (cursor.moveToNext()) {
//        long id = cursor.getLong(cursor.getColumnIndex("id"));
//        String name = cursor.getString(cursor.getColumnIndex("name"));
//        long dateInMillis = cursor.getLong(cursor.getColumnIndex("date"));
//        Date date = new Date(dateInMillis);
//        Log.d(TAG, "id: " + id + ", name: " + name + ", date: " + date);
//    }
//cursor.close();

//    // Get Contacts list by date
//    public List<Contact> getContactsByDate(String mDate) {
//        List<Contact> contactList = new ArrayList<>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + KEY_DATE + " LIKE " + "'%" + mDate + "'";
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Contact contact = new Contact();
//                contact.setID(Integer.parseInt(cursor.getString(0)));
//                contact.setName(cursor.getString(1));
//                contact.setPhoneNumber(cursor.getString(2));
//                contact.setDate(cursor.getString(3));
//                // Adding contact to list
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }
//        // return contact list
//        return contactList;
//    }
public void addAddressList(List<Address_> addressList ) {
//        MyDatabase.execSQL("create Table " + TABLE_MEMBERS + "(GENID INTEGER PRIMARY KEY AUTOINCREMENT, id TEXT, pertype TEXT, fname TEXT, mname TEXT, " +
//                "lname TEXT, suffix TEXT, dob TEXT, gender TEXT, civilstat TEXT, mobileno TEXT, cardmember TEXT," +
//                " poc TEXT, status TEXT, transtat TEXT, sendtag TEXT, reltype TEXT)");

    SQLiteDatabase db = this.getWritableDatabase();
    db.beginTransaction();
    String creteTable= "create Table " + TABLE_ADDRESS + "(GENID INTEGER PRIMARY KEY AUTOINCREMENT, province TEXT, city TEXT, barangay TEXT)";
    db.execSQL(creteTable);
    db.delete(TABLE_ADDRESS, null, new String[]{});
    try {
        for (Address_ address : addressList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("province", address.getProvince());
            contentValues.put("city", address.getCity());
            contentValues.put("barangay", address.getBarangay());

            db.insert(TABLE_ADDRESS, null, contentValues);
        }
        db.setTransactionSuccessful();
    } finally {
        db.endTransaction();
        db.close();
    }
}

    public List<String>getCities(String province) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery(
                "Select distinct city from " + TABLE_ADDRESS + " where province = ? " +
                        "order by province asc", new String[]{province});
        if (cursor != null)
            cursor.moveToFirst();
        List<String> list = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<String>getBarangays(String province, String city) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery(
                "Select distinct barangay from " + TABLE_ADDRESS + " where province = ? and city = ?" +
                        "order by province asc", new String[]{province, city});
        if (cursor != null)
            cursor.moveToFirst();
        List<String> list = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return list;
    }


    public List<String>getAllProvinces() {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery(
                "Select distinct province from " + TABLE_ADDRESS  +
                        " order by province asc", new String[]{});
        if (cursor != null)
            cursor.moveToFirst();
        List<String> list = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return list;
    }
}