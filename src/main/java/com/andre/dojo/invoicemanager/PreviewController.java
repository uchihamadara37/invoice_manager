package com.andre.dojo.invoicemanager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class PreviewController implements Initializable {

    private HelloController helloController;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Pane previewPane;


    @FXML
    private Label btnInvoiceSelection;
    @FXML
    private Label btnItem;
    @FXML
    private Label btnJsonData;
    @FXML
    private Label btnJrxml;
    @FXML
    private Label btnPreview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnInvoiceSelection.setOnMouseClicked(e -> {
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getSetupController().getRootPane());
        });
        btnItem.setOnMouseClicked(e -> {
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getAddItemController().getRootPane());
        });
        btnJsonData.setOnMouseClicked(e -> {
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getJsonDataController().getRootPane());
        });
        btnJrxml.setOnMouseClicked(e -> {
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getJrxmlController().getRootPane());

        });
    }

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    public Pane getPreviewPane() {
        return previewPane;
    }
}
