package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.KodeSurat;
import com.andre.dojo.Models.Organization;
import com.andre.dojo.Models.Personal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ChangeDataController {
    @FXML
    private Label tombolCustomer;
    @FXML
    private Label tombolInvoice;
    @FXML
    private AnchorPane anchorPaneMain;
    @FXML
    private Pane save;
    @FXML
    private Pane edit;
    @FXML
    private Pane cancel;
    @FXML
    private TextField organizationName;
    @FXML
    private TextField email;
    @FXML
    private TextField address;
    @FXML
    private TextArea desc;
    @FXML
    private TextField personalName;
    @FXML
    private TextField bankID;
    @FXML
    private TextField bankName;
    @FXML
    private TextField bankNumber;
    @FXML
    private Button changeLogo;
    @FXML
    private Button changeSignature;
    @FXML
    private TextField totalLetter;
    @FXML
    private TextField totalInvoice;
    @FXML
    private ImageView logoImage;
    @FXML
    private ImageView signatureImage;

    private ChangeDataCustomerController changeDataCustomerController;
    private ChangeDataInvoiceController changeDataInvoiceController;
    private HelloController helloController;
    private Personal personal;
    private long idPerson;
    private long idOrganization;
    private long idKode;
    private int yearOr;
    private int agencyNum;
    private String idSurat = "1723596852024";
    private Image previousLogoImage, previousSignatureImage, logoFix, signatureFix;
    private static final String PREDEFINED_SAVE_PATH = "D:\\invoiceManagerSource\\logo\\";
    private File selectedLogo, selectedSignature;

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public AnchorPane getAnchorPaneMain() {
        return anchorPaneMain;
    }

    public void setChangeDataCustomerController(ChangeDataCustomerController changeDataInvoiceController){
        this.changeDataCustomerController = changeDataInvoiceController;
    }

    public void setChangeDataInvoiceController(ChangeDataInvoiceController changeDataInvoiceController) {
        this.changeDataInvoiceController = changeDataInvoiceController;
    }

    public void initialize(){
        tombolCustomer.setOnMouseClicked(e -> {
            openCustomerPane();
        });
        tombolInvoice.setOnMouseClicked(e -> {
            openInvoicePane();
        });
        loadData();
        disable();
        edit.setOnMouseClicked(e -> {
            enable();
            previousSignatureImage = signatureImage.getImage();
            previousLogoImage = logoImage.getImage();
            textAccess(false);
        });
        cancel.setOnMouseClicked(e -> {
            disable();
            loadData();
            logoImage.setImage(previousLogoImage);
            signatureImage.setImage(previousSignatureImage);
            textAccess(true);
        });
        save.setOnMouseClicked(e -> {
            disable();
            saveData(idOrganization,idPerson);
            saveImage(getSelectedLogo(), getSelectedSignature());
            textAccess(true);
        });

        changeLogo.setOnAction(event -> changeLogoOrganization());

        changeSignature.setOnAction(event -> changeSignatureOrganization());
    }

    private void openCustomerPane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data-customer.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ChangeDataCustomerController test = fxmlLoader.getController();
            test.setChangedataController(this);

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
            changeDataInvoiceController.setChangeDataController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void loadData(){
        List<Organization> allOrganizations = Organization.getAllData();
        System.out.println(allOrganizations);
        Organization firstOrganization = allOrganizations.get(0);
        totalLetter.setText(String.valueOf(firstOrganization.getTotalLetter()));
        yearOr = firstOrganization.getTahunOperasi();
        organizationName.setText(firstOrganization.getBrandName());
        email.setText(firstOrganization.getEmail());
        address.setText(firstOrganization.getAddress());
        desc.setText(firstOrganization.getDescription());
        agencyNum = firstOrganization.getNoUrutInstansi();
        idOrganization = firstOrganization.getId();
        List<Personal> personalList = Personal.getAllData();
        Personal firstPersonal = personalList.get(0);
        personalName.setText(firstPersonal.getName());
        bankID.setText(firstPersonal.getBankIDName());
        bankName.setText(firstPersonal.getBankName());
        bankNumber.setText(String.valueOf(firstPersonal.getBankIDNumber()));
        idPerson = firstPersonal.getId();
        KodeSurat kodeSurats = KodeSurat.getOneData(Long.parseLong(idSurat));
        totalInvoice.setText(String.valueOf(kodeSurats.getNoUrut()));
        idKode = kodeSurats.getId();
        textAccess(true);
    }

    private void textAccess(boolean con){
        organizationName.setDisable(con);
        email.setDisable(con);
        address.setDisable(con);
        desc.setDisable(con);
        personalName.setDisable(con);
        bankID.setDisable(con);
        bankName.setDisable(con);
        bankNumber.setDisable(con);
        changeLogo.setDisable(con);
        changeSignature.setDisable(con);
        totalInvoice.setDisable(con);
        totalLetter.setDisable(con);
    }

    private void disable(){
        edit.setVisible(true);
        save.setVisible(false);
        cancel.setVisible(false);
    }

    private void enable(){
        save.setVisible(true);
        cancel.setVisible(true);
        edit.setVisible(false);
    }

    private void saveData(long idOrganization, long idPerson){
        Organization.updateById(new Organization(
                idOrganization, "D:\\invoiceManagerSource\\logo\\logo.png",organizationName.getText(),desc.getText(),address.getText(), email.getText(),agencyNum,yearOr,parseInt(totalLetter.getText()))
        ,idOrganization);
        System.out.println("total letter : " + totalLetter.getText());
        Personal.updateById(new Personal(
                personalName.getText(),bankName.getText(), bankNumber.getText(), bankID.getText(),"D:\\invoiceManagerSource\\logo\\signature.png",idOrganization, idPerson)
        );
        KodeSurat.updateById(new KodeSurat(
                idKode,"INV",parseInt(totalInvoice.getText()), idOrganization
        ), Long.parseLong(idSurat));
        System.out.println("total invoice : " + totalInvoice.getText());
    }

    private void changeLogoOrganization() {
        // Create a file chooser
        FileChooser fileChooser = new FileChooser();

        // Set the title for the file chooser
        fileChooser.setTitle("Choose a Logo");

        // Set the extension filters (optional)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show the open dialog
        Stage stage = (Stage) changeLogo.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Get the selected file's path
            String filePath = selectedFile.getAbsolutePath();
            Image image = new Image("file:" + filePath);
            setLogoFix(image);
            logoImage.setImage(image);
            setSelectedLogo(selectedFile);
            // Perform your action with the file path, e.g., update a logo
            System.out.println("Selected file: " + filePath);
        } else {
            System.out.println("No file selected");
        }
    }

    private void changeSignatureOrganization() {
        // Create a file chooser
        FileChooser fileChooser = new FileChooser();

        // Set the title for the file chooser
        fileChooser.setTitle("Choose a Signature");

        // Set the extension filters (optional)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show the open dialog
        Stage stage = (Stage) changeLogo.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Get the selected file's path
            String filePath = selectedFile.getAbsolutePath();
            Image image = new Image("file:" + filePath);
            signatureImage.setImage(image);
            setSelectedSignature(selectedFile);
            setSignatureFix(image);
            // Perform your action with the file path, e.g., update a logo
            System.out.println("Selected file: " + filePath);
        } else {
            System.out.println("No file selected");
        }
    }

    private void saveImage(File logo, File signature){
        String logoName = "logo.png";
        String signatureName = "signature.png";
        if(logo != null){
            saveFileToPredefinedLocation(logo, logoName);
        }
        if (signature != null){
            saveFileToPredefinedLocation(signature, signatureName);
        }
    }

    private void saveFileToPredefinedLocation(File selectedFile, String fileName) {
        // Set the destination file path
        File destinationFile = new File(PREDEFINED_SAVE_PATH + fileName);

        // Copy the file to the predefined location
        try (FileInputStream inputStream = new FileInputStream(selectedFile);
             FileOutputStream outputStream = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            System.out.println("File saved to: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLogoFix(Image logoFix) {
        this.logoFix = logoFix;
    }

    public Image getLogoFix() {
        return logoFix;
    }

    public Image getSignatureFix() {
        return signatureFix;
    }

    public void setSignatureFix(Image signatureFix) {
        this.signatureFix = signatureFix;
    }

    public void setSelectedLogo(File selectedLogo) {
        this.selectedLogo = selectedLogo;
    }

    public void setSelectedSignature(File selectedSignature) {
        this.selectedSignature = selectedSignature;
    }

    public File getSelectedLogo() {
        return selectedLogo;
    }

    public File getSelectedSignature() {
        return selectedSignature;
    }
}
