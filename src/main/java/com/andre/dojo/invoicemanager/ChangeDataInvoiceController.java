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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.Year;

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
    private TableColumn<Customer, Void> tableColumnDelete;

    @FXML
    private TextField name;
    @FXML
    private TextField desc;
    @FXML
    private TextField phone;
    @FXML
    private TextField descInv;

    @FXML
    private Label tombolOrganization;
    @FXML
    private Label inv;
    @FXML
    private Label no;

    @FXML
    private AnchorPane anchorPaneMain;

    @FXML
    private Pane add;
    @FXML
    private Pane update;
    @FXML
    private Pane reset;

    private ChangeDataController changedataController;
    private HelloController helloController;
    private long idCustomer;
    private long idOrganization;

    public void setChangedataController(ChangeDataController changedataController){
        this.changedataController = changedataController;
    }

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public void initialize(){
        update.setVisible(false);
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
            name.setText(customer.getName());
            desc.setText(customer.getDescription());
            phone.setText(customer.getPhoneNumber());
//            Organization organization = Organization.getOneData(12);
            Invoice invoice = Invoice.getOneDataByCustomer(customer.getId());
            idCustomer = customer.getId();
            idOrganization = customer.getOrganization_id();
            descInv.setText(invoice.getDescription());
            no.setText(invoice.getInvoiceCode());
            update.setVisible(true);
            add.setVisible(false);
            no.setVisible(true);
            inv.setVisible(true);
        } else {
            name.setText("");
            desc.setText("");
            phone.setText("");
        }
    }

    private void addCustomer(){
        String cId = "1723509861951";
        String designId = "1725261011387";
        Customer.addToDB(
                new Customer(
                        name.getText(),
                        desc.getText(),
                        phone.getText(),
                        Long.parseLong(cId)
                ));
        Customer cek = Customer.getLastInsertedData();
        System.out.println("Last id data after insert : "+ cek.getId());
        String idSurat = "1723596852024";
        KodeSurat kodeSurat = KodeSurat.getOneData(Long.parseLong(idSurat));
        String year = String.valueOf(Year.now().getValue());
        String yearCode = year.substring(year.length() - 2);
        int urutanInvoice = kodeSurat.getNoUrut()+1;
        String idOr = "1723509861951";
        Organization organization = Organization.getOneData(Long.parseLong(idOr));
        int urutanSurat = organization.getTotalLetter() + 1;
        String invoiceCode = urutanSurat + "/SG/INV/" + urutanInvoice + "/" + yearCode;
        KodeSurat.updateById(new KodeSurat(
                Long.parseLong(idSurat), kodeSurat.getKode(),urutanInvoice,kodeSurat.getOrganization_id()
        ), Long.parseLong(idSurat));
        Organization.updateTotalLetter(new Organization(
                Long.parseLong(idOr), urutanSurat, organization.getBrandName()
        ), Long.parseLong(idOr));
        Invoice.addToDB(new Invoice(
                "Alfiander Comunity", descInv.getText(), invoiceCode, "29 Agustus 2024", 0, "", "", Long.parseLong(designId),cek.getId())
        );
        loadTableView();
        reset();
    }

    private void editCustomer(){
        Customer.updateById(new Customer(
                name.getText(),
                desc.getText(),
                phone.getText(),
                idOrganization,
                idCustomer
        ));
        System.out.println("is clikced?");
        loadTableView();
        reset();
    }

    private void reset(){
        name.setText("");
        desc.setText("");
        phone.setText("");
        descInv.setText("");
        no.setVisible(false);
        inv.setVisible(false);
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
