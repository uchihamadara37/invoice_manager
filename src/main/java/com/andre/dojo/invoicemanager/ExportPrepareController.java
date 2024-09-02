package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Invoice;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Optional;

public class ExportPrepareController {
    @FXML
    private TableView<Invoice> tableViewPrepare;
    @FXML
    private TableColumn<Invoice, Boolean> tableColumnCheckbox;
    @FXML
    private TableColumn<Invoice, String> tableColumnCode;
    @FXML
    private TableColumn<Invoice, String> tableColumnDescription;
    @FXML
    private TableColumn<Invoice, String> tableColumnCustomer;
    @FXML
    private Pane generate;
    @FXML
    private AnchorPane anchorPaneMain;

    private ExportController exportController;

    ObservableList<Invoice> selectedInvoices = FXCollections.observableArrayList();

    public void initialize(){
        loadTable();
        generate.setOnMouseClicked(e -> {
            ObservableList<Invoice> currentData = tableViewPrepare.getItems();
            System.out.println("Checked TableView Data:");
            selectedInvoices.clear();
            for (Invoice item : currentData) {
                if (item.getChecked().get()) {
                    System.out.println("Item: " + item.getChecked().get() + ", Desc: " + item.getDescription());
                    selectedInvoices.add(item);
                }
            }
            if (!selectedInvoices.isEmpty()) {
//                exportController.setDataExport(selectedInvoices);
                setSelectedInvoices(selectedInvoices);
//                exportController.setDataExport(selectedInvoices);
                openExportPane();
            } else {
                showInformationDialog("Please select at least one invoice to export.");
            }
        });
    }

    private void loadTable(){
        tableViewPrepare.setEditable(true);
        tableViewPrepare.setItems(FXCollections.observableArrayList(Invoice.getAllData()));
        tableColumnCheckbox.setCellValueFactory(e -> e.getValue().getChecked());
        tableColumnCheckbox.setCellFactory(e -> new CheckBoxTableCell<>());

        tableColumnCode.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceCode()));
        tableColumnDescription.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnCustomer.setCellValueFactory(e -> new SimpleStringProperty(Customer.getOneData(e.getValue().getCustomer_id()).getName()));
    }

    private boolean showInformationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public void setSelectedInvoices(ObservableList<Invoice> selectedInvoices) {
        this.selectedInvoices = selectedInvoices;
    }

    private void openExportPane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("export-view.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ExportController test = fxmlLoader.getController();
            test.setExportPrepareController(this);
            test.setDataExport(selectedInvoices);
            test.customInitialize();

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    public void setExportController(ExportController exportController) {
        this.exportController = exportController;
    }
}
