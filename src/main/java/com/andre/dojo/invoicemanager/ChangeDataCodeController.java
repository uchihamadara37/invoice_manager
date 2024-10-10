package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Bank;
import com.andre.dojo.Models.KodeSurat;
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

public class ChangeDataCodeController {
    @FXML
    private AnchorPane anchorPaneMain;

    @FXML
    private Label tombolInvoice;
    @FXML
    private Label tombolCustomer;
    @FXML
    private Label tombolLetter;

    @FXML
    private TableView<Bank> tableViewBank;
    @FXML
    private TableColumn<Bank, String> tableBankName;
    @FXML
    private TableColumn<Bank, String> tableOwner;
    @FXML
    private TableColumn<Bank, String> tableBankId;
    @FXML
    private TableColumn<Bank, Void> tableBankDelete;

    @FXML
    private TableView<KodeSurat> tableViewCode;
    @FXML
    private TableColumn<KodeSurat, String> tableCodeName;
    @FXML
    private TableColumn<KodeSurat, String> tableSerialNumber;
    @FXML
    private TableColumn<KodeSurat, Void> tableDeleteCode;

    @FXML
    private TextField BankId;
    @FXML
    private TextField BankName;
    @FXML
    private TextField Owner;
    @FXML
    private TextField Rekening;

    @FXML
    private TextField KodeName;
    @FXML
    private TextField SerialNumber;

    @FXML
    private Pane addBank;
    @FXML
    private Pane updateBank;
    @FXML
    private Pane resetBank;

    @FXML
    private Pane addCode;
    @FXML
    private Pane updateCode;
    @FXML
    private Pane resetCode;


    private ChangeDataCustomerController changeDataCustomerController;
    private ChangeDataController changeDataController;
    private ChangeDataInvoiceController changeDataInvoiceController;
    private Bank selectedBank;
    private KodeSurat selectedKodeSurat;


    public void initialize(){
        loadDataKode();
        loadDataBank();

        tombolInvoice.setOnMouseClicked(e -> {
            openInvoicePane();
        });
        tombolCustomer.setOnMouseClicked(e -> {
            openCustomerPane();
        });
        tombolLetter.setOnMouseClicked(e -> {
            openLetterPane();
        });
        addBank.setOnMouseClicked(event -> {
            handleAddBank();
        });
        resetBank.setOnMouseClicked(event -> {
            conButtonBank(true);
            resetBank();
        });
        updateBank.setOnMouseClicked(event -> {
            handleUpdateBank();
        });
        addCode.setOnMouseClicked(event -> {
            handleAddKode();
        });
        resetCode.setOnMouseClicked(event -> {
            conButtonKode(true);
            resetKode();
        });
        updateCode.setOnMouseClicked(event -> {
            handleUpdateKode();
        });

        tableViewBank.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetailBank(newValue));

        tableViewCode.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetailCode(newValue));
    }

    public void setChangeDataController(ChangeDataController changeDataController) {
        this.changeDataController = changeDataController;
    }

    public void setChangeDataInvoiceController(ChangeDataInvoiceController changeDataInvoiceController) {
        this.changeDataInvoiceController = changeDataInvoiceController;
    }

    public void setChangeDataCustomerController(ChangeDataCustomerController changeDataCustomerController) {
        this.changeDataCustomerController = changeDataCustomerController;
    }

    private void openInvoicePane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data-invoice.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ChangeDataInvoiceController changeDataInvoiceController = fxmlLoader.getController();
            changeDataInvoiceController.setChangeDataCodeController(this);

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
            changeDataCustomerController.setChangeDataCodeController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void openLetterPane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ChangeDataController changeDataController = fxmlLoader.getController();
            changeDataController.setChangeDataCodeController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void loadDataKode(){
        conButtonKode(true);
        tableViewCode.setEditable(true);
        tableViewCode.setItems(FXCollections.observableArrayList(KodeSurat.getAllData()));
        tableCodeName.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getKode()));
        tableSerialNumber.setCellValueFactory(e -> new SimpleStringProperty(String.valueOf(e.getValue().getNoUrut())));
        tableDeleteCode.setCellFactory(e -> new TableCell<KodeSurat, Void>() {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("icon/circle-minus.png")));
            private final HBox container = new HBox();
            {
                icon.setFitHeight(20);
                icon.setFitWidth(20);

                container.setAlignment(Pos.CENTER_LEFT);
                container.getChildren().add(icon);
                container.setPadding(new Insets(0,0,0,20));

                container.setOnMouseClicked(event -> {
                    KodeSurat kodeSurat = getTableView().getItems().get(getIndex());
                    handleDeleteKode(kodeSurat);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    private void showDetailCode(KodeSurat kodeSurat){
        selectedKodeSurat = kodeSurat;
        if (kodeSurat != null) {
            KodeName.setText(kodeSurat.getKode());
            SerialNumber.setText(String.valueOf(kodeSurat.getNoUrut()));
            conButtonKode(false);
        } else {
            KodeName.setText("");
            SerialNumber.setText("");
        }
    }

    private void handleDeleteKode(KodeSurat kodeSurat){
        KodeSurat.deleteOneById(kodeSurat.getId());
        loadDataKode();
    }

    private void handleAddKode(){
        if(KodeName.getText().trim().isEmpty() || SerialNumber.getText().trim().isEmpty()){
            HelloApplication.showAlert("All field in code must be filled!");
        } else {
            String idOr = "1723509861951";
            KodeSurat.addToDB(new KodeSurat(KodeName.getText(), Integer.parseInt(SerialNumber.getText()), Long.parseLong(idOr)));
            loadDataKode();
            resetKode();
        }
    }

    private void handleUpdateKode(){
        if(KodeName.getText().trim().isEmpty() || SerialNumber.getText().trim().isEmpty()){
            HelloApplication.showAlert("All field in code must be filled!");
        } else {
            selectedKodeSurat.setKode(KodeName.getText());
            selectedKodeSurat.setNoUrut(Integer.parseInt(SerialNumber.getText()));
            KodeSurat.updateById(selectedKodeSurat);
            resetKode();
            loadDataKode();
        }
    }

    private void conButtonKode(boolean con){
        addCode.setVisible(con);
        updateCode.setVisible(!con);
    }

    private void resetKode(){
        KodeName.setText("");
        SerialNumber.setText("");
    }

    private void loadDataBank(){
        conButtonBank(true);
        tableViewBank.setEditable(true);
        tableViewBank.setItems(FXCollections.observableArrayList(Bank.getAllData()));
        tableBankName.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getBank_name()));
        tableBankId.setCellValueFactory(e -> new SimpleStringProperty(String.valueOf(e.getValue().getBank_id())));
        tableOwner.setCellValueFactory(e -> new SimpleStringProperty(String.valueOf(e.getValue().getOwner())));
        tableBankDelete.setCellFactory(e -> new TableCell<Bank, Void>() {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("icon/circle-minus.png")));
            private final HBox container = new HBox();
            {
                icon.setFitHeight(20);
                icon.setFitWidth(20);

                container.setAlignment(Pos.CENTER_LEFT);
                container.getChildren().add(icon);
                container.setPadding(new Insets(0,0,0,20));

                container.setOnMouseClicked(event -> {
                    Bank bank = getTableView().getItems().get(getIndex());
                    handleDeleteBank(bank);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    private void handleDeleteBank(Bank bank){
        Bank.deleteOneById(bank.getId());
        loadDataBank();
        resetBank();
    }

    private void handleAddBank(){
        if(BankId.getText().trim().isEmpty() || BankName.getText().trim().isEmpty() || Owner.getText().trim().isEmpty() || Rekening.getText().trim().isEmpty()){
            HelloApplication.showAlert("All field in bank must be filled!");
        } else {
            Bank.addToDB(new Bank(BankId.getText(), BankName.getText(), Owner.getText(), Rekening.getText()));
            loadDataBank();
            resetBank();
        }
    }

    private void showDetailBank(Bank bank){
        selectedBank = bank;
        if (bank != null) {
            BankName.setText(bank.getBank_name());
            BankId.setText(bank.getBank_id());
            Owner.setText(bank.getOwner());
            Rekening.setText(bank.getAccount_number());
            conButtonBank(false);
        } else {
            BankName.setText("");
            BankId.setText("");
            Owner.setText("");
            Rekening.setText("");
        }
    }

    private void conButtonBank(boolean con){
        addBank.setVisible(con);
        updateBank.setVisible(!con);
    }

    private void resetBank(){
        BankId.setText("");
        BankName.setText("");
        Owner.setText("");
        Rekening.setText("");
    }

    private void handleUpdateBank(){
        if(BankId.getText().trim().isEmpty() || BankName.getText().trim().isEmpty() || Owner.getText().trim().isEmpty() || Rekening.getText().trim().isEmpty()){
            HelloApplication.showAlert("All field in bank must be filled!");
        } else {
            selectedBank.setBank_name(BankName.getText());
            selectedBank.setBank_id(BankId.getText());
            selectedBank.setOwner(Owner.getText());
            selectedBank.setAccount_number(Rekening.getText());
            Bank.updateById(selectedBank);
            resetBank();
            loadDataBank();
        }
    }
}
