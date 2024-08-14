package com.andre.dojo.Models;

import com.andre.dojo.Utils.DatabaseManager;

import java.time.Instant;
import java.util.List;

public class Item {
    private long id;
    private String name;
    private int price;
    private int qty;
    private int totalPriceItem;
    private int invoice_id;

    public Item(){

    }

    public Item(String name, int price, int qty, int total_price_item) {
        this.id = Instant.now().toEpochMilli();
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.totalPriceItem = total_price_item;
    }

    public static boolean addToDB(Item item){
        String query = """
                INSERT INTO item (
                id, 
                name, 
                price,
                qty,
                totalPriceItem,
                invoice_id
                ) VALUES (
                :id, 
                :name, 
                :price,
                :qty,
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
                name = :kode, 
                price = :noUrut, 
                qty = :qty,
                totalPriceItem = :totalPriceItem,
                invoice_id = :invoice_id
                WHERE id = :id
                """;
        return DatabaseManager.updateData(query, item);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTotalPriceItem() {
        return totalPriceItem;
    }

    public void setTotalPriceItem(int totalPriceItem) {
        this.totalPriceItem = totalPriceItem;
    }

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
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
