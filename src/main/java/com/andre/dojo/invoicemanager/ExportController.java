package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.*;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.TextField;
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


import java.awt.*;
import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExportController {
    @FXML
    private AnchorPane mainPage;
    @FXML
    private AnchorPane anchorPaneMain;
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
    private Label prepare;

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
//    private ExportPrepareController exportPrepareController;/''

//    public void setExportPrepareController(ExportPrepareController exportPrepareController) {
//        this.exportPrepareController = exportPrepareController;
//    }

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

    DateTimeFormatter tglBulanTahun = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));

    Map<String, Integer> indonesianToEnglishMonth = new HashMap<>();

    private static final String[] bulanIndo = {
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };
    Pattern monthPattern = Pattern.compile("\\b(" + String.join("|", bulanIndo) + ")\\b");

    public void initialize(){

//        indonesianToEnglishMonth.put("Januari", "JANUARY");
//        indonesianToEnglishMonth.put("Februari", "FEBRUARY");
//        indonesianToEnglishMonth.put("Maret", "MARCH");
//        indonesianToEnglishMonth.put("April", "APRIL");
//        indonesianToEnglishMonth.put("Mei", "MAY");
//        indonesianToEnglishMonth.put("Juni", "JUNE");
//        indonesianToEnglishMonth.put("Juli", "JULY");
//        indonesianToEnglishMonth.put("Agustus", "AUGUST");
//        indonesianToEnglishMonth.put("September", "SEPTEMBER");
//        indonesianToEnglishMonth.put("Oktober", "OCTOBER");
//        indonesianToEnglishMonth.put("November", "NOVEMBER");
//        indonesianToEnglishMonth.put("Desember", "DECEMBER");

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

        setupSpinner();
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
//            System.out.println("Bulan dipilih: " + newValue);
            monthSelected = newValue;
        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", new Locale("id", "ID"));
        monthSelected = LocalDate.now().format(formatter);


        btnExport.setOnMouseClicked(e -> {
            // filter confirm bulan
            // filter folder configured
            // filter folder sudah pernah konfigure
            if (HelloController.showConfirmationDialog("Are you really want to Export this "+dataExport.size()+" invoices on this "+monthSelected+"?")){
//                System.out.println(HelloApplication.dirExport);
                if (Objects.equals(HelloApplication.dirExport, "") || HelloApplication.dirExport == null){
                    HelloApplication.showAlert("Your export folder is not configured yet! Please configure export folder bellow!");
                }else{
                    File checkFolder = new File(HelloApplication.dirExport);
                    if (checkFolder.exists()){
                        // membuat nama folder baru
//                        LocalDate today = LocalDate.now();
//                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMMMyyyy", new Locale("id", "ID"));
                        String folderName = LocalDate.now().getYear()+monthSelected;
                        File folderAsli = new File(checkFolder, folderName);
                        if (folderAsli.exists() && !HelloController.showConfirmationDialog("It seems you have already exported some invoices this month. Are you sure you want to export again?")){

                        }else{
                            // mulai export pakai folder asli
                            // membuat folder
                            if (!folderAsli.exists()) {
                                folderAsli.mkdir();
//                                System.out.println(folderAsli+" telah dibuat");
                            }
                            startExportOneByOne(folderAsli);
                        }
                    }else{
                        HelloApplication.showAlert("Your export folder is not exist, please reconfigure it!");
                    }
                }


            }

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
        if (Objects.equals(HelloApplication.dirExport, "") || HelloApplication.dirExport == null){
//            HelloApplication.showAlert("");
            System.out.println("dir export null");
        }else{
            System.out.println("dir export : "+HelloApplication.dirExport);
            directoryChooser.setInitialDirectory(new File(HelloApplication.dirExport));
        }
        File selectedDirectory = directoryChooser.showDialog(HelloApplication.getMainStage());

        if (selectedDirectory != null) {
//            System.out.println("Folder yang dipilih: " + selectedDirectory.getAbsolutePath());
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
//                    System.out.println("folder dir.export belum diset ke config");
                    fieldFolder.setText("folder dir.export is not configured, please change this..");
                }
            } catch (IOException e) {
//                System.out.println("file config.prop gagal di-load(in)");
            }
        }else{
//            System.out.println("file config.properties tidak ditemukan dari url : "+HelloApplication.filePropDir.getAbsolutePath());
        }

    }

    private void setupSpinner() {
        ObservableList<String> listMoon = FXCollections.observableArrayList();
        for (Month month : Month.values()) {
            listMoon.add(month.getDisplayName(TextStyle.FULL, new Locale("id", "ID")));
        }
        Collections.reverse(listMoon);
        SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(listMoon);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", new Locale("id", "ID"));
        valueFactory.setValue(LocalDate.now().format(formatter));

        spinner.setValueFactory(valueFactory);

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

        int untukIDInvoice = 1;
        if (HelloApplication.dirExport == null || Objects.equals(HelloApplication.dirExport, "")){
            HelloApplication.showAlert("Please choose the export directory first!");
        }else{
            for (Invoice invoice : dataExport) {
                // cek jika data lengkap
                if (Objects.equals(invoice.getJsonData(), "") || invoice.getJrxml_id() == 0 ){
                    HelloApplication.showAlert("There is invoice don`t has complete data, so it is ignored during export. code : "+invoice.getInvoiceCode());
                }else{
                    // generate file baru
                    Invoice invBaru = new Invoice();
                    invBaru.setId(Instant.now().toEpochMilli() + untukIDInvoice);
                    untukIDInvoice++;
                    invBaru.setJrxml_id(invoice.getJrxml_id());
                    invBaru.setDescription(invoice.getDescription());
                    invBaru.setTotalPriceAll(invoice.getTotalPriceAll());
                    invBaru.setInvoiceMarkText(invoice.getInvoiceMarkText());
                    invBaru.setDate(LocalDate.now().withMonth(indonesianToEnglishMonth.get(monthSelected)).format(tglBulanTahun));
                    invBaru.setCustomer_id(invoice.getCustomer_id());
                    invBaru.setTimestamp(Instant.now().toString());
                    invBaru.setBank_id(invoice.getBank_id());

                    String kodeSurat = "";
                    int nomorSurat = 0;

                    String[] kodes = invoice.getInvoiceCode().split("/");
                    for (KodeSurat ks : KodeSurat.getAllData()){
                        if (Objects.equals(ks.getKode(), kodes[2])){
                            ks.setNoUrut(ks.getNoUrut()+1);

                            kodeSurat = ks.getKode();
                            nomorSurat = ks.getNoUrut();
                            // jika ganti tahun maka reset ke 1
                            // mengecek ada berapa invoice dalam setahun ini

                            List<Invoice> invSebelum = Invoice.getAllDataBetweenTime();

                            if (invSebelum.isEmpty()){
                                HelloApplication.organization.setNoUrutInstansi(1);
                                HelloApplication.organization.setTahunOperasi(LocalDate.now().getYear());
//                            System.out.println("Ada antara "+LocalDate.now().getYear());
                            }else{
//                            System.out.println("tidak ada antara");
                                HelloApplication.organization.setNoUrutInstansi(HelloApplication.organization.getNoUrutInstansi()+1);
                            }
                            Organization.updateById(HelloApplication.organization);
                            KodeSurat.updateNumber(ks);


                            break;
                        }
                    }
                    invBaru.setInvoiceCode(
                            HelloApplication.organization.getNoUrutInstansi()+
                                    "/"+HelloApplication.organization.getKodeInstansi()+
                                    "/"+kodeSurat+
                                    "/"+nomorSurat+
                                    "/"+LocalDate.now().getYear() % 100
                    );
//                System.out.println(invBaru.getInvoiceCode());

                    // create item
                    List<Item> listItem = Item.getListByInvoiceID(invoice.getId());

                    int i = 1;
                    for (Item e : listItem){
                        e.setInvoice_id(invBaru.getId());
                        e.setId(Instant.now().toEpochMilli() + i);
                        if (e.isFillNameWithMonth()){
                            Matcher matcher = monthPattern.matcher(e.getName());
                            e.setName(matcher.replaceAll(monthSelected));
//                        System.out.println("Name : "+e.getName());
                        }
                        i++;
                    }
//                if (!listItem.isEmpty()){
//
//                    System.out.println("item first : "+listItem.getFirst().getName());
//                }else{
//                    System.out.println("kosong");
//                }
                    invBaru.setListItems(listItem);

                    // save invoice
                    loadJsonData(invBaru);

//                invBaru.setPdfUrl("Datenya : "+invBaru.getDate());

//                System.out.println(invBaru.getPdfUrl());
//                System.out.println("========================================");
//                System.out.println(invBaru.getJsonData());
                    Invoice.addToDB(invBaru);


                    // update data invoice
                    // update data kode_surat
                    // update data noUrutInstansi di organization
                    // update data item

//                System.out.println("Invoice ID: " + invoice.getId());
//                System.out.println("with jxrml ID : " + invoice.getJrxml_id());

                    JasperReport jasperReport = null;
                    try {
                        jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(Design.getOneData(invBaru.getJrxml_id()).getJrxml().getBytes()));
                    } catch (JRException e) {
                        HelloApplication.showAlert("There is some error : "+e.getMessage());
                        return;
                    }
                    JsonDataSource dataSource = null;
                    try {
                        dataSource = new JsonDataSource(new ByteArrayInputStream(invBaru.getJsonData().getBytes()));
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
                    String[] splitTime = invBaru.getDate().split(" ");
                    String fileName = invBaru.getDescription()+"_"+splitTime[1]+"_"+splitTime[2]+".pdf";
                    String pdfFullPath = folderAsli + File.separator + fileName;

                    JrxmlController.exportToPdf(jasperPrint, pdfFullPath);

                }
            }
        }
        dataExport.clear();
        total.setText(String.valueOf(dataExport.size()));
        loadTableView();

        // tampilkan pesan
        // ... inside your method or event handler
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Process Complete");
        alert.setHeaderText(null);
        alert.setContentText("Export process has finished successfully.");

        ButtonType cancelButton = new ButtonType("Cancel");
        ButtonType openButton = new ButtonType("Open Folder");

        alert.getButtonTypes().setAll(cancelButton, openButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == openButton) {
            try {
                Desktop.getDesktop().open(folderAsli);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error alert)
            }
        }
    }

//    private void openPreparePane() {
//        try{
//            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("export-prepare-view.fxml"));
//            Pane anchorPane = new Pane((Node) fxmlLoader.load());
//
//            ExportPrepareController test = fxmlLoader.getController();
//            test.setExportController(this);
//
//            anchorPaneMain.getChildren().removeFirst();
//            anchorPaneMain.getChildren().add(anchorPane);
//        }catch (IOException e1){
//            e1.printStackTrace();
//        }
//    }

    public void loadJsonData(Invoice invoice) {
        invoice.setTotalPriceAll(Item.getSumOfPriceByInvoiceId(invoice.getId()));

        HelloController.customJSON.setOrganization(HelloApplication.organization);
        HelloController.customJSON.setCustomer(Customer.getOneData(invoice.getCustomer_id()));
        HelloController.customJSON.setInvoice(invoice);
        HelloController.customJSON.setBank(Bank.getOneData(invoice.getBank_id()));
        String json = HelloApplication.gson.toJson(HelloController.customJSON, CustomJSON.class);

        invoice.setJsonData(json);
        Invoice.updateById(invoice);
    }


    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public AnchorPane getMainPage() {
        return mainPage;
    }
}
