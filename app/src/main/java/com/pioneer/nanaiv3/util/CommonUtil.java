package com.pioneer.nanaiv3.util;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class CommonUtil {


    public static Date getEffMinDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR , 1);

        return cal.getTime();
    }

    public static Date getEffMaxDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR , -1);

        return cal.getTime();
    }

    public static Date getMinDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE , -14);

        return cal.getTime();
    }

    public static Date getMaxDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR , -80);

        return cal.getTime();
    }

    static String TAG = "commonutil";

//    CC_NEW_PRINCIPAL_UNIT
//    CC_NEW_PRINCIPAL_MIN
//    CC_NEW_PRINCIPAL_MAX
    public static Boolean isProductValid(SharedPreferences sharedPreferences, String code,String type, String status, Date birthdate, Date effdate){
        if (birthdate == null) return false;
        if (effdate == null) effdate = new Date();
        status = status.toUpperCase();
        type = type.toUpperCase();
        int currentAge = getAge(birthdate,effdate);
        int ageDay = 0;
        if (currentAge == 0){
            ageDay = getAgeDay(birthdate);
        }else{
            ageDay = currentAge * 365;
        }
        Log.d("currentAge", "currentAge : "+ currentAge);
        if (currentAge < 0) return false;

        int maxAge = 0;
        int minAge = 0;
        try{
            maxAge = Integer.parseInt(sharedPreferences.getString(code+"_"+status+"_"+type+"_MAX", "0"));
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            minAge = Integer.parseInt(sharedPreferences.getString(code+"_"+status+"_"+type+"_MIN", "0"));
        }catch (Exception e){
            e.printStackTrace();
        }



        String unit = sharedPreferences.getString(code+"_"+status+"_"+type+"_UNIT", "YEAR");


        Log.e(TAG, "CODE : " + code+"_"+status+"_"+type);
        Log.e(TAG, "maxAge : " + maxAge);
        Log.e(TAG, "unit : " + unit);
        Log.e(TAG, "minAge : " + minAge);

        //validate max
        if (currentAge > maxAge) return false;

        //validate min
        if (unit.equalsIgnoreCase("YEAR") && currentAge < minAge){
            return false;
        }
        if (unit.equalsIgnoreCase("DAY") && ageDay < minAge){
            return false;
        }

        return true;
    }
    public static Integer getAge(Date birthDate, Date effdate){
        int age = 0;
        Calendar referenceDate = new GregorianCalendar();
        referenceDate.setTime(effdate) ;
        Calendar bdate = new GregorianCalendar();
        bdate.setTime(birthDate);

        Calendar dateToday = Calendar.getInstance();
        if ((referenceDate.get(referenceDate.DAY_OF_MONTH) >= bdate.get(bdate.DAY_OF_MONTH)) &&
                (referenceDate.get(referenceDate.MONTH) >= bdate.get(bdate.MONTH))){
            age = referenceDate.get(referenceDate.YEAR) - bdate.get(bdate.YEAR);
        }
        else if ((referenceDate.get(referenceDate.MONTH) > bdate.get(bdate.MONTH))){
            age = referenceDate.get(referenceDate.YEAR) - bdate.get(bdate.YEAR);
        } else {
            age = referenceDate.get(referenceDate.YEAR) - bdate.get(bdate.YEAR) - 1;
        }

        if (age == 0) {
            long diffInMillies = Math.abs(bdate.getTime().getTime() - referenceDate.getTime().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diff < 14) {
                age = -1;
            }
        }
        return age;
    }

    public static int getAgeDay(Date aReferenceDate){
        int age = 0;
        Calendar referenceDate = new GregorianCalendar();
        referenceDate.setTime(new Date()) ;
        Calendar bdate = new GregorianCalendar();
        bdate.setTime(aReferenceDate);

        long diff = 0;

        long diffInMillies = Math.abs(bdate.getTime().getTime() - referenceDate.getTime().getTime());
        diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff < 14) {
            age = -1;
        }
        return  (int)diff;
    }

}
