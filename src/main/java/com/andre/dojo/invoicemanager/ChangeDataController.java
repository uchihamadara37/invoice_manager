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

import static java.lang.Integer.parseInt;

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
    private TextField agency;
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
            saveData(idOrganization,idPerson);
            textAccess(false);
        });
        cancel.setOnMouseClicked(e -> {
            disable();
            loadData();
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
        Organization firstOrganization = allOrganizations.get(0);
        year.setText(String.valueOf(firstOrganization.getTahunOperasi()));
        organizationName.setText(firstOrganization.getBrandName());
        email.setText(firstOrganization.getEmail());
        address.setText(firstOrganization.getAddress());
        desc.setText(firstOrganization.getDescription());
        agency.setText(String.valueOf(firstOrganization.getNoUrutInstansi()));
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
        agency.setDisable(con);
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
                idOrganization, "",organizationName.getText(),desc.getText(),address.getText(), email.getText(),parseInt(agency.getText()),parseInt(year.getText()))
        ,idOrganization);
        Personal.updateById(new Personal(
                personalName.getText(),bankName.getText(), bankNumber.getText(), bankID.getText(),"",idOrganization
        ));
    }
}
