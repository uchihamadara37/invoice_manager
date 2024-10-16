package com.andre.dojo.Models;

import com.andre.dojo.Utils.DatabaseManager;

import java.time.Instant;
import java.util.List;

public class Config {
    private long id;
    private String name;
    private String dir;

    public Config(){

    }
    public Config(long id){
        this.id = id;
    }

    public Config(String name, String dir) {
        this.id = Instant.now().toEpochMilli();
        this.name = name;
        this.dir = dir;
    }

    public static boolean addToDB(Config config){
        String query = """
                INSERT INTO config (
                id,
                name,
                dir
                ) VALUES (
                :id,
                :name,
                :dir
                )""";
        return DatabaseManager.addOneData(query, config);
    }
    public static List<Config> getAllData(){
        String query = """
                SELECT * FROM config
                """;
        return DatabaseManager.getListData(query, Config.class);
    }

    public static boolean deleteOneById(long id){
        String query = """
                DELETE FROM config WHERE id = :p1
                """;
        return DatabaseManager.deleteData(query, Long.toString(id));
    }

    public static boolean updateById(Config config){
        String query = """
                UPDATE config SET 
                name = :name, 
                dir = :dir
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, config);
    }

    public static Config getFirstData(){
        String query = """
                SELECT * FROM config LIMIT 1
                """;
        return DatabaseManager.getOneData(query, Config.class);
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

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
