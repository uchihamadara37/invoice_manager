package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Invoice;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PrepareExportController implements Initializable {
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Label exportPage;
    @FXML
    private Pane exportPage2;
    @FXML
    private TableView<Invoice> tableViewInvoice;
    @FXML
    private TableColumn<Invoice, Boolean> tableColumnCheckbox;
    @FXML
    private TableColumn<Invoice, String> tableColumnCode;
    @FXML
    private TableColumn<Invoice, String> tableColumnDescription;
    @FXML
    private TableColumn<Invoice, String> tableColumnCustomer;

    private HelloController helloController;

    private List<Invoice> invoiceSelected = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTableView(Invoice.getAllDataGroubByTemplate());

        tableViewInvoice.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    System.out.println("bool: "+newValue.getChecked().get());

                });
        exportPage.setOnMouseClicked(e -> {
            openExportPage();
        });
        exportPage2.setOnMouseClicked(e -> {
            openExportPage();
        });

    }

    private void openExportPage() {
        // check apakah ada yang diselect atau tidak
        ObservableList<Invoice> currentData = tableViewInvoice.getItems();
        System.out.println("Checked TableView Data:");
        invoiceSelected.clear();
        for (Invoice item : currentData) {
            if (item.getChecked().get()) {
//                System.out.println("Item: " + item.getChecked().get() + ", Describ: " + item.getDescription());
                invoiceSelected.add(item);
            }
        }
        if (!invoiceSelected.isEmpty()) {
//                exportController.setDataExport(selectedInvoices);
            helloController.getExportController().setDataExport(invoiceSelected);
            helloController.getExportController().customInitialize();
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getExportController().getMainPage());

        } else {
            helloController.showInformationDialog("Please select at least one invoice to export.");
        }
    }

    private void loadTableView(List<Invoice> allData) {
        tableViewInvoice.setEditable(true);
        tableViewInvoice.setItems(FXCollections.observableArrayList(allData));
        tableColumnCheckbox.setCellValueFactory(e -> e.getValue().getChecked());
        tableColumnCheckbox.setCellFactory(e -> new CheckBoxTableCell<>());

        tableColumnCode.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceCode()));
        tableColumnDescription.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnCustomer.setCellValueFactory(e -> new SimpleStringProperty(Customer.getOneData(e.getValue().getCustomer_id()).getName()));
    }

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public AnchorPane getMainPage() {
        return mainPage;
    }
}
