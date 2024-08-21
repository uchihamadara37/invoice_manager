package com.andre.dojo.Models;

import com.andre.dojo.Utils.DatabaseManager;

import java.time.Instant;
import java.util.List;

public class Design {
    private long id;
    private String jrxml;
    private String first_invoice_id;

    public Design(){}

    public Design(String jrxml, String invoice_id) {
        this.id = Instant.now().toEpochMilli();
        this.jrxml = jrxml;
        this.first_invoice_id = invoice_id;
    }

    public static boolean addToDB(Design design){
        String query = """
                INSERT INTO design (
                id, 
                jrxml,
                first_invoice_id
                ) VALUES (
                :id, 
                :jrxml,
                :first_invoie_id
                )""";
        return DatabaseManager.addOneData(query, design);
    }
    public static List<Design> getAllData(){
        String query = """
                SELECT * FROM design
                """;
        return DatabaseManager.getListData(query, Design.class);
    }

    public static Design getOneData(long id){
        String query = """
                SELECT * FROM design WHERE id = :p1
                """;
        return DatabaseManager.getOneData(query, Design.class, Long.toString(id));
    }
    public static Design getOneByInvoiceFirst(long invoice_id){
        String query = """
                SELECT * FROM design WHERE first_invoice_id = :p1
                """;
        return DatabaseManager.getOneData(query, Design.class, Long.toString(invoice_id));
    }
    public static boolean deleteOneById(long id){
        String query = """
                DELETE FROM design WHERE id = :p1
                """;
        return DatabaseManager.deleteData(query, Long.toString(id));
    }
    public static boolean updateById(Design design){
        String query = """
                UPDATE design SET 
                jrxml = :jrxml,
                first_invoice_id = :first_invoice_id
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, design);
    }

    public String getFirst_invoice_id() {
        return first_invoice_id;
    }

    public void setFirst_invoice_id(String first_invoice_id) {
        this.first_invoice_id = first_invoice_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJrxml() {
        return jrxml;
    }

    public void setJrxml(String jrxml) {
        this.jrxml = jrxml;
    }
}
