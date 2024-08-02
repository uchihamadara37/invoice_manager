package com.andre.dojo.Utils;


import com.google.gson.*;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.lang.reflect.Type;

public class JavaFXPropertyAdapter implements JsonSerializer<Property>, JsonDeserializer<Property> {
    @Override
    public JsonElement serialize(Property src, Type typeOfSrc, JsonSerializationContext context) {
        System.out.println("cek serialize json");
        return new JsonPrimitive(src.getValue().toString());
    }

    @Override
    public Property deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (typeOfT.equals(StringProperty.class)) {
            System.out.println("cek deserialize json");
            return new SimpleStringProperty(json.getAsString());
        }
        // Tambahkan logic untuk tipe properti lain jika diperlukan
        throw new JsonParseException("Unsupported property type: " + typeOfT);
    }
}

