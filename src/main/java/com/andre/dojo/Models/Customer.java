package com.andre.dojo.Models;

import com.andre.dojo.Utils.DatabaseManager;

import java.time.Instant;
import java.util.List;

public class Customer {
    private long id;
    private String name;
    private String description;
    private String phoneNumber;
    private int organization_id;

    public Customer(){

    }

    public Customer(String name, String description, String phone_number) {
        this.id = Instant.now().toEpochMilli();
        this.name = name;
        this.description = description;
        this.phoneNumber = phone_number;
    }

    public static boolean addToDB(Customer customer){
        String query = """
                INSERT INTO customer (
                id, 
                name, 
                description,
                phoneNumber,
                organization_id
                ) VALUES (
                :id, 
                :name, 
                :description,
                :phoneNumber,
                :organization_id
                )""";
        return DatabaseManager.addOneData(query, customer);
    }
    public static List<Customer> getAllData(){
        String query = """
                SELECT * FROM customer
                """;
        return DatabaseManager.getListData(query, Customer.class);
    }
    public static List<Customer> getListByOrganizeID(long id){
        String query = """
                SELECT * FROM customer WHERE organization_id = :p1
                """;
        return DatabaseManager.getListData(query, Customer.class, Long.toString(id));
    }
    public static Customer getOneData(long id){
        String query = """
                SELECT * FROM customer WHERE id = :p1
                """;
        return DatabaseManager.getOneData(query, Customer.class, Long.toString(id));
    }
    public static boolean deleteOneById(long id){
        String query = """
                DELETE FROM customer WHERE id = :p1
                """;
        return DatabaseManager.deleteData(query, Long.toString(id));
    }
    public static boolean updateById(Invoice invoice){
        String query = """
                UPDATE customer SET 
                name = :name, 
                describtion = :description,
                phoneNumber = :phoneNumber,
                organization_id = :organization_id
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, invoice);
    }

    public int getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(int organization_id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
