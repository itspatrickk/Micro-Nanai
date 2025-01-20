package com.pioneer.microhmo.util;

public class Statics {


    public static String headerValue =
        //prod
       //"Basic RE1lYTVLTWEzRGE0OWZXUXFrSlBUM250NWxrYToyT253QmxfRjlqZF9HcHk4R1JuTmZzX0tONDhRSGhIaGtwbHdpb2k4QVlvYQ==";
      //UAT
      //"Basic bnVlVkJfQ091ejkxR0dnRUh0ZkN1S1VydXFrYTpUZjZzNlk4ZVBwR0FvSmFBRnRHN0RReGE3VG9h";

         //NEW UAT
      "Basic bFBHYWxkWnhyb0VsY0hLSVZmcUpwcFFfbFU0YTp0RFd5T2pPZU1YZHFaZkhsYWhMZzhEWGhnaTNkcVZuOHZnaXYyT3lpTjI4YQ==";

    public static String BASE_URL =
            //prod
        // "https://apiprod.pioneer.com.ph/hodu/nan.ai/v1.0";


    //LOCALHOST not working in office check in ipconfig <CMD>
  //"http://192.168.1.21:8000";

    //server
    //"http://172.21.1.145:7000";

            //OLD API
   //"https://api2.pioneer.com.ph/dev-microhmo/2.0";

    // NEW API
    "https://api-dev.pioneer.com.ph/hodu/nanaihmo/v1";

    public static String CRED_URL =
    //"https://api.asgardeo.io/t/pioneer/oauth2/token?grant_type=client_credentials";
    //"https://api2.pioneer.com.ph/token?grant_type=client_credentials";

    "https://api-dev.pioneer.com.ph/hodu/token/v1.0/oauth2/token?grant_type=client_credentials";


   // public static String SEND_URL = BASE_URL + "/api/v1/nanai-hmo/app/save/";
    public static String SEND_URL = BASE_URL + "/api/v1/nanai-hmo/app/register";
    public static String uploadURL = BASE_URL + "/api/v1/nanai-hmo/app/";
    public static String LOV_URL = BASE_URL + "/api/v1/nanai-hmo/lovs";
    public static String OTP_URL = BASE_URL + "/api/v1/nanai-hmo/app/otp/";

    public static String AGENTINFO_URL = BASE_URL + "/api/v1/nanai-hmo/app/agentinfo/";
    public static  String UPDATE_URL = BASE_URL + "/api/v1/nanai-hmo/app/update";
    public static String VALIDATE_URL = BASE_URL + "/api/v1/nanai-hmo/app/validateOtp";
    public static String SYNC_URL =  BASE_URL + "/api/v1/nanai-hmo/app/";



    //LOCAL CONNECTION
    public static String BASE_URL_HMO = "http://192.168.1.3:7000";
    public static String API_KEY_TEST = "Basic bmFuYWk6bmFuYWlAMjAyMQ==";


}

