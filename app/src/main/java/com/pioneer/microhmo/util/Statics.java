package com.pioneer.microhmo.util;

public class Statics {

//    public static String testheader = "Basic bXY5VGh0MDhpQ1NlaTBuMF9uR2NGa2RVNGU4YTpMTEtHOWYwaklLWkQ2cDdTSEZNNkVWZjV3bzQ1bk9oTUNUZnhzd3kzUDhJYQ==";
//    public static String liveheader = "Basic RE1lYTVLTWEzRGE0OWZXUXFrSlBUM250NWxrYToyT253QmxfRjlqZF9HcHk4R1JuTmZzX0tONDhRSGhIaGtwbHdpb2k4QVlvYQ==";
//    public static String URL_PROD = "https://73b1dfa2-5f88-47e1-a46f-c4a3b288afb7-prod.e1-us-east-azure.choreoapis.dev/hodu/nan.ai/v1";
//    public static String URL_TEST = "https://73b1dfa2-5f88-47e1-a46f-c4a3b288afb7-dev.e1-us-east-azure.choreoapis.dev/hodu/nan.ai/v1";
//    public static String CRED_URL = "https://api.asgardeo.io/t/pioneer/oauth2/token?grant_type=client_credentials";
//
//    public static String headerValue = testheader;
//    public static String BASE_URL = URL_TEST;
//    curl -k -X POST https://api.asgardeo.io/t/pioneer/oauth2/token -d "grant_type=client_credentials" -H "Authorization: Basic RE1lYTVLTWEzRGE0OWZXUXFrSlBUM250NWxrYToyT253QmxfRjlqZF9HcHk4R1JuTmZzX0tONDhRSGhIaGtwbHdpb2k4QVlvYQ=="
    public static String headerValue =
        //prod
       //"Basic RE1lYTVLTWEzRGE0OWZXUXFrSlBUM250NWxrYToyT253QmxfRjlqZF9HcHk4R1JuTmZzX0tONDhRSGhIaGtwbHdpb2k4QVlvYQ==";
    //UAT
   "Basic bnVlVkJfQ091ejkxR0dnRUh0ZkN1S1VydXFrYTpUZjZzNlk4ZVBwR0FvSmFBRnRHN0RReGE3VG9h";
    public static String BASE_URL =
            //prod
        // "https://apiprod.pioneer.com.ph/hodu/nan.ai/v1.0";
    //uat
            //  "https://api2.pioneer.com.ph/dev-microhmo/2.0";

    //LOCALHOST not working in office check in ipconfig <CMD>
   //"http://192.168.1.6:8000";

    //server
    //"http://172.21.1.145:7000";

    "https://api2.pioneer.com.ph/dev-microhmo/2.0";

    //"https://73b1dfa2-5f88-47e1-a46f-c4a3b288afb7-prod.e1-us-east-azure.choreoapis.dev/hodu/nan.ai/v1";
    public static String CRED_URL =
    //"https://api.asgardeo.io/t/pioneer/oauth2/token?grant_type=client_credentials";
    "https://api2.pioneer.com.ph/token?grant_type=client_credentials";



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

