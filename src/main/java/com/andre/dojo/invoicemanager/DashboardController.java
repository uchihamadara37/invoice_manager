//package com.andre.dojo.invoicemanager;
//
//import com.andre.dojo.Models.Organization;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.geometry.Insets;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.*;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//public class DashboardController implements Initializable {
//    @FXML
//    private Button btnOrganization;
//    @FXML
//    private Button btnCustomers;
//    @FXML
//    private Button btnLayoutsDesign;
//    @FXML
//    private Button btnMakeInvoices;
//
//    @FXML
//    private Button btnEdit_org;
//    @FXML
//    private Button btnDelete_org;
//    @FXML
//    private AnchorPane paneDashboard;
//    @FXML
//    private VBox vboxOrganization;
//    @FXML
//    private HBox hboxMain;
//
//    // pop up insert org
//    @FXML
//    private Button btnAdd;
//
//
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//
//
//        btnEdit_org.setOnAction(e -> {
//            System.out.println("edit org");
//        });
//        btnDelete_org.setOnAction(e -> {
//            System.out.println("delete org");
//        });
//        btnAdd.setOnAction(e -> {
//            System.out.println("Add");
//            makeInputableData();
//        });
//        btnLayoutsDesign.setOnAction(e -> {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("layouts-design-view.fxml"));
//                AnchorPane anchorPane = new AnchorPane((AnchorPane)fxmlLoader.load());
//                HelloApplication.getMainStage().setTitle("Layout Design");
//                hboxMain.getChildren().remove(1);
//                hboxMain.getChildren().add(1, anchorPane);
//            }catch (IOException e1){
//                e1.printStackTrace();
//            }
//        });
//        btnOrganization.setOnAction(e -> {
//            HelloApplication.getMainStage().setTitle("Dashboard");
//            hboxMain.getChildren().remove(1);
//            hboxMain.getChildren().add(1, paneDashboard);
////            runInit();
//        });
//
//        runInit();
//    }
//
//    private void runInit() {
//        if (!HelloApplication.getDataOrganization().isEmpty()){
//            int i = 1;
//            for (Organization org : HelloApplication.getDataOrganization()){
//                Pane pane = new Pane();
////                pane.setPrefSize(839, Region.USE_COMPUTED_SIZE);
//                pane.setPrefHeight(169.0);
//                pane.setPrefWidth(839.0);
////                pane.setPadding(new Insets(40, 0, 0, 0));
//
//                ImageView imageView = new ImageView();
//                imageView.setFitHeight(133.0);
//                imageView.setFitWidth(140.0);
//                imageView.setLayoutX(24.0);
//                imageView.setLayoutY(18.0);
//                imageView.setPickOnBounds(true);
//                imageView.setPreserveRatio(true);
//
//                // Organization Name Label
//                Label orgNameLabel = new Label(org.getName());
//                orgNameLabel.setLayoutX(179.0);
//                orgNameLabel.setLayoutY(20.0);
//                orgNameLabel.setPrefHeight(40.0);
//                orgNameLabel.setPrefWidth(295.0);
//                orgNameLabel.setFont(new Font(24.0));
//
//                // Address Label
//                Label addressLabel = new Label(org.getEmail()+(i));
//                addressLabel.setLayoutX(179.0);
//                addressLabel.setLayoutY(147.0);
//                addressLabel.setPrefHeight(16.0);
//                addressLabel.setPrefWidth(351.0);
//
//                // Separator
//                Separator separator = new Separator();
//                separator.setLayoutX(179.0);
//                separator.setLayoutY(137.0);
//                separator.setPrefHeight(3.0);
//                separator.setPrefWidth(211.0);
//
//                // CEO / Direction Label
//                Label ceoLabel = new Label(org.getPersonManagement().getName());
//                ceoLabel.setLayoutX(567.0);
//                ceoLabel.setLayoutY(67.0);
//                ceoLabel.setPrefHeight(61.0);
//                ceoLabel.setPrefWidth(247.0);
//                ceoLabel.setAlignment(javafx.geometry.Pos.TOP_LEFT);
//
//                // Text
//                Text text = new Text(org.getAddress()+(i++));
//                text.setLayoutX(179.0);
//                text.setLayoutY(72.0);
//                text.setWrappingWidth(368.0);
//
//                // Add all elements to the pane
//                pane.getChildren().addAll(imageView, orgNameLabel, addressLabel, separator, ceoLabel, text);
//
//                vboxOrganization.getChildren().add(pane);
//
//            }
//        }
//    }
//
//    private void makeInputableData() {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("popUp-add-org-view.fxml"));
//            Scene scene = new Scene(fxmlLoader.load());
//
//            Stage popupStage = new Stage();
//            popupStage.initModality(Modality.APPLICATION_MODAL);
//            popupStage.setTitle("Add Organization");
//            popupStage.setScene(scene);
//            popupStage.showAndWait();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//
//    }
//}
