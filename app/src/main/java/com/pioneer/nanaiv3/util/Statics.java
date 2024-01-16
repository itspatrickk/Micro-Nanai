package com.pioneer.nanaiv3.util;

import com.pioneer.nanaiv3.databinding.ItemMemberSalesBinding;

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
    public static String headerValue = "Basic RE1lYTVLTWEzRGE0OWZXUXFrSlBUM250NWxrYToyT253QmxfRjlqZF9HcHk4R1JuTmZzX0tONDhRSGhIaGtwbHdpb2k4QVlvYQ==";
    //"Basic V1pkZkVhZ05obElWTUQ4TWZDZDhiYTVBM3MwYTpPZmVIQnoxTTFSY1dzV1NhX28zbTdJZlZ3VVhySTcyNVFVcHl3S182ZTBjYQ==";
    // "Basic VlVLMG9WVDVSSGJWNTN5aEVLaWtpNkJMNFRVYTo3cTJmVVdjZ21WMnFvYkxoZnowTVVoTWJwQ3Nh";

    public static String BASE_URL = "https://apiprod.pioneer.com.ph/hodu/nan.ai/v1.0";
            //"https://73b1dfa2-5f88-47e1-a46f-c4a3b288afb7-prod.e1-us-east-azure.choreoapis.dev/hodu/nan.ai/v1";
    //https://api-dev.pioneer.com.ph/hodu/nan.ai/v1.0";
    //https://api2.pioneer.com.ph/uat-nanaiapp/2.0";
    public static String CRED_URL = "https://api.asgardeo.io/t/pioneer/oauth2/token?grant_type=client_credentials";
    //https://api2.pioneer.com.ph/token?grant_type=client_credentials";


    public static String SEND_URL = BASE_URL + "/api/v1/nanai/app/save/";
    public static String uploadURL = BASE_URL + "/api/v1/nanai/app/";
    public static String LOV_URL = BASE_URL + "/api/v1/nanai/lovs";
    public static String OTP_URL = BASE_URL + "/api/v1/nanai/app/otp/";

    public static String AGENTINFO_URL = BASE_URL + "/api/v1/nanai/app/agentinfo/";
    public static  String UPDATE_URL = BASE_URL + "/api/v1/nanai/app/update";
    public static String VALIDATE_URL = BASE_URL + "/api/v1/nanai/app/validateOtp";
    public static String SYNC_URL =  BASE_URL + "/api/v1/nanai/app/";
}

