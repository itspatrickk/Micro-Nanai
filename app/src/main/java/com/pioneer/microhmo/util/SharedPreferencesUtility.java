package com.pioneer.microhmo.util;

import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesUtility {
    private static final String PREFERENCE_FILE_NAME = "MyAppPreferences";  
    private static final String TOKEN = "TOKEN";
    private static final String AGENT_ID = "AGENT_ID_KEY";

    private static final String ANDROID_ID = "ANDROID_ID";
    private static final String AGENT_SEQNO = "AGENT_SEQNO";
    private static final String AGENT_NAME = "AGENT_NAME";
    private static final String AGENT_REF = "AGENT_REF";
    private static final String AGENT_REF_TEMP = "AGENT_REF_TEMP";
    private static final String AGENT_OTP = "AGENT_OTP";
    private static final String AGENT_OTP_CHANGE = "AGENT_OTP_CHANGE";
    private static final String MOBILENO = "MOBILENO";
    private static final String CENTERS = "CENTERS";
    private static final String LOGIN_ATTEMPTS = "LOGIN_ATTEMPTS";
    private static SharedPreferences sharedPreferences;

    public static void setInitialValues(SharedPreferences sharedPreferences){
        saveString(sharedPreferences, "CC_NEW_PRINCIPAL_UNIT","DAY");
        saveString(sharedPreferences, "CC_RENEWAL_PRINCIPAL_UNIT","DAY");
        saveString(sharedPreferences, "KP_NEW_PRINCIPAL_UNIT","DAY");
        saveString(sharedPreferences, "KP_RENEWAL_PRINCIPAL_UNIT","DAY");
        saveString(sharedPreferences, "SF_NEW_PRINCIPAL_UNIT","YEAR");
        saveString(sharedPreferences, "SF_RENEWAL_PRINCIPAL_UNIT","YEAR");
        saveString(sharedPreferences, "SF_NEW_SPOUSE_UNIT","YEAR");
        saveString(sharedPreferences, "SF_RENEWAL_SPOUSE_UNIT","YEAR");
        saveString(sharedPreferences, "SF_NEW_CHILD_UNIT","DAY");
        saveString(sharedPreferences, "SF_RENEWAL_CHILD_UNIT","DAY");
        saveString(sharedPreferences, "SI_NEW_PRINCIPAL_UNIT","YEAR");
        saveString(sharedPreferences, "SI_RENEWAL_PRINCIPAL_UNIT","YEAR");
        saveString(sharedPreferences, "SI_NEW_CHILD_UNIT","DAY");
        saveString(sharedPreferences, "SI_RENEWAL_CHILD_UNIT","DAY");
        saveString(sharedPreferences, "SP_NEW_PRINCIPAL_UNIT","YEAR");
        saveString(sharedPreferences, "SP_RENEWAL_PRINCIPAL_UNIT","YEAR");
        saveString(sharedPreferences, "SP_NEW_CHILD_UNIT","DAY");
        saveString(sharedPreferences, "SP_RENEWAL_CHILD_UNIT","DAY");
        saveInt(sharedPreferences, "CC_NEW_PRINCIPAL_MIN",14);
        saveInt(sharedPreferences, "CC_RENEWAL_PRINCIPAL_MIN",14);
        saveInt(sharedPreferences, "KP_NEW_PRINCIPAL_MIN",14);
        saveInt(sharedPreferences, "KP_RENEWAL_PRINCIPAL_MIN",14);
        saveInt(sharedPreferences, "SF_NEW_PRINCIPAL_MIN",18);
        saveInt(sharedPreferences, "SF_RENEWAL_PRINCIPAL_MIN",18);
        saveInt(sharedPreferences, "SF_NEW_SPOUSE_MIN",18);
        saveInt(sharedPreferences, "SF_RENEWAL_SPOUSE_MIN",18);
        saveInt(sharedPreferences, "SF_NEW_CHILD_MIN",14);
        saveInt(sharedPreferences, "SF_RENEWAL_CHILD_MIN",14);
        saveInt(sharedPreferences, "SI_NEW_PRINCIPAL_MIN",18);
        saveInt(sharedPreferences, "SI_RENEWAL_PRINCIPAL_MIN",18);
        saveInt(sharedPreferences, "SI_NEW_CHILD_MIN",14);
        saveInt(sharedPreferences, "SI_RENEWAL_CHILD_MIN",14);
        saveInt(sharedPreferences, "SP_NEW_PRINCIPAL_MIN",18);
        saveInt(sharedPreferences, "SP_RENEWAL_PRINCIPAL_MIN",18);
        saveInt(sharedPreferences, "SP_NEW_CHILD_MIN",14);
        saveInt(sharedPreferences, "SP_RENEWAL_CHILD_MIN",14);
        saveInt(sharedPreferences, "CC_NEW_PRINCIPAL_MAX",65);
        saveInt(sharedPreferences, "CC_RENEWAL_PRINCIPAL_MAX",70);
        saveInt(sharedPreferences, "KP_NEW_PRINCIPAL_MAX",65);
        saveInt(sharedPreferences, "KP_RENEWAL_PRINCIPAL_MAX",70);
        saveInt(sharedPreferences, "SF_NEW_PRINCIPAL_MAX",65);
        saveInt(sharedPreferences, "SF_RENEWAL_PRINCIPAL_MAX",70);
        saveInt(sharedPreferences, "SF_NEW_SPOUSE_MAX",65);
        saveInt(sharedPreferences, "SF_RENEWAL_SPOUSE_MAX",70);
        saveInt(sharedPreferences, "SF_NEW_CHILD_MAX",17);
        saveInt(sharedPreferences, "SF_RENEWAL_CHILD_MAX",17);
        saveInt(sharedPreferences, "SI_NEW_PRINCIPAL_MAX",65);
        saveInt(sharedPreferences, "SI_RENEWAL_PRINCIPAL_MAX",70);
        saveInt(sharedPreferences, "SI_NEW_CHILD_MAX",17);
        saveInt(sharedPreferences, "SI_RENEWAL_CHILD_MAX",17);
        saveInt(sharedPreferences, "SP_NEW_PRINCIPAL_MAX",65);
        saveInt(sharedPreferences, "SP_RENEWAL_PRINCIPAL_MAX",75);
        saveInt(sharedPreferences, "SP_NEW_CHILD_MAX",17);
        saveInt(sharedPreferences, "SP_RENEWAL_CHILD_MAX",17);

    }


    public static void saveTempReference(SharedPreferences sharedPreferences,  String reference) {
        saveString(sharedPreferences, AGENT_REF_TEMP,reference);
    }
    public static String getTempReference(SharedPreferences sharedPreferences) {
        return getString(sharedPreferences,  AGENT_REF_TEMP, null);
    }

    public static void saveAgentName(SharedPreferences sharedPreferences,  String agentName) {
        saveString(sharedPreferences, AGENT_NAME,agentName);
    }
    public static void saveAgentId(SharedPreferences sharedPreferences, String agentId) {
        saveString(sharedPreferences, AGENT_ID,agentId);
    }

    public static String getAgentName(SharedPreferences sharedPreferences) {
        return getString(sharedPreferences,  AGENT_NAME, null);
    }

    public static void saveReference(SharedPreferences sharedPreferences,  String reference) {
        saveString(sharedPreferences, AGENT_REF,reference);
    }
    public static String getReference(SharedPreferences sharedPreferences) {
        return getString(sharedPreferences,  AGENT_REF, null);
    }

    public static String getAgentId(SharedPreferences sharedPreferences) {
        return getString(sharedPreferences,  AGENT_ID, null);
    }

    public static String getAndroidId(SharedPreferences sharedPreferences) {
        return getString(sharedPreferences,  ANDROID_ID, null);
    }
    public static void saveAndroidId(SharedPreferences sharedPreferences, String androidid) {
        saveString(sharedPreferences, ANDROID_ID,androidid);
    }
    public static void saveAgentSeqno(SharedPreferences sharedPreferences, String agentseqno) {
        saveString(sharedPreferences, AGENT_SEQNO,agentseqno);
    }
    public static String getAgentSeqno(SharedPreferences sharedPreferences) {
        return getString(sharedPreferences,  AGENT_SEQNO, null);
    }


    public static void saveOtp(SharedPreferences sharedPreferences,  String reference) {
        saveString(sharedPreferences, AGENT_OTP,reference);
        saveInt(sharedPreferences , LOGIN_ATTEMPTS , 0);
    }

    public static void addAttemptCount(SharedPreferences sharedPreferences) {
        int count = getAttemptCount(sharedPreferences);
        saveInt(sharedPreferences,LOGIN_ATTEMPTS,count+1);
    }

    public static  int getAttemptCount(SharedPreferences sharedPreferences){
        int counts = getInt(sharedPreferences , LOGIN_ATTEMPTS,0);
        return counts;
    }


    public static String getOtp(SharedPreferences sharedPreferences) {
        return getString(sharedPreferences, AGENT_OTP, null);
    }

    public static void removeOtp(SharedPreferences sharedPreferences) {
        removeKey(sharedPreferences, AGENT_OTP);
    }

    public static void saveToken(SharedPreferences sharedPreferences, String reference) {
        saveString(sharedPreferences, TOKEN,reference);
    }
    public static String getToken(SharedPreferences sharedPreferences) {
        return getString(sharedPreferences, TOKEN, null);
    }

    public static boolean isUserExist(SharedPreferences sharedPreferences) {
        String mobile = getMobileNo(sharedPreferences);
        String opt = getOtp(sharedPreferences);
        if (mobile == null || mobile.length() == 0) return false;
        if (opt == null) return false;
        return true;
    }

    public static void saveOtpChange(SharedPreferences sharedPreferences,  String reference) {
        saveString(sharedPreferences, AGENT_OTP,reference);
    }
    public static boolean isOtpChanged(SharedPreferences sharedPreferences) {
        String opt = sharedPreferences.getString(AGENT_OTP_CHANGE, null);
        if (opt == null) return false;
        return true;
    }

    public static void saveString( SharedPreferences sharedPreferences, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void removeKey( SharedPreferences sharedPreferences, String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static String getString(SharedPreferences sharedPreferences,  String key, String defaultValue) {
        String value =  null;//sharedPreferences.getString(key, defaultValue);
        try{
            value =  sharedPreferences.getString(key, defaultValue);
        }catch (Exception e){

        }
        return value;
    }

    public static Double getProductPrem(SharedPreferences sharedPreferences,  String key) {
        Double prem =  Double.parseDouble(sharedPreferences.getString(key+"_PREM", "0.00"));
        Log.d("key", "prem : " + prem);
        return prem;
    }

    public static void saveInt(SharedPreferences sharedPreferences,  String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(SharedPreferences sharedPreferences,  String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void saveBoolean(SharedPreferences sharedPreferences,  String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean( SharedPreferences sharedPreferences, String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }


    public static void saveMobileNo(SharedPreferences sharedPreferences, String reference) {
        saveString(sharedPreferences, MOBILENO,reference);
    }
    public static String getMobileNo(SharedPreferences sharedPreferences) {
        return getString(sharedPreferences, MOBILENO, "");
    }

    public static void saveCenters(SharedPreferences sharedPreferences, String reference) {
        saveString(sharedPreferences, CENTERS,reference);
    }
    public static String getCenters(SharedPreferences sharedPreferences) {
        return getString(sharedPreferences, CENTERS, "");
    }
}
