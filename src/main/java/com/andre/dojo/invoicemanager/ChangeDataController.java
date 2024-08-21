package com.andre.dojo.invoicemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ChangeDataController {
    @FXML
    private Label tombolCustomer;
    @FXML
    private AnchorPane anchorPaneMain;

    private ChangeDataInvoiceController changeDataInvoiceController;

    public void setChangeDataInvoiceController(ChangeDataInvoiceController changeDataInvoiceController){
        this.changeDataInvoiceController = changeDataInvoiceController;
    }

    public void initialize(){
        tombolCustomer.setOnMouseClicked(e -> {
            openCustomerPane();
        });
    }



    private void openCustomerPane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data-customer.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ChangeDataInvoiceController test = fxmlLoader.getController();
            test.setChangedataController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }
}
