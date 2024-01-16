package com.pioneer.nanaiv3;

public class ItemPerson {
    private String name;
    private String dob;
    private String effdate;
    private String poc;
    private String product;


    public ItemPerson(String name, String dob, String effdate, String poc, String product) {
        this.name = name;
        this.dob = dob;
        this.effdate = effdate;
        this.poc = poc;
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEffdate() {
        return effdate;
    }

    public void setEffdate(String effdate) {
        this.effdate = effdate;
    }

    public String getPoc() {
        return poc;
    }

    public void setPoc(String poc) {
        this.poc = poc;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
