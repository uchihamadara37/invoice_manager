package com.andre.dojo.invoicemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ChangeDataCodeController {
    @FXML
    private AnchorPane anchorPaneMain;

    @FXML
    private Label tombolInvoice;
    @FXML
    private Label tombolCustomer;
    @FXML
    private Label tombolLetter;


    private ChangeDataCustomerController changeDataCustomerController;
    private ChangeDataController changeDataController;
    private ChangeDataInvoiceController changeDataInvoiceController;


    public void initialize(){
        tombolInvoice.setOnMouseClicked(e -> {
            openInvoicePane();
        });
        tombolCustomer.setOnMouseClicked(e -> {
            openCustomerPane();
        });
        tombolLetter.setOnMouseClicked(e -> {
            openLetterPane();
        });

    }

    public void setChangeDataController(ChangeDataController changeDataController) {
        this.changeDataController = changeDataController;
    }

    public void setChangeDataInvoiceController(ChangeDataInvoiceController changeDataInvoiceController) {
        this.changeDataInvoiceController = changeDataInvoiceController;
    }

    public void setChangeDataCustomerController(ChangeDataCustomerController changeDataCustomerController) {
        this.changeDataCustomerController = changeDataCustomerController;
    }

    private void openInvoicePane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data-invoice.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ChangeDataInvoiceController changeDataInvoiceController = fxmlLoader.getController();
            changeDataInvoiceController.setChangeDataCodeController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void openCustomerPane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data-customer.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ChangeDataCustomerController changeDataCustomerController = fxmlLoader.getController();
            changeDataCustomerController.setChangeDataCodeController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void openLetterPane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ChangeDataController changeDataController = fxmlLoader.getController();
            changeDataController.setChangeDataCodeController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }
}
