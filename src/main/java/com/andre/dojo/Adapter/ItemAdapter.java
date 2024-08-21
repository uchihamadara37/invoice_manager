package com.andre.dojo.Adapter;

import com.andre.dojo.Models.Item;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ItemAdapter extends TypeAdapter<Item> {
    @Override
    public void write(JsonWriter jsonWriter, Item item) throws IOException {
        if (item == null){
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginObject();
        jsonWriter.name("id").value(item.getId());
        jsonWriter.name("name").value(item.getName());
        jsonWriter.name("price").value(item.getPrice());
        jsonWriter.name("qty").value(item.getQty());
        jsonWriter.name("totalPriceItem").value(item.getTotal_price_item());
        jsonWriter.endObject();
    }

    @Override
    public Item read(JsonReader jsonReader) throws IOException {
        Item item = new Item();

        jsonReader.beginObject();
        while(jsonReader.hasNext()){
            String name = jsonReader.nextName();
            switch (name){
                case "id":
                    item.setId(jsonReader.nextLong());
                    break;
                case "name":
                    item.setName(jsonReader.nextString());
                    break;
                case "price":
                    item.setPrice(jsonReader.nextInt());
                    break;
                case "qty":
                    item.setQty(jsonReader.nextInt());
                    break;
                case "totalPriceItem":
                    item.setTotalPriceItem(jsonReader.nextInt());
                    break;
                default :
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        return item;
    }
}
