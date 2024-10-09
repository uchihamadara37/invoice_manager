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
    private Label tombolCode;

    @FXML
    private ChoiceBox<String> custName;
    @FXML
    private ChoiceBox<String> code;
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
    private ChangeDataCodeController changeDataCodeController;
    private Map<String, Long> customerMap = new HashMap<>();
    private Map<String, Long> kodeMap = new HashMap<>();
    private Long customerId;
    private Long codeLetter;
    private String invCode;
    private long invId;
    private Invoice selectedInvoice;
    private Customer selectedCustomer;

    public void initialize(){
        tombolLetter.setOnMouseClicked(e -> {
            openLetterPane();
        });
        tombolCustomer.setOnMouseClicked(e -> {
            openCustomerPane();
        });
        tombolCode.setOnMouseClicked(e -> {
            openCodePane();
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
        custName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleSelection();
        });
        loadBox();
        loadBox2();
        loadData();
        conButton(true);

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

    public void setChangeDataCodeController(ChangeDataCodeController changeDataCodeController) {
        this.changeDataCodeController = changeDataCodeController;
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

    private void openCodePane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data-code.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ChangeDataCodeController changeDataCodeController = fxmlLoader.getController();
            changeDataCodeController.setChangeDataInvoiceController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void loadData(){
        tableViewInvoice.setEditable(true);
        tableViewInvoice.setItems(FXCollections.observableArrayList(Invoice.getAllDataGroubByTemplate()));
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
        customerId = customerMap.get(selectedName);
        if(customerId != null) {
            selectedCustomer = Customer.getOneData(customerId);
            System.out.println("berhasil ambil : " + customerId);
        }else{
            System.out.println("gagal ambil");
        }
    }

    private void showDetail(Invoice invoice){
        selectedInvoice = invoice;
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
        Customer customer = Customer.getOneData(customerId);
        custName.getSelectionModel().select(customer.getName());
    }

    private void handleUpdate(){
        if (name.getText().trim().isEmpty() || desc.getText().trim().isEmpty()){
            HelloApplication.showAlert("All field in invoice must be filled!");
        } else {
            selectedInvoice.setInvoiceMarkText(name.getText());
            selectedInvoice.setDescription(desc.getText());
            selectedInvoice.setCustomer_id(selectedCustomer.getId());
            Invoice.updateById(selectedInvoice);
            loadData();
            reset();
        }
    }

    private void handleAdd(){
        String idSurat = "1723596852024";
        String designId = "1725261011387";
        KodeSurat kodeSurat = KodeSurat.getOneData(Long.parseLong(idSurat));
        String year = String.valueOf(Year.now().getValue());
        String yearCode = year.substring(year.length() - 2);
        int urutanInvoice = kodeSurat.getNoUrut()+1;
        String idOr = "1723509861951";
        String bankId = "1727219874871";
        Organization organization = Organization.getOneData(Long.parseLong(idOr));
        int urutanSurat = organization.getTotalLetter() + 1;
        String invoiceCode = urutanSurat + "/SG/INV/" + urutanInvoice + "/" + yearCode;

        KodeSurat.updateById(
                new KodeSurat(
                    Long.parseLong(idSurat), kodeSurat.getKode(),urutanInvoice,kodeSurat.getOrganization_id()
                )
        );
        Organization.updateTotalLetter(
                new Organization(
                    Long.parseLong(idOr), urutanSurat, organization.getBrandName()
                )
        );
        Invoice.addToDB(new Invoice(
                name.getText(), desc.getText(), invoiceCode, "29 Agustus 2024", 0, "", "", Long.parseLong(designId),customerId,1==1, Long.parseLong(bankId))
        );

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
            custName.getItems().add(customer.getName());
            customerMap.put(customer.getName(), customer.getId());
        }
    }

    private void loadBox2(){
        List<KodeSurat> kodeSurats = KodeSurat.getAllData();
        for (KodeSurat kodeSurat : kodeSurats) {
            code.getItems().add(kodeSurat.getKode());
            kodeMap.put(kodeSurat.getKode(), kodeSurat.getId());
        }
    }
}
