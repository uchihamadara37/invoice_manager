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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private TextField orgLetter;
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
//    private String idSurat = "1723596852024";
    private Image previousLogoImage, previousSignatureImage, logoFix, signatureFix;
//    private static final String PREDEFINED_SAVE_PATH = "D:\\invoiceManagerSource\\logo\\";
    private File selectedLogo, selectedSignature;
    private Organization selectedOrganization;
    private Personal selectedPersonal;

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
        selectedOrganization = Organization.getFirstData();
        selectedPersonal = Personal.getFirstData();

        if (selectedOrganization.getLogo() != null){
            if (new File(selectedOrganization.getLogo()).exists()){
                Image image = new Image("file:" + selectedOrganization.getLogo());
                setLogoFix(image);
                logoImage.setImage(image);
                selectedLogo = new File(selectedOrganization.getLogo());
            }
        }
        if (selectedPersonal.getUrlTtd() != null){
            if (new File(selectedPersonal.getUrlTtd()).exists()){
                Image image = new Image("file:" + selectedPersonal.getUrlTtd());
                setSignatureFix(image);
                signatureImage.setImage(image);
                selectedSignature = new File(selectedPersonal.getUrlTtd());
            }
        }


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
//            disable();

            saveData();


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

        selectedOrganization = HelloApplication.organization;
        yearOr = selectedOrganization.getTahunOperasi();
        organizationName.setText(selectedOrganization.getBrandName());
        email.setText(selectedOrganization.getEmail());
        address.setText(selectedOrganization.getAddress());
        desc.setText(selectedOrganization.getDescription());
        personalName.setText(selectedPersonal.getName());
        orgLetter.setText(selectedOrganization.getKodeInstansi());
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
        orgLetter.setDisable(con);
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
        if (Objects.equals(orgLetter.getText(), "" )|| selectedLogo == null || selectedSignature == null){
            HelloController.showInformationDialog("Please fill out the Code Organization and Image File!");
        }else{
            saveImage(selectedLogo, selectedSignature);
            selectedOrganization.setSignatureName(personalName.getText());
            selectedPersonal.setName(personalName.getText());

            selectedOrganization.setBrandName(organizationName.getText());
            selectedOrganization.setEmail(email.getText());
            selectedOrganization.setAddress(address.getText());
            selectedOrganization.setDescription(desc.getText());
            selectedOrganization.setKodeInstansi(orgLetter.getText());
            Organization.updateById(selectedOrganization);
            Personal.updateById(selectedPersonal);
            textAccess(true);
            loadData();
            disable();
        }
    }

    private void changeLogoOrganization() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Logo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) changeLogo.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            Image image = new Image("file:" + filePath);
            setLogoFix(image);
            logoImage.setImage(image);
            selectedLogo = new File(filePath);
        } else {
        }
    }

    private void changeSignatureOrganization() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Signature");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        // Show the open dialog
        Stage stage = (Stage) changeLogo.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Get the selected file's path
            String filePath = selectedFile.getAbsolutePath();
            Image image = new Image("file:" + filePath);
            signatureImage.setImage(image);
            setSignatureFix(image);
            selectedSignature = new File(filePath);
        } else {
        }
    }

    private void saveImage(File logo, File signature) {
        if (logo != null && logo.exists()){
            File destinationFile = new File(HelloApplication.dirLogo + File.separator+ logo.getName());
            Path source = logo.toPath();
            Path destination = Paths.get(HelloApplication.dirLogo + File.separator+ logo.getName());
            try {
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                selectedOrganization.setLogo(destinationFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (signature != null && signature.exists()){
            File destinationFile = new File(HelloApplication.dirLogo + File.separator+ signature.getName());
            Path source = signature.toPath();
            Path destination = Paths.get(HelloApplication.dirLogo + File.separator+ signature.getName());
            try {
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                selectedPersonal.setUrlTtd(destinationFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
