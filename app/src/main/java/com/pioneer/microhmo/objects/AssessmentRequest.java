package com.pioneer.microhmo.objects;

public class AssessmentRequest {
    private String event;
    private String token;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
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

    private String siteKey;

    public AssessmentRequest(String token, String siteKey) {
        this.token = token;
        this.siteKey = siteKey;
        this.event = createEvent(); // You can create an event object here if needed
    }

    private String createEvent() {
        // Create your event object here. This is typically a JSON representation
        // that includes information about the action the user is taking.
        // Example:
        return "{ \"action\": \"login\", \"token\": \"" + token + "\" }";
    }
}
