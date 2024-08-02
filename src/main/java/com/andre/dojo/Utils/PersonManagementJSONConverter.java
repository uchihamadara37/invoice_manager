package com.andre.dojo.Utils;

import com.andre.dojo.Models.PersonManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Type;
import java.util.List;

public class PersonManagementJSONConverter {

    public static ObservableList<PersonManagement> jsonToObservableList(String json) {
        // Definisikan tipe untuk List<PersonManagement>
        Type listType = new TypeToken<List<PersonManagement>>(){}.getType();

        // Parse JSON ke List<PersonManagement>
        List<PersonManagement> personList = ObjectSaver.gsonPretty.fromJson(json, listType);
        return FXCollections.observableArrayList(personList);
    }

    public static String observableListToJson(ObservableList<PersonManagement> list) {
        return ObjectSaver.gsonPretty.toJson(list);
    }

}
