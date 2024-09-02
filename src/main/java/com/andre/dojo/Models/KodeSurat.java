package com.andre.dojo.Models;

import com.andre.dojo.Utils.DatabaseManager;
import javafx.scene.input.KeyCode;

import java.time.Instant;
import java.util.List;

public class KodeSurat {
    private long id;
    private String kode;
    private int noUrut;
    private long organization_id;

    public KodeSurat(){

    }

    public KodeSurat(long id, int noUrut){
        this.id = id;
        this.noUrut = noUrut;
    }

    public KodeSurat(long id, String kode, int noUrut, long organization_id){
        this.id = id;
        this.kode = kode;
        this.noUrut = noUrut;
        this.organization_id = organization_id;
    }

    public KodeSurat(String kode, int no_urut, long organization_id) {
        this.id = Instant.now().toEpochMilli();
        this.kode = kode;
        this.noUrut = no_urut;
        this.organization_id = organization_id;
    }

    public static boolean addToDB(KodeSurat kodeSurat){
        String query = """
                INSERT INTO kode_surat (
                id, 
                kode, 
                noUrut,
                organization_id
                ) VALUES (
                :id, 
                :kode, 
                :noUrut,
                :organization_id
                )""";
        return DatabaseManager.addOneData(query, kodeSurat);
    }
    public static List<KodeSurat> getAllData(){
        String query = """
                SELECT * FROM kode_surat
                """;
        return DatabaseManager.getListData(query, KodeSurat.class);
    }
    public static KodeSurat getOneData(long id){
        String query = """
                SELECT * FROM kode_surat WHERE id = :p1
                """;
        return DatabaseManager.getOneData(query, KodeSurat.class, Long.toString(id));
    }
    public static boolean deleteOneById(long id){
        String query = """
                DELETE FROM kode_surat WHERE id = :p1
                """;
        return DatabaseManager.deleteData(query, Long.toString(id));
    }
    public static boolean updateById(KodeSurat kodeSurat, long id){
        String query = """
                UPDATE kode_surat SET
                id = :id,
                kode = :kode,
                noUrut = :noUrut,
                organization_id = :organization_id
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, kodeSurat);
    }
    public static boolean updateNumber(KodeSurat kodeSurat, long id){
        String query = """
                UPDATE kode_surat SET
                id = :id,
                noUrut = :noUrut
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, kodeSurat);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public long getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(int organization_id) {
        this.organization_id = organization_id;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public int getNoUrut() {
        return noUrut;
    }

    public void setNoUrut(int noUrut) {
        this.noUrut = noUrut;
    }
}
