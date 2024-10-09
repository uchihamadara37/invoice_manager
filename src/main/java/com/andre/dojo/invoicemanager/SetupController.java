package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    private Pane save;


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
    private Label showSelectBank;

    @FXML
    private ChoiceBox<String> showBank;

    @FXML
    private AnchorPane rootPane;

    private HelloController helloController;
    private Invoice selectedInvoice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showMessage.setVisible(false);
        showSelectedInvoice(null);
        loadIsiTable();
        loadBox();
        tableViewInvoice.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                showSelectedInvoice(newValue);
                showMessage.setVisible(false);
                helloController.setInvoiceSelected(newValue);

                helloController.getAddItemController().showInvoiceSelected(helloController.getInvoiceSelected());
                helloController.getAddItemController().reloadTableItem();
                helloController.getAddItemController().reloadTotalPrice();
                helloController.getJrxmlController().loadDesignSideBar();
                helloController.getJrxmlController().setState("lihat");

                loadJsonData(newValue);
                loadJrxmlString(newValue);
                helloController.getPreviewController().setLabelDesignSelected("Design "+helloController.getInvoiceSelected().getJrxml_id()+" has been selected for this invoice. Lets start preview!");
            });
        save.setOnMouseClicked(e -> {
            if (Objects.equals(showTimeStamp.getText(), "")){
                showMessage.setVisible(true);
                showTooltip(btnAdd);
            }else{
                saveBank(showBank.getSelectionModel().getSelectedItem());
            }
        });
        btnAdd.setOnMouseClicked(e -> {
            if (Objects.equals(showTimeStamp.getText(), "")){
                showMessage.setVisible(true);
                showTooltip(btnAdd);
            }else{
                openAddItems();
            }
        });
        btnItems.setOnMouseClicked(e -> {
            if (Objects.equals(showTimeStamp.getText(), "")){
                showMessage.setVisible(true);
                showTooltip(btnItems);
            }else{
                openAddItems();
            }
        });
        btnJsonData.setOnMouseClicked(e -> {
            if (Objects.equals(showTimeStamp.getText(), "")){
                showMessage.setVisible(true);
                showTooltip(btnJsonData);
            }else{
                helloController.getAnchorPaneMain().getChildren().removeFirst();
                helloController.getAnchorPaneMain().getChildren().add(helloController.getJsonDataController().getRootPane());
            }
        });
        btnJrxml.setOnMouseClicked(e -> {
            if (Objects.equals(showTimeStamp.getText(), "")){
                showMessage.setVisible(true);
                showTooltip(btnJrxml);
            }else{
                helloController.getAnchorPaneMain().getChildren().removeFirst();
                helloController.getAnchorPaneMain().getChildren().add(helloController.getJrxmlController().getRootPane());
            }
        });
        btnPreview.setOnMouseClicked(e -> {
            if (Objects.equals(showTimeStamp.getText(), "")){
                showMessage.setVisible(true);
                showTooltip(btnPreview);
            }else{
                helloController.getAnchorPaneMain().getChildren().removeFirst();
                helloController.getAnchorPaneMain().getChildren().add(helloController.getPreviewController().getRootPane());
            }
        });

    }

    private void showTooltip(Pane Component) {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Please select one invoice first!");
        tooltip.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 15px;");
        Point2D p = Component.localToScene(0.0, 0.0);
        tooltip.show(
                Component,
                p.getX() + HelloApplication.getMainStage().getX()
                        + Component.getScene().getX() + 30,
                p.getY() + HelloApplication.getMainStage().getY()
                        + Component.getScene().getY() - 35
        );
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                ae -> tooltip.hide()
        ));
        timeline.play();
    }
    private void showTooltip(Label Component) {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Please select one invoice first!");
        tooltip.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 15px;");
        Point2D p = Component.localToScene(0.0, 0.0);
        tooltip.show(
                Component,
                p.getX() + HelloApplication.getMainStage().getX()
                        + Component.getScene().getX() + 30,
                p.getY() + HelloApplication.getMainStage().getY()
                        + Component.getScene().getY() - 35
        );
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                ae -> tooltip.hide()
        ));
        timeline.play();
    }

    private void loadJrxmlString(Invoice newValue) {
        if (helloController.getInvoiceSelected().getJrxml_id() != 0){
            // helloController.getJrxmlController().setJrxmlTextArea(Design.getOneData(newValue.getJrxml_id()).getJrxml());
            Platform.runLater(() ->{
                helloController.getJrxmlController().showPreview();
            });
        }else{
            helloController.getJrxmlController().setJrxmlTextArea("");
            helloController.getPreviewController().getPreviewPane().getChildren().clear();
        }
    }

    public static void loadJsonData(Invoice invoice) {
        invoice.setTotalPriceAll(Item.getSumOfPriceByInvoiceId(invoice.getId()));

        HelloController.customJSON.setOrganization(Organization.getOneData(Customer.getOneData(invoice.getCustomer_id()).getOrganization_id()));
        HelloController.customJSON.setCustomer(Customer.getOneData(invoice.getCustomer_id()));
        HelloController.customJSON.setInvoice(invoice);
        String json = HelloApplication.gson.toJson(HelloController.customJSON, CustomJSON.class);

        invoice.setJsonData(json);
        Invoice.updateById(invoice);
        HelloController.jsonDataController.setJsonText(json);

    }

    private void goToAddItem() {

    }

    private void openAddItems() {
        helloController.getAnchorPaneMain().getChildren().removeFirst();
        helloController.getAnchorPaneMain().getChildren().add(helloController.getAddItemController().getRootPane());
    }

    private void showSelectedInvoice(Invoice invoice) {
        if (invoice != null){
            selectedInvoice = invoice;
            showCode.setText(invoice.getInvoiceCode());
            showDescription.setText(invoice.getDescription());
            showCustomer.setText(Customer.getOneData(invoice.getCustomer_id()).getName()+" | "+Customer.getOneData(invoice.getCustomer_id()).getDescription());
            showTimeStamp.setText("Created at "+ZonedDateTime.parse(invoice.getTimestamp()).format(formatter));
            loadBank(invoice.getBank_id());
        }else{
            showCode.setText("No Invoice Selected.");
            showDescription.setText("Please select invoice first then you can add some items on it.");
            showCustomer.setText("(^_^)/");
            showTimeStamp.setText("");
            showBank.setVisible(false);
            showSelectBank.setVisible(false);
            save.setVisible(false);
        }
    }

    private void loadIsiTable() {
        tableViewInvoice.setEditable(true);
        tableViewInvoice.setItems(FXCollections.observableArrayList(Invoice.getAllDataGroubByTemplate()));

        tableColumnTimeCreated.setCellValueFactory(e -> {

            ZonedDateTime zonedDateTime = ZonedDateTime.parse(e.getValue().getTimestamp());
//            System.out.println(e.getValue().getTimestamp());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a");
            return new SimpleStringProperty(zonedDateTime.format(formatter));
//            return new SimpleStringProperty("okoko");
        });
        tableColumnCode.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceCode()));
        tableColumnDescription.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnCustomer.setCellValueFactory(e -> new SimpleStringProperty(Customer.getOneData(e.getValue().getCustomer_id()).getName()));
    }

    private void loadBox(){
        List<Bank> bank = Bank.getAllData();
        for (Bank banks : bank) {
            showBank.getItems().add(banks.getBank_name());
        }
    }

    private void loadBank(long idBank){
        Bank bank = Bank.getOneData(idBank);
        showBank.setVisible(true);
        showSelectBank.setVisible(true);
        save.setVisible(true);
        showBank.getSelectionModel().select(bank.getBank_name());
    }

    private void saveBank(String selectedBankName){
        Bank bank = Bank.getOneDataByName(selectedBankName);
        selectedInvoice.setBank_id(bank.getId());
        Invoice.updateById(selectedInvoice);
    }

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }
}
