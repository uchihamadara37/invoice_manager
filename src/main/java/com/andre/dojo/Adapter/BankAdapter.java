package com.andre.dojo.Adapter;

import com.andre.dojo.Models.Bank;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class BankAdapter extends TypeAdapter<Bank> {
    @Override
    public void write(JsonWriter jsonWriter, Bank bank) throws IOException {
        if (bank == null) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.beginObject();
        jsonWriter.name("bank_name").value(bank.getBank_name());
        jsonWriter.name("bank_number").value(bank.getAccount_number());
        jsonWriter.name("bank_owner").value(bank.getOwner());
        jsonWriter.endObject();
    }

    @Override
    public Bank read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
