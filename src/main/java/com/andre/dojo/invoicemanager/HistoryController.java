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
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class HistoryController implements Initializable {
    @FXML
    private Spinner<String> spinner ;
    @FXML
    private TableView<Invoice> tableViewInvoice;
    @FXML
    private TableColumn<Invoice, String> tableColumnCode;
    @FXML
    private TableColumn<Invoice, String> tableColumnDescription;
    @FXML
    private TableColumn<Invoice, String> tableColumnCustomer;
    @FXML
    private TableColumn<Invoice, String> tableColumnTime;
    @FXML
    private TableColumn<Invoice, String> tableColumnDate;

    @FXML
    private Label tombolHistory;
    @FXML
    private Label tombolTanggal;
    @FXML
    private Label tombolShowAll;
    @FXML
    private TextField searchField;

    private HelloController helloController;

    public void setHelloController(HelloController helloController){
        this.helloController = helloController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // installasi spinner
        setupSpinner();
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
//            System.out.println("Bulan dipilih: " + newValue);
            List<Invoice> listInvoice = Invoice.getAllDataGroubByBulan(newValue);
            if (listInvoice != null || !listInvoice.isEmpty()){
                loadTableView(listInvoice);
            }else{
                loadTableView(new ArrayList<Invoice>());
            }
        });

        loadTableView(Invoice.getAllData());

        tombolTanggal.setOnMouseClicked(e -> {
            openInvoicePane();
        });

        searchField.setOnKeyReleased(e -> {
            searchNow();
        });

        tombolShowAll.setOnMouseClicked(e -> {
            loadTableView(Invoice.getAllData());
        });

    }

    private void setupSpinner() {
        ObservableList<String> listMoon = FXCollections.observableArrayList();
        for (Month month : Month.values()) {
            listMoon.add(month.getDisplayName(TextStyle.FULL, new Locale("id", "ID")));
        }
        Collections.reverse(listMoon);
        SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(listMoon);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", new Locale("id", "ID"));
        valueFactory.setValue(LocalDate.now().format(formatter));

        spinner.setValueFactory(valueFactory);

    }

    private void searchNow() {
        if (!Objects.equals(searchField.getText(), "")){
            loadTableView(Invoice.searchInvoice(searchField.getText()));
        }else{
            loadTableView(Invoice.getAllData());
        }
    }

    private void openInvoicePane() {
        helloController.getAnchorPaneMain().getChildren().removeFirst();
        helloController.getAnchorPaneMain().getChildren().add(helloController.getPaneInvoice());
        helloController.loadTableView(Invoice.getAllDataGroubByTemplate());
    }

    private void loadTableView(Collection<Invoice> data) {
        tableViewInvoice.setItems(FXCollections.observableArrayList(data));
        tableColumnCode.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceCode()));
        tableColumnDescription.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnDate.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDate()));
        tableColumnCustomer.setCellValueFactory(e -> new SimpleStringProperty(Customer.getOneData(e.getValue().getCustomer_id()).getName()));
        tableColumnTime.setCellValueFactory(e -> {
            if (e == null){
                return new SimpleStringProperty("jangkrik");
            }else{
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(e.getValue().getTimestamp());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a");
                return new SimpleStringProperty(zonedDateTime.format(formatter));
            }
        });
    }
}
