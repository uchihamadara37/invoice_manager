package com.andre.dojo.Models;

import com.andre.dojo.Utils.DatabaseManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.chart.PieChart;

import java.time.Instant;
import java.util.List;

public class Organization {
    private long id;
    private String logo;
    private String brandName;
    private String description;
    private String address;
    private String email;
    private int noUrutInstansi;
    private String kodeInstansi;
    private int tahunOperasi;
    private int totalLetter;
    private Personal personal;

    public Organization(){}

    public Organization(long id, int totalLetter, String brandName){
        this.id = id;
        this.totalLetter = totalLetter;
        this.brandName = brandName;
    }

    public Organization(long id, String logo, String brandName, String description, String address, String email, int noUrutInstansi, String kodeInstansi, int tahunOperasi) {
        this.id = id;
        this.logo = logo;
        this.brandName = brandName;
        this.description = description;
        this.address = address;
        this.email = email;
        this.noUrutInstansi = noUrutInstansi;
        this.tahunOperasi = tahunOperasi;
        this.totalLetter = 0;
        this.kodeInstansi = kodeInstansi;
    }

    public static boolean addToDB(Organization organization){
        String query = """
                INSERT INTO organization (
                id, 
                logo, 
                brandName, 
                description, 
                address, 
                email, 
                noUrutInstansi, 
                tahunOperasi.
                totalLetter
                ) VALUES (
                :id, 
                :logo, 
                :brandName, 
                :description, 
                :address, 
                :email, 
                :noUrutInstansi, 
                :tahunOperasi,
                :totalLetter
                )""";
        return DatabaseManager.addOneData(query, organization);
    }
    public static List<Organization> getAllData(){
        String query = """
                SELECT * FROM organization
                """;
        return DatabaseManager.getListData(query, Organization.class);
    }
    public static Organization getOneData(long id){
        String query = """
                SELECT * FROM organization WHERE id = :p1
                """;
        return DatabaseManager.getOneData(query, Organization.class, Long.toString(id));
    }
    public static boolean deleteOneById(long id){
        String query = """
                DELETE FROM organization WHERE id = :p1
                """;
        return DatabaseManager.deleteData(query, Long.toString(id));
    }
    public static boolean updateById(Organization organization){
        String query = """
                UPDATE organization SET
                logo = :logo, 
                brandName = :brandName, 
                description = :description, 
                address = :address, 
                email = :email, 
                noUrutInstansi = :noUrutInstansi, 
                tahunOperasi = :tahunOperasi,
                totalLetter = :totalLetter
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, organization);
    }
    public static boolean updateTotalLetter(Organization organization){
        String query = """
                UPDATE organization SET
                totalLetter = :totalLetter
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, organization);
    }
    public Organization(String brand_name) {
        this.id = Instant.now().toEpochMilli();
        this.brandName = brand_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setKodeInstansi(String kodeInstansi) {
        this.kodeInstansi = kodeInstansi;
    }

    public String getKodeInstansi() {
        return kodeInstansi;
    }

    public int getNoUrutInstansi() {
        return noUrutInstansi;
    }

    public void setNoUrutInstansi(int noUrutInstansi) {
        this.noUrutInstansi = noUrutInstansi;
    }

    public int getTahunOperasi() {
        return tahunOperasi;
    }

    public void setTahunOperasi(int tahunOperasi) {
        this.tahunOperasi = tahunOperasi;
    }

    public int getTotalLetter() {
        return totalLetter;
    }

    public void setTotalLetter(int totalLetter) {
        this.totalLetter = totalLetter;
    }

    public Personal getPersonal() {
        return Personal.getOneDataByOrganizeId(id);
    }

    public void setPersonal(Personal personManagement) {
        if (personManagement != null && personManagement.getOrganization_id() == id){
            Personal.addToDB(personManagement);
        }
    }
}
