package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Invoice;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ChangeDataInvoiceController {

    @FXML
    private TableView<Customer> tableViewCustomer;
    @FXML
    private TableColumn<Customer, String> tableColumnName;
    @FXML
    private TableColumn<Customer, String> tableColumnDesc;
    @FXML
    private TableColumn<Customer, String> tableColumnPhone;

    @FXML
    private TextField name;
    @FXML
    private TextField desc;
    @FXML
    private TextField phone;

    @FXML
    private Label tombolOrganization;
    @FXML
    private Label inv;
    @FXML
    private Label no;

    @FXML
    private AnchorPane anchorPaneMain;

    @FXML
    private Button add;
    @FXML
    private Button update;
    @FXML
    private Button reset;

    private ChangeDataController changedataController;

    public void setChangedataController(ChangeDataController changedataController){
        this.changedataController = changedataController;
    }

    public void initialize(){
        update.setDisable(true);
        no.setVisible(false);
        inv.setVisible(false);
        tombolOrganization.setOnMouseClicked(e -> {
            openOrganizationPane();
        });
        loadTableView();

        // Clear person details.
        showDataCustomer(null);

        // Listen for selection changes and show the person details when changed.
        tableViewCustomer.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDataCustomer(newValue));

        reset.setOnAction(event -> {
            name.setText("");
            desc.setText("");
            phone.setText("");
            no.setVisible(false);
            inv.setVisible(false);
            add.setDisable(false);
            update.setDisable(true);
        });
    }

    private void openOrganizationPane() {
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

    public void loadTableView() {
        tableViewCustomer.setEditable(true);
        tableViewCustomer.setItems(FXCollections.observableArrayList(Customer.getAllData()));

        tableColumnName.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getName()));
        tableColumnDesc.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnPhone.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getPhoneNumber()));
    }

    private void showDataCustomer(Customer customer) {
        if (customer != null) {
            name.setText(customer.getName());
            desc.setText(customer.getDescription());
            phone.setText(customer.getPhoneNumber());
            update.setDisable(false);
            add.setDisable(true);
            no.setVisible(true);
            inv.setVisible(true);
        } else {
            name.setText("");
            desc.setText("");
            phone.setText("");
        }
    }
}
