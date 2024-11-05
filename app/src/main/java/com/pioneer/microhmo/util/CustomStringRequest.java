package com.pioneer.microhmo.util;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class CustomStringRequest  extends StringRequest {
    private final Response.Listener<String> listener;
    private final Response.ErrorListener errorListener;

    public CustomStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.listener = listener;
        this.errorListener = errorListener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode == 429) {
            // Trigger the error listener for the specific 429 status code
            return Response.error(new VolleyError("OTP request limit exceeded. Please try again in 2 minutes."));
        }

        return super.parseNetworkResponse(response);
    }
}
