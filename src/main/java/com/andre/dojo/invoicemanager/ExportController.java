package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Design;
import com.andre.dojo.Models.Invoice;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ExportController {

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

    public ObservableList<Invoice> dataExport = FXCollections.observableArrayList();
    public String message;

    public void setDataExport(ObservableList<Invoice> dataExport) {
        this.dataExport = dataExport;
    }

    public ObservableList<Invoice> getDataExport() {
        return dataExport;
    }

    public void initialize(){
        export.setOnMouseClicked(e -> {
            try {
                toPDF();
            } catch (JRException ex) {
                throw new RuntimeException(ex);
            }
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
        String cek = "1724503671986", cek2 = "1724656789071";
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
//                data.put("nomorSurat", null);
//                data.put("tanggal", null);
//                data.put("tujuan", null);
//                data.put("keterangan", null);
//                data.put("totalHarga", null);

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

    private void insertDesign(){
        Design.addToDB(new Design(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<!-- Created with Jaspersoft Studio version 6.21.3.final using JasperReports Library version 6.21.3-4a3078d20785ebe464f18037d738d12fc98c13cf  -->\n" +
                        "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" name=\"Invoice_1\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"535\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\" uuid=\"4eedbb89-b4f6-4469-9ab6-f642a1688cf7\">\n" +
                        "\t<property name=\"com.jaspersoft.studio.data.defaultdataadapter\" value=\"JsonInvoice\"/>\n" +
                        "\t<style name=\"Title\" forecolor=\"#FFFFFF\" fontName=\"Times New Roman\" fontSize=\"50\" isBold=\"false\" pdfFontName=\"Times-Bold\"/>\n" +
                        "\t<style name=\"SubTitle\" forecolor=\"#CCCCCC\" fontName=\"Times New Roman\" fontSize=\"18\" isBold=\"false\" pdfFontName=\"Times-Roman\"/>\n" +
                        "\t<style name=\"Column header\" forecolor=\"#666666\" fontName=\"Times New Roman\" fontSize=\"14\" isBold=\"true\"/>\n" +
                        "\t<style name=\"Detail\" mode=\"Transparent\" fontName=\"Times New Roman\"/>\n" +
                        "\t<style name=\"Row\" mode=\"Transparent\" fontName=\"Times New Roman\" pdfFontName=\"Times-Roman\">\n" +
                        "\t\t<conditionalStyle>\n" +
                        "\t\t\t<conditionExpression><![CDATA[$V{REPORT_COUNT}%2 == 0]]></conditionExpression>\n" +
                        "\t\t\t<style mode=\"Opaque\" backcolor=\"#EEEFF0\"/>\n" +
                        "\t\t</conditionalStyle>\n" +
                        "\t</style>\n" +
                        "\t<style name=\"Table\">\n" +
                        "\t\t<box>\n" +
                        "\t\t\t<pen lineWidth=\"1.0\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<topPen lineWidth=\"1.0\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<leftPen lineWidth=\"1.0\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<bottomPen lineWidth=\"1.0\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<rightPen lineWidth=\"1.0\" lineColor=\"#000000\"/>\n" +
                        "\t\t</box>\n" +
                        "\t</style>\n" +
                        "\t<style name=\"Table_TH\" mode=\"Opaque\" backcolor=\"#FFFFFF\">\n" +
                        "\t\t<box>\n" +
                        "\t\t\t<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t</box>\n" +
                        "\t</style>\n" +
                        "\t<style name=\"Table_CH\" mode=\"Opaque\" backcolor=\"#CACED0\">\n" +
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
                        "\t\t<conditionalStyle>\n" +
                        "\t\t\t<conditionExpression><![CDATA[$V{REPORT_COUNT}%2 == 0]]></conditionExpression>\n" +
                        "\t\t\t<style backcolor=\"#D8D8D8\"/>\n" +
                        "\t\t</conditionalStyle>\n" +
                        "\t</style>\n" +
                        "\t<style name=\"Table 1_TD\" mode=\"Opaque\" backcolor=\"#FFFFFF\">\n" +
                        "\t\t<box>\n" +
                        "\t\t\t<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t</box>\n" +
                        "\t</style>\n" +
                        "\t<style name=\"Table 1_TH\" mode=\"Opaque\" backcolor=\"#F0F8FF\">\n" +
                        "\t\t<box>\n" +
                        "\t\t\t<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t</box>\n" +
                        "\t</style>\n" +
                        "\t<style name=\"Table 1_CH\" mode=\"Opaque\" backcolor=\"#BFE1FF\">\n" +
                        "\t\t<box>\n" +
                        "\t\t\t<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t\t<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\n" +
                        "\t\t</box>\n" +
                        "\t</style>\n" +
                        "\t<subDataset name=\"tableDataset\" uuid=\"f13e6d36-5148-4ecc-bbe3-3035def80980\">\n" +
                        "\t\t<queryString>\n" +
                        "\t\t\t<![CDATA[]]>\n" +
                        "\t\t</queryString>\n" +
                        "\t</subDataset>\n" +
                        "\t<queryString language=\"JSON\">\n" +
                        "\t\t<![CDATA[data.pesanan]]>\n" +
                        "\t</queryString>\n" +
                        "\t<field name=\"no\" class=\"java.lang.Integer\">\n" +
                        "\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"no\"/>\n" +
                        "\t\t<fieldDescription><![CDATA[no]]></fieldDescription>\n" +
                        "\t</field>\n" +
                        "\t<field name=\"item\" class=\"java.lang.String\">\n" +
                        "\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"item\"/>\n" +
                        "\t\t<fieldDescription><![CDATA[item]]></fieldDescription>\n" +
                        "\t</field>\n" +
                        "\t<field name=\"harga\" class=\"java.lang.Integer\">\n" +
                        "\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"harga\"/>\n" +
                        "\t\t<fieldDescription><![CDATA[harga]]></fieldDescription>\n" +
                        "\t</field>\n" +
                        "\t<field name=\"qty\" class=\"java.lang.Integer\">\n" +
                        "\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"qty\"/>\n" +
                        "\t\t<fieldDescription><![CDATA[qty]]></fieldDescription>\n" +
                        "\t</field>\n" +
                        "\t<field name=\"total\" class=\"java.lang.Integer\">\n" +
                        "\t\t<property name=\"net.sf.jasperreports.json.field.expression\" value=\"total\"/>\n" +
                        "\t\t<fieldDescription><![CDATA[total]]></fieldDescription>\n" +
                        "\t</field>\n" +
                        "\t<title>\n" +
                        "\t\t<band height=\"89\" splitType=\"Stretch\">\n" +
                        "\t\t\t<line>\n" +
                        "\t\t\t\t<reportElement x=\"0\" y=\"80\" width=\"556\" height=\"1\" uuid=\"806ce5df-1219-4876-ae0c-ca7405b1f246\">\n" +
                        "\t\t\t\t\t<property name=\"local_mesure_unitheight\" value=\"pixel\"/>\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\n" +
                        "\t\t\t\t</reportElement>\n" +
                        "\t\t\t\t<graphicElement>\n" +
                        "\t\t\t\t\t<pen lineWidth=\"2.0\" lineColor=\"#48B8BA\"/>\n" +
                        "\t\t\t\t</graphicElement>\n" +
                        "\t\t\t</line>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement x=\"382\" y=\"20\" width=\"60\" height=\"20\" uuid=\"0f86baff-6386-4f3f-b3fe-2388707babe8\"/>\n" +
                        "\t\t\t\t<box rightPadding=\"4\"/>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Left\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Nomor\t   :]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<image hAlign=\"Center\">\n" +
                        "\t\t\t\t<reportElement x=\"0\" y=\"10\" width=\"65\" height=\"60\" uuid=\"94883631-a913-43e2-b182-ab8d77d0181e\"/>\n" +
                        "\t\t\t\t<imageExpression><![CDATA[\"D:/Downloads/WhatsApp Image 2024-08-02 at 16.24.24_e389c7d3.jpg\"]]></imageExpression>\n" +
                        "\t\t\t</image>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement x=\"382\" y=\"40\" width=\"60\" height=\"20\" uuid=\"0b3f9342-da78-4cfa-9fc5-2301c4749678\"/>\n" +
                        "\t\t\t\t<box rightPadding=\"4\"/>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Left\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Tanggal  :]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement x=\"80\" y=\"15\" width=\"170\" height=\"50\" forecolor=\"#46B8BA\" uuid=\"e622555d-198b-4ccd-a4a1-c59c53304058\">\n" +
                        "\t\t\t\t\t<property name=\"local_mesure_unitheight\" value=\"pixel\"/>\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\n" +
                        "\t\t\t\t</reportElement>\n" +
                        "\t\t\t\t<textElement verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"37\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[INVOICE]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<line>\n" +
                        "\t\t\t\t<reportElement x=\"0\" y=\"0\" width=\"556\" height=\"1\" uuid=\"c55e32d7-7538-46ab-b4cb-be1502e33bd4\">\n" +
                        "\t\t\t\t\t<property name=\"local_mesure_unitheight\" value=\"pixel\"/>\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\n" +
                        "\t\t\t\t</reportElement>\n" +
                        "\t\t\t\t<graphicElement>\n" +
                        "\t\t\t\t\t<pen lineWidth=\"2.0\" lineColor=\"#48B8BA\"/>\n" +
                        "\t\t\t\t</graphicElement>\n" +
                        "\t\t\t</line>\n" +
                        "\t\t</band>\n" +
                        "\t</title>\n" +
                        "\t<pageHeader>\n" +
                        "\t\t<band height=\"70\">\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement x=\"0\" y=\"3\" width=\"120\" height=\"19\" uuid=\"de04f7e4-c002-49fb-bd5a-a1dbff84f90b\"/>\n" +
                        "\t\t\t\t<textElement>\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Ditunjukkan Kepada :]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t</band>\n" +
                        "\t</pageHeader>\n" +
                        "\t<columnHeader>\n" +
                        "\t\t<band height=\"23\">\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement mode=\"Opaque\" x=\"0\" y=\"1\" width=\"50\" height=\"21\" backcolor=\"#46B8BA\" uuid=\"435044fc-0664-48b1-8d8d-e952a6d17c70\"/>\n" +
                        "\t\t\t\t<box topPadding=\"0\" leftPadding=\"0\" bottomPadding=\"0\" rightPadding=\"0\">\n" +
                        "\t\t\t\t\t<pen lineWidth=\"3.0\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<topPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<leftPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<bottomPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<rightPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t</box>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[No]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement mode=\"Opaque\" x=\"380\" y=\"1\" width=\"60\" height=\"21\" backcolor=\"#46B8BA\" uuid=\"f20cca4e-8c43-4b47-aefd-905004a12a94\"/>\n" +
                        "\t\t\t\t<box topPadding=\"0\" leftPadding=\"0\" bottomPadding=\"0\" rightPadding=\"0\">\n" +
                        "\t\t\t\t\t<pen lineWidth=\"3.0\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<topPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<leftPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<bottomPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<rightPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t</box>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Qty]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement mode=\"Opaque\" x=\"270\" y=\"1\" width=\"110\" height=\"21\" backcolor=\"#46B8BA\" uuid=\"038c175f-6b70-4a32-8451-07e75aba9888\"/>\n" +
                        "\t\t\t\t<box topPadding=\"0\" leftPadding=\"0\" bottomPadding=\"0\" rightPadding=\"0\">\n" +
                        "\t\t\t\t\t<pen lineWidth=\"3.0\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<topPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<leftPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<bottomPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<rightPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t</box>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Harga]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement mode=\"Opaque\" x=\"440\" y=\"1\" width=\"110\" height=\"21\" backcolor=\"#46B8BA\" uuid=\"a2d03c56-66e2-4975-b984-835fc79f3e03\"/>\n" +
                        "\t\t\t\t<box topPadding=\"0\" leftPadding=\"0\" bottomPadding=\"0\" rightPadding=\"0\">\n" +
                        "\t\t\t\t\t<pen lineWidth=\"3.0\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<topPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<leftPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<bottomPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<rightPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t</box>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Total]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement mode=\"Opaque\" x=\"50\" y=\"1\" width=\"220\" height=\"21\" backcolor=\"#46B8BA\" uuid=\"c8e9f472-8d16-4f82-a525-0bbcb7e300e1\"/>\n" +
                        "\t\t\t\t<box topPadding=\"0\" leftPadding=\"0\" bottomPadding=\"0\" rightPadding=\"0\">\n" +
                        "\t\t\t\t\t<pen lineWidth=\"3.0\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<topPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<leftPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<bottomPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<rightPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t</box>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Item]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t</band>\n" +
                        "\t</columnHeader>\n" +
                        "\t<detail>\n" +
                        "\t\t<band height=\"23\" splitType=\"Stretch\">\n" +
                        "\t\t\t<property name=\"com.jaspersoft.studio.layout\"/>\n" +
                        "\t\t\t<textField>\n" +
                        "\t\t\t\t<reportElement mode=\"Opaque\" x=\"380\" y=\"2\" width=\"60\" height=\"21\" backcolor=\"#D9D9D9\" uuid=\"17dab421-48a5-4827-b339-0cd10c7e6c99\">\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.spreadsheet.connectionID\" value=\"d4ccffc5-146b-4891-a706-16e830f3d1fb\"/>\n" +
                        "\t\t\t\t</reportElement>\n" +
                        "\t\t\t\t<box leftPadding=\"5\">\n" +
                        "\t\t\t\t\t<pen lineWidth=\"3.0\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<topPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<leftPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<bottomPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<rightPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t</box>\n" +
                        "\t\t\t\t<textElement verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<textFieldExpression><![CDATA[$F{qty}]]></textFieldExpression>\n" +
                        "\t\t\t</textField>\n" +
                        "\t\t\t<textField>\n" +
                        "\t\t\t\t<reportElement mode=\"Opaque\" x=\"50\" y=\"2\" width=\"220\" height=\"21\" backcolor=\"#D9D9D9\" uuid=\"603d3b2c-b6e9-4c07-ad32-11f9f951efa2\">\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.spreadsheet.connectionID\" value=\"e9f7abe4-13bc-4124-abd1-64c25d0dbbbf\"/>\n" +
                        "\t\t\t\t</reportElement>\n" +
                        "\t\t\t\t<box leftPadding=\"5\">\n" +
                        "\t\t\t\t\t<pen lineWidth=\"3.0\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<topPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<leftPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<bottomPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<rightPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t</box>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Left\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<textFieldExpression><![CDATA[$F{item}]]></textFieldExpression>\n" +
                        "\t\t\t</textField>\n" +
                        "\t\t\t<textField>\n" +
                        "\t\t\t\t<reportElement mode=\"Opaque\" x=\"0\" y=\"2\" width=\"50\" height=\"21\" backcolor=\"#D9D9D9\" uuid=\"2a0b600c-be93-4a7c-8c28-3fce9e8d7ad6\"/>\n" +
                        "\t\t\t\t<box leftPadding=\"5\">\n" +
                        "\t\t\t\t\t<pen lineWidth=\"3.0\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<topPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<leftPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<bottomPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<rightPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t</box>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Left\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<textFieldExpression><![CDATA[$F{no}]]></textFieldExpression>\n" +
                        "\t\t\t</textField>\n" +
                        "\t\t\t<textField>\n" +
                        "\t\t\t\t<reportElement mode=\"Opaque\" x=\"270\" y=\"2\" width=\"110\" height=\"21\" backcolor=\"#D9D9D9\" uuid=\"886b4876-b414-4b54-96e1-d17367548671\">\n" +
                        "\t\t\t\t\t<property name=\"com.jaspersoft.studio.spreadsheet.connectionID\" value=\"1ec81c0e-58a2-46ef-95fa-8920c96d2170\"/>\n" +
                        "\t\t\t\t</reportElement>\n" +
                        "\t\t\t\t<box leftPadding=\"5\">\n" +
                        "\t\t\t\t\t<pen lineWidth=\"3.0\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<topPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<leftPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<bottomPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<rightPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t</box>\n" +
                        "\t\t\t\t<textElement verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<textFieldExpression><![CDATA[$F{harga}]]></textFieldExpression>\n" +
                        "\t\t\t</textField>\n" +
                        "\t\t</band>\n" +
                        "\t</detail>\n" +
                        "\t<columnFooter>\n" +
                        "\t\t<band height=\"27\" splitType=\"Immediate\">\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement mode=\"Opaque\" x=\"440\" y=\"3\" width=\"110\" height=\"21\" backcolor=\"#D9D9D9\" uuid=\"4fdf5d15-1af6-49ea-ae0f-ed3ba757dab7\"/>\n" +
                        "\t\t\t\t<box topPadding=\"0\" leftPadding=\"5\" bottomPadding=\"0\" rightPadding=\"0\">\n" +
                        "\t\t\t\t\t<pen lineWidth=\"3.0\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<topPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<leftPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<bottomPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<rightPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t</box>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Left\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Total]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement mode=\"Opaque\" x=\"0\" y=\"3\" width=\"440\" height=\"21\" backcolor=\"#D9D9D9\" uuid=\"667ab26f-eb07-4d2d-b56e-974a9993ee54\"/>\n" +
                        "\t\t\t\t<box topPadding=\"0\" leftPadding=\"5\" bottomPadding=\"0\" rightPadding=\"0\">\n" +
                        "\t\t\t\t\t<pen lineWidth=\"3.0\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<topPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<leftPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<bottomPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t\t<rightPen lineWidth=\"3.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>\n" +
                        "\t\t\t\t</box>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Total]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t</band>\n" +
                        "\t</columnFooter>\n" +
                        "\t<lastPageFooter>\n" +
                        "\t\t<band height=\"220\">\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement positionType=\"Float\" mode=\"Opaque\" x=\"-1\" y=\"190\" width=\"556\" height=\"30\" backcolor=\"#E6E8E9\" uuid=\"36aa233d-4305-48e6-974a-1bbf89bb3c8f\"/>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"Serif\" size=\"9\" isItalic=\"true\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Terimakasih atas pesanannya]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement x=\"0\" y=\"2\" width=\"340\" height=\"38\" uuid=\"38c78a6b-96e8-4c7a-8f0e-b58b42395b31\"/>\n" +
                        "\t\t\t\t<textElement>\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\" isBold=\"false\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Tagihan dapat ditransfer ke rekening :\n" +
                        "BSI (Bank Syariah Indonesia) 4828625660 a.n Joko Munandar]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t\t<staticText>\n" +
                        "\t\t\t\t<reportElement x=\"435\" y=\"140\" width=\"120\" height=\"35\" uuid=\"42269aba-ae23-4bb0-9bda-bddf5d1cdf1d\"/>\n" +
                        "\t\t\t\t<textElement textAlignment=\"Right\" verticalAlignment=\"Middle\">\n" +
                        "\t\t\t\t\t<font fontName=\"SansSerif\" size=\"12\"/>\n" +
                        "\t\t\t\t</textElement>\n" +
                        "\t\t\t\t<text><![CDATA[Joko Munandar\n" +
                        "Seigan Technology]]></text>\n" +
                        "\t\t\t</staticText>\n" +
                        "\t\t</band>\n" +
                        "\t</lastPageFooter>\n" +
                        "</jasperReport>\n","1723600924619"
        ));
    }
}
