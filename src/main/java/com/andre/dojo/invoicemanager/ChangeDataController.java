package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Invoice;
import com.andre.dojo.Models.KodeSurat;
import com.andre.dojo.Models.Organization;
import com.andre.dojo.Models.Personal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.lang.Integer.parseInt;

public class ChangeDataController {
    @FXML
    private Label tombolCustomer;
    @FXML
    private Label tombolInvoice;
    @FXML
    private Label tombolCode;
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
    private Button changeLogo;
    @FXML
    private Button changeSignature;
    @FXML
    private ImageView logoImage;
    @FXML
    private ImageView signatureImage;

    private ChangeDataCustomerController changeDataCustomerController;
    private ChangeDataInvoiceController changeDataInvoiceController;
    private ChangeDataCodeController changeDataCodeController;
    private HelloController helloController;
    private long idPerson;
    private long idOrganization;
    private long idKode;
    private int yearOr;
    private int agencyNum;
    private String idSurat = "1723596852024";
    private Image previousLogoImage, previousSignatureImage, logoFix, signatureFix;
    private static final String PREDEFINED_SAVE_PATH = "D:\\invoiceManagerSource\\logo\\";
    private File selectedLogo, selectedSignature;
    private Organization selectedOrganization;

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

    public void setChangeDataCodeController(ChangeDataCodeController changeDataCodeController) {
        this.changeDataCodeController = changeDataCodeController;
    }

    public void initialize(){
        tombolCustomer.setOnMouseClicked(e -> {
            openCustomerPane();
        });
        tombolInvoice.setOnMouseClicked(e -> {
            openInvoicePane();
        });
        tombolCode.setOnMouseClicked(e -> {
            openCodePane();
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
            saveData();
            saveImage(getSelectedLogo(), getSelectedSignature());
            textAccess(true);
            loadData();
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

    private void openCodePane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data-code.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ChangeDataCodeController changeDataCodeController = fxmlLoader.getController();
            changeDataCodeController.setChangeDataController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void loadData(){
        List<Organization> allOrganizations = Organization.getAllData();
        System.out.println(allOrganizations);
        selectedOrganization = allOrganizations.get(0);
        yearOr = selectedOrganization.getTahunOperasi();
        organizationName.setText(selectedOrganization.getBrandName());
        email.setText(selectedOrganization.getEmail());
        address.setText(selectedOrganization.getAddress());
        desc.setText(selectedOrganization.getDescription());
        personalName.setText(selectedOrganization.getSignatureName());
        textAccess(true);
    }

    private void textAccess(boolean con){
        organizationName.setDisable(con);
        email.setDisable(con);
        address.setDisable(con);
        desc.setDisable(con);
        personalName.setDisable(con);
        changeLogo.setDisable(con);
        changeSignature.setDisable(con);
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

    private void saveData(){
        selectedOrganization.setSignatureName(personalName.getText());
        selectedOrganization.setBrandName(organizationName.getText());
        selectedOrganization.setEmail(email.getText());
        selectedOrganization.setAddress(address.getText());
        selectedOrganization.setDescription(desc.getText());
        Organization.updateById(selectedOrganization);
    }

    private void changeLogoOrganization() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Logo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) changeLogo.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            Image image = new Image("file:" + filePath);
            setLogoFix(image);
            logoImage.setImage(image);
            setSelectedLogo(selectedFile);
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
