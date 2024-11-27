package com.pioneer.microhmo.objects;


public class MemberInfo {

    private String id;
    private String pertype;
    private String fname;
    private String mname;
    private String lname;
    private String suffix;
    private String dob;
    private String gender;
    private String civilstat;
    private String mobileno;
    private String status;
    private String paytype;
    private String grptag;
    private String cardmember;
    private String prov;
    private String city;
    private String brgy;
    private String street;
    private String email;
    private String placeOfBirth;
    private String nationality;
    private String reltype;
    private String acctOfficer;
    private String unitManager;
    private String center;
    private String authRepresentative;
    private String relToMember;
    private String product;
    private String product1;
    private String product2;
    private String product3;
    private String registerType;
    private String poc;
    private String clientType;
    private String institution;
    private String authRepFName;
    private String authRepMName;
    private String authRepLName;

    public String getAuthRepFName() {
        return authRepFName;
    }

    public void setAuthRepFName(String authRepFName) {
        this.authRepFName = authRepFName;
    }

    public String getAuthRepMName() {
        return authRepMName;
    }

    public void setAuthRepMName(String authRepMName) {
        this.authRepMName = authRepMName;
    }

    public String getAuthRepLName() {
        return authRepLName;
    }

    public void setAuthRepLName(String authRepLName) {
        this.authRepLName = authRepLName;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPoc() {
        return poc;
    }

    public void setPoc(String poc) {
        this.poc = poc;
    }
    private String effectivity;
    private String premium;
    private String currentstat;

    private String transtat;

    public MemberInfo() {}

    public MemberInfo(String id, String pertype, String fname, String mname, String lname, String suffix, String dob,
                      String gender, String civilstat, String mobileno,  String cardmember,
                      String prov , String city , String brgy, String street ,
                      String poc , String status , String areltype , String acctOfficer , String unitManager ,
                      String center , String authRepresentative , String relToMember , String email , String placeOfBirth , String nationality  , String clientType, String institution ,String authRepFName , String authRepMName , String authRepLName) {
        super();
        this.id = id;
        this.pertype = pertype;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.suffix = suffix;
        this.dob = dob;
        this.gender = gender;
        this.civilstat = civilstat;
        this.mobileno = mobileno;
        this.cardmember = cardmember;
        this.poc = poc;
        this.status = status;
        this.reltype = areltype;
        this.acctOfficer = acctOfficer;
        this.unitManager = unitManager ;
        this.center = center;
        this.authRepresentative = authRepresentative;
        this.relToMember = relToMember;
        this.prov = prov;
        this.city = city;
        this.brgy = brgy;
        this.street = street;
        this.email = email;
        this.placeOfBirth = placeOfBirth;
        this.nationality = nationality;
        this.clientType = clientType;
        this.institution = institution;
        this.authRepFName = authRepFName;
        this.authRepMName = authRepMName;
        this.authRepLName=authRepLName;
    }


    public MemberInfo(String id, String pertype, String fname, String mname, String lname, String suffix, String dob,
                      String gender, String civilstat, String mobileno, String status, String paytype, String grptag,
                      String cardmember, String prov, String city, String brgy, String street,String poc , String acctOfficer , String unitManager , String center , String authRepresentative , String relToMember , String email , String placeOfBirth , String nationality , String clientType , String institution) {
        super();
        this.id = id;
        this.pertype = pertype;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.suffix = suffix;
        this.dob = dob;
        this.gender = gender;
        this.civilstat = civilstat;
        this.mobileno = mobileno;
        this.status = status;
        this.paytype = paytype;
        this.grptag = grptag;
        this.cardmember = cardmember;
        this.prov = prov;
        this.city = city;
        this.brgy = brgy;
        this.street = street;
        this.poc = poc;
        this.acctOfficer = acctOfficer;
        this.unitManager = unitManager ;
        this.center = center;
        this.authRepresentative = authRepresentative;
        this.relToMember = relToMember;
        this.email = email;
        this.placeOfBirth = placeOfBirth;
        this.nationality = nationality;
    }

    public String getAcctOfficer() {
        return acctOfficer;
    }

    public void setAcctOfficer(String acctOfficer) {
        this.acctOfficer = acctOfficer;
    }

    public String getUnitManager() {
        return unitManager;
    }

    public void setUnitManager(String unitManager) {
        this.unitManager = unitManager;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getAuthRepresentative() {
        return authRepresentative;
    }

    public void setAuthRepresentative(String authRepresentative) {
        this.authRepresentative = authRepresentative;
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
    public String getPertype() {
        return pertype;
    }
    public void setPertype(String pertype) {
        this.pertype = pertype;
    }
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getMname() {
        return mname;
    }
    public void setMname(String mname) {
        this.mname = mname;
    }
    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getCivilstat() {
        return civilstat;
    }
    public void setCivilstat(String civilstat) {
        this.civilstat = civilstat;
    }
    public String getMobileno() {
        return mobileno;
    }
    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getPaytype() {
        return paytype;
    }
    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }
    public String getGrptag() {
        return grptag;
    }
    public void setGrptag(String grptag) {
        this.grptag = grptag;
    }
    public String getCardmember() {
        return cardmember;
    }
    public void setCardmember(String cardmember) {
        this.cardmember = cardmember;
    }
    public String getProv() {
        return prov;
    }
    public void setProv(String prov) {
        this.prov = prov;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getBrgy() {
        return brgy;
    }
    public void setBrgy(String brgy) {
        this.brgy = brgy;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }


    public String getProduct1() {
        return product1;
    }

    public void setProduct1(String product1) {
        this.product1 = product1;
    }

    public String getProduct2() {
        return product2;
    }

    public void setProduct2(String product2) {
        this.product2 = product2;
    }

    public String getProduct3() {
        return product3;
    }

    public void setProduct3(String product3) {
        this.product3 = product3;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getReltype() {
        return reltype;
    }

    public void setReltype(String reltype) {
        this.reltype = reltype;
    }

    public String getEffectivity() {
        return effectivity;
    }

    public void setEffectivity(String effectivity) {
        this.effectivity = effectivity;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getCurrentstat() {
        return currentstat;
    }

    public void setCurrentstat(String currentstat) {
        this.currentstat = currentstat;
    }

    public String getTranstat() {
        return transtat;
    }

    public void setTranstat(String transtat) {
        this.transtat = transtat;
    }
}
