//package com.andre.dojo.Adapter;
//
//import com.andre.dojo.Models.PersonManagement;
//import com.google.gson.TypeAdapter;
//import com.google.gson.stream.JsonReader;
//import com.google.gson.stream.JsonWriter;
//
//import java.io.IOException;
//
//public class PersonAdapter extends TypeAdapter<PersonManagement> {
//    @Override
//    public void write(JsonWriter jsonWriter, PersonManagement personManagement) throws IOException {
//        if (personManagement == null) {
//            jsonWriter.nullValue();
//            return;
//        }
//
//        jsonWriter.beginObject();
//        jsonWriter.name("id").value(personManagement.getId());
//        jsonWriter.name("name").value(personManagement.getName());
//        jsonWriter.name("mobile_phone").value(personManagement.getMobilePhone());
//        jsonWriter.name("bank_id").value(personManagement.getNoRekening());
//        jsonWriter.name("bank").value(personManagement.getBank());
//        jsonWriter.endObject();
//    }
//
//    @Override
//    public PersonManagement read(JsonReader jsonReader) throws IOException {
//        PersonManagement personManagement = new PersonManagement();
//
//        jsonReader.beginObject();
//        while (jsonReader.hasNext()) {
//            String name = jsonReader.nextName();
//            switch (name) {
//                case "id":
//                    personManagement.setId(jsonReader.nextString());
//                    break;
//                case "name":
//                    personManagement.setName(jsonReader.nextString());
//                    break;
//                case "mobile_phone":
//                    personManagement.setMobilePhone(jsonReader.nextString());
//                    break;
//                case "bank_id":
//                    personManagement.setNoRekening(jsonReader.nextString());
//                    break;
//                case "bank":
//                    personManagement.setBank(jsonReader.nextString());
//                    break;
//                default:
//                    jsonReader.skipValue();
//                    break;
//            }
//        }
//        jsonReader.endObject();
//
//        return personManagement;
//    }
//}
//
