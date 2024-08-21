package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class SetupController implements Initializable {

    @FXML
    private TableView<Invoice> tableViewInvoice;
    @FXML
    private TableColumn<Invoice, String> tableColumnCode;
    @FXML
    private TableColumn<Invoice, String> tableColumnDescription;
    @FXML
    private TableColumn<Invoice, String> tableColumnTimeCreated;
    @FXML
    private TableColumn<Invoice, String> tableColumnCustomer;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a");

    @FXML
    private Label btnItems;
    @FXML
    private Label  btnJsonData;
    @FXML
    private Label btnJrxml;
    @FXML
    private Label btnPreview;

    @FXML
    private Pane btnAdd;


    @FXML
    private Label showCode;
    @FXML
    private Label showDescription;
    @FXML
    private Label showCustomer;
    @FXML
    private Label showTimeStamp;
    @FXML
    private Label showMessage;

    @FXML
    private AnchorPane rootPane;

    private HelloController helloController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showMessage.setVisible(false);
        showSelectedInvoice(null);
        loadIsiTable();
        tableViewInvoice.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                showSelectedInvoice(newValue);
                showMessage.setVisible(false);
                helloController.setInvoiceSelected(newValue);

                helloController.getAddItemController().showInvoiceSelected(helloController.getInvoiceSelected());
                helloController.getAddItemController().reloadTableItem();
                helloController.getAddItemController().reloadTotalPrice();
                loadJsonData(newValue);
                loadJrxmlString(newValue);
            });

        btnAdd.setOnMouseClicked(e -> {
            goToAddItem();
        });
        btnItems.setOnMouseClicked(e -> {
            goToAddItem();
        });
        btnJsonData.setOnMouseClicked(e -> {
            if (Objects.equals(showTimeStamp.getText(), "")){
                showMessage.setVisible(true);
            }else{
                helloController.getAnchorPaneMain().getChildren().removeFirst();
                helloController.getAnchorPaneMain().getChildren().add(helloController.getJsonDataController().getRootPane());
            }
        });
        btnJrxml.setOnMouseClicked(e -> {
            if (Objects.equals(showTimeStamp.getText(), "")){
                showMessage.setVisible(true);
            }else{
                helloController.getAnchorPaneMain().getChildren().removeFirst();
                helloController.getAnchorPaneMain().getChildren().add(helloController.getJrxmlController().getRootPane());
            }
        });
        btnPreview.setOnMouseClicked(e -> {
            if (Objects.equals(showTimeStamp.getText(), "")){
                showMessage.setVisible(true);
            }else{
                helloController.getAnchorPaneMain().getChildren().removeFirst();
                helloController.getAnchorPaneMain().getChildren().add(helloController.getPreviewController().getRootPane());
            }
        });

    }

    private void loadJrxmlString(Invoice newValue) {
        if (helloController.getInvoiceSelected().getJrxml_id() != 0){
            helloController.getJrxmlController().setJrxmlTextArea(Design.getOneData(newValue.getJrxml_id()).getJrxml());
            Platform.runLater(() ->{
                helloController.getJrxmlController().showPreview();
            });
        }else{
            helloController.getJrxmlController().setJrxmlTextArea("");
            helloController.getPreviewController().getPreviewPane().getChildren().clear();
        }
    }

    private void loadJsonData(Invoice invoice) {
        invoice.setTotalPriceAll(Item.getSumOfPriceByInvoiceId(invoice.getId()));

        HelloController.customJSON.setOrganization(Organization.getOneData(Customer.getOneData(invoice.getCustomer_id()).getOrganization_id()));
        HelloController.customJSON.setCustomer(Customer.getOneData(invoice.getCustomer_id()));
        HelloController.customJSON.setInvoice(invoice);
        String json = HelloApplication.gson.toJson(HelloController.customJSON, CustomJSON.class);

        invoice.setJsonData(json);
        Invoice.updateById(invoice);
        helloController.getJsonDataController().setJsonText(json);

    }

    private void goToAddItem() {
        if (Objects.equals(showTimeStamp.getText(), "")){
            showMessage.setVisible(true);
        }else{
            openAddItems();
        }
    }

    private void openAddItems() {
        helloController.getAnchorPaneMain().getChildren().removeFirst();
        helloController.getAnchorPaneMain().getChildren().add(helloController.getAddItemController().getRootPane());
    }

    private void showSelectedInvoice(Invoice invoice) {
        if (invoice != null){
            showCode.setText(invoice.getInvoiceCode());
            showDescription.setText(invoice.getDescription());
            showCustomer.setText(Customer.getOneData(invoice.getCustomer_id()).getName()+" | "+Customer.getOneData(invoice.getCustomer_id()).getDescription());
            showTimeStamp.setText("Created at "+ZonedDateTime.parse(invoice.getTimeStamp()).format(formatter));
        }else{
            showCode.setText("No Invoice Selected.");
            showDescription.setText("Please select invoice first then you can add some items on it.");
            showCustomer.setText("(^_^)/");
            showTimeStamp.setText("");
        }
    }

    private void loadIsiTable() {
        tableViewInvoice.setEditable(true);
        tableViewInvoice.setItems(FXCollections.observableArrayList(Invoice.getAllData()));

        tableColumnTimeCreated.setCellValueFactory(e -> {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(e.getValue().getTimeStamp());
            return new SimpleStringProperty(zonedDateTime.format(formatter));
        });
        tableColumnCode.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceCode()));
        tableColumnDescription.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnCustomer.setCellValueFactory(e -> new SimpleStringProperty(Customer.getOneData(e.getValue().getCustomer_id()).getName()));
    }

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }
}
