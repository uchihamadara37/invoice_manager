package com.andre.dojo.invoicemanager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.export.Graphics2DExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleGraphics2DExporterOutput;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

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
    private AnchorPane anchorPanePreview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        try {
//            InputStream reportStream = getClass().getResourceAsStream("Jrxml/sample.jrxml");
//            JasperReport jasperReport = JasperCompileManager.compileReport("/home/andre/Documents/invoiceManager/src/main/resources/com/andre/dojo/invoicemanager/Jrxml/sample.jrxml");
//            Map<String, Object> parameters = new HashMap<>();
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());
//
//            BufferedImage image = new BufferedImage(595, 842, BufferedImage.TYPE_INT_RGB);
//            JRGraphics2DExporter exporter = new JRGraphics2DExporter();
//            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//
//            SimpleGraphics2DExporterOutput output = new SimpleGraphics2DExporterOutput();
//            output.setGraphics2D(image.createGraphics());
//            exporter.setExporterOutput(output);
//
//            exporter.exportReport();
//
//            // Convert BufferedImage to JavaFX Image
//            javafx.scene.image.Image fxImage = convertToFXImage(image);
//
//            ImageView imageView = new ImageView(fxImage);
//            anchorPanePreview.getChildren().add(imageView);
//        } catch (JRException e) {
//            throw new RuntimeException(e);
//        }
        InputStream reportStream = getClass().getResourceAsStream("Jrxml/sample.jrxml");
//        System.out.println(getClass().getResource("Jrxml/sample.jrxml"));
        File reportFile = new File(getClass().getResource("Jrxml/sample.jrxml").getFile());
        System.out.println(reportFile);

        if (reportStream == null){
            System.out.println("jrxml masih kosong");
        }else{
            System.out.println("jrxml ada isinya");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(reportStream))) {
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
            // show and compile from jasper file.
            try {
                // Kompilasi JRXML menjadi JasperReport
                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
                System.out.println("JRXML berhasil dikompilasi menjadi JasperReport");

                // Opsional: Simpan JasperReport ke file .jasper
                JasperCompileManager.compileReportToFile("Jrxml/sample.jrxml", "output/sample.jasper");
                System.out.println("JasperReport berhasil disimpan ke file .jasper");

            } catch (JRException e) {
                System.out.println("Error saat mengompilasi JRXML: " + e.getMessage());

            }
        }
//        System.out.println(System.getProperty("java.class.path"));
    }

    public static javafx.scene.image.Image convertToFXImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }
        return wr;
    }
}
