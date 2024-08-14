//package com.andre.dojo.Adapter;
//
//import com.andre.dojo.Models.Organization;
//import com.google.gson.TypeAdapter;
//import com.google.gson.stream.JsonReader;
//import com.google.gson.stream.JsonWriter;
//
//import java.io.IOException;
//
//public class OrganizationAdapter extends TypeAdapter<Organization> {
//    PersonAdapter personAdapter = new PersonAdapter();
//
//    @Override
//    public void write(JsonWriter jsonWriter, Organization organization) throws IOException {
//        if (organization == null) {
//            jsonWriter.nullValue();
//            return;
//        }
//
//        jsonWriter.beginObject();
//        jsonWriter.name("id").value(organization.getId());
//        jsonWriter.name("name").value(organization.getName());
//        jsonWriter.name("address").value(organization.getAddress());
//        jsonWriter.name("email").value(organization.getEmail());
//        jsonWriter.name("urlLogo").value(organization.getUrlLogo());
//        jsonWriter.name("person");
//        if (organization.getPersonManagement() != null){
//            personAdapter.write(jsonWriter, organization.getPersonManagement());
//        }
//        jsonWriter.endObject();
//    }
//
//    @Override
//    public Organization read(JsonReader jsonReader) throws IOException {
//        Organization organization = new Organization();
//
//        jsonReader.beginObject();
//        while (jsonReader.hasNext()) {
//            String name = jsonReader.nextName();
//            switch (name) {
//                case "id":
//                    organization.setId(jsonReader.nextString());
//                    break;
//                case "name":
//                    organization.setName(jsonReader.nextString());
//                    break;
//                case "address":
//                    organization.setAddress(jsonReader.nextString());
//                    break;
//                case "email":
//                    organization.setEmail(jsonReader.nextString());
//                    break;
//                case "urlLogo":
//                    organization.setUrlLogo(jsonReader.nextString());
//                    break;
//                case "person":
//                    organization.setPersonManagement(personAdapter.read(jsonReader));
//                    break;
//                default:
//                    jsonReader.skipValue();
//                    break;
//            }
//        }
//        jsonReader.endObject();
//
//        return organization;
//    }
//}
