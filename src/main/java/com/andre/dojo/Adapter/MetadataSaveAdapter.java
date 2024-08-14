//package com.andre.dojo.Adapter;
//
//import com.andre.dojo.Models.MetadataSave;
//import com.andre.dojo.Models.Organization;
//import com.andre.dojo.Models.PersonManagement;
//import com.google.gson.JsonParser;
//import com.google.gson.TypeAdapter;
//import com.google.gson.stream.JsonReader;
//import com.google.gson.stream.JsonToken;
//import com.google.gson.stream.JsonWriter;
//
//import java.io.IOException;
//
//public class MetadataSaveAdapter extends TypeAdapter<MetadataSave> {
//
//    PersonAdapter personAdapter = new PersonAdapter();
//    OrganizationAdapter organization = new OrganizationAdapter();
//
//    @Override
//    public void write(JsonWriter jsonWriter, MetadataSave metadataSave) throws IOException {
//        if (metadataSave == null) {
//            jsonWriter.nullValue();
//            return;
//        }
//
//        jsonWriter.beginObject();
//        jsonWriter.name("persons");
//        jsonWriter.beginArray();
//        for(PersonManagement person : metadataSave.getPersons()){
//            personAdapter.write(jsonWriter, person);
//        }
//        jsonWriter.endArray();
//        jsonWriter.name("organizations");
//        jsonWriter.beginArray();
//        for (Organization org : metadataSave.getOrganizations()){
//            organization.write(jsonWriter, org);
//        }
//        jsonWriter.endArray();
//        jsonWriter.endObject();
//    }
//
//    @Override
//    public MetadataSave read(JsonReader jsonReader) throws IOException {
//        MetadataSave metadataSave = new MetadataSave();
//
//
//        jsonReader.beginObject();
//        while (jsonReader.hasNext()) {
//            String name = jsonReader.nextName();
//            switch (name) {
//                case "persons":
////                    JsonToken token = jsonReader.peek();
//                    jsonReader.beginArray();
//                    System.out.println("beginArray");
//                    while(jsonReader.hasNext()){
//                        PersonManagement p = personAdapter.read(jsonReader);
//                        metadataSave.addPerson(p);
//                    }
//                    jsonReader.endArray();
//                    break;
//                case "organizations":
//                    jsonReader.beginArray();
//                    while(jsonReader.hasNext()){
//                        Organization p = organization.read(jsonReader);
//                        metadataSave.addOrganization(p);
//                    }
//                    jsonReader.endArray();
//                    break;
//                default:
//                    jsonReader.skipValue();
//                    break;
//            }
//        }
//        jsonReader.endObject();
//        return metadataSave;
//    }
//}
