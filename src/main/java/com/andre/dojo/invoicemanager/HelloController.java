package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.CustomJSON;
import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Invoice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class HelloController implements Initializable {
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


    @FXML
    private Pane tombolInvoice;
    @FXML
    private Pane tombolChangeData;
    @FXML
    private Pane tombolExport;
    @FXML
    private Label tombolHistory;
    @FXML
    private Label tombolTanggal;
    @FXML
    private Pane tombolGenerate;
    @FXML
    private Pane tombolSetup;

    @FXML
    private AnchorPane anchorPaneMain;
    @FXML
    private AnchorPane paneInvoice;


    private SetupController setupController;
    private AddItemController addItemController;
    private JsonDataController jsonDataController;
    private JrxmlController jrxmlController;
    private PreviewController previewController;


    private Invoice invoiceSelected;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static CustomJSON customJSON = new CustomJSON();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("jangkrik");

        loadTableView();

        // event tombol
        tombolChangeData.setOnMouseClicked(e -> {
            tombolChangeData.getStyleClass().removeFirst();
            tombolChangeData.getStyleClass().add("side-button-active");
            tombolInvoice.getStyleClass().removeFirst();
            tombolInvoice.getStyleClass().add("side-button");
            tombolExport.getStyleClass().removeFirst();
            tombolExport.getStyleClass().add("side-button");
            tombolSetup.getStyleClass().removeFirst();
            tombolSetup.getStyleClass().add("side-button");
            openChangeDataPane();
        });
        tombolInvoice.setOnMouseClicked(e -> {
            tombolChangeData.getStyleClass().removeFirst();
            tombolChangeData.getStyleClass().add("side-button");
            tombolInvoice.getStyleClass().removeFirst();
            tombolInvoice.getStyleClass().add("side-button-active");
            tombolExport.getStyleClass().removeFirst();
            tombolExport.getStyleClass().add("side-button");
            tombolSetup.getStyleClass().removeFirst();
            tombolSetup.getStyleClass().add("side-button");
            openInvoicePane();
        });
        tombolSetup.setOnMouseClicked(e -> {
            tombolChangeData.getStyleClass().removeFirst();
            tombolChangeData.getStyleClass().add("side-button");
            tombolInvoice.getStyleClass().removeFirst();
            tombolInvoice.getStyleClass().add("side-button");
            tombolExport.getStyleClass().removeFirst();
            tombolExport.getStyleClass().add("side-button");
            tombolSetup.getStyleClass().removeFirst();
            tombolSetup.getStyleClass().add("side-button-active");
            openSetupPane();
        });
        tombolExport.setOnMouseClicked(e -> {
            tombolChangeData.getStyleClass().removeFirst();
            tombolChangeData.getStyleClass().add("side-button");
            tombolInvoice.getStyleClass().removeFirst();
            tombolInvoice.getStyleClass().add("side-button");
            tombolExport.getStyleClass().removeFirst();
            tombolExport.getStyleClass().add("side-button-active");
            tombolSetup.getStyleClass().removeFirst();
            tombolSetup.getStyleClass().add("side-button");
            openExportPane();
        });



        tombolHistory.setOnMouseClicked(e -> {
            openHistoryPane();
        });
        tombolGenerate.setOnMouseClicked(e -> {
            ObservableList<Invoice> currentData = tableViewInvoice.getItems();
            System.out.println("Current TableView Data:");
            for (Invoice item : currentData) {
                System.out.println("Item: " + item.getChecked().get() + ", Desc: " + item.getDescription());
            }
        });
        tableViewInvoice.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
//                    System.out.println("bool: "+newValue.getChecked().get());

                });
    }

    private void openSetupPane() {
        try {
            FXMLLoader setupLoader = new FXMLLoader(HelloApplication.class.getResource("setup-view.fxml"));
            FXMLLoader addLoader = new FXMLLoader(HelloApplication.class.getResource("addItem-view.fxml"));
            FXMLLoader jsonLoader = new FXMLLoader(HelloApplication.class.getResource("jsonData-view.fxml"));
            FXMLLoader jrxmlLoader = new FXMLLoader(HelloApplication.class.getResource("jrxml-view.fxml"));
            FXMLLoader previewLoader = new FXMLLoader(HelloApplication.class.getResource("preview-view.fxml"));

            setupLoader.load();
            addLoader.load();
            jsonLoader.load();
            jrxmlLoader.load();
            previewLoader.load();
            setupController = setupLoader.getController();
            setupController.setHelloController(this);
            addItemController = addLoader.getController();
            addItemController.setHelloController(this);
            jsonDataController = jsonLoader.getController();
            jsonDataController.setHelloController(this);
            jrxmlController = jrxmlLoader.getController();
            jrxmlController.setHelloController(this);
            previewController = previewLoader.getController();
            previewController.setHelloController(this);


            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(setupController.getRootPane());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void openHistoryPane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("history-view.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            HistoryController historyController = fxmlLoader.getController();
            historyController.setHelloController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
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
    public AnchorPane getAnchorPaneMain(){
        return anchorPaneMain;
    }
    public AnchorPane getPaneInvoice(){
        return paneInvoice;
    }

    public void loadTableView() {
        tableViewInvoice.setEditable(true);
        tableViewInvoice.setItems(FXCollections.observableArrayList(Invoice.getAllData()));
        tableColumnCheckbox.setCellValueFactory(e -> e.getValue().getChecked());
        tableColumnCheckbox.setCellFactory(e -> new CheckBoxTableCell<>());

        tableColumnCode.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceCode()));
        tableColumnDescription.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnCustomer.setCellValueFactory(e -> new SimpleStringProperty(Customer.getOneData(e.getValue().getCustomer_id()).getName()));
    }

    public void setInvoiceSelected(Invoice invoiceSelected) {
        this.invoiceSelected = invoiceSelected;
    }

    public Invoice getInvoiceSelected() {
        return invoiceSelected;
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

    public SetupController getSetupController() {
        return setupController;
    }

    public AddItemController getAddItemController() {
        return addItemController;
    }

    public JsonDataController getJsonDataController() {
        return jsonDataController;
    }

    public PreviewController getPreviewController() {
        return previewController;
    }

    public JrxmlController getJrxmlController() {
        return jrxmlController;
    }

    public void setJsonDataController(JsonDataController jsonDataController) {
        this.jsonDataController = jsonDataController;
    }

    public boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    public boolean showInformationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}