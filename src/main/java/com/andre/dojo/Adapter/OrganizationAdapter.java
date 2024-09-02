package com.andre.dojo.Adapter;

import com.andre.dojo.Models.Organization;
import com.andre.dojo.Models.Personal;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class OrganizationAdapter extends TypeAdapter<Organization> {
    PersonAdapter personAdapter = new PersonAdapter();

    @Override
    public void write(JsonWriter jsonWriter, Organization organization) throws IOException {
        if (organization == null) {
            jsonWriter.nullValue();
            return;
        }

//        private long id;
//        private String logo;
//        private String brandName;
//        private String description;
//        private String address;
//        private String email;
//        private int noUrutInstansi;
//        private int tahunOperasi;
//        private Personal personal;
        jsonWriter.beginObject();
            jsonWriter.name("id").value(organization.getId());
            jsonWriter.name("logo").value(organization.getLogo());
            jsonWriter.name("brandName").value(organization.getBrandName());
            jsonWriter.name("description").value(organization.getDescription());
            jsonWriter.name("address").value(organization.getAddress());
            jsonWriter.name("email").value(organization.getEmail());
            jsonWriter.name("noUrutInstansi").value(organization.getNoUrutInstansi());
            jsonWriter.name("tahunOperasi").value(organization.getTahunOperasi());
            jsonWriter.name("totalLetter").value(organization.getTotalLetter());
            jsonWriter.name("personal");
            if (organization.getPersonal() != null){
                personAdapter.write(jsonWriter, organization.getPersonal());
            }
        jsonWriter.endObject();
    }

    @Override
    public Organization read(JsonReader jsonReader) throws IOException {
        Organization organization = new Organization();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "id":
                    organization.setId(jsonReader.nextLong());
                    break;
                case "logo":
                    organization.setLogo(jsonReader.nextString());
                    break;
                case "brandName":
                    organization.setBrandName(jsonReader.nextString());
                    break;
                case "description":
                    organization.setDescription(jsonReader.nextString());
                    break;
                case "address":
                    organization.setAddress(jsonReader.nextString());
                    break;
                case "email":
                    organization.setEmail(jsonReader.nextString());
                    break;
                case "noUrutInstansi":
                    organization.setNoUrutInstansi(jsonReader.nextInt());
                    break;
                case "tahunOperasi":
                    organization.setTahunOperasi(jsonReader.nextInt());
                    break;
                case "totalLetter":
                    organization.setTotalLetter(jsonReader.nextInt());
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();

        return organization;
    }
}
