package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Design;
import com.andre.dojo.Models.Invoice;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ExportController {

    @FXML
    private AnchorPane anchorPaneMain;
    @FXML
    private TableView<Invoice> tableViewExport;
    @FXML
    private TableColumn<Invoice, String> tableColumnCode;
    @FXML
    private TableColumn<Invoice, String> tableColumnDesc;
    @FXML
    private TableColumn<Invoice, String> tableColumnCustomer;
    @FXML
    private TableColumn<Invoice, String> tableColumnTotalItem;
    @FXML
    private Pane export;
    @FXML
    private Label total;
    @FXML
    private Label prepare;

    public ObservableList<Invoice> dataExport = FXCollections.observableArrayList();
    public String message;
    private ExportPrepareController exportPrepareController;

    public void setExportPrepareController(ExportPrepareController exportPrepareController) {
        this.exportPrepareController = exportPrepareController;
    }

    public void setDataExport(ObservableList<Invoice> dataExport) {
        this.dataExport = dataExport;
    }

    public ObservableList<Invoice> getDataExport() {
        return dataExport;
    }

    public void initialize(){
        export.setOnMouseClicked(e -> {
//            insertDesign();
            try {
                toPDF();
            } catch (JRException ex) {
                throw new RuntimeException(ex);
            }
        });

        prepare.setOnMouseClicked(e -> {
            openPreparePane();
        });
    }

    public void customInitialize(){
        loadTableView();
        total.setText(String.valueOf(dataExport.size()));
        System.out.println("hasil pesan dari hello : " + message);
//        insertDesign();
    }

    public void loadTableView() {
        tableViewExport.setEditable(true);
        tableViewExport.setItems(FXCollections.observableArrayList(dataExport));
        tableColumnCode.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceCode()));
        tableColumnDesc.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnCustomer.setCellValueFactory(e -> new SimpleStringProperty(Long.toString(e.getValue().getCustomer_id())));
        tableColumnTotalItem.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceMarkText()));
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private void toPDF() throws JRException {
        String cek = "1724503671986", cek2 = "1724897200823";
        for (Invoice invoice : dataExport) {
            long id = invoice.getId(); // Assuming the id is an int and there's a getId() method
            if(id == Long.parseLong(cek2)){
                System.out.println("Invoice ID: " + id);
                System.out.println("with jxrml : " + Design.getOneData(invoice.getJrxml_id()));
                Design design = Design.getOneData(invoice.getJrxml_id());
                String jrxmlContent = design.getJrxml();
                ByteArrayInputStream jrxmlInputStream = new ByteArrayInputStream(jrxmlContent.getBytes(StandardCharsets.UTF_8));
                JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInputStream);
                // Prepare a Map-based data source, ensuring all required fields are present
                Map<String, Object> data = new HashMap<>();
                JRDataSource dataSource = new JRMapArrayDataSource(new Map[] { data });
                Map<String, Object> parameters = new HashMap<>();
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
                JasperViewer viewer = new JasperViewer(jasperPrint, false);
                viewer.setVisible(true);
            }
        }
        dataExport.clear();
        total.setText(String.valueOf(dataExport.size()));
        loadTableView();
    }

    private void openPreparePane() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("export-prepare-view.fxml"));
            Pane anchorPane = new Pane((Node) fxmlLoader.load());

            ExportPrepareController test = fxmlLoader.getController();
            test.setExportController(this);

            anchorPaneMain.getChildren().removeFirst();
            anchorPaneMain.getChildren().add(anchorPane);
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void insertDesign(){
        Design.addToDB(new Design(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<!-- Created with Jaspersoft Studio version 6.21.3.final using JasperReports Library version 6.21.3-4a3078d20785ebe464f18037d738d12fc98c13cf  -->\n" +
                        "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" name=\"InvoceFix\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\" uuid=\"a459e462-d98f-4aa8-bc92-a240ca6fdc71\">\n" +
                        "\t<property name=\"com.jaspersoft.studio.data.defaultdataadapter\" value=\"New Data Adapter\"/>\n" +
                        "\t<style name=\"Table_TH\" mode=\"Opaque\" backcolor=\"#F0F8FF\">\n" +
                        "\t\t<box>\n" +
                        "\t\t\t<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t</box>\n" +
                        "\t</style>\n" +
                        "\t<style name=\"Table_CH\" mode=\"Opaque\" backcolor=\"#BFE1FF\">\n" +
                        "\t\t<box>\n" +
                        "\t\t\t<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t</box>\n" +
                        "\t</style>\n" +
                        "\t<style name=\"Table_TD\" mode=\"Opaque\" backcolor=\"#FFFFFF\">\n" +
                        "\t\t<box>\n" +
                        "\t\t\t<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t</box>\n" +
                        "\t</style>\n" +
                        "\t<subDataset name=\"Dataset1\" uuid=\"6ec154ec-dcea-4380-b27c-a26b4ac4fa62\">\n" +
                        "\t\t<property name=\"com.jaspersoft.studio.data.defaultdataadapter\" value=\"New Data Adapter (2)\"/>\n" +
                        "\t\t<queryString language=\"json\">\n" +
                        "\t\t\t<![CDATA[invoice.listItem]]>\n" +
                        "\t\t</queryString>\n" +
                        "\t\t<field name=\"id\" class=\"java.lang.String\">\n" +
                        "\t\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"id\"/>\n" +
                        "\t\t\t<fieldDescription><![CDATA[id]]></fieldDescription>\n" +
                        "\t\t</field>\n" +
                        "\t\t<field name=\"name\" class=\"java.lang.String\">\n" +
                        "\t\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"name\"/>\n" +
                        "\t\t\t<fieldDescription><![CDATA[name]]></fieldDescription>\n" +
                        "\t\t</field>\n" +
                        "\t\t<field name=\"price\" class=\"java.lang.String\">\n" +
                        "\t\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"price\"/>\n" +
                        "\t\t\t<fieldDescription><![CDATA[price]]></fieldDescription>\n" +
                        "\t\t</field>\n" +
                        "\t\t<field name=\"qty\" class=\"java.lang.String\">\n" +
                        "\t\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"qty\"/>\n" +
                        "\t\t\t<fieldDescription><![CDATA[qty]]></fieldDescription>\n" +
                        "\t\t</field>\n" +
                        "\t\t<field name=\"totalPriceItem\" class=\"java.lang.String\">\n" +
                        "\t\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"totalPriceItem\"/>\n" +
                        "\t\t\t<fieldDescription><![CDATA[totalPriceItem]]></fieldDescription>\n" +
                        "\t\t</field>\n" +
                        "\t</subDataset>\n" +
                        "\t<queryString language=\"json\">\n" +
                        "\t\t<![CDATA[]]>\n" +
                        "\t</queryString>\n" +
                        "\t<field name=\"invoiceCode\" class=\"java.lang.String\">\n" +
                        "\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"invoice.invoiceCode\"/>\n" +
                        "\t\t<fieldDescription><![CDATA[invoice.invoiceCode]]></fieldDescription>\n" +
                        "\t</field>\n" +
                        "\t<field name=\"date\" class=\"java.lang.String\">\n" +
                        "\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"invoice.date\"/>\n" +
                        "\t\t<fieldDescription><![CDATA[invoice.date]]></fieldDescription>\n" +
                        "\t</field>\n" +
                        "\t<field name=\"name\" class=\"java.lang.String\">\n" +
                        "\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"customer.name\"/>\n" +
                        "\t\t<fieldDescription><![CDATA[customer.name]]></fieldDescription>\n" +
                        "\t</field>\n" +
                        "\t<field name=\"description\" class=\"java.lang.String\">\n" +
                        "\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"customer.description\"/>\n" +
                        "\t\t<fieldDescription><![CDATA[customer.description]]></fieldDescription>\n" +
                        "\t</field>\n" +
                        "\t<field name=\"listItem\" class=\"java.lang.String\">\n" +
                        "\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"invoice.listItem\"/>\n" +
                        "\t\t<fieldDescription><![CDATA[invoice.listItem]]></fieldDescription>\n" +
                        "\t</field>\n" +
                        "\t<background>\n" +
                        "\t\t<band splitType=\"Stretch\"/>\n" +
                        "\t</background>\n" +
                        "\t<title>\n" +
                        "\t\t<band height=\"86\" splitType=\"Stretch\">\n" +
                        "\t\t\t<line>\n" +
                        "\t\t\t\t<reportElement x=\"0\" y=\"80\" width=\"556\" height=\"1\" uuid=\"826097c6-ddf3-4d12-bd8b-ac016d807f8c\">\n" +
                        "\t\t\t\t\t<property name=\"local_mesure_unitheight\" value=\"pixel\"/>\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\n" +
                        "\t\t\t\t</reportElement>\n" +
                        "\t\t\t\t<graphicElement>\n" +
                        "\t\t\t\t\t<pen lineWidth=\"2.0\" lineColor=\"#48B8BA\"/>\n" +
                        "\t\t\t\t</graphicElement>\n" +
                        "\t\t\t</line>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement x=\"382\" y=\"20\" width=\"60\" height=\"20\" uuid=\"00eabf94-8a8c-4ae1-8222-2b7813eaad70\"/>\n" +
                        "\t\t\t\t<box rightPadding=\"4\"/>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Left\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Nomor\t   :]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<image hAlign=\"Center\">\n" +
                        "\t\t\t\t<reportElement x=\"0\" y=\"10\" width=\"65\" height=\"60\" uuid=\"1accd4fe-7d17-4988-87c5-92cd88483784\"/>\n" +
                        "\t\t\t\t<imageExpression><![CDATA[\"D:/Downloads/WhatsApp Image 2024-08-02 at 16.24.24_e389c7d3.jpg\"]]></imageExpression>\n" +
                        "\t\t\t</image>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement x=\"382\" y=\"40\" width=\"60\" height=\"20\" uuid=\"55ffa9bf-74f9-45a0-b519-0a11537db91b\"/>\n" +
                        "\t\t\t\t<box rightPadding=\"4\"/>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Left\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Tanggal  :]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement x=\"80\" y=\"15\" width=\"170\" height=\"50\" forecolor=\"#46B8BA\" uuid=\"820b2599-2b3d-4655-9935-5c27da542542\">\n" +
                        "\t\t\t\t\t<property name=\"local_mesure_unitheight\" value=\"pixel\"/>\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\n" +
                        "\t\t\t\t</reportElement>\n" +
                        "\t\t\t\t<textElement verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"37\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[INVOICE]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<line>\n" +
                        "\t\t\t\t<reportElement x=\"0\" y=\"0\" width=\"556\" height=\"1\" uuid=\"d75f8260-d349-41c1-867c-e2542ad7fd92\">\n" +
                        "\t\t\t\t\t<property name=\"local_mesure_unitheight\" value=\"pixel\"/>\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\n" +
                        "\t\t\t\t</reportElement>\n" +
                        "\t\t\t\t<graphicElement>\n" +
                        "\t\t\t\t\t<pen lineWidth=\"2.0\" lineColor=\"#48B8BA\"/>\n" +
                        "\t\t\t\t</graphicElement>\n" +
                        "\t\t\t</line>\n" +
                        "\t\t\t<textField>\n" +
                        "\t\t\t\t<reportElement x=\"444\" y=\"20\" width=\"100\" height=\"20\" uuid=\"c838adc9-27bb-4aef-ae65-2c43071a4c99\"/>\n" +
                        "\t\t\t\t<textElement verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font size=\"12\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<textFieldExpression><![CDATA[$F{invoiceCode}]]></textFieldExpression>\n" +
                        "\t\t\t</textField>\n" +
                        "\t\t\t<textField>\n" +
                        "\t\t\t\t<reportElement x=\"444\" y=\"40\" width=\"100\" height=\"20\" uuid=\"489bd084-4369-460a-b4bc-feac50d5eb33\"/>\n" +
                        "\t\t\t\t<textElement verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font size=\"12\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<textFieldExpression><![CDATA[$F{date}]]></textFieldExpression>\n" +
                        "\t\t\t</textField>\n" +
                        "\t\t</band>\n" +
                        "\t</title>\n" +
                        "\t<pageHeader>\n" +
                        "\t\t<band height=\"70\" splitType=\"Stretch\">\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement x=\"0\" y=\"3\" width=\"120\" height=\"19\" uuid=\"0117616f-f468-4a74-b2e3-2567626f0837\"/>\n" +
                        "\t\t\t\t<textElement>\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Ditunjukkan Kepada :]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<textField>\n" +
                        "\t\t\t\t<reportElement x=\"65\" y=\"20\" width=\"100\" height=\"20\" uuid=\"f6d510da-fcc5-4cfb-86e7-c29e4551f528\"/>\n" +
                        "\t\t\t\t<textElement verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font size=\"12\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<textFieldExpression><![CDATA[$F{name}]]></textFieldExpression>\n" +
                        "\t\t\t</textField>\n" +
                        "\t\t\t<textField>\n" +
                        "\t\t\t\t<reportElement x=\"65\" y=\"40\" width=\"100\" height=\"20\" uuid=\"47a56d1b-5e05-4c6e-8210-d9ec9cdb28da\"/>\n" +
                        "\t\t\t\t<textElement verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font size=\"12\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<textFieldExpression><![CDATA[$F{description}]]></textFieldExpression>\n" +
                        "\t\t\t</textField>\n" +
                        "\t\t</band>\n" +
                        "\t</pageHeader>\n" +
                        "\t<columnHeader>\n" +
                        "\t\t<band height=\"61\" splitType=\"Stretch\"/>\n" +
                        "\t</columnHeader>\n" +
                        "\t<detail>\n" +
                        "\t\t<band height=\"271\" splitType=\"Stretch\">\n" +
                        "\t\t\t<componentElement>\n" +
                        "\t\t\t\t<reportElement x=\"-1\" y=\"0\" width=\"556\" height=\"200\" uuid=\"97b56d5d-0751-4da4-8bcc-87c97e3bd5ce\">\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.layout\" value=\"com.jaspersoft.studio.editor.layout.VerticalRowLayout\"/>\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.table.style.table_header\" value=\"Table_TH\"/>\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.table.style.column_header\" value=\"Table_CH\"/>\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.table.style.detail\" value=\"Table_TD\"/>\n" +
                        "\t\t\t\t</reportElement>\n" +
                        "\t\t\t\t<jr:table xmlns:jr=\"http://jasperreports.sourceforge.net/jasperreports/components\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports/components http://jasperreports.sourceforge.net/xsd/components.xsd\">\n" +
                        "\t\t\t\t\t<datasetRun subDataset=\"Dataset1\" uuid=\"5a2f3e12-3ea0-42d3-ac74-95db45a92803\">\n" +
                        "\t\t\t\t\t\t<dataSourceExpression><![CDATA[new net.sf.jasperreports.engine.data.JsonDataSource(new ByteArrayInputStream($F{listItem}.getBytes()))]]></dataSourceExpression>\n" +
                        "\t\t\t\t\t</datasetRun>\n" +
                        "\t\t\t\t\t<jr:column width=\"40\" uuid=\"c7bb488a-4bc3-4de3-a5f6-5e915da86ccb\">\n" +
                        "\t\t\t\t\t\t<jr:columnHeader style=\"Table_CH\" height=\"30\">\n" +
                        "\t\t\t\t\t\t\t<staticText>\n" +
                        "\t\t\t\t\t\t\t\t<reportElement x=\"0\" y=\"0\" width=\"40\" height=\"30\" uuid=\"f5a49c1b-8825-49ab-9b90-b5d688438637\"/>\n" +
                        "\t\t\t\t\t\t\t\t<text><![CDATA[id]]></text>\n" +
                        "\t\t\t\t\t\t\t</staticText>\n" +
                        "\t\t\t\t\t\t</jr:columnHeader>\n" +
                        "\t\t\t\t\t\t<jr:detailCell style=\"Table_TD\" height=\"30\">\n" +
                        "\t\t\t\t\t\t\t<textField>\n" +
                        "\t\t\t\t\t\t\t\t<reportElement x=\"0\" y=\"0\" width=\"40\" height=\"30\" uuid=\"608a4c86-ca53-45b7-ab27-75e63b98b956\"/>\n" +
                        "\t\t\t\t\t\t\t\t<textFieldExpression><![CDATA[$F{id}]]></textFieldExpression>\n" +
                        "\t\t\t\t\t\t\t</textField>\n" +
                        "\t\t\t\t\t\t</jr:detailCell>\n" +
                        "\t\t\t\t\t</jr:column>\n" +
                        "\t\t\t\t\t<jr:column width=\"80\" uuid=\"3ec7acc0-9ef8-4aba-bd6f-e39700387e39\">\n" +
                        "\t\t\t\t\t\t<jr:columnHeader style=\"Table_CH\" height=\"30\">\n" +
                        "\t\t\t\t\t\t\t<staticText>\n" +
                        "\t\t\t\t\t\t\t\t<reportElement x=\"0\" y=\"0\" width=\"80\" height=\"30\" uuid=\"ba0a3d04-72d9-4189-87c0-fa5c0040b695\"/>\n" +
                        "\t\t\t\t\t\t\t\t<text><![CDATA[name]]></text>\n" +
                        "\t\t\t\t\t\t\t</staticText>\n" +
                        "\t\t\t\t\t\t</jr:columnHeader>\n" +
                        "\t\t\t\t\t\t<jr:detailCell style=\"Table_TD\" height=\"30\">\n" +
                        "\t\t\t\t\t\t\t<textField>\n" +
                        "\t\t\t\t\t\t\t\t<reportElement x=\"0\" y=\"0\" width=\"80\" height=\"30\" uuid=\"82745347-cb8d-4f37-aaa9-11307f82c3cb\"/>\n" +
                        "\t\t\t\t\t\t\t\t<textFieldExpression><![CDATA[$F{name}]]></textFieldExpression>\n" +
                        "\t\t\t\t\t\t\t</textField>\n" +
                        "\t\t\t\t\t\t</jr:detailCell>\n" +
                        "\t\t\t\t\t</jr:column>\n" +
                        "\t\t\t\t\t<jr:column width=\"130\" uuid=\"c62b8f8f-069a-46e4-b21f-edac8300f9b6\">\n" +
                        "\t\t\t\t\t\t<jr:columnHeader style=\"Table_CH\" height=\"30\">\n" +
                        "\t\t\t\t\t\t\t<staticText>\n" +
                        "\t\t\t\t\t\t\t\t<reportElement x=\"0\" y=\"0\" width=\"130\" height=\"30\" uuid=\"e7363c10-9f18-4e1d-b102-f3d986aee407\"/>\n" +
                        "\t\t\t\t\t\t\t\t<text><![CDATA[price]]></text>\n" +
                        "\t\t\t\t\t\t\t</staticText>\n" +
                        "\t\t\t\t\t\t</jr:columnHeader>\n" +
                        "\t\t\t\t\t\t<jr:detailCell style=\"Table_TD\" height=\"30\">\n" +
                        "\t\t\t\t\t\t\t<textField>\n" +
                        "\t\t\t\t\t\t\t\t<reportElement x=\"0\" y=\"0\" width=\"130\" height=\"30\" uuid=\"1bc84fa4-f965-4bff-86f5-734831de4b44\"/>\n" +
                        "\t\t\t\t\t\t\t\t<textFieldExpression><![CDATA[$F{price}]]></textFieldExpression>\n" +
                        "\t\t\t\t\t\t\t</textField>\n" +
                        "\t\t\t\t\t\t</jr:detailCell>\n" +
                        "\t\t\t\t\t</jr:column>\n" +
                        "\t\t\t\t\t<jr:column width=\"140\" uuid=\"0b10bf02-2d00-441a-ad30-0a21824dd9fc\">\n" +
                        "\t\t\t\t\t\t<jr:columnHeader style=\"Table_CH\" height=\"30\">\n" +
                        "\t\t\t\t\t\t\t<staticText>\n" +
                        "\t\t\t\t\t\t\t\t<reportElement x=\"0\" y=\"0\" width=\"140\" height=\"30\" uuid=\"80382420-6796-4916-b22d-571592d7a666\"/>\n" +
                        "\t\t\t\t\t\t\t\t<text><![CDATA[qty]]></text>\n" +
                        "\t\t\t\t\t\t\t</staticText>\n" +
                        "\t\t\t\t\t\t</jr:columnHeader>\n" +
                        "\t\t\t\t\t\t<jr:detailCell style=\"Table_TD\" height=\"30\">\n" +
                        "\t\t\t\t\t\t\t<textField>\n" +
                        "\t\t\t\t\t\t\t\t<reportElement x=\"0\" y=\"0\" width=\"140\" height=\"30\" uuid=\"0f5d8b6c-3a5f-463f-90a8-311d6adf1d82\"/>\n" +
                        "\t\t\t\t\t\t\t\t<textFieldExpression><![CDATA[$F{qty}]]></textFieldExpression>\n" +
                        "\t\t\t\t\t\t\t</textField>\n" +
                        "\t\t\t\t\t\t</jr:detailCell>\n" +
                        "\t\t\t\t\t</jr:column>\n" +
                        "\t\t\t\t\t<jr:column width=\"160\" uuid=\"7da8e2bf-fd15-4adc-b2e7-24fdd5c959a8\">\n" +
                        "\t\t\t\t\t\t<jr:columnHeader style=\"Table_CH\" height=\"30\">\n" +
                        "\t\t\t\t\t\t\t<staticText>\n" +
                        "\t\t\t\t\t\t\t\t<reportElement x=\"0\" y=\"0\" width=\"160\" height=\"30\" uuid=\"3cde91b7-1d75-4712-a13f-251dda69e53f\"/>\n" +
                        "\t\t\t\t\t\t\t\t<text><![CDATA[totalPriceItem]]></text>\n" +
                        "\t\t\t\t\t\t\t</staticText>\n" +
                        "\t\t\t\t\t\t</jr:columnHeader>\n" +
                        "\t\t\t\t\t\t<jr:detailCell style=\"Table_TD\" height=\"30\">\n" +
                        "\t\t\t\t\t\t\t<textField>\n" +
                        "\t\t\t\t\t\t\t\t<reportElement x=\"0\" y=\"0\" width=\"160\" height=\"30\" uuid=\"3d110016-5d34-4ba2-a09f-e23f48f83f8e\"/>\n" +
                        "\t\t\t\t\t\t\t\t<textFieldExpression><![CDATA[$F{totalPriceItem}]]></textFieldExpression>\n" +
                        "\t\t\t\t\t\t\t</textField>\n" +
                        "\t\t\t\t\t\t</jr:detailCell>\n" +
                        "\t\t\t\t\t</jr:column>\n" +
                        "\t\t\t\t</jr:table>\n" +
                        "\t\t\t</componentElement>\n" +
                        "\t\t</band>\n" +
                        "\t</detail>\n" +
                        "\t<columnFooter>\n" +
                        "\t\t<band height=\"45\" splitType=\"Stretch\"/>\n" +
                        "\t</columnFooter>\n" +
                        "\t<lastPageFooter>\n" +
                        "\t\t<band height=\"220\">\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement positionType=\"Float\" mode=\"Opaque\" x=\"-1\" y=\"190\" width=\"556\" height=\"30\" backcolor=\"#E6E8E9\" uuid=\"3f581135-f514-420b-a71a-9baef6426796\"/>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"Serif\" size=\"9\" isItalic=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Terimakasih atas pesanannya]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement x=\"430\" y=\"155\" width=\"120\" height=\"35\" uuid=\"15e1aed8-8a74-49e4-88ee-226a72ccb8fc\"/>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Right\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Joko Munandar\n" +
                        "Seigan Technology]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement x=\"-1\" y=\"0\" width=\"340\" height=\"38\" uuid=\"c1de8656-5315-4779-bed0-7085a4b40021\"/>\n" +
                        "\t\t\t\t<textElement>\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"false\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Tagihan dapat ditransfer ke rekening :\n" +
                        "BSI (Bank Syariah Indonesia) 4828625660 a.n Joko Munandar]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t</band>\n" +
                        "\t</lastPageFooter>\n" +
                        "</jasperReport>\n","",""
        ));
    }
}
