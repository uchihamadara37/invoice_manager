package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Design;
import com.andre.dojo.Models.Invoice;
import com.dlsc.pdfviewfx.PDFView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.swing.JRViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class JrxmlController implements Initializable {
    private HelloController helloController;
    private Design designSelected;

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
    private Pane btnCancel;
    @FXML
    private Label labelEdit;
    @FXML
    private Pane btnAddJrxml;
    @FXML
    private Pane btnSelectDesign;

    @FXML
    private Pane btnGeneratePreview;
    @FXML
    private VBox listDesign;
    @FXML
    private Label labelSave;

    @FXML
    private Label information;
    @FXML
    private Label information2;

    private String state = "lihat";
    // lihat
    // insert
    // select
    // edit

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        setState("lihat");

        btnGeneratePreview.setOnMouseClicked(e -> {
            if (Objects.equals(labelSave.getText(), "Add Now")){
                Design design2 = new Design(jrxmlText.getText(), "", "");
                generateFirstReportImage(jrxmlText.getText(), helloController.getInvoiceSelected().getJsonData(), design2);

            } else if (Objects.equals(labelSave.getText(), "Save Changes")) {
                Design design = Design.getOneData(designSelected.getId());
                design.setJrxml(jrxmlText.getText());
                generateReportImage(jrxmlText.getText(), helloController.getInvoiceSelected().getJsonData(), design);

            } else if (Objects.equals(labelSave.getText(), "Cancel")) {
                setState("lihat");
            }
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
        btnAddJrxml.setOnMouseClicked(e -> {
            setState("insert");

        });
        btnCancel.setOnMouseClicked(e -> {
            if (Objects.equals(labelEdit.getText(), "Edit")){
                setState("edit");
            } else if (Objects.equals(labelEdit.getText(), "Cancel")) {
                if (Objects.equals(state, "edit")){
                    setState("select");
                }else{
                    setState("lihat");
                }
            }
        });
        btnSelectDesign.setOnMouseClicked(e -> {
            if (designSelected != null && helloController.getInvoiceSelected() != null){
                helloController.getInvoiceSelected().setJrxml_id(designSelected.getId());
                helloController.getPreviewController().setLabelDesignSelected("Design "+designSelected.getId()+" has been selected for this invoice. Lets start preview!");
                loadDesignSideBar();
                Invoice.updateById(helloController.getInvoiceSelected());
            }
        });


    }

    public void setState(String state) {
        this.state = state;
        if (Objects.equals(state, "lihat")){
            labelEdit.setText("Edit");
            jrxmlText.setText("");
            jrxmlText.setPromptText("You can add new design and fill this field with your jrxml then click save button bellow.\n " +
                    "To select the design to this invoice you can click the image and then click select.\n " +
                    "To start editing jrxml, you can select the design first.");
            jrxmlText.setEditable(false);
            jrxmlText.setDisable(true);
            btnCancel.setVisible(false);
            btnGeneratePreview.setVisible(false);
            btnAddJrxml.setVisible(true);
            btnSelectDesign.setVisible(false);
            information.setText("You can select the design by clicking in the left sidebar or add a new design.");
            if (helloController.getInvoiceSelected() != null && helloController.getInvoiceSelected().getJrxml_id() != 0){
                information2.setVisible(true);
            }

        } else if (Objects.equals(state, "insert")){
            jrxmlText.setPromptText("Paste your jrxml in here, or you can paste directly from existing design by click on the left sidebar image preview.");
            jrxmlText.setText("");
            jrxmlText.setEditable(true);
            jrxmlText.setDisable(false);
            btnCancel.setVisible(true);
            btnGeneratePreview.setVisible(true);
            btnAddJrxml.setVisible(false);
            btnSelectDesign.setVisible(false);
            information.setText("Please fill text area bellow with correct and suitable jrxml string.");
            labelEdit.setText("Cancel");
            labelSave.setText("Add Now");
            information2.setVisible(false);

        } else if (Objects.equals(state, "select")){
            jrxmlText.setEditable(false);
            jrxmlText.setDisable(true);

            labelEdit.setText("Edit");
            labelSave.setText("Cancel");
            btnCancel.setVisible(true);

            btnGeneratePreview.setVisible(true);
            btnAddJrxml.setVisible(false);
            btnSelectDesign.setVisible(true);
            information.setText("To confirm that you want to select this design, click button select bellow!");
            information2.setVisible(false);

        } else if (Objects.equals(state, "edit")){
            jrxmlText.setEditable(true);
            jrxmlText.setDisable(false);
            btnCancel.setVisible(true);
            btnGeneratePreview.setVisible(true);
            btnAddJrxml.setVisible(false);
            btnSelectDesign.setVisible(false);
            information.setText("Lets edit your jrxml!");
            labelSave.setText("Save Changes");
            labelEdit.setText("Cancel");
            information2.setVisible(false);

        }
    }

    public void loadDesignSideBar() {
        listDesign.getChildren().clear();
        for (Design d : Design.getAllData()){
            Pane pane = new Pane();
            if (helloController.getInvoiceSelected() != null && helloController.getInvoiceSelected().getJrxml_id() == d.getId()){
                pane.setStyle("-fx-background-color: -fx-light-accent-color;");
            } else if (designSelected != null && designSelected.getId() == d.getId()){
                pane.setStyle("-fx-background-color: -fx-light-2-color;");

            }else {
                pane.setOnMouseEntered(e -> {
                    pane.setStyle("-fx-background-color: -fx-light-2-color;");
                });
                pane.setOnMouseExited(e -> {
                    pane.setStyle("-fx-background-color: -fx-light-background-color;");
                });
            }
            pane.setPrefHeight(134.0);
            pane.setPrefWidth(208.0);

            Label label = new Label();
            label.setText(String.valueOf(d.getId()));
            if (helloController.getInvoiceSelected() != null){
                if (helloController.getInvoiceSelected().getJrxml_id() == d.getId()){
                    label.setText("Selected");
                    label.setStyle("-fx-text-fill: white; -fx-font-weight: 700;");
                }
            }
            label.setLayoutX(12.0);
            label.setLayoutY(108.0);
            label.setPrefHeight(17.0);
            label.setPrefWidth(127.0);

            ImageView imageView = new ImageView();
            imageView.setFitHeight(25.0);
            imageView.setFitWidth(25.0);
            imageView.setLayoutX(170.0);
            imageView.setLayoutY(102.0);
            imageView.setPickOnBounds(true);
            imageView.setPreserveRatio(true);
            Image image = new Image(getClass().getResource("icon/circle-minus.png").toExternalForm());
            imageView.setImage(image);
            imageView.setOnMouseClicked(e -> {
                deleteDesign(d);
            });
            if (helloController.getInvoiceSelected() != null && helloController.getInvoiceSelected().getJrxml_id() == d.getId()){
                imageView.setVisible(false);
            }

            ImageView imageView1 = new ImageView();
            imageView1.setFitHeight(25.0);
            imageView1.setFitWidth(25.0);
            imageView1.setLayoutX(139.0);
            imageView1.setLayoutY(102.0);
            imageView1.setPickOnBounds(true);
            imageView1.setPreserveRatio(true);
            Image image1 = null;
            if (helloController.getInvoiceSelected() != null && helloController.getInvoiceSelected().getJrxml_id() == d.getId()){
                image1 = new Image(getClass().getResource("icon/copy.png").toExternalForm());
            }else{
                image1 = new Image(getClass().getResource("icon/copy(1).png").toExternalForm());
            }
            imageView1.setImage(image1);
            imageView1.setOnMouseClicked(e -> {
                copyJrxmlDesign(d.getJrxml(), imageView1);

            });


            ImageView imageView2 = new ImageView();
            if (!Objects.equals(d.getDirImage(), "") || d.getDirImage() != null){
                File checkImg = new File(d.getDirImage());
                if (checkImg.exists()){
                    Image img = new Image("file:/"+d.getDirImage());
                    imageView2.setImage(img);
                }else{
//                    WritableImage blankImg = new WritableImage(100, 100);
//                    PixelWriter pw = blankImg.getPixelWriter();
//
//                    for (int x = 0; x < 100; x++) {
//                        for (int y = 0; y < 100; y++) {
//                            pw.setColor(x, y, Color.TRANSPARENT);
//                        }
//                    }
//                    imageView2.setImage(blankImg);
                }
            }
            imageView2.setFitWidth(204.0); // Atur lebar ImageView
            imageView2.setFitHeight(95.0); // Atur tinggi ImageView
            imageView2.setPreserveRatio(true);
            imageView2.setSmooth(true);
            imageView2.setViewport(new Rectangle2D(0,0,600, 289));
            imageView2.setLayoutX(3.0);
            imageView2.setLayoutY(3.0);
            Pane wrapper = new Pane(imageView2);
            wrapper.setPrefSize(204.0, 95.0);
            wrapper.setOnMouseClicked(e -> {
                setState("select");
                designSelected = d;
                if (!Objects.equals(d.getJrxml(), "")){
                    jrxmlText.setText(d.getJrxml());
                }
                loadDesignSideBar();
            });


            pane.getChildren().addAll(label, imageView, wrapper, imageView1);

            listDesign.getChildren().add(pane);
        }
    }



    private void deleteDesign(Design d) {
        if (helloController.showConfirmationDialog("Do you really want to delete this Design?")) {
            // process delete design
            if (Design.isAnyInvoiceUseThisDesign(d.getId())){
                HelloApplication.showAlert("This design is being used by another invoice!");
            }else{
                // delete file img dan pdfnya dulu
                deleteFile(d.getDirImage());
                deleteFile(d.getDirPdf());
                Design.deleteOneById(d.getId());
                loadDesignSideBar();
            }
        }
    }

    public void deleteFile(String absolutePath) {
        try {
            Path path = Paths.get(absolutePath);
            Files.delete(path);
//            System.out.println("File deleted successfully: " + absolutePath);
        } catch (NoSuchFileException e) {
            System.err.println("The file does not exist: " + absolutePath);
        } catch (DirectoryNotEmptyException e) {
            System.err.println("The file is a directory and is not empty: " + absolutePath);
        } catch (IOException e) {
            System.err.println("Failed to delete the file: " + absolutePath);
//            e.printStackTrace();
        }
    }

    private void copyJrxmlDesign(String jrxml, ImageView imgView) {
        ClipboardContent content = new ClipboardContent();
        content.putString(jrxml);
        Clipboard.getSystemClipboard().setContent(content);

        Tooltip tooltip = new Tooltip();
        tooltip.setText("Json copied!");
        Point2D p = imgView.localToScene(0.0, 0.0);
        tooltip.show(
                imgView,
                p.getX() + HelloApplication.getMainStage().getX()
                        + imgView.getScene().getX() + 30,
                p.getY() + HelloApplication.getMainStage().getY()
                        + imgView.getScene().getY() - 35
        );
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                ae -> tooltip.hide()
        ));
        timeline.play();
    }

//    private void saveDesign() {
//        System.out.println("jr id = "+helloController.getInvoiceSelected().getJrxml_id());
//        if (helloController.getInvoiceSelected().getJrxml_id() == 0 ){
//            Design design2 = new Design(jrxmlText.getText(), "", "");
//            helloController.getInvoiceSelected().setJrxml_id(design2.getId());
//            Invoice.updateById(helloController.getInvoiceSelected());
//
//            try {
//                generateFirstReportImage(jrxmlText.getText(), helloController.getInvoiceSelected().getJsonData(), design2);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            Design.addToDB(design2);
//        }else{
//            Design design = Design.getOneData(helloController.getInvoiceSelected().getJrxml_id());
//            design.setJrxml(jrxmlText.getText());
//            Design.updateById(design);
//            try {
//                generateReportImage(jrxmlText.getText(), helloController.getInvoiceSelected().getJsonData(), design);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        Platform.runLater(() ->{
//            helloController.getJrxmlController().showPreview();
//        });
//    }

    public void generateFirstReportImage(String jrxmlString, String jsonData, Design design){
        JasperReport jasperReport = null;
        try {
            jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(jrxmlString.getBytes()));
        } catch (JRException e) {
            HelloApplication.showAlert("There is some error : "+e.getMessage());
            return;
        }
        JsonDataSource dataSource = null;
        try {
            dataSource = new JsonDataSource(new ByteArrayInputStream(jsonData.getBytes()));
        } catch (JRException e) {
            HelloApplication.showAlert("There is some error : "+e.getMessage());
            return;
        }
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
        } catch (JRException e) {
            HelloApplication.showAlert("There is some error : "+e.getMessage());
            return;
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String baseFileName = "report_" + timestamp;
        String pngFileName = baseFileName + ".png";
        String pdfFileName = baseFileName + ".pdf";
        String pngFullPath = HelloApplication.dirImage + File.separator + pngFileName;
        String pdfFullPath = HelloApplication.dirPdf + File.separator + pdfFileName;

        // Export to PNG
        exportToPng(jasperPrint, pngFullPath);

        // Export to PDF
        exportToPdf(jasperPrint, pdfFullPath);

        design.setDirImage(pngFullPath);
        design.setDirPdf(pdfFullPath);

//        System.out.println("PNG exported to: " + pngFullPath);
//        System.out.println("PDF exported to: " + pdfFullPath);

        Design.addToDB(design);
        loadDesignSideBar();
        HelloApplication.showAlert("Design has been added!");
    }

    public void generateReportImage(String jrxmlString, String jsonData, Design design){
        // Compile JRXML
        JasperReport jasperReport = null;
        try {
            jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(jrxmlString.getBytes()));
        } catch (JRException e) {
            HelloApplication.showAlert("There is some error : "+e.getMessage());
            return;
        }
        JsonDataSource dataSource = null;
        try {
            dataSource = new JsonDataSource(new ByteArrayInputStream(jsonData.getBytes()));
        } catch (JRException e) {
            HelloApplication.showAlert("There is some error : "+e.getMessage());
            return;
        }
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
        } catch (JRException e) {
            HelloApplication.showAlert("There is some error : "+e.getMessage());
            return;
        }

        // check if dataDir berbeda
        File dirInDBImg = new File(design.getDirImage());
        File dirInDBPdf = new File(design.getDirPdf());
        File dirUpdate = new File(HelloApplication.dirSource);

//        System.out.println("dir parent : "+dirInDBImg.getParent());
//        System.out.println("dir update : "+dirUpdate.getAbsolutePath());

        if (Objects.equals(dirInDBImg.getParent(), dirUpdate.getAbsolutePath())){
            replaceImage(jasperPrint, design.getDirImage());
            replacePdf(jasperPrint, design.getDirPdf());
        }else{
            String pngFullPath = HelloApplication.dirImage + File.separator + dirInDBImg.getName();
            String pdfFullPath = HelloApplication.dirPdf + File.separator + dirInDBPdf.getName();

            // Export to PNG
            exportToPng(jasperPrint, pngFullPath);

            // Export to PDF
            exportToPdf(jasperPrint, pdfFullPath);

//            System.out.println("PNG exported to ganti folder: " + pngFullPath);
//            System.out.println("PDF exported to ganti folder: " + pdfFullPath);

            design.setDirImage(pngFullPath);
            design.setDirPdf(pdfFullPath);

            loadDesignSideBar();
        }
        Design.updateById(design);
        HelloApplication.showAlert("The design has been successfully edited!");
    }

    private static void exportToPng(JasperPrint jasperPrint, String fullPath){
        int pageIndex = 0;
        int pageWidth = (int) jasperPrint.getPageWidth();
        int pageHeight = (int) jasperPrint.getPageHeight();

        BufferedImage image = new BufferedImage(pageWidth, pageHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();
        JRGraphics2DExporter exporter = null;
        try {
            exporter = getJrGraphics2DExporter(jasperPrint, g2d, pageIndex);
        } catch (JRException e) {
            HelloApplication.showAlert("There is some error : "+e.getMessage());
            return;
        }
        try {
            exporter.exportReport();
        } catch (JRException e) {
            HelloApplication.showAlert("There is some error : "+e.getMessage());
            return;
        }
        g2d.dispose();

        try {
            ImageIO.write(image, "png", new File(fullPath));
        } catch (Exception e) {
            HelloApplication.showAlert("Error writing PNG file : "+e.getMessage());
        }
    }

    public static void exportToPdf(JasperPrint jasperPrint, String fullPath){
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fullPath));

        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(configuration);

        try {
            exporter.exportReport();
        } catch (JRException e) {
            HelloApplication.showAlert("There is some error : "+e.getMessage());
            return;
        }
    }



    private static void replacePdf(JasperPrint jasperPrint, String dirImage) {

        File outputFile = new File(dirImage);
        File tempFile = null;

        try {
            // Buat file temporary
            tempFile = File.createTempFile("temp_", ".pdf", outputFile.getParentFile());

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(tempFile));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();

            // Ganti file lama dengan file temporary
            Files.move(tempFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

//            System.out.println("File berhasil diganti pdf: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Terjadi kesalahan saat mengganti file: " + e.getMessage());
            HelloApplication.showAlert("Terjadi kesalahan saat mengganti file: "+e.getMessage());
            return;
        } catch (JRException e) {
            HelloApplication.showAlert("Terjadi kesalahan saat mengganti file: "+e.getMessage());
            return;
        } finally {
            // Hapus file temporary jika masih ada
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    public static void replaceImage(JasperPrint jasperPrint, String filePath) {

        int pageIndex = 0;
        int pageWidth = (int) jasperPrint.getPageWidth();
        int pageHeight = (int) jasperPrint.getPageHeight();
        BufferedImage image = new BufferedImage(pageWidth, pageHeight, BufferedImage.TYPE_INT_RGB);

        File outputFile = new File(filePath);
        File tempFile = null;

        try {
            // Buat file temporary
            tempFile = File.createTempFile("temp_", ".png", outputFile.getParentFile());

            Graphics2D g2d = image.createGraphics();
            JRGraphics2DExporter exporter = getJrGraphics2DExporter(jasperPrint, g2d, pageIndex);
            exporter.exportReport();
            g2d.dispose();
            // Tulis gambar baru ke file temporary
            ImageIO.write(image, "png", tempFile);

            // Ganti file lama dengan file temporary
            Files.move(tempFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

//            System.out.println("File berhasil diganti png: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Terjadi kesalahan saat mengganti file: " + e.getMessage());
//            e.printStackTrace();
        } catch (JRException e) {
            throw new RuntimeException(e);
        } finally {
            // Hapus file temporary jika masih ada
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private static JRGraphics2DExporter getJrGraphics2DExporter(JasperPrint jasperPrint, Graphics2D g2d, int pageIndex) throws JRException {
        JRGraphics2DExporter exporter = new JRGraphics2DExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        // Use Graphics2DExporterOutput instead of SimpleGraphics2DExporterOutput
        Graphics2DExporterOutput exporterOutput = new Graphics2DExporterOutput() {
            @Override
            public Graphics2D getGraphics2D() {
                return g2d;
            }
        };
        exporter.setExporterOutput(exporterOutput);

        SimpleGraphics2DReportConfiguration configuration = new SimpleGraphics2DReportConfiguration();
        configuration.setPageIndex(pageIndex);
        exporter.setConfiguration(configuration);
        return exporter;
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
            HelloApplication.showAlert("There is some error : "+e.getMessage());
            return;
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
