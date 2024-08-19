package com.andre.dojo.invoicemanager;

import com.andre.dojo.Adapter.*;
import com.andre.dojo.Models.*;
import com.andre.dojo.Utils.DatabaseManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {

    public static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Personal.class, new PersonAdapter())
            .registerTypeAdapter(Customer.class, new CustomerAdapter())
            .registerTypeAdapter(CustomJSON.class, new CustomJSONAdapter())
            .registerTypeAdapter(InvoiceAdapter.class, new InvoiceAdapter())
            .registerTypeAdapter(Item.class, new ItemAdapter())
            .registerTypeAdapter(Organization.class, new OrganizationAdapter())
            .create();

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {


        mainStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Invoice Manager!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(message);
//        alert.setContentText("Ini adalah contoh pesan alert di JavaFX.");
        alert.showAndWait();
    }
}