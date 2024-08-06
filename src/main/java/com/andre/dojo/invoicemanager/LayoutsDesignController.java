package com.andre.dojo.invoicemanager;

import com.andre.dojo.Utils.ObjectSaver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.pdf.PdfDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.control.ScrollPane;

public class LayoutsDesignController implements Initializable {

    @FXML
    private TextArea textJrxml;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;

    @FXML
    private TextArea textAreaDataSource;

    @FXML
    private AnchorPane anchorPanePreview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        textAreaDataSource.setText(ObjectSaver.jsonData);

//        URL reportStream = getClass().getResource("C:\\Users\\andre\\JaspersoftWorkspace\\MyReports\\test.jrxml");
        URL reportStream = null;
        try {
            reportStream = new File("C:\\Users\\andre\\JaspersoftWorkspace\\MyReports\\test.jrxml").toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        File inputFile = new File(getClass().getResource("Jrxml/Invoice.jrxml").getFile());
        File exportFile = new File(getClass().getResource("Jrxml/test.jasper").getFile());
        System.out.println(inputFile.getAbsolutePath());

        System.out.println(reportStream.getFile());

        System.out.println("jrxml ada isinya");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(reportStream.openStream()))) {
            String line;
            StringBuilder str = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                str.append(line);
                str.append("\n");
            }
            textJrxml.setText(str.toString());
        } catch (IOException e) {
            System.out.println("Error membaca file: " + e.getMessage());
        }


        try {
            String filePath = new File(reportStream.toURI()).getAbsolutePath();
            System.out.println("alamat test : "+filePath);
            JasperReport jasperReport = JasperCompileManager.compileReport(filePath);
            Map<String, Object> params = new HashMap<>();
            params.put("DATA_SOURCE1", "Andrea Alfian");
            ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(ObjectSaver.jsonData.getBytes(StandardCharsets.UTF_8));
            JRDataSource dataSource = new JsonDataSource(jsonDataStream);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\andre\\JaspersoftWorkspace\\MyReports\\hasil.pdf");
            System.out.println("persamaan");

            System.out.println("Report generated successfully.");

        } catch (JRException | URISyntaxException e) {
            System.out.println("gagal : "+ e.getMessage() );
            throw new RuntimeException(e);
        }

        // preview pdf on anchorPane
        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox();
        scrollPane.setContent(content);
        anchorPanePreview.getChildren().add(scrollPane);

        try {
            File file = new File("C:\\Users\\andre\\JaspersoftWorkspace\\MyReports\\hasil.pdf");
            PDDocument document = PDDocument.load(file);
            PDFRenderer pdfRenderer = new PDFRenderer(document);


            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 100);
                WritableImage fxImage = convertToFXImage(bufferedImage);
                ImageView imageView = new ImageView(fxImage);
                content.getChildren().add(imageView);
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static WritableImage convertToFXImage(BufferedImage image) {
        WritableImage wr = new WritableImage(image.getWidth(), image.getHeight());;
        wr = new WritableImage(image.getWidth(), image.getHeight());
        PixelWriter pw = wr.getPixelWriter();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pw.setArgb(x, y, image.getRGB(x, y));
            }
        }
        return wr;
    }
}
