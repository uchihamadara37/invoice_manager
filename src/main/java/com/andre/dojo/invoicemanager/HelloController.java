package com.andre.dojo.invoicemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        openDashboardPage();
    }

    private void openDashboardPage() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Dashboard-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            HelloApplication.getMainStage().setTitle("Authentication!");
            HelloApplication.getMainStage().setScene(scene);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }
}