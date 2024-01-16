package com.pioneer.nanaiv3.util;

public class ApiUtil {

    static String CRED_URL = "https://api2.pioneer.com.ph/token?grant_type=client_credentials";
    static String OTP_URL = "https://api2.pioneer.com.ph/uat-nanaiapp/2.0/api/v1/nanai/app/otp/";
    static String headerValue = "Basic VlVLMG9WVDVSSGJWNTN5aEVLaWtpNkJMNFRVYTo3cTJmVVdjZ21WMnFvYkxoZnowTVVoTWJwQ3Nh";

    static String accessToken = null;
//
//    public static void activateAccount(Context context, final ProgressBar progressBar, final String mobileNo, final TextView messageView){
//
//        progressBar.setVisibility(View.VISIBLE);
//        RequestQueue queue = Volley.newRequestQueue(context);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, CRED_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject json = new JSONObject(response);
//                            accessToken = json.getString("access_token");
//
//                            SharedPreferencesUtility.saveToken(context, accessToken);
//
//                            activateMobileno(context, mobileNo, messageView);
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//
//                        progressBar.setVisibility(View.GONE);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();
//                    }
//                }) {
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                // Calculate the progress based on the response data
//                long contentLength = response.data.length;
//                long transferredBytes = 0;
//
//                // Update your progress bar with the calculated progress value
//                int progress = (int) ((transferredBytes / (float) contentLength) * 100);
//                 progressBar.setProgress(progress);
//
//                // Return the response to be delivered
//                return super.parseNetworkResponse(response);
//            }
//
//            @Override
//            protected void deliverResponse(String response) {
//                // Handle the response
//                super.deliverResponse(response);
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", headerValue);
//                return headers;
//            }
//        };
//
//        queue.add(stringRequest);
//    }
//
//    public static void activateMobileno(Context context, String mobileNo,  TextView messageView){
//        RequestQueue queue = Volley.newRequestQueue(context);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, OTP_URL+mobileNo,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            Gson gson = new Gson();
//                            AgentInfo agent = gson.fromJson(response, AgentInfo.class);
//                            String message = "Magandang araw "+agent.getFullname()+
//                                    ", ilagay ang OTP na pinadala sa iyong numero";
//
//                            messageView.setText(message);
//
//                            SharedPreferencesUtility.saveAgentName(context, agent.getFullname());
//                            SharedPreferencesUtility.saveReference(context, agent.getReference());
//                            SharedPreferencesUtility.saveAgentId(context, agent.getKyid());
//
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();
//                    }
//                })  {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer "+accessToken);
//                return headers;
//            }
//        };
//
//        queue.add(stringRequest);
//    }


}
