package com.andre.dojo.invoicemanager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.Graphics2DExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleGraphics2DExporterOutput;
import net.sf.jasperreports.pdf.common.PdfDocument;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
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
//            InputStream reportStream = getClass().getResourceAsStream("Jrxml/Invoice.jrxml");
//            JasperReport jasperReport = JasperCompileManager.compileReport("/home/andre/Documents/invoiceManager/src/main/resources/com/andre/dojo/invoicemanager/Jrxml/Invoice.jrxml");
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
        InputStream reportStream = getClass().getResourceAsStream("Jrxml/Invoice.jrxml");
//        System.out.println(getClass().getResource("Jrxml/Invoice.jrxml"));
        File reportFile = new File(getClass().getResource("Jrxml/Invoice.jrxml").getFile());
        File exportFile = new File(getClass().getResource("Jrxml/Blank_A4.jasper").getFile());
        System.out.println(reportFile.getAbsolutePath());

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

//            try {
//                long start = System.currentTimeMillis();
//                JasperCompileManager.compileReportToFile(reportFile.toString(), exportFile.toString());
//                long end = System.currentTimeMillis();
//                System.out.println("Kompilasi selesai dalam " + (end - start) + " ms");
//                System.out.println("File .jasper tersimpan di: " + exportFile);
//            } catch (JRException e) {
//                System.out.println("Error saat kompilasi: " + e.getMessage());
//                e.printStackTrace();
//            }

            try {
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(exportFile);
                System.out.println("jare berhasil .jasper");

                JasperPrint jrPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());
                byte[] pdfByte = JasperExportManager.exportReportToPdf(jrPrint);

                BufferedImage bufferedImage = new BufferedImage(
                        jrPrint.getPageWidth(),
                        jrPrint.getPageHeight(),
                        BufferedImage.TYPE_INT_RGB
                );

//                JRGraphics2DExporter exporter = new JRGraphics2DExporter();
//                exporter.setExporterInput(new SimpleExporterInput(jrPrint));
//                exporter.setExporterOutput(new SimpleGraphics2DExporterOutput());
//                exporter.exportReport();

                // Convert BufferedImage to JavaFX Image
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", outputStream);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

                WritableImage fxImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
                PixelWriter pixelWriter = fxImage.getPixelWriter();

                for (int x = 0; x < bufferedImage.getWidth(); x++) {
                    for (int y = 0; y < bufferedImage.getHeight(); y++) {
                        pixelWriter.setArgb(x, y, bufferedImage.getRGB(x, y));
                    }
                }

                ImageView imageView = new ImageView(fxImage);

                // Add canvas to AnchorPane
                anchorPanePreview.getChildren().add(imageView);
                AnchorPane.setTopAnchor(imageView, 0.0);
                AnchorPane.setLeftAnchor(imageView, 0.0);



                System.out.println("Report generated successfully.");

            } catch (JRException e) {
                System.out.println("gagal : "+ e.getMessage() );
//                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//             show and compile from jasper file.
//            try {
//                JasperReportsContext jrContext = DefaultJasperReportsContext.getInstance();
//                JRXmlLoader jrXmlLoader = new JRXmlLoader(jrContext, JRXmlLoader);
//                JasperDesign jasperDesign = jrXmlLoader.loadXML(reportStream);
//                System.out.println("berhasil mencetak gambar");
//
//
//                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
//                System.out.println("JRXML berhasil dikompilasi menjadi JasperReport");
//
//
//            } catch (JRException e) {
//                System.out.println("Error saat mengompilasi JRXML: " + e.getMessage());
////                e.printStackTrace();
//            }
        }
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
