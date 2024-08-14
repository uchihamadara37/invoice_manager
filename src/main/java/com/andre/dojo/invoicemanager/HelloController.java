package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Invoice;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private TableView<Invoice> tableViewInvoice;
    @FXML
    private TableColumn<Invoice, String> tableColumnCheckbox;
    @FXML
    private TableColumn<Invoice, String> tableColumnCode;
    @FXML
    private TableColumn<Invoice, String> tableColumnDescription;
    @FXML
    private TableColumn<Invoice, String> tableColumnCustomer;


    @FXML
    private Pane tombolInvoice;
    @FXML
    private Pane tombolChangeData;
    @FXML
    private Pane tombolExport;

    @FXML
    private AnchorPane anchorPaneMain;
    @FXML
    private Pane paneInvoice;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("jangkrik");

        loadTableView();

        // event tombol
        tombolChangeData.setOnMouseClicked(e -> {
            openChangeDataPane();
        });
        tombolInvoice.setOnMouseClicked(e -> {
            openInvoicePane();
        });
        tombolExport.setOnMouseClicked(e -> {
            openExportPane();
        });

    }

    private void openExportPane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("export-view.fxml"));
            AnchorPane anchorPane = new AnchorPane((Node) fxmlLoader.load());
            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void openInvoicePane() {
        anchorPaneMain.getChildren().removeFirst();
        anchorPaneMain.getChildren().add(paneInvoice);
        loadTableView();
    }

    private void loadTableView() {
        List<Invoice> oi = Invoice.getAllData();
        ObservableList<Invoice> list = FXCollections.observableArrayList();
        list.addAll(oi);
        System.out.println(list.size());
        tableViewInvoice.setItems(list);
        tableColumnCode.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceCode()));
        tableColumnDescription.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnCustomer.setCellValueFactory(e -> new SimpleStringProperty(Customer.getOneData(e.getValue().getCustomer_id()).getName()));
    }

    private void openChangeDataPane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data.fxml"));
            AnchorPane anchorPane = new AnchorPane((Node) fxmlLoader.load());
            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }
}