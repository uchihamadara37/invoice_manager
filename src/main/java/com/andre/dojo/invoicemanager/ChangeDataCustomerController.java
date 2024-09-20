package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.*;
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

public class ChangeDataCustomerController {

    @FXML
    private TableView<Customer> tableViewCustomer;
    @FXML
    private TableColumn<Customer, String> tableColumnName;
    @FXML
    private TableColumn<Customer, String> tableColumnDesc;
    @FXML
    private TableColumn<Customer, String> tableColumnPhone;
    @FXML
    private TableColumn<Customer, Void> tableColumnDelete;

    @FXML
    private TextField name;
    @FXML
    private TextField desc;
    @FXML
    private TextField phone;

    @FXML
    private Label tombolLetter;
    @FXML
    private Label tombolInvoice;
    @FXML
    private Label tombolCode;

    @FXML
    private AnchorPane anchorPaneMain;

    @FXML
    private Pane add;
    @FXML
    private Pane update;
    @FXML
    private Pane reset;

    private ChangeDataController changedataController;
    private ChangeDataInvoiceController changeDataInvoiceController;
    private ChangeDataCodeController changeDataCodeController;
    private HelloController helloController;
    private Customer selectedCustomer;
    private long idCustomer;
    private long idOrganization;

    public void setChangedataController(ChangeDataController changedataController){
        this.changedataController = changedataController;
    }

    public void setChangeDataInvoiceController(ChangeDataInvoiceController changeDataInvoiceController) {
        this.changeDataInvoiceController = changeDataInvoiceController;
    }

    public void setChangeDataCodeController(ChangeDataCodeController changeDataCodeController) {
        this.changeDataCodeController = changeDataCodeController;
    }

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public void initialize(){
        update.setVisible(false);

        tombolLetter.setOnMouseClicked(e -> {
            openOrganizationPane();
        });

        tombolInvoice.setOnMouseClicked(e -> {
            openInvoicePane();
        });

        tombolCode.setOnMouseClicked(e -> {
            openCodePane();
        });

        loadTableView();
        showDataCustomer(null);
        tableViewCustomer.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDataCustomer(newValue));
        reset.setOnMouseClicked(event -> {
            reset();
        });
        add.setOnMouseClicked(event->{
            addCustomer();
        });
        update.setOnMouseClicked(event->{
            editCustomer();
        });
    }

    private void openOrganizationPane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());
            ChangeDataController changeDataController = fxmlLoader.getController();
            changeDataController.setChangeDataCustomerController(this);
            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void openInvoicePane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data-invoice.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());
            ChangeDataInvoiceController changeDataInvoiceController = fxmlLoader.getController();
            changeDataInvoiceController.setChangeDataCustomerController(this);
            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void openCodePane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data-code.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());
            ChangeDataCodeController changeDataCodeController = fxmlLoader.getController();
            changeDataCodeController.setChangeDataCustomerController(this);
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
        tableColumnDelete.setCellFactory(e -> new TableCell<Customer, Void>() {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("icon/circle-minus.png")));
            private final HBox container = new HBox();
            {
                icon.setFitHeight(20);
                icon.setFitWidth(20);

                container.setAlignment(Pos.CENTER_LEFT);
                container.getChildren().add(icon);
                container.setPadding(new Insets(0,0,0,20));

                container.setOnMouseClicked(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    handleDelete(customer);
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

    private void showDataCustomer(Customer customer) {
        if (customer != null) {
            selectedCustomer = customer;
            name.setText(customer.getName());
            desc.setText(customer.getDescription());
            phone.setText(customer.getPhoneNumber());
            idCustomer = customer.getId();
            idOrganization = customer.getOrganization_id();
            update.setVisible(true);
            add.setVisible(false);
        } else {
            name.setText("");
            desc.setText("");
            phone.setText("");
        }
    }

    private void addCustomer(){
        String cId = "1723509861951";
        Customer.addToDB(
                new Customer(
                        name.getText(),
                        desc.getText(),
                        phone.getText(),
                        Long.parseLong(cId)
                ));
        loadTableView();
        reset();
    }

    private void editCustomer(){
        selectedCustomer.setName(name.getText());
        selectedCustomer.setDescription(desc.getText());
        selectedCustomer.setPhoneNumber(phone.getText());
        Customer.updateById(selectedCustomer);
        System.out.println("is clikced?");
        loadTableView();
        reset();
    }

    private void reset(){
        name.setText("");
        desc.setText("");
        phone.setText("");
        add.setVisible(true);
        update.setVisible(false);
    }

    private void handleDelete(Customer customer){
        Invoice.deleteOneByIdCustomer(customer.getId());
        Customer.deleteOneById(customer.getId());
        System.out.println("Delete operation with id you just clicked is = " + customer.getId());
        loadTableView();
        reset();
    }
}
