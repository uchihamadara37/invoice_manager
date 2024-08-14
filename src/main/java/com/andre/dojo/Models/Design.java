package com.andre.dojo.Models;

import com.andre.dojo.Utils.DatabaseManager;

import java.util.List;

public class Design {
    private long id;
    private String jrxml;

    public Design(){}

    public Design(long id, String jrxml) {
        this.id = id;
        this.jrxml = jrxml;
    }

    public static boolean addToDB(Design design){
        String query = """
                INSERT INTO design (
                id, 
                jrxml
                ) VALUES (
                :id, 
                :jrxml
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
    public static boolean deleteOneById(long id){
        String query = """
                DELETE FROM design WHERE id = :p1
                """;
        return DatabaseManager.deleteData(query, Long.toString(id));
    }
    public static boolean updateById(Design design){
        String query = """
                UPDATE design SET 
                jrxml = :jrxml
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, design);
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
