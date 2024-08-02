package com.andre.dojo.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MetadataSave {
    private ObservableList<PersonManagement> persons = FXCollections.observableArrayList();
    private ObservableList<Organization> organizations = FXCollections.observableArrayList();

    public void setOrganizations(ObservableList<Organization> organizations) {
        this.organizations = organizations;
    }

    public void setPersons(ObservableList<PersonManagement> persons) {
        this.persons = persons;
    }

    public ObservableList<Organization> getOrganizations() {
        return organizations;
    }

    public ObservableList<PersonManagement> getPersons() {
        return persons;
    }
    public void addPerson(PersonManagement person){
        this.persons.add(person);
    }
    public void addOrganization(Organization organization){
        this.organizations.add(organization);
    }
}
