package com.andre.dojo.Models;

import com.andre.dojo.Utils.DatabaseManager;

import java.time.Instant;
import java.util.List;

public class Design {
    private long id;
    private String jrxml;
    private String dirImage;
    private String dirPdf;

    public Design(){}

    public Design(String jrxml, String dirImage, String dirPdf) {
        this.id = Instant.now().toEpochMilli();
        this.jrxml = jrxml;
        this.dirImage = dirImage;
        this.dirPdf = dirPdf;

    }

    public static boolean addToDB(Design design){
        String query = """
                INSERT INTO design (
                id, 
                jrxml,
                dirImage,
                dirPdf
                ) VALUES (
                :id, 
                :jrxml,
                :dirImage,
                :dirPdf
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
                dirImage = :dirImage,
                dirPdf = :dirPdf
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, design);
    }

    public static boolean isAnyInvoiceUseThisDesign(long id_design){
        String query = """
                SELECT * FROM invoice WHERE jrxml_id = :p1
                """;
        Invoice in= DatabaseManager.getOneData(query, Invoice.class, Long.toString(id_design));
        if (in != null){
            return true;
        }else{
            return false;
        }
    }

    public String getDirImage() {
        return dirImage;
    }

    public void setDirImage(String dirImage) {
        this.dirImage = dirImage;
    }

    public String getDirPdf() {
        return dirPdf;
    }

    public void setDirPdf(String dirPdf) {
        this.dirPdf = dirPdf;
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
