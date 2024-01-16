package com.pioneer.nanaiv3.objects;

import java.util.List;

public class AgentInfo {

    private String centers;

    public String getCenters() {
        return centers;
    }
    public void setCenters(String centers) {
        this.centers = centers;
    }

    public AgentInfo() {}

    public AgentInfo(String kyid, String mobileno, String fullname, String id) {
        super();
        this.kyid = kyid;
        this.mobileno = mobileno;
        this.fullname = fullname;
        this.id = id;
    }


    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    private String reference;
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public String getKyid() {
        return kyid;
    }
    public void setKyid(String kyid) {
        this.kyid = kyid;
    }
    public String getMobileno() {
        return mobileno;
    }
    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    private String kyid;
    private String mobileno;
    private String fullname;


}
