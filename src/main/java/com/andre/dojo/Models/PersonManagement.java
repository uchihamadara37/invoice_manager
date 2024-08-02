package com.andre.dojo.Models;

import com.google.gson.annotations.SerializedName;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PersonManagement {
    private StringProperty id;
    private StringProperty name;
    private StringProperty noRekening;
    private StringProperty bank;
    private StringProperty mobilePhone;

    public PersonManagement(){

    }

    public PersonManagement(String name, String mobilePhone, String bank_number, String bank_name) {
        this.id = new SimpleStringProperty(String.valueOf(System.currentTimeMillis()));
        System.out.println(id.get());
        this.name = new SimpleStringProperty(name);
        this.noRekening = new SimpleStringProperty(bank_number);
        this.bank = new SimpleStringProperty(bank_name);
        this.mobilePhone = new SimpleStringProperty(mobilePhone);
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public void setBank(String bank) {
        this.bank = new SimpleStringProperty(bank);
    }

    public void setNoRekening(String no_rekening) {
        this.noRekening = new SimpleStringProperty(no_rekening);
    }
    public String getId() {
        return id.get();
    }
    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }

    public String getName() {
        return name.get();
    }

    public String getBank() {
        return bank.get();
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = new SimpleStringProperty(mobilePhone);
    }

    public String getMobilePhone() {
        return mobilePhone.get();
    }

    public String getNoRekening() {
        return noRekening.get();
    }

    public PersonManagementOri toOriData(){
        return new PersonManagementOri(
            getId(), getName(), getNoRekening(), getBank(), getMobilePhone()
        );
    }
    public PersonManagement toFXData(PersonManagementOri personOri){
        PersonManagement p = new PersonManagement();
        p.name = new SimpleStringProperty(personOri.getName());
        p.mobilePhone = new SimpleStringProperty(personOri.getMobilePhone());
        p.noRekening = new SimpleStringProperty(personOri.getNoRekening());
        p.bank = new SimpleStringProperty(personOri.getBank());
        p.id = new SimpleStringProperty(personOri.getId());
        return p;
    }
}

