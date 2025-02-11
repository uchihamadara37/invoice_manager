package com.andre.dojo.Models;

import com.andre.dojo.Utils.DatabaseManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.List;

public class Invoice {
    private long id;
    private String invoiceMarkText;
    private String date;
    private String description;
    private String invoiceCode;
    private int totalPriceAll;
    private String jsonData;
    private String pdfUrl;
    private String timestamp;
    private long jrxml_id;
    private long customer_id;
    private long bank_id;
    private boolean status;
    private BooleanProperty checked = new SimpleBooleanProperty(true);
    private List<Item> listItems;

    public Invoice(){

    }

    public Invoice(
            String invoiceMarkText,
            String description,
            String invoiceCode,
            String date,
            int totalPriceAll,
            String jsonData,
            String pdfUrl,
            long design_id,
            long customer_id,
            boolean status,
            long bank_id
    ) {
        this.id = Instant.now().toEpochMilli();
        this.invoiceMarkText = invoiceMarkText;
        this.date = date;
        this.totalPriceAll = totalPriceAll;
        this.jsonData = jsonData;
        this.pdfUrl = pdfUrl;
        this.timestamp = Instant.now().toString();
        this.description = description;
        this.invoiceCode = invoiceCode;
        this.jrxml_id = design_id;
        this.customer_id = customer_id;
        this.status = status;
        this.bank_id = bank_id;
    }

    public Invoice(
            String invoiceMarkText,
            String description,
            String invoiceCode,
            String date,
            int totalPriceAll,
            String jsonData,
            String pdfUrl,
            long design_id,
            long customer_id,
            long id
    ) {
        this.id = id;
        this.invoiceMarkText = invoiceMarkText;
        this.date = date;
        this.totalPriceAll = totalPriceAll;
        this.jsonData = jsonData;
        this.pdfUrl = pdfUrl;
        this.timestamp = Instant.now().toString();
        this.description = description;
        this.invoiceCode = invoiceCode;
        this.jrxml_id = design_id;
        this.customer_id = customer_id;
    }

    public static boolean addToDB(Invoice invoice){
        String query = """
                INSERT INTO invoice (
                id, 
                invoiceMarkText, 
                date,
                totalPriceAll,
                jsonData,
                pdfUrl,
                timestamp,
                jrxml_id,
                customer_id,
                description,
                invoiceCode,
                bank_id,
                status
                ) VALUES (
                :id, 
                :invoiceMarkText, 
                :date,
                :totalPriceAll,
                :jsonData,
                :pdfUrl,
                :timestamp,
                :jrxml_id,
                :customer_id,
                :description,
                :invoiceCode,
                :bank_id,
                :status
                )""";
        return DatabaseManager.addOneData(query, invoice);
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }
    public BooleanProperty getChecked(){
        return this.checked;
    }


    public static List<Invoice> getAllData(){
        String query = """
                SELECT * FROM invoice
                """;
        return DatabaseManager.getListData(query, Invoice.class);
    }

    public static List<Invoice> getAllDataGroubByTemplate(){
        String query = """
                SELECT * FROM invoice WHERE status = 1
                """;
        return DatabaseManager.getListData(query, Invoice.class);
    }
    public static List<Invoice> getAllDataGroubByBulan(String bulan){
        String query = """
                SELECT * FROM invoice WHERE date LIKE :p1
                """;
        return DatabaseManager.getListData(query, Invoice.class, "%"+bulan+"%");
    }
    public static List<Invoice> getAllDataBetweenTime(){
        Timestamp timeNow = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
        Timestamp januari = new Timestamp(calendar.getTimeInMillis());
        String query = """
                SELECT * FROM invoice WHERE timestamp BETWEEN :p1 AND :p2
                """;

//        System.out.println("kampretor");
        return DatabaseManager.getListData(query, Invoice.class, januari.toInstant().toString(), timeNow.toInstant().toString());
    }
    public static List<Invoice> getListByCustomerID(long cs_id){
        String query = """
                SELECT * FROM invoice WHERE customer_id = :p1
                """;
        return DatabaseManager.getListData(query, Invoice.class, Long.toString(cs_id));
    }
    public static Invoice getOneData(long id){
        String query = """
                SELECT * FROM invoice WHERE id = :p1
                """;
        return DatabaseManager.getOneData(query, Invoice.class, Long.toString(id));
    }
    public static Invoice getOneDataByCustomer(long customer_id){
        String query = """
                SELECT * FROM invoice WHERE customer_id = :p1
                """;
        return DatabaseManager.getOneData(query, Invoice.class, Long.toString(customer_id));
    }
    public static boolean deleteOneByIdCustomer(long id){
        String query = """
                DELETE FROM invoice WHERE customer_id = :p1
                """;
        return DatabaseManager.deleteData(query, Long.toString(id));
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timeStamp) {
        this.timestamp = timeStamp;
    }

    public static boolean deleteOneById(long id){
        String query = """
                DELETE FROM invoice WHERE id = :p1
                """;
        return DatabaseManager.deleteData(query, Long.toString(id));
    }

    public static List<Invoice> searchInvoiceByStatus(String text){
        String query = """
                SELECT DISTINCT
                    a.id,
                    a.invoiceCode,
                    a.invoiceMarkText,
                    a.date,
                    a.description,
                    a.totalPriceAll,
                    a.customer_id,
                    a.timestamp
               
                FROM invoice a LEFT JOIN customer b ON a.customer_id = b.id
                WHERE ((a.invoiceMarkText LIKE :p1)
                   OR (a.date LIKE :p1)
                   OR (a.description LIKE :p1)
                   OR (a.invoiceCode LIKE :p1)
                   OR (b.name LIKE :p1))
                   AND a.status = 1
                """;
        return DatabaseManager.getListData(query, Invoice.class, "%"+text+"%");
    }
    public static List<Invoice> searchInvoice(String text){
        String query = """
                SELECT DISTINCT
                    a.id,
                    a.invoiceCode,
                    a.invoiceMarkText,
                    a.date,
                    a.description,
                    a.totalPriceAll,
                    a.customer_id,
                    a.timestamp
               
                FROM invoice a LEFT JOIN customer b ON a.customer_id = b.id
                WHERE (a.invoiceMarkText LIKE :p1)
                   OR (a.date LIKE :p1)
                   OR (a.description LIKE :p1)
                   OR (a.invoiceCode LIKE :p1)
                   OR (b.name LIKE :p1)
                """;
        return DatabaseManager.getListData(query, Invoice.class, "%"+text+"%");
    }


    public static boolean updateById(Invoice invoice){
        String query = """
                UPDATE invoice SET 
                invoiceMarkText = :invoiceMarkText, 
                date = :date,
                totalPriceAll = :totalPriceAll,
                jsonData = :jsonData,
                pdfUrl = :pdfUrl,
                timestamp = :timestamp,
                jrxml_id = :jrxml_id,
                customer_id = :customer_id,
                description = :description,
                invoiceCode = :invoiceCode,
                status = :status,
                bank_id = :bank_id
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, invoice);
    }

    public static Invoice getLastData() {
        String query = """
            SELECT * FROM invoice
            ORDER BY id DESC
            LIMIT 1
            """;
        return DatabaseManager.getOneData(query, Invoice.class);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(long id) {
        this.id = id;
    }



    public long getJrxml_id() {
        return jrxml_id;
    }

    public void setJrxml_id(long jrxml_id) {
        this.jrxml_id = jrxml_id;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public String getDescription() {
        return description;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }


    public void setInvoiceMarkText(String invoiceMarkText) {
        this.invoiceMarkText = invoiceMarkText;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotalPriceAll(int totalPriceAll) {
        this.totalPriceAll = totalPriceAll;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }


    public void setListItems(List<Item> listItems) {
        for (Item i : listItems){
            Item.addToDB(i);
        }
    }

    public long getId() {
        return id;
    }

    public String getInvoiceMarkText() {
        return invoiceMarkText;
    }

    public String getDate() {
        return date;
    }

    public int getTotalPriceAll() {
        return totalPriceAll;
    }

    public String getJsonData() {
        return jsonData;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public long getBank_id() {
        return bank_id;
    }

    public void setBank_id(long bank_id) {
        this.bank_id = bank_id;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public List<Item> getListItems() {
        return Item.getListByInvoiceID(id);
    }
}
