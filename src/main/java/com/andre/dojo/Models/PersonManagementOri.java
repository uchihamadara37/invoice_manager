package com.andre.dojo.Models;

public class PersonManagementOri{
    private String id;
    private String name;
    private String noRekening;
    private String bank;
    private String mobilePhone;

    public PersonManagementOri(String id, String name, String noRekening, String bank, String mobilePhone) {
        this.id = id;
        this.name = name;
        this.noRekening = noRekening;
        this.bank = bank;
        this.mobilePhone = mobilePhone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNoRekening(String noRekening) {
        this.noRekening = noRekening;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNoRekening() {
        return noRekening;
    }

    public String getBank() {
        return bank;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }
}
