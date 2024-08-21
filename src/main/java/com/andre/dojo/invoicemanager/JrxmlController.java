package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Design;
import com.andre.dojo.Models.Invoice;
import com.dlsc.pdfviewfx.PDFView;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class JrxmlController implements Initializable {
    private HelloController helloController;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextArea jrxmlText;
    @FXML
    private Label btnItem;
    @FXML
    private Label btnInvoiceSelection;
    @FXML
    private Label btnJsonData;
    @FXML
    private Label btnPreview;

    @FXML
    private Pane btnGeneratePreview;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnGeneratePreview.setOnMouseClicked(e -> {
            saveDesign();
        });
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
        btnPreview.setOnMouseClicked(e -> {
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getPreviewController().getRootPane());
        });


    }

    private void saveDesign() {
        System.out.println("jr id = "+helloController.getInvoiceSelected().getJrxml_id());
        if (helloController.getInvoiceSelected().getJrxml_id() == 0 ){
            Design design2 = new Design(jrxmlText.getText(), String.valueOf(helloController.getInvoiceSelected().getId()));
            Design.addToDB(design2);
        }else{
            Design design = Design.getOneData(helloController.getInvoiceSelected().getJrxml_id());
            design.setJrxml(jrxmlText.getText());
            Design.updateById(design);
        }

        Platform.runLater(() ->{
            helloController.getJrxmlController().showPreview();
        });


    }

    public void showPreview() {
        String jrxml = Design.getOneData(helloController.getInvoiceSelected().getJrxml_id()).getJrxml();
        String json = helloController.getInvoiceSelected().getJsonData();
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(jrxml.getBytes()));
            JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(json.getBytes()));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, jsonDataSource);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
            InputStream pdfInputStream = new ByteArrayInputStream(baos.toByteArray());

            PDFView pdfView = new PDFView();
            pdfView.load(pdfInputStream);
            pdfView.prefWidthProperty().bind(helloController.getPreviewController().getPreviewPane().widthProperty());
            pdfView.prefHeightProperty().bind(helloController.getPreviewController().getPreviewPane().heightProperty());

            helloController.getPreviewController().getPreviewPane().getChildren().add(pdfView);

        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public void setJrxmlTextArea(String jrxmlString){
        jrxmlText.setText(jrxmlString);
    }

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }
}
