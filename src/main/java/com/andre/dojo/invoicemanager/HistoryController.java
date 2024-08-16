package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Invoice;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class HistoryController implements Initializable {
    @FXML
    private Spinner<Month> spinner ;
    @FXML
    private TableView<Invoice> tableViewInvoice;
    @FXML
    private TableColumn<Invoice, String> tableColumnCode;
    @FXML
    private TableColumn<Invoice, String> tableColumnDescription;
    @FXML
    private TableColumn<Invoice, String> tableColumnCustomer;

    @FXML
    private Label tombolHistory;
    @FXML
    private Label tombolTanggal;

    private HelloController helloController;

    public void setHelloController(HelloController helloController){
        this.helloController = helloController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Month> listMoon = FXCollections.observableArrayList(Month.values());

        SpinnerValueFactory<Month> val = new SpinnerValueFactory.ListSpinnerValueFactory<>(listMoon);
        val.setValue(LocalDate.now().getMonth());
        spinner.setValueFactory(val);

        loadTableView();

        tombolTanggal.setOnMouseClicked(e -> {
            openInvoicePane();
        });

    }

    private void openInvoicePane() {
        helloController.getAnchorPaneMain().getChildren().removeFirst();
        helloController.getAnchorPaneMain().getChildren().add(helloController.getPaneInvoice());
        helloController.loadTableView();
    }

    private void loadTableView() {
        tableViewInvoice.setItems(FXCollections.observableArrayList(Invoice.getAllData()));
        tableColumnCode.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceCode()));
        tableColumnDescription.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnCustomer.setCellValueFactory(e -> new SimpleStringProperty(Customer.getOneData(e.getValue().getCustomer_id()).getName()));
    }
}
