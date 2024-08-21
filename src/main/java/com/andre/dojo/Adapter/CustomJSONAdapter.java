package com.andre.dojo.Adapter;

import com.andre.dojo.Models.*;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class CustomJSONAdapter extends TypeAdapter<CustomJSON> {

    OrganizationAdapter organizationAdapter = new OrganizationAdapter();
    CustomerAdapter customerAdapter = new CustomerAdapter();
    InvoiceAdapter invoiceAdapter = new InvoiceAdapter();

    @Override
    public void write(JsonWriter jsonWriter, CustomJSON customJSON) throws IOException {
        if (customJSON == null) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.beginObject();
            jsonWriter.name("organization");
                if (customJSON.getOrganization() != null){
                    organizationAdapter.write(jsonWriter, customJSON.getOrganization());
                }else{
                    jsonWriter.nullValue();
                }
            jsonWriter.name("customer");
                if (customJSON.getCustomer() != null){
                    customerAdapter.write(jsonWriter, customJSON.getCustomer());
                }else{
                    jsonWriter.nullValue();
                }
            jsonWriter.name("invoice");
                if (customJSON.getInvoice() != null){
                    invoiceAdapter.write(jsonWriter, customJSON.getInvoice());
                }else{
                    jsonWriter.nullValue();
                }
        jsonWriter.endObject();
    }

    @Override
    public CustomJSON read(JsonReader jsonReader) throws IOException {
        CustomJSON customJSON = new CustomJSON();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "organization":
                    customJSON.setOrganization(organizationAdapter.read(jsonReader));
                    break;
                case "customer":
                    customJSON.setCustomer(customerAdapter.read(jsonReader));
                    break;
                case "invoice":
                    customJSON.setInvoice(invoiceAdapter.read(jsonReader));
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        return customJSON;
    }
}
