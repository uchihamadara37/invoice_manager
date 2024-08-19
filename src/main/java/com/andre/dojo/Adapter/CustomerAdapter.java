package com.andre.dojo.Adapter;

import com.andre.dojo.Models.CustomJSON;
import com.andre.dojo.Models.Customer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class CustomerAdapter extends TypeAdapter<Customer> {
    @Override
    public void write(JsonWriter jsonWriter, Customer customer) throws IOException {
        if (customer == null){
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginObject();
            jsonWriter.name("id").value(customer.getId());
            jsonWriter.name("name").value(customer.getName());
            jsonWriter.name("description").value(customer.getDescription());
            jsonWriter.name("phoneNumber").value(customer.getPhoneNumber());
            jsonWriter.name("organization_id").value(customer.getOrganization_id());
        jsonWriter.endObject();
    }

    @Override
    public Customer read(JsonReader jsonReader) throws IOException {
        Customer customer = new Customer();

        jsonReader.beginObject();
        while(jsonReader.hasNext()){
            String name = jsonReader.nextName();
            switch (name) {
                case "id" :
                    customer.setId(jsonReader.nextLong());
                    break;
                case "name" :
                    customer.setName(jsonReader.nextString());
                    break;
                case "description" :
                    customer.setDescription(jsonReader.nextString());
                    break;
                case "phoneNumber" :
                    customer.setPhoneNumber(jsonReader.nextString());
                    break;
                case "organization_id" :
                    customer.setOrganization_id(jsonReader.nextLong());
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        return customer;
    }
}
