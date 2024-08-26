package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Design;
import com.dlsc.pdfviewfx.PDFView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    @FXML
    private Label labelDesignSelected;
    @FXML
    private Pane btnLoadPreview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelDesignSelected.setText("There is no design has been selected!");
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

        btnLoadPreview.setOnMouseClicked(e -> {
            loadPreviewTemporary();
        });
    }

    private void loadPreviewTemporary() {
        Platform.runLater(() -> {
            try {
                JasperReport jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(Design.getOneData(helloController.getInvoiceSelected().getJrxml_id()).getJrxml().getBytes()));
                JsonDataSource dataSource = new JsonDataSource(new ByteArrayInputStream(helloController.getInvoiceSelected().getJsonData().getBytes()));
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
                // Convert JasperPrint to PDF
                ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, pdfOutputStream);

                // Create an InputStream from the PDF bytes
                ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());

                PDFView pdfView = new PDFView();
                pdfView.load(pdfInputStream);

                pdfView.prefWidthProperty().bind(previewPane.widthProperty());
                pdfView.prefHeightProperty().bind(previewPane.heightProperty());

                previewPane.getChildren().clear();
                previewPane.getChildren().add(pdfView);


            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public void setLabelDesignSelected(String labelDesignSelected) {
        this.labelDesignSelected.setText(labelDesignSelected);
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    public Pane getPreviewPane() {
        return previewPane;
    }
}
