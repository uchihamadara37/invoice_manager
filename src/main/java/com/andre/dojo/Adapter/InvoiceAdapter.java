package com.andre.dojo.Adapter;

import com.andre.dojo.Models.Invoice;
import com.andre.dojo.Models.Item;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.util.List;

public class InvoiceAdapter extends TypeAdapter<Invoice> {

    ItemAdapter itemAdapter = new ItemAdapter();

    @Override
    public void write(JsonWriter jsonWriter, Invoice invoice) throws IOException {
        if (invoice == null){
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.beginObject();
        jsonWriter.name("id").value(invoice.getId());
        jsonWriter.name("invoiceCode").value(invoice.getInvoiceCode());
        jsonWriter.name("invoiceMarkText").value(invoice.getInvoiceMarkText());
        jsonWriter.name("date").value(invoice.getDate());
        jsonWriter.name("description").value(invoice.getDescription());
        jsonWriter.name("pdfUrl").value(invoice.getPdfUrl());
        jsonWriter.name("totalPriceAll").value(invoice.getTotalPriceAll());
        jsonWriter.name("listItem");
        jsonWriter.beginArray();
        for (Item i : invoice.getListItems()){
            itemAdapter.write(jsonWriter, i);
        }
        jsonWriter.endArray();
        jsonWriter.endObject();
    }

    @Override
    public Invoice read(JsonReader jsonReader) throws IOException {
        Invoice invoice = new Invoice();

        jsonReader.beginObject();
        while (jsonReader.hasNext()){
            String name = jsonReader.nextName();
            switch (name){
                case "id" :
                    invoice.setId(jsonReader.nextLong());
                    break;
                case "invoiceCode" :
                    invoice.setInvoiceCode(jsonReader.nextString());
                    break;
                case "invoiceMarkText" :
                    invoice.setInvoiceMarkText(jsonReader.nextString());
                    break;
                case "date" :
                    invoice.setDate(jsonReader.nextString());
                    break;
                case "description" :
                    invoice.setDescription(jsonReader.nextString());
                    break;
                case "pdfUrl" :
                    invoice.setPdfUrl(jsonReader.nextString());
                    break;
                case "totalPriceAll" :
                    invoice.setTotalPriceAll(jsonReader.nextInt());
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        return invoice;
    }
}
