package com.pioneer.microhmo.objects;

public class Event {
    private String token; // reCAPTCHA token
    private String siteKey; // Your reCAPTCHA site key

    public Event(String token, String siteKey) {
        this.token = token;
        this.siteKey = siteKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }
}
