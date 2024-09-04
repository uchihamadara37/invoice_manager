package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Invoice;
import com.andre.dojo.Models.KodeSurat;
import com.andre.dojo.Models.Organization;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeDataInvoiceController {
    @FXML
    private AnchorPane anchorPaneMain;
    @FXML
    private TableView<Invoice> tableViewInvoice;
    @FXML
    private TableColumn<Invoice, String> tableColumnCode;
    @FXML
    private TableColumn<Invoice, String> tableColumnName;
    @FXML
    private TableColumn<Invoice, String> tableColumnDesc;
    @FXML
    private TableColumn<Invoice, Void> tableColumnDelete;

    @FXML
    private Label tombolCustomer;
    @FXML
    private Label tombolLetter;

    @FXML
    private ChoiceBox<String> custName;
    @FXML
    private TextField desc;
    @FXML
    private TextField name;

    @FXML
    private Pane add;
    @FXML
    private Pane update;
    @FXML
    private Pane reset;

    private ChangeDataCustomerController changeDataCustomerController;
    private ChangeDataController changeDataController;
    private Map<String, Customer> customerMap = new HashMap<>();
    private long customerId;
    private String invCode;
    private long invId;

    public void initialize(){
        tombolLetter.setOnMouseClicked(e -> {
            openLetterPane();
        });
        tombolCustomer.setOnMouseClicked(e -> {
            openCustomerPane();
        });
        reset.setOnMouseClicked(event -> {
            reset();
        });
        update.setOnMouseClicked(event -> {
            handleUpdate();
        });
        add.setOnMouseClicked(event -> {
            handleAdd();
        });
        loadBox();
        loadData();
        conButton(true);
        custName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleSelection();
        });
        tableViewInvoice.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetail(newValue));
    }

    public AnchorPane getAnchorPaneMain() {
        return anchorPaneMain;
    }

    public void setChangeDataCustomerController(ChangeDataCustomerController changeDataCustomerController) {
        this.changeDataCustomerController = changeDataCustomerController;
    }

    public void setChangeDataController(ChangeDataController changeDataController) {
        this.changeDataController = changeDataController;
    }

    private void openLetterPane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ChangeDataController changeDataController = fxmlLoader.getController();
            changeDataController.setChangeDataInvoiceController(this);

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
            changeDataCustomerController.setChangeDataInvoiceController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void loadData(){
//        List<Customer> customers = Customer.getAllData();
//
//        for (Customer customer : customers) {
//            String name = customer.getName(); // Assuming getName() returns the customer's name
//            custName.getItems().add(name);
//            customerMap.put(name, customer); // Map the name to the Customer object
//        }
        tableViewInvoice.setEditable(true);
        tableViewInvoice.setItems(FXCollections.observableArrayList(Invoice.getAllData()));
        tableColumnName.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceMarkText()));
        tableColumnDesc.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnCode.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceCode()));
        tableColumnDelete.setCellFactory(e -> new TableCell<Invoice, Void>() {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("icon/circle-minus.png")));
            private final HBox container = new HBox();
            {
                icon.setFitHeight(20);
                icon.setFitWidth(20);

                container.setAlignment(Pos.CENTER_LEFT);
                container.getChildren().add(icon);
                container.setPadding(new Insets(0,0,0,20));

                container.setOnMouseClicked(event -> {
                    Invoice invoice = getTableView().getItems().get(getIndex());
                    handleDelete(invoice);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);  // Don't show anything if the cell is empty
                } else {
                    setGraphic(container);  // Show the HBox containing the icon
                }
            }
        });
    }

    private void handleDelete(Invoice invoice){
        Invoice.deleteOneById(invoice.getId());
        loadData();
        reset();
    }

    private void handleSelection() {
        String selectedName = custName.getSelectionModel().getSelectedItem();
        if (selectedName != null) {
            Customer selectedCustomer = customerMap.get(selectedName);
            if (selectedCustomer != null) {
                customerId = selectedCustomer.getId();
            }
        }
    }

    private void showDetail(Invoice invoice){
        if (invoice != null) {
            name.setText(invoice.getInvoiceMarkText());
            desc.setText(invoice.getDescription());
            invCode = invoice.getInvoiceCode();
            invId = invoice.getId();
            selectCustomerById(invoice.getCustomer_id());
            conButton(false);
        } else {
            name.setText("");
            desc.setText("");
        }

    }

    private void selectCustomerById(long customerId) {
        for (Map.Entry<String, Customer> entry : customerMap.entrySet()) {
            Customer customer = entry.getValue();
            if (customer.getId() == customerId) { // Check if the customer ID matches
                custName.getSelectionModel().select(entry.getKey()); // Select the corresponding name
                break;
            }
        }
    }

    private void handleUpdate(){
        String designId = "1725261011387";
        Invoice.updateById(new Invoice(
                name.getText(), desc.getText(), invCode, "29 Agustus 2024", 0,"","",Long.parseLong(designId), customerId, invId
        ));
        loadData();
        reset();
    }

    private void handleAdd(){
        String idSurat = "1723596852024";
        String designId = "1725261011387";
        KodeSurat kodeSurat = KodeSurat.getOneData(Long.parseLong(idSurat));
        String year = String.valueOf(Year.now().getValue());
        String yearCode = year.substring(year.length() - 2);
        int urutanInvoice = kodeSurat.getNoUrut()+1;
        String idOr = "1723509861951";
        Organization organization = Organization.getOneData(Long.parseLong(idOr));
        int urutanSurat = organization.getTotalLetter() + 1;
        String invoiceCode = urutanSurat + "/SG/INV/" + urutanInvoice + "/" + yearCode;
        Invoice.addToDB(new Invoice(
                name.getText(), desc.getText(), invoiceCode, "29 Agustus 2024", 0,"","",Long.parseLong(designId), customerId
        ));
        Organization.updateTotalLetter(new Organization(
                Long.parseLong(idOr), urutanSurat, organization.getBrandName()
        ), Long.parseLong(idOr));
        KodeSurat.updateById(new KodeSurat(
                Long.parseLong(idSurat), kodeSurat.getKode(),urutanInvoice,kodeSurat.getOrganization_id()
        ), Long.parseLong(idSurat));
        loadData();
        reset();
    }

    private void conButton(boolean con){
        add.setVisible(con);
        update.setVisible(!con);
    }

    private void reset(){
        name.setText("");
        desc.setText("");
        custName.getSelectionModel().clearSelection();
        conButton(true);
    }

    private void loadBox(){
        List<Customer> customers = Customer.getAllData();

        for (Customer customer : customers) {
            String name = customer.getName(); // Assuming getName() returns the customer's name
            custName.getItems().add(name);
            customerMap.put(name, customer); // Map the name to the Customer object
        }
    }
}
