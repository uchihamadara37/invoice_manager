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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        try {
            InputStream reportStream = getClass().getResourceAsStream("Jrxml/sample.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport("/home/andre/Documents/invoiceManager/src/main/resources/com/andre/dojo/invoicemanager/Jrxml/sample.jrxml");
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());

            BufferedImage image = new BufferedImage(595, 842, BufferedImage.TYPE_INT_RGB);
            JRGraphics2DExporter exporter = new JRGraphics2DExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

            SimpleGraphics2DExporterOutput output = new SimpleGraphics2DExporterOutput();
            output.setGraphics2D(image.createGraphics());
            exporter.setExporterOutput(output);

            exporter.exportReport();

            // Convert BufferedImage to JavaFX Image
            javafx.scene.image.Image fxImage = convertToFXImage(image);

            ImageView imageView = new ImageView(fxImage);
            anchorPanePreview.getChildren().add(imageView);
        } catch (JRException e) {
            throw new RuntimeException(e);
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
