package com.andre.dojo.Models;

import com.andre.dojo.Utils.DatabaseManager;

import java.time.Instant;
import java.util.List;

public class Bank {
    private long id;
    private String bank_id;
    private String bank_name;
    private String owner;

    public Bank(){

    }

    public Bank(String bank_id, String bank_name, String owner) {
        this.id = Instant.now().toEpochMilli();
        this.bank_id = bank_id;
        this.bank_name = bank_name;
        this.owner = owner;
    }

    public static boolean addToDB(Bank bank){
        String query = """
                INSERT INTO bank (
                id,
                bank_name,
                owner,
                bank_id
                ) VALUES (
                :id,
                :bank_name,
                :owner,
                :bank_id
                )""";
        return DatabaseManager.addOneData(query, bank);
    }

    public static List<Bank> getAllData(){
        String query = """
                SELECT * FROM bank
                """;
        return DatabaseManager.getListData(query, Bank.class);
    }

    public static boolean deleteOneById(long id){
        String query = """
                DELETE FROM bank WHERE id = :p1
                """;
        return DatabaseManager.deleteData(query, Long.toString(id));
    }

    public static boolean updateById(Bank bank){
        String query = """
                UPDATE bank SET 
                bank_name = :bank_name, 
                owner = :owner,
                bank_id = :bank_id
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, bank);
    }

    public static Bank getOneData(long id){
        String query = """
                SELECT * FROM bank WHERE id = :p1
                """;
        return DatabaseManager.getOneData(query, Bank.class, Long.toString(id));
    }

    public static Bank getOneDataByName(String bank_name){
        String query = """
                SELECT * FROM bank WHERE bank_name = :p1
                """;
        return DatabaseManager.getOneData(query, Bank.class, bank_name);
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}