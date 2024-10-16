package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    private Label labelLetter;
    @FXML
    private Label labelCustomer;
    @FXML
    private Label labelMarkText;
    @FXML
    private Label labelDescription;
    @FXML
    private Label labelSelectedCustomerName;

    @FXML
    private TableView<Customer> tableViewCustomer;
    @FXML
    private TableColumn<Customer, String> tableColumnCsName;
    @FXML
    private TableColumn<Customer, String> tableColumnCsDesc;

    @FXML
    private Label tombolCustomer;
    @FXML
    private Label tombolLetter;
    @FXML
    private Label tombolCode;

    @FXML
    private Pane btnSelectCustomer;
    @FXML
    private ChoiceBox<KodeSurat> code;
    @FXML
    private ChoiceBox<Bank> bankSelector;
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
    private ObservableList<KodeSurat> kodeSuratList = FXCollections.observableArrayList();
    private ObservableList<Bank> bankList = FXCollections.observableArrayList();
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    private Long customerId;
    private Long codeLetterId;
    private String invCode;
    private long invId;
    private Invoice selectedInvoice;
    private Customer selectedCustomer;
    private KodeSurat selectedCodeSurat;
    private Bank selectedBank;
    Map<String, Integer> indonesianToEnglishMonth = new HashMap<>();
    DateTimeFormatter tglBulanTahun = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));
    DateTimeFormatter formatterBulan = DateTimeFormatter.ofPattern("MMMM", new Locale("id", "ID"));
    String monthSelected = LocalDate.now().format(formatterBulan);


    public void initialize(){


        kodeSuratList.addAll(KodeSurat.getAllData());
        code.setItems(kodeSuratList);
        code.setConverter(new StringConverter<KodeSurat>() {
            @Override
            public String toString(KodeSurat kodeSurat) {
                if (kodeSurat == null){
                    return null;
                }else{
                    return kodeSurat.getKode();
                }
            }
            @Override
            public KodeSurat fromString(String string) {
                return null; // Tidak digunakan untuk ChoiceBox
            }
        });
        bankList.addAll(Bank.getAllData());
        bankSelector.setItems(bankList);
        bankSelector.setConverter(new StringConverter<Bank>() {
            @Override
            public String toString(Bank bank) {
                if (bank == null){
                    return null;
                }else{
                    return bank.getBank_name()+" | "+bank.getAccount_number()+" | "+bank.getOwner();
                }
            }
            @Override
            public Bank fromString(String string) {
                return null; // Tidak digunakan untuk ChoiceBox
            }
        });
        indonesianToEnglishMonth.put("Januari", 1);
        indonesianToEnglishMonth.put("Februari", 2);
        indonesianToEnglishMonth.put("Maret", 3);
        indonesianToEnglishMonth.put("April", 4);
        indonesianToEnglishMonth.put("Mei", 5);
        indonesianToEnglishMonth.put("Juni", 6);
        indonesianToEnglishMonth.put("Juli", 7);
        indonesianToEnglishMonth.put("Agustus", 8);
        indonesianToEnglishMonth.put("September", 9);
        indonesianToEnglishMonth.put("Oktober", 10);
        indonesianToEnglishMonth.put("November", 11);
        indonesianToEnglishMonth.put("Desember", 12);

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

        code.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("isi code "+oldValue+" "+newValue);
            handleSelectionCode(newValue);
        });
        bankSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleSelectionBank(newValue);
//            System.out.println("isi bank "+selectedBank.getBank_name());
        });
        loadData();
        conButton(true);

        btnSelectCustomer.setOnMouseClicked(e -> {
            setLabelsVisible(false);
        });
        tableViewCustomer.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Cek apakah ini single-click
                Customer selectedCustomer = tableViewCustomer.getSelectionModel().getSelectedItem();
                if (selectedCustomer != null) {
                    handleSelection(selectedCustomer);
                }
            }
        });

        loadTableCustomer();
        tableViewInvoice.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetail(newValue));

        setLabelsVisible(true);

    }

    private void loadTableCustomer() {
//        tableViewInvoice.setEditable(true);
        List<Customer> customerList = Customer.getAllData();
        List<Customer> sortedList = customerList.stream()
                .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
                .toList();

        tableViewCustomer.setItems(FXCollections.observableArrayList(sortedList));
        tableColumnCsName.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getName()));
        tableColumnCsDesc.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
    }

    private void setLabelsVisible(boolean bool) {
        labelCustomer.setVisible(bool);
        labelMarkText.setVisible(bool);
        labelDescription.setVisible(bool);
        labelSelectedCustomerName.setVisible(bool);
        btnSelectCustomer.setVisible(bool);
        name.setVisible(bool);
        desc.setVisible(bool);
        // menampilkan tabel
        tableViewCustomer.setVisible(!bool);
    }


    private void handleSelection(Customer cst) {
        if (cst != null){
            selectedCustomer = cst;
            customerId = cst.getId();
            setLabelsVisible(true);
            labelSelectedCustomerName.setText(selectedCustomer.getName()+" | "+selectedCustomer.getDescription());
        }
    }
    private void handleSelectionBank(Bank newValue) {
        if(newValue != null) {
            selectedBank = newValue;
//            System.out.println("berhasil ambil bank : " + selectedBank.getBank_name());
        }else{
//            System.out.println("gagal ambil bank");
        }
    }

    private void handleSelectionCode(KodeSurat kodeSurat) {
        if(kodeSurat != null) {
            codeLetterId = kodeSurat.getId();
            selectedCodeSurat = kodeSurat;
//            System.out.println("berhasil ambil code : " + codeLetterId);
        }else{
//            System.out.println("gagal ambil kode surat");
        }
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



    private void showDetail(Invoice invoice){
        selectedInvoice = invoice;
        if (invoice != null) {
            bankSelector.getSelectionModel().clearSelection();
            name.setText(invoice.getInvoiceMarkText());
            desc.setText(invoice.getDescription());
            invCode = invoice.getInvoiceCode();
            invId = invoice.getId();
            selectedCustomer = Customer.getOneData(invoice.getCustomer_id());
            selectedBank = Bank.getOneData(invoice.getBank_id());
//            custName.getSelectionModel().select(selectedCustomer);
            bankSelector.getSelectionModel().select(selectedBank);
            conButton(false);
            code.setVisible(false);
            labelLetter.setVisible(false);
        } else {
            name.setText("");
            desc.setText("");
        }
    }



    private void handleUpdate(){
        if (Objects.equals(desc.getText(), "") ||
                Objects.equals(name.getText(), "") ||
                selectedBank == null || selectedCustomer == null
        ){
            HelloApplication.showAlert("All field in invoice must be filled!");
        } else {
            selectedInvoice.setInvoiceMarkText(name.getText());
            selectedInvoice.setDescription(desc.getText());
            selectedInvoice.setCustomer_id(selectedCustomer.getId());
//            System.out.println("bank select : "+ selectedBank.getBank_name());
            selectedInvoice.setBank_id(selectedBank.getId());
//            System.out.println("bank hasil : "+Bank.getOneData(selectedInvoice.getBank_id()).getBank_name());
            Invoice.updateById(selectedInvoice);
            loadData();
            reset();
        }

    }

    private void handleAdd(){
        if (Objects.equals(desc.getText(), "") || Objects.equals(name.getText(), "") || selectedBank == null || selectedCustomer == null || selectedCodeSurat == null){
            HelloController.showInformationDialog("Maaf silakan lengkapi data terlebih dahulu");
        }else{
//            Mengurus kode surat


//            KodeSurat kodeSurat = KodeSurat.getOneData(Long.parseLong(idSurat));
            String year = String.valueOf(Year.now().getValue());
            String yearCode = year.substring(year.length() - 2);
            int urutanInvoice = selectedCodeSurat.getNoUrut()+1;
            String idOr = "1723509861951";
//            String bankId = "1727219874871";
            Organization organization = HelloApplication.organization;
            int urutanSurat = organization.getNoUrutInstansi() + 1;
            String invoiceCode = urutanSurat + "/"+organization.getKodeInstansi()+"/"+selectedCodeSurat.getKode()+"/" + urutanInvoice + "/" + yearCode;
//            System.out.println(invoiceCode);

            selectedCodeSurat.setNoUrut(urutanInvoice);
            organization.setNoUrutInstansi(urutanSurat);
            KodeSurat.updateById(selectedCodeSurat);
            Organization.updateById(organization);
            Invoice.addToDB(new Invoice(
                    name.getText(),
                    desc.getText(),
                    invoiceCode,
                    LocalDate.now().withMonth(indonesianToEnglishMonth.get(monthSelected)).format(tglBulanTahun),
                    0,
                    "",
                    "",
                    0L,
                    selectedCustomer.getId(),
                    true,
                    selectedBank.getId())
            );

            reset();
            loadData();

        }
    }

    private void conButton(boolean con){
        add.setVisible(con);
        update.setVisible(!con);
    }

    private void reset(){
        name.setText("");
        desc.setText("");
//        custName.getSelectionModel().clearSelection();
        code.getSelectionModel().clearSelection();
        bankSelector.getSelectionModel().clearSelection();
        conButton(true);
        customerId = null;
        codeLetterId = null;
        selectedCustomer = null;
        selectedCodeSurat = null;
        selectedInvoice = null;
        code.setVisible(true);
        labelLetter.setVisible(true);
        labelSelectedCustomerName.setText("Selected customer Name Null");
    }

//    private void loadBox(){
//        List<Customer> customers = Customer.getAllData();
//        for (Customer customer : customers) {
//            custName.getItems().add(customer.getName());
//            customerMap.put(customer.getName(), customer.getId());
//        }
//    }

//    private void loadBox2(){
//        List<KodeSurat> kodeSurats = KodeSurat.getAllData();
//        for (KodeSurat kodeSurat : kodeSurats) {
//            code.getItems().add(kodeSurat.getKode());
//            kodeMap.put(kodeSurat.getKode(), kodeSurat.getId());
//        }
//    }
}
