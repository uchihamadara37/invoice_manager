package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.*;
import com.andre.dojo.Utils.DatabaseManager;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    Gson gson = new Gson();

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