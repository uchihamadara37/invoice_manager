package com.andre.dojo.Models;

import com.andre.dojo.Utils.DatabaseManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.Instant;
import java.util.List;

public class Personal {
    private long id;
    private String name;
    private String bankName;
    private String bankIDNumber;
    private String bankIDName;
    private String urlTtd;
    private long organization_id;


    public Personal(){
    }

    public Personal(String name, String bank_name, String bank_id_number, String bank_id_name, String urlTtd, long organization_id) {
        this.id = Instant.now().toEpochMilli();
        this.name = name;
        this.bankName = bank_name;
        this.bankIDNumber = bank_id_number;
        this.bankIDName = bank_id_name;
        this.organization_id = organization_id;
        this.urlTtd = urlTtd;
    }

    public Personal(String name, String bank_name, String bank_id_number, String bank_id_name, String urlTtd, long organization_id, long id) {
        this.id = id;
        this.name = name;
        this.bankName = bank_name;
        this.bankIDNumber = bank_id_number;
        this.bankIDName = bank_id_name;
        this.organization_id = organization_id;
        this.urlTtd = urlTtd;
    }

    public static boolean addToDB(Personal personal){
        String query = """
                INSERT INTO personal (
                id, 
                name, 
                bankName, 
                bankIDNumber, 
                bankIDName, 
                urlTtd,
                organization_id 
                ) VALUES (
                :id, 
                :name, 
                :bankName, 
                :bankIDNumber, 
                :bankIDName, 
                :urlTtd,
                :organization_id
                )""";
        return DatabaseManager.addOneData(query, personal);
    }
    public static List<Personal> getAllData(){
        String query = """
                SELECT * FROM personal
                """;
        return DatabaseManager.getListData(query, Personal.class);
    }
    public static Personal getOneData(long id){
        String query = """
                SELECT * FROM personal WHERE id = :p1
                """;
        return DatabaseManager.getOneData(query, Personal.class, Long.toString(id));
    }
    public static Personal getOneDataByOrganizeId(long organization_id){
        String query = """
                SELECT * FROM personal WHERE organization_id = :p1
                """;
        return DatabaseManager.getOneData(query, Personal.class, Long.toString(organization_id));
    }
    public static boolean deleteOneById(long id){
        String query = """
                DELETE FROM personal WHERE id = :p1
                """;
        return DatabaseManager.deleteData(query, Long.toString(id));
    }
    public static boolean updateById(Personal personal){
        String query = """
                UPDATE personal SET 
                name = :name, 
                bankName = :bankName, 
                bankIDNumber = :bankIDNumber, 
                bankIDName = :bankIDName, 
                organization_id = :organization_id,
                urlTtd = :urlTtd 
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, personal);
    }

    public String getUrlTtd() {
        return urlTtd;
    }

    public void setUrlTtd(String urlTtd) {
        this.urlTtd = urlTtd;
    }

    public long getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(long organization_id) {
        this.organization_id = organization_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankIDNumber() {
        return bankIDNumber;
    }

    public void setBankIDNumber(String bankIDNumber) {
        this.bankIDNumber = bankIDNumber;
    }

    public String getBankIDName() {
        return bankIDName;
    }

    public void setBankIDName(String bankIDName) {
        this.bankIDName = bankIDName;
    }
}

