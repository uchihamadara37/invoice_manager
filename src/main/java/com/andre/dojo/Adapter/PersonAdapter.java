package com.andre.dojo.Adapter;

import com.andre.dojo.Models.Personal;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class PersonAdapter extends TypeAdapter<Personal> {
    @Override
    public void write(JsonWriter jsonWriter, Personal person) throws IOException {
        if (person == null) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.beginObject();
        jsonWriter.name("id").value(person.getId());
        jsonWriter.name("name").value(person.getName());
        jsonWriter.name("bankName").value(person.getBankName());
        jsonWriter.name("bankIDNumber").value(person.getBankIDNumber());
        jsonWriter.name("bankIDName").value(person.getBankIDName());
        jsonWriter.name("urlTtd").value(person.getUrlTtd());
        jsonWriter.name("organization_id").value(person.getOrganization_id());
        jsonWriter.endObject();
    }

    @Override
    public Personal read(JsonReader jsonReader) throws IOException {
        Personal person = new Personal();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "id":
                    person.setId(jsonReader.nextLong());
                    break;
                case "name":
                    person.setName(jsonReader.nextString());
                    break;
                case "bankName":
                    person.setBankName(jsonReader.nextString());
                    break;
                case "bankIDNumber":
                    person.setBankIDNumber(jsonReader.nextString());
                    break;
                case "bankIDName":
                    person.setBankIDName(jsonReader.nextString());
                    break;
                case "urlTtd":
                    person.setUrlTtd(jsonReader.nextString());
                    break;
                case "organization_id":
                    person.setOrganization_id(jsonReader.nextLong());
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();

        return person;
    }
}

