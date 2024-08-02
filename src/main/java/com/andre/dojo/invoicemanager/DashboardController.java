package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Organization;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Button btnOrganization;
    @FXML
    private Button btnCustomers;
    @FXML
    private Button btnLayoutsDesign;
    @FXML
    private Button btnMakeInvoices;

    @FXML
    private Button btnEdit_org;
    @FXML
    private Button btnDelete_org;
    @FXML
    private AnchorPane paneDashboard;
    @FXML
    private VBox vboxOrganization;
    @FXML
    private HBox hboxMain;

    // pop up insert org
    @FXML
    private Button btnAdd;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        btnEdit_org.setOnAction(e -> {
            System.out.println("edit org");
        });
        btnDelete_org.setOnAction(e -> {
            System.out.println("delete org");
        });
        btnAdd.setOnAction(e -> {
            System.out.println("Add");
            makeInputableData();
        });
        btnLayoutsDesign.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("layouts-design-view.fxml"));
                AnchorPane anchorPane = new AnchorPane((AnchorPane)fxmlLoader.load());
                HelloApplication.getMainStage().setTitle("Layout Design");
                hboxMain.getChildren().remove(1);
                hboxMain.getChildren().add(1, anchorPane);
            }catch (IOException e1){
                e1.printStackTrace();
            }
        });
        btnOrganization.setOnAction(e -> {
            HelloApplication.getMainStage().setTitle("Dashboard");
            hboxMain.getChildren().remove(1);
            hboxMain.getChildren().add(1, paneDashboard);
//            runInit();
        });

        runInit();
    }

    private void runInit() {
        if (!HelloApplication.getDataOrganization().isEmpty()){
            for (Organization org : HelloApplication.getDataOrganization()){
                Pane pane = new Pane();
                pane.setPrefSize(839, 169);

                // ImageView
                ImageView imageView = new ImageView();
                imageView.setFitHeight(133);
                imageView.setFitWidth(140);
                imageView.setLayoutX(24);
                imageView.setLayoutY(18);
                imageView.setPreserveRatio(true);
                imageView.setPickOnBounds(true);

                // Organization Name Label
                Label nameLabel = new Label(org.getName());
                nameLabel.setLayoutX(179);
                nameLabel.setLayoutY(25);
                nameLabel.setFont(new Font(24));

                // Organization Description Label
                Label descLabel = new Label(org.getEmail());
                descLabel.setLayoutX(179);
                descLabel.setLayoutY(67);
                descLabel.setPrefSize(377, 61);
                descLabel.setAlignment(javafx.geometry.Pos.TOP_LEFT);

                // Address Label
                Label addressLabel = new Label(org.getAddress());
                addressLabel.setLayoutX(179);
                addressLabel.setLayoutY(147);
                addressLabel.setPrefSize(351, 16);

                // Separator
                Separator separator = new Separator();
                separator.setLayoutX(179);
                separator.setLayoutY(137);
                separator.setPrefSize(211, 3);

                // CEO / Direction Label
                Label ceoLabel = new Label(org.getPersonManagement().getName());
                ceoLabel.setLayoutX(567);
                ceoLabel.setLayoutY(67);
                ceoLabel.setPrefSize(247, 61);
                ceoLabel.setAlignment(javafx.geometry.Pos.TOP_LEFT);

                // Add all elements to the pane
                pane.getChildren().addAll(imageView, nameLabel, descLabel, addressLabel, separator, ceoLabel);

                vboxOrganization.getChildren().add(pane);
            }
        }
    }

    private void makeInputableData() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("popUp-add-org-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Add Organization");
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
