package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Design;
import com.andre.dojo.Models.Invoice;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class ExportController {
    @FXML
    private AnchorPane mainPage;
    @FXML
    private TableView<Invoice> tableViewExport;
    @FXML
    private TableColumn<Invoice, String> tableColumnCode;
    @FXML
    private TableColumn<Invoice, String> tableColumnDesc;
    @FXML
    private TableColumn<Invoice, String> tableColumnCustomer;
    @FXML
    private TableColumn<Invoice, String> tableColumnTotalItem;
    @FXML
    private Pane btnExport;
    @FXML
    private Label total;

    @FXML
    private Spinner<String> spinner ;
    @FXML
    private TextField fieldFolder;
    @FXML
    private Label btnChangeFolder;
    @FXML
    private Label btnPrepareExport;

    public List<Invoice> dataExport = new ArrayList<>();
    public String message;

    public void setDataExport(List<Invoice> dataExport) {
        this.dataExport = dataExport;
    }

    public List<Invoice> getDataExport() {
        return dataExport;
    }

    private String monthSelected;

    private HelloController helloController;
    Properties properties = new Properties();
    File fileProp = new File(HelloApplication.filePropDir, "config.properties");
    private String folderWithSubFolder ;

    public void initialize(){
        setupSpinner();
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            System.out.println("Bulan dipilih: " + newValue);
            monthSelected = newValue;
        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", new Locale("id", "ID"));
        monthSelected = LocalDate.now().format(formatter);


        btnExport.setOnMouseClicked(e -> {
            // filter confirm bulan
            // filter folder configured
            // filter folder sudah pernah konfigure
            if (HelloController.showConfirmationDialog("Are you really want to Export this "+dataExport.size()+" invoices on this "+monthSelected+"?")){
                if (!Objects.equals(HelloApplication.dirExport, "")){
                    File checkFolder = new File(HelloApplication.dirExport);
                    if (checkFolder.exists()){
                        // membuat nama folder baru
                        LocalDate today = LocalDate.now();
                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMMMyyyy", new Locale("id", "ID"));
                        String folderName = today.format(formatter2);
                        File folderAsli = new File(checkFolder, folderName);
                        if (folderAsli.exists() && !HelloController.showConfirmationDialog("It seems like you have already exported some invoices this month. Are you sure you want to export again?")){

                        }else{
                            // mulai export pakai folder asli
                            // membuat folder
                            if (!folderAsli.exists()) {
                                folderAsli.mkdir();
                            }
                            startExportOneByOne(folderAsli);
                        }
                    }else{
                        HelloApplication.showAlert("Your export folder is not exist, please reconfigure it!");
                    }
                }else{
                    HelloApplication.showAlert("Your export folder is not configured yet! Please configure export folder bellow!");
                }
            }

//            try {
//                toPDF();
//            } catch (JRException ex) {
//                throw new RuntimeException(ex);
//            }
        });
        btnPrepareExport.setOnMouseClicked(e -> {
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getPrepareExportController().getMainPage());
        });
        showFolderOnTheField();
        btnChangeFolder.setOnMouseClicked(e -> {
            selectFolder();
        });
    }



    private void selectFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        File selectedDirectory = directoryChooser.showDialog(HelloApplication.getMainStage());

        if (selectedDirectory != null) {
            System.out.println("Folder yang dipilih: " + selectedDirectory.getAbsolutePath());
            HelloApplication.dirExport = selectedDirectory.getAbsolutePath();
            fieldFolder.setText(selectedDirectory.getAbsolutePath());
            // simpan ke config.properties
            properties.setProperty("dir.export", selectedDirectory.getAbsolutePath());
            try (FileOutputStream out = new FileOutputStream(fileProp)) {
                properties.store(out, "Invoice Generator Configuration");
            } catch (IOException e) {
                Platform.runLater(() -> {
                    HelloApplication.showAlert("Gagal menyimpan properties ke file!");
                });
                e.printStackTrace();
            }
        }else{
        }
    }

    private void showFolderOnTheField() {


        if (fileProp.exists()) {
            try (FileInputStream in = new FileInputStream(fileProp)) {
                properties.load(in);
                if (properties.containsKey("dir.export")){
                    fieldFolder.setText(properties.getProperty("dir.export"));
                    HelloApplication.dirExport = properties.getProperty("dir.export");
                }else{
                    System.out.println("folder dir.export belum diset ke config");
                    fieldFolder.setText("folder dir.export is not configured, please change this..");
                }
            } catch (IOException e) {
                System.out.println("file config.prop gagal di-load(in)");
            }
        }else{
            System.out.println("file config.properties tidak ditemukan dari url : "+HelloApplication.filePropDir.getAbsolutePath());
        }

    }

    private void setupSpinner() {
        ObservableList<String> listMoon = FXCollections.observableArrayList();
        for (Month month : Month.values()) {
            listMoon.add(month.getDisplayName(TextStyle.FULL, new Locale("id", "ID")));
        }
        Collections.reverse(listMoon);
        SpinnerValueFactory<String> val = new SpinnerValueFactory.ListSpinnerValueFactory<>(listMoon);

        spinner.setValueFactory(val);

    }

    public void customInitialize(){
        loadTableView();
        total.setText(String.valueOf(dataExport.size()));

//        insertDesign();
    }

    public void loadTableView() {
        tableViewExport.setEditable(true);
        tableViewExport.setItems(FXCollections.observableArrayList(dataExport));
        tableColumnCode.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceCode()));
        tableColumnDesc.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnCustomer.setCellValueFactory(e -> new SimpleStringProperty(Customer.getOneData(e.getValue().getCustomer_id()).getName()));
        tableColumnTotalItem.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceMarkText()));
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private void startExportOneByOne(File folderAsli){
//        String cek = "1724503671986", cek2 = "1724656789071";

        for (Invoice invoice : dataExport) {
            // cek jika data lengkap
            if (Objects.equals(invoice.getJsonData(), "") || invoice.getJrxml_id() == 0){
                HelloApplication.showAlert("There is invoice don`t has complete data, so it is ignored during export. code : "+invoice.getInvoiceCode());
            }else{

                System.out.println("Invoice ID: " + invoice.getId());
                System.out.println("with jxrml ID : " + invoice.getJrxml_id());

                JasperReport jasperReport = null;
                try {
                    jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(Design.getOneData(invoice.getJrxml_id()).getJrxml().getBytes()));
                } catch (JRException e) {
                    HelloApplication.showAlert("There is some error : "+e.getMessage());
                    return;
                }
                JsonDataSource dataSource = null;
                try {
                    dataSource = new JsonDataSource(new ByteArrayInputStream(invoice.getJsonData().getBytes()));
                } catch (JRException e) {
                    HelloApplication.showAlert("There is some error : "+e.getMessage());
                    return;
                }
                JasperPrint jasperPrint = null;
                try {
                    jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
                } catch (JRException e) {
                    HelloApplication.showAlert("There is some error : "+e.getMessage());
                    return;
                }

                // setting nama file
                String fileName = "report_" + invoice.getId()+".pdf";
                String pdfFullPath = folderAsli + File.separator + fileName;

                JrxmlController.exportToPdf(jasperPrint, pdfFullPath);

            }
        }
        dataExport.clear();
        total.setText(String.valueOf(dataExport.size()));
        loadTableView();
    }

    private void insertDesign(){
        Design.addToDB(new Design(

        ));
    }

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public AnchorPane getMainPage() {
        return mainPage;
    }
}
