package com.pioneer.microhmo.objects;

public class PolicyInfo {

    private String agentid;
    private String androidid;

    private String image1;
    private String image2;

    private String currentstatus;

    public PolicyInfo() {}
    public PolicyInfo(String id, String product, String poc, String transdate, String effdate, String mistat,
                      String paymode, String provoffice, String unit, String center, String product1, String product2, String product3, String premium,
                      String image1, String image2 , String expDate ) {
        super();
        this.id = id;
        this.product = product;
        this.poc = poc;
        this.transdate = transdate;
        this.effdate = effdate;
        this.mistat = mistat;
        this.paymode = paymode;
        this.provoffice = provoffice;
        this.unit = unit;
        this.center = center;
        this.product1 = product1;
        this.product2 = product2;
        this.product3 = product3;
        this.premium = premium;
        this.setImage1(image1);
        this.setImage2(image2);
        this.setExpDate(expDate);
    }

    public PolicyInfo(String id, String product, String poc,  String image1, String image2,  String image1stat, String image2stat, String currentstatus) {
        super();
        this.id = id;
        this.product = product;
        this.poc = poc;
        this.setImage1(image1);
        this.setImage2(image2);
        this.image1stat = image1stat;
        this.image2stat = image2stat;
        this.setCurrentstatus(currentstatus);
    }

    public PolicyInfo(String product, String product1, String product2,  String product3) {
        super();
        this.product = product;
        this.product1 = product1;
        this.product2 = product2;
        this.product3 = product3;
    }
    private String transtat;

    private String image1stat;
    private String image2stat;
    private String id;
    private String product;
    private String poc;
    private String transdate;
    private String effdate;
    private String expDate;
    private String mistat;
    private String paymode;
    private String provoffice;
    private String unit;
    private String center;

    private String product1;
    private String product2;
    private String product3;
    private String product4;
    private String product5;
    private String premium;

    private String accountOfficer;
    private String authRep;
    private String unitManager;
    private String relToMember;
    private String clientType;

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getAccountOfficer() {
        return accountOfficer;
    }

    public void setAccountOfficer(String accountOfficer) {
        this.accountOfficer = accountOfficer;
    }

    public String getAuthRep() {
        return authRep;
    }

    public void setAuthRep(String authRep) {
        this.authRep = authRep;
    }

    public String getUnitManager() {
        return unitManager;
    }

    public void setUnitManager(String unitManager) {
        this.unitManager = unitManager;
    }

    public String getRelToMember() {
        return relToMember;
    }

    public void setRelToMember(String relToMember) {
        this.relToMember = relToMember;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }
    public String getPoc() {
        return poc;
    }
    public void setPoc(String poc) {
        this.poc = poc;
    }
    public String getTransdate() {
        return transdate;
    }
    public void setTransdate(String transdate) {
        this.transdate = transdate;
    }
    public String getEffdate() {
        return effdate;
    }
    public void setEffdate(String effdate) {
        this.effdate = effdate;
    }
    public String getMistat() {
        return mistat;
    }
    public void setMistat(String mistat) {
        this.mistat = mistat;
    }
    public String getPaymode() {
        return paymode;
    }
    public void setPaymode(String paymode) {
        this.paymode = paymode;
    }
    public String getProvoffice() {
        return provoffice;
    }
    public void setProvoffice(String provoffice) {
        this.provoffice = provoffice;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getCenter() {
        return center;
    }
    public void setCenter(String center) {
        this.center = center;
    }


    public String getProduct1() {
        if (product1 == null ) product1 = "";
        return product1;
    }

    public void setProduct1(String product1) {
        this.product1 = product1;
    }

    public String getProduct2() {
        if (product2 == null ) product2 = "";
        return product2;
    }

    public void setProduct2(String product2) {
        this.product2 = product2;
    }

    public String getProduct3() {
        if (product3 == null ) product3 = "";
        return product3;
    }

    public void setProduct3(String product3) {
        this.product3 = product3;
    }

    public String getProduct4() {
        return product4;
    }

    public void setProduct4(String product4) {
        this.product4 = product4;
    }

    public String getProduct5() {
        return product5;
    }

    public void setProduct5(String product5) {
        this.product5 = product5;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage1stat() {
        return image1stat;
    }

    public void setImage1stat(String image1stat) {
        this.image1stat = image1stat;
    }

    public String getImage2stat() {
        return image2stat;
    }

    public void setImage2stat(String image2stat) {
        this.image2stat = image2stat;
    }

    public String getTranstat() {
        return transtat;
    }

    public void setTranstat(String transtat) {
        this.transtat = transtat;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getAndroidid() {
        return androidid;
    }

    public void setAndroidid(String androidid) {
        this.androidid = androidid;
    }

    public String getCurrentstatus() {
        return currentstatus;
    }

    public void setCurrentstatus(String currentstatus) {
        this.currentstatus = currentstatus;
    }
}
