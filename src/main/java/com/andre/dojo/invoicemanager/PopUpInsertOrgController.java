package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Organization;
import com.andre.dojo.Models.PersonManagement;
import com.andre.dojo.Utils.ObjectSaver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PopUpInsertOrgController implements Initializable {
    @FXML
    private TextField orgName;
    @FXML
    private TextArea orgAddress;
    @FXML
    private TextField orgEmail;
    @FXML
    private TextField perName;
    @FXML
    private TextField perMobilePhone;
    @FXML
    private TextField perBankID;
    @FXML
    private TextField perBankName;
    @FXML
    private TableView<PersonManagement> tableViewPopUpInsert;
    @FXML
    private TableColumn<PersonManagement, String> columnPopUpInsert;
    @FXML
    private Button btnAddOrgInput;
    @FXML
    private Button cancelInput;
    @FXML
    private Button cancelSelect;



    private String idSelected = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reloadTableContent();
        cancelInput.setOnAction(e -> {
            Stage stage = (Stage) cancelInput.getScene().getWindow();
            stage.close();
        });
        btnAddOrgInput.setOnAction(e -> {
            btnAddOrgInputAction();
        });
        cancelSelect.setVisible(false);
        cancelSelect.setOnAction(e -> {
            enableFieldPerson();
        });

    }

    private void enableFieldPerson() {
        perName.setText("");
        perMobilePhone.setText("");
        perBankID.setText("");
        perBankName.setText("");
        perName.setDisable(false);
        perMobilePhone.setDisable(false);
        perBankID.setDisable(false);
        perBankName.setDisable(false);
        idSelected = "";
        cancelSelect.setVisible(false);
        tableViewPopUpInsert.getSelectionModel().clearSelection();
    }

    private void reloadTableContent() {
        tableViewPopUpInsert.setItems(HelloApplication.getDataPerson());
        columnPopUpInsert.setCellValueFactory(obj -> obj.getValue().getPropertyName());

        tableViewPopUpInsert.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    showPersonOnField(newValue);

//
                });
    }

    private void showPersonOnField(PersonManagement p) {
        if (p != null){
            perName.setText(p.getName());
            perMobilePhone.setText(p.getMobilePhone());
            perBankID.setText(String.valueOf(p.getNoRekening()));
            perBankName.setText(p.getBank());
            perName.setDisable(true);
            perMobilePhone.setDisable(true);
            perBankID.setDisable(true);
            perBankName.setDisable(true);
            idSelected = p.getId();

            cancelSelect.setVisible(true);
        }else{
            System.out.println("milih null malahan");
        }
    }

    private void btnAddOrgInputAction() {
            // kumpulkan dahulu var org
            String nameOrg = orgName.getText();
            String addressOrg = orgAddress.getText();
            String emailOrg = orgEmail.getText();

            String namePer = perName.getText();
            String mobilePer = perMobilePhone.getText();
            String bankID = perBankID.getText();
            String bankName = perBankName.getText();

            if (!Objects.equals(nameOrg, "") ||
                    !Objects.equals(addressOrg, "") ||
                    !Objects.equals(emailOrg, "") ||
                    !Objects.equals(namePer, "") ||
                    !Objects.equals(mobilePer, "") ||
                    !Objects.equals(bankID, "") ||
                    !Objects.equals(bankName, "")
            ){
                Organization organization = new Organization(nameOrg, addressOrg, emailOrg);
                if (Objects.equals(idSelected, "") || idSelected == null){
                    PersonManagement person = new PersonManagement(namePer, mobilePer, bankID, bankName);
                    HelloApplication.addDataPerson(person);
                    organization.setPersonManagement(person);
                    HelloApplication.addDataOrganization(organization);
                    // save data
                    ObjectSaver.saveData(HelloApplication.getMetadataSave());
                    // alert and close popup
                    HelloApplication.showAlert("Data Berhasil ditambahkan");
                    Stage stage = (Stage) cancelInput.getScene().getWindow();
                    stage.close();
                }else{
                    for(PersonManagement p : HelloApplication.getDataPerson()){
                        if (Objects.equals(p.getId(), idSelected)){
//
                            organization.setPersonManagement(p);
                            break;
                        }
                    }
                    HelloApplication.addDataOrganization(organization);
                    ObjectSaver.saveData(HelloApplication.getMetadataSave());
                    HelloApplication.showAlert("Data Berhasil ditambahkan");
                    idSelected = "";
                    Stage stage = (Stage) cancelInput.getScene().getWindow();
                    stage.close();
                }
            }else{
                // popup pesan harus mengisi semua data
                HelloApplication.showAlert("please fill all field with correct data.");
            }


    }
}
