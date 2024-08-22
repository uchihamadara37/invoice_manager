package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Organization;
import com.andre.dojo.Models.Personal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class ChangeDataController {
    @FXML
    private Label tombolCustomer;
    @FXML
    private AnchorPane anchorPaneMain;
    @FXML
    private Pane save;
    @FXML
    private Pane edit;
    @FXML
    private Pane cancel;
    @FXML
    private TextField year;
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

    private ChangeDataInvoiceController changeDataInvoiceController;
    private HelloController helloController;
    private Personal personal;
    private long idPerson;
    private long idOrganization;
//    private Organization organization;

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public AnchorPane getAnchorPaneMain() {
        return anchorPaneMain;
    }

    public void setChangeDataInvoiceController(ChangeDataInvoiceController changeDataInvoiceController){
        this.changeDataInvoiceController = changeDataInvoiceController;
    }

    public void initialize(){
        tombolCustomer.setOnMouseClicked(e -> {
            openCustomerPane();
        });
        loadData();
        disable();
        edit.setOnMouseClicked(e -> {
            enable();
            textAccess(false);
        });
        cancel.setOnMouseClicked(e -> {
            disable();
            textAccess(true);
        });
        save.setOnMouseClicked(e -> {
            disable();
            textAccess(true);
        });
    }



    private void openCustomerPane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("change-data-customer.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ChangeDataInvoiceController test = fxmlLoader.getController();
            test.setChangedataController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void loadData(){
        List<Organization> allOrganizations = Organization.getAllData();
        System.out.println(allOrganizations);
        System.out.println("tahun operasi : " + allOrganizations.get(0));
        Organization firstOrganization = allOrganizations.get(0);
        year.setText(String.valueOf(firstOrganization.getTahunOperasi()));
        organizationName.setText(firstOrganization.getBrandName());
        email.setText(firstOrganization.getEmail());
        address.setText(firstOrganization.getAddress());
        desc.setText(firstOrganization.getDescription());
        idOrganization = firstOrganization.getId();
        List<Personal> personalList = Personal.getAllData();
        Personal firstPersonal = personalList.get(0);
        personalName.setText(firstPersonal.getName());
        bankID.setText(firstPersonal.getBankIDName());
        bankName.setText(firstPersonal.getBankName());
        bankNumber.setText(String.valueOf(firstPersonal.getBankIDNumber()));
        idPerson = firstPersonal.getId();
        textAccess(true);
    }

    private void textAccess(boolean con){
        year.setDisable(con);
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
}
