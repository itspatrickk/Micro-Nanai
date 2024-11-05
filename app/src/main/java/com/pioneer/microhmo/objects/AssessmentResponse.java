package com.pioneer.microhmo.objects;

public class AssessmentResponse {
    // Define fields based on the API response.
    // For example, you might have:
    private String name; // Assessment name
    private String score; // The score given by reCAPTCHA
    // Add other fields as needed.

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
