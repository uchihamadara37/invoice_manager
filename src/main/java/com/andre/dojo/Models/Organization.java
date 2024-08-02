package com.andre.dojo.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Organization {
    private StringProperty id;
    private StringProperty name;
    private StringProperty address;
    private StringProperty email;
    private PersonManagement personManagement;

    public Organization(){

    }

    public Organization(String name, String address, String email) {
        this.id = new SimpleStringProperty(String.valueOf(System.currentTimeMillis()));
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.email = new SimpleStringProperty(email);
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public void setAddress(String address) {
        this.address = new SimpleStringProperty(address);
    }

    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }

    public void setEmail(String email) {
        this.email = new SimpleStringProperty(email);
    }

    public void setPersonManagement(PersonManagement personManagement) {
        this.personManagement = personManagement;
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getEmail() {
        return email.get();
    }

    public PersonManagement getPersonManagement() {
        return personManagement;
    }
}
