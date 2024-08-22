package com.andre.dojo.Utils;

import com.andre.dojo.Models.Organization;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_NAME = "mydatabase.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;
    private static final Sql2o sql2o = new Sql2o(DB_URL, "", "");

    public static void createNewDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
        }catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("Database baru telah dibuat.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTableOrganization() {
        String sql = "CREATE TABLE IF NOT EXISTS organization (\n" +
                "    id INTEGER PRIMARY KEY,\n" +
                "    logo TEXT,\n" +
                "    brandName TEXT NOT NULL,\n" +
                "    description TEXT,\n" +
                "    address TEXT,\n" +
                "    email TEXT,\n" +
                "    noUrutInstansi INTEGER,\n" +
                "    tahunOperasi INTEGER\n" +
                ");";

        try (
                Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
            stmt.close();
            conn.close();
            System.out.println("Tabel users telah dibuat.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTableCustomer() {
        String sql = "CREATE TABLE IF NOT EXISTS customer (\n" +
                "    id INTEGER PRIMARY KEY,\n" +
                "    name TEXT NOT NULL,\n" +
                "    description TEXT NOT NULL,\n" +
                "    phoneNumber TEXT,\n" +
                "    organization_id INTEGER NOT NULL, \n"+
                "    FOREIGN KEY (organization_id) REFERENCES organization(id)"+
                ");";

        try (
                Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
            stmt.close();
            conn.close();
            System.out.println("Tabel customer telah dibuat.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTableKodeSurat() {
        String sql = "CREATE TABLE IF NOT EXISTS kode_surat (\n" +
                "    id INTEGER PRIMARY KEY,\n" +
                "    kode TEXT NOT NULL,\n" +
                "    noUrut INTEGER NOT NULL,\n" +
                "    organization_id INTEGER NOT NULL, \n"+
                "    FOREIGN KEY (organization_id) REFERENCES organization(id)"+
                ");";

        try (
                Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
            stmt.close();
            conn.close();
            System.out.println("Tabel kode_surat telah dibuat.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTablePerson() {
        String sql = "CREATE TABLE IF NOT EXISTS personal (\n" +
                "    id INTEGER PRIMARY KEY,\n" +
                "    name TEXT NOT NULL,\n" +
                "    bankName TEXT,\n" +
                "    bankIDNumber TEXT,\n" +
                "    bankIDName TEXT,\n" +
                "    organization_id INTEGER NOT NULL, \n"+
                "    FOREIGN KEY (organization_id) REFERENCES organization(id)"+
                ");";

        try (
                Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
            stmt.close();
            conn.close();
            System.out.println("Tabel personal telah dibuat.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTableInvoice() {
        String sql = "CREATE TABLE IF NOT EXISTS invoice (\n" +
                "    id INTEGER PRIMARY KEY,\n" +
                "    invoiceMarkText TEXT,\n" +
                "    date TEXT,\n" +
                "    totalPriceAll INTEGER,\n" +
                "    jsonData TEXT,\n" +
                "    pdfUrl TEXT,\n" +
                "    timestamp TEXT,\n" +
                "    jrxml_id INTEGER,\n" +
                "    customer_id INTEGER NOT NULL, \n"+
                "    FOREIGN KEY (customer_id) REFERENCES organization(id)"+
                "    FOREIGN KEY (jrxml_id) REFERENCES design(id)"+
                ");";

        try (
                Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
            stmt.close();
            conn.close();
            System.out.println("Tabel invoice telah dibuat.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTableDesign() {
        String sql = "CREATE TABLE IF NOT EXISTS design (\n" +
                "    id INTEGER PRIMARY KEY,\n" +
                "    jrxml TEXT NOT NULL\n" +
                ");";

        try (
                Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
            stmt.close();
            conn.close();
            System.out.println("Tabel design telah dibuat.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void createTableItem() {
        String sql = "CREATE TABLE IF NOT EXISTS item2 (\n" +
                "    id INTEGER PRIMARY KEY,\n" +
                "    name TEXT NOT NULL,\n" +
                "    price INTEGER NOT NULL,\n" +
                "    qty INTEGER NOT NULL,\n" +
                "    fillNameWithMonth BOOLEAN,\n" +
                "    totalPriceItem INTEGER NOT NULL,\n" +
                "    invoice_id INTEGER NOT NULL,\n" +
                "    FOREIGN KEY (invoice_id) REFERENCES invoice(id)\n" +
                ");";

        try (
                Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
            stmt.close();
            conn.close();
            System.out.println("Tabel design telah dibuat.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void dropTable() {

        String sql = "DROP TABLE IF EXISTS organization;";

        try (
                Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()
        ) {
//            stmt.execute("DROP TABLE IF EXISTS organization;");
//            stmt.execute("DROP TABLE IF EXISTS customer;");
//            stmt.execute("ALTER TABLE item2 RENAME TO item;");
//            stmt.execute("DROP TABLE IF EXISTS item;");
//            stmt.execute("DROP TABLE IF EXISTS kode_surat;");
//            stmt.execute("INSERT INTO item2 SELECT * FROM item;");
            stmt.execute("ALTER TABLE design ADD COLUMN first_invoice_id TEXT;");
            stmt.close();
            conn.close();
            System.out.println("Tabel telah dihapus.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //========================================================================
    public static <T> boolean addOneData (String query, T object) {
        try (org.sql2o.Connection conn = sql2o.beginTransaction()) {
            conn.createQuery(query).bind(object).executeUpdate();
            System.out.println("query masuk telah dijalankan");
            conn.commit();
            return true;

        }catch (Sql2oException e1){
            e1.printStackTrace();
            return false;
        }
    }

    public static <T> List<T> getListData(String query, Class<T> classe){
        List<T> listData = new ArrayList<>();

        try(org.sql2o.Connection con = sql2o.open()){
            return con.createQuery(query).executeAndFetch(classe);
        }catch (Sql2oException e1){
            e1.printStackTrace();
            return listData;
        }
    }
    public static <T> List<T> getListData(String query, Class<T> classe, String... params){
        List<T> listData = new ArrayList<>();

        try(org.sql2o.Connection con = sql2o.open()){
            return con.createQuery(query).withParams((Object) params).executeAndFetch(classe);
        }catch (Sql2oException e1){
            e1.printStackTrace();
            return listData;
        }
    }
    public static <T> T getOneData(String query, Class<T> classe, String... params){
        try(org.sql2o.Connection con = sql2o.open()){
            return con.createQuery(query).withParams((Object) params).executeAndFetchFirst(classe);
        }catch (Sql2oException e1){
            e1.printStackTrace();
            return null;
        }
    }
    public static <T> T getLastInsertedData(String query, Class<T> classe){
        try(org.sql2o.Connection con = sql2o.open()){
            return con.createQuery(query).executeAndFetchFirst(classe);
        }catch (Sql2oException e1){
            e1.printStackTrace();
            return null;
        }
    }
    public static boolean deleteData(String query, String... params){
        try(org.sql2o.Connection con = sql2o.open()){
            con.createQuery(query).withParams((Object) params).executeUpdate();
            return true;
        }catch (Sql2oException e1){
            e1.printStackTrace();
            return false;
        }
    }
    public static <T> boolean updateData(String query, T object){
        try(org.sql2o.Connection con = sql2o.open()){
            con.createQuery(query).bind(object).executeUpdate();
            return true;
        }catch (Sql2oException e1){
            e1.printStackTrace();
            return false;
        }
    }

}
