package com.andre.dojo.Models;

import com.andre.dojo.Utils.DatabaseManager;
import javafx.scene.chart.PieChart;

import java.time.Instant;
import java.util.List;

public class Item {
    private long id;
    private String name;
    private int price;
    private int qty;
    private boolean fillNameWithMonth = false;
    private int totalPriceItem;
    private long invoice_id;

    public Item(){

    }

    public Item(String name, int price, int qty, int total_price_item, long invoice_id) {
        this.id = Instant.now().toEpochMilli();
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.totalPriceItem = total_price_item;
        this.invoice_id = invoice_id;
    }

    public static boolean addToDB(Item item){
        System.out.println("Item.tambhkan item"+item.getName());
        String query = """
                INSERT INTO item (
                id, 
                name, 
                price,
                qty,
                fillNameWithMonth,
                totalPriceItem,
                invoice_id
                ) VALUES (
                :id, 
                :name, 
                :price,
                :qty,
                :fillNameWithMonth,
                :totalPriceItem,
                :invoice_id
                )""";
        return DatabaseManager.addOneData(query, item);
    }
    public static List<Item> getAllData(){
        String query = """
                SELECT * FROM item
                """;
        return DatabaseManager.getListData(query, Item.class);
    }
    public static List<Item> getListByInvoiceID(long invoice_id){
        String query = """
                SELECT * FROM item WHERE invoice_id = :p1
                """;
//        System.out.println("jangkrik item invoiceid");
        return DatabaseManager.getListData(query, Item.class, Long.toString(invoice_id));
    }
    public static Item getOneData(long id_item){
        String query = """
                SELECT * FROM item WHERE id = :p1
                """;
        return DatabaseManager.getOneData(query, Item.class, Long.toString(id_item));
    }
    public static boolean deleteOneById(long id){
        String query = """
                DELETE FROM item WHERE id = :p1
                """;
        return DatabaseManager.deleteData(query, Long.toString(id));
    }
    public static boolean updateById(Item item){
        String query = """
                UPDATE item SET 
                name = :name, 
                price = :price, 
                qty = :qty,
                totalPriceItem = :totalPriceItem,
                invoice_id = :invoice_id,
                fillNameWithMonth = :fillNameWithMonth
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, item);
    }

    public static int getSumOfPriceByInvoiceId(long invoice_id){
        String query = "SELECT SUM(totalPriceItem) FROM item WHERE invoice_id = :p1;";

        Integer val = DatabaseManager.getOneData(query, Integer.class, String.valueOf(invoice_id));
        if (val != null){
            return val;
        }
        return 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isFillNameWithMonth() {
        return fillNameWithMonth;
    }

    public boolean getFillNameWithMonth(){
        return fillNameWithMonth;
    }

    public void setFillNameWithMonth(boolean fillNameWithMonth) {
        this.fillNameWithMonth = fillNameWithMonth;
    }


    public int getTotalPriceItem() {
        return totalPriceItem;
    }

    public void setTotalPriceItem(int totalPriceItem) {
        this.totalPriceItem = totalPriceItem;
    }

    public long getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(long invoice_id) {
        this.invoice_id = invoice_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setTotal_price_item(int total_price_item) {
        this.totalPriceItem = total_price_item;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public int getTotal_price_item() {
        return totalPriceItem;
    }
}
