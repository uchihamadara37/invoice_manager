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
            long customer_id
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
                invoiceCode
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
                :invoiceCode
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
    public static List<Invoice> getAllDataBetweenTime(){
        Timestamp timeNow = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
        Timestamp januari = new Timestamp(calendar.getTimeInMillis());
        String query = """
                SELECT * FROM invoice WHERE timestamp BETWEEN :q1 AND :q2
                """;

        System.out.println("kampretor");
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

    public static boolean deleteOneById(long id){
        String query = """
                DELETE FROM invoice WHERE id = :p1
                """;
        return DatabaseManager.deleteData(query, Long.toString(id));
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
                    a.customer_id
               
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
                invoiceCode = :invoiceCode
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, invoice);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public String getTimestamp() {
        return timestamp;
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

    public void setTimeStamp(String timeStamp) {
        this.timestamp = timeStamp;
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

    public String getTimeStamp() {
        return timestamp;
    }

    public List<Item> getListItems() {
        return Item.getListByInvoiceID(id);
    }
}
