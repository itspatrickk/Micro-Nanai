package com.pioneer.microhmo.objects;

public class Address {
    private String brgy;
    private String city;
    private int id;
    private String province;

    // Constructors, getters, and setters
    public Address(String brgy, String city, int id, String province) {
        this.brgy = brgy;
        this.city = city;
        this.id = id;
        this.province = province;
    }

    public String getBrgy() {
        return brgy;
    }

    public void setBrgy(String brgy) {
        this.brgy = brgy;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}

