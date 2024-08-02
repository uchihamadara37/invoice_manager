package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.MetadataSave;
import com.andre.dojo.Models.Organization;
import com.andre.dojo.Models.PersonManagement;
import com.andre.dojo.Models.PersonManagementOri;
import com.andre.dojo.Utils.ObjectSaver;
import com.andre.dojo.Utils.Orang;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    Gson gson = new Gson();

    private static Stage mainStage;

    private static MetadataSave metadataSave = new MetadataSave();
//    private static ObservableList<Organization> dataOrganization = FXCollections.observableArrayList();
//    private static ObservableList<PersonManagement> dataPerson = FXCollections.observableArrayList();
    @Override
    public void start(Stage stage) throws IOException {

        if (ObjectSaver.fileNameAddress != null && ObjectSaver.retrieveData() != null) {
            metadataSave = (ObjectSaver.retrieveData());

            System.out.println("data berhasil dibaca");
        }else{
            System.out.println("harusnya");
        }

        PersonManagement pr = new PersonManagement( "iuoiuoiu", "iouoiu", "uiyiuyi", "iuyiuy");

        mainStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Authentication!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setDataOrganization(ObservableList<Organization> data) {
        metadataSave.setOrganizations(data);
    }
    public static void addDataOrganization(Organization organization) {
        metadataSave.getOrganizations().add(organization);
    }

    public static ObservableList<Organization> getDataOrganization() {
        return metadataSave.getOrganizations();
    }

    public static void setDataPerson(ObservableList<PersonManagement> data) {
        metadataSave.setPersons(data);
    }

    public static ObservableList<PersonManagement> getDataPerson() {
        return metadataSave.getPersons();
    }

    public static void addDataPerson(PersonManagement person) {
        metadataSave.getPersons().add(person);
    }


    public static void setMetadataSave(MetadataSave metadataSave) {
        HelloApplication.metadataSave = metadataSave;
    }

    public static MetadataSave getMetadataSave() {
        return metadataSave;
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(message);
//        alert.setContentText("Ini adalah contoh pesan alert di JavaFX.");
        alert.showAndWait();
    }
}