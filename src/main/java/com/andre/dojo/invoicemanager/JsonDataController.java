package com.andre.dojo.invoicemanager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class JsonDataController implements Initializable {

    private HelloController helloController;

    @FXML
    private TextArea jsonText;
    @FXML
    private Pane btnCopy;
    @FXML
    private Label btnItem;
    @FXML
    private Label btnInvoiceSelection;
    @FXML
    private Label btnJrxml;
    @FXML
    private Label btnPreview;

    @FXML
    private AnchorPane rootPane;

    private Tooltip tooltip = new Tooltip();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnCopy.setOnMouseClicked(e -> {
            copyToClipboard();
            tooltip.setText("Json copied!");
            Point2D p = btnCopy.localToScene(0.0, 0.0);
            tooltip.show(
                    btnCopy,
                    p.getX() + HelloApplication.getMainStage().getX()
                            + btnCopy.getScene().getX() + 30,
                    p.getY() + HelloApplication.getMainStage().getY()
                            + btnCopy.getScene().getY() - 35
            );
            System.out.println("p.getx = "+p.getX()+" stage.x = "+HelloApplication.getMainStage().getX()+" scenex = "+btnCopy.getScene().getX());
            System.out.println("p.getY = "+p.getY()+" stage.Y = "+HelloApplication.getMainStage().getY()+" scene Y = "+btnCopy.getScene().getY());
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.seconds(3),
                    ae -> tooltip.hide()
            ));
            timeline.play();
        });
        btnCopy.setOnDragEntered(e -> {
            tooltip.hide();
        });


        btnInvoiceSelection.setOnMouseClicked(e -> {
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getSetupController().getRootPane());
        });
        btnItem.setOnMouseClicked(e -> {
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getAddItemController().getRootPane());
        });
        btnJrxml.setOnMouseClicked(e -> {
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getJrxmlController().getRootPane());
        });
        btnPreview.setOnMouseClicked(e -> {
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getPreviewController().getRootPane());
        });
    }

    private void copyToClipboard() {
        ClipboardContent content = new ClipboardContent();
        content.putString(jsonText.getText());
        Clipboard.getSystemClipboard().setContent(content);
//        Tooltip tooltip1 = new Tooltip("json text already copied!");
//        tooltip1.sh
    }

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public void setJsonText(String json) {
        jsonText.setText(json);
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }
}
