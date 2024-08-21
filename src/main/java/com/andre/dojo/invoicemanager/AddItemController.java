package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.CustomJSON;
import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Invoice;
import com.andre.dojo.Models.Item;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddItemController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    private HelloController helloController;
    @FXML
    private Label btnSelectInvoice;
    @FXML
    private Label btnJsonData;
    @FXML
    private Label btnJrxml;
    @FXML
    private Label btnPreview;
    @FXML
    private Pane btnJsonBawah;

    @FXML
    private Pane btnAdd;

    @FXML
    private TextField itemName;
    @FXML
    private TextField itemQty;
    @FXML
    private TextField itemPrice;
    @FXML
    private TextField itemtotalPrice;


    @FXML
    private TableView<Item> tableViewItem;
    @FXML
    private TableColumn<Item, String> tableColumnName;
    @FXML
    private TableColumn<Item, String> tableColumnPrice;
    @FXML
    private TableColumn<Item, Integer> tableColumnQty;
    @FXML
    private TableColumn<Item, String> tableColumnPriceAll;
    @FXML
    private TableColumn<Item, Boolean> tableColumnAdjust;
    @FXML
    private TableColumn<Item, Void> tableColumnDelete;

    @FXML
    private Label showMessage;
    @FXML
    private Label showCode;
    @FXML
    private Label showDescription;
    @FXML
    private Label showCustomer;
    @FXML
    private Label showTimeStamp;

    private Tooltip tooltip = new Tooltip("Field must be filled");
    private Tooltip tooltip2 = new Tooltip("Field must be filled");
    private Tooltip tooltip3 = new Tooltip("Field must be filled");
    private Double xKiri, yAtas;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a");
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("id", "ID"));
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showMessage.setVisible(false);



        setupFloatTextField(itemPrice);
        setupFloatTextField(itemQty);

        btnSelectInvoice.setOnMouseClicked(e -> {
            openInvoiceSelection();
        });
        btnAdd.setOnMouseClicked(e -> {
            addItemToTable();
        });
        btnJsonData.setOnMouseClicked(e -> {
            openJsonPage();
        });
        btnJsonBawah.setOnMouseClicked(e -> {
            openJsonPage();
        });
        btnJrxml.setOnMouseClicked(e -> {
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getJrxmlController().getRootPane());
        });
        btnPreview.setOnMouseClicked(e -> {
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getPreviewController().getRootPane());
        });

        itemName.setTooltip(tooltip);
        itemPrice.setTooltip(tooltip2);
        itemQty.setTooltip(tooltip3);
        tooltip.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 15px;");
        tooltip2.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 15px;");
        tooltip3.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 15px;");

        xKiri = HelloApplication.getMainStage().getX();
        yAtas = HelloApplication.getMainStage().getY();

        itemName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            tooltip.hide();
        });
        itemPrice.focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            tooltip2.hide();
        }));
        itemQty.focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            tooltip3.hide();
        }));

    }

    private void openJsonPage() {
        if (helloController.getInvoiceSelected().getListItems() != null){
            helloController.getAnchorPaneMain().getChildren().removeFirst();
            helloController.getAnchorPaneMain().getChildren().add(helloController.getJsonDataController().getRootPane());
        }else{
            helloController.showInformationDialog("Please add at least one item on this invoice!");
        }
    }

    public void reloadTotalPrice() {
        itemtotalPrice.setText(numberFormat.format(Item.getSumOfPriceByInvoiceId(helloController.getInvoiceSelected().getId())));
    }

    public void showInvoiceSelected(Invoice invoiceSelected) {
        if (invoiceSelected != null){
            showCode.setText(invoiceSelected.getInvoiceCode());
            showDescription.setText(invoiceSelected.getDescription());
            showCustomer.setText(Customer.getOneData(invoiceSelected.getCustomer_id()).getName()+ " | "+Customer.getOneData(invoiceSelected.getCustomer_id()).getDescription());
            showTimeStamp.setText(ZonedDateTime.parse(invoiceSelected.getTimeStamp()).format(formatter));
        }else{
            showCode.setText("");
            showDescription.setText("");
            showCustomer.setText("");
            showTimeStamp.setText("");
        }
    }

    private void addItemToTable() {
        if (Objects.equals(itemName.getText(), "")){
            tooltip.show(
                    itemName,
                    itemName.getLayoutX() + HelloApplication.getMainStage().getX() + xKiri,
                    itemName.getLayoutY() + itemName.getHeight() + HelloApplication.getMainStage().getY() + yAtas + 20
            );
        }else{
            if (Objects.equals(itemPrice.getText(), "")){
                tooltip2.show(
                        itemName,
                        itemName.getLayoutX() + HelloApplication.getMainStage().getX() + xKiri + 400,
                        itemName.getLayoutY() + itemName.getHeight() + HelloApplication.getMainStage().getY() + yAtas + 20
                );
            }else{
                if (Objects.equals(itemQty.getText(), "")){
                    tooltip3.show(
                            itemName,
                            itemName.getLayoutX() + HelloApplication.getMainStage().getX() + xKiri + 600,
                            itemName.getLayoutY() + itemName.getHeight() + HelloApplication.getMainStage().getY() + yAtas + 20
                    );
                }else{
                    // masukan data
                    Item.addToDB(
                            new Item(
                                    itemName.getText(),
                                    Integer.parseInt(itemPrice.getText()),
                                    Integer.parseInt(itemQty.getText()),
                                    Integer.parseInt(itemPrice.getText()) * Integer.parseInt(itemQty.getText()),
                                    helloController.getInvoiceSelected().getId()
                            )
                    );

                    reloadAfterChangeData();
//                    System.out.println("Y :=> scene.window.getX : "+itemName.getScene().getWindow().getX()+" itemName.getLX : "+itemName.getLayoutX()+" mainStage.X : "+HelloApplication.getMainStage().getX());
                }
            }
        }
    }

    public void reloadAfterChangeData(){
        reloadTableItem();
        reloadTotalPrice();
        // update harga total
        helloController.getInvoiceSelected().setTotalPriceAll(Item.getSumOfPriceByInvoiceId(helloController.getInvoiceSelected().getId()));
        String json = HelloApplication.gson.toJson(HelloController.customJSON, CustomJSON.class);
        System.out.println(json);
        // update json
        helloController.getInvoiceSelected().setJsonData(json);
        Invoice.updateById(helloController.getInvoiceSelected());
        // update textarea
        helloController.getJsonDataController().setJsonText(json);
    }

    public void reloadTableItem() {
        tableViewItem.setEditable(true);
        tableViewItem.setItems(FXCollections.observableArrayList(Item.getListByInvoiceID(helloController.getInvoiceSelected().getId())));
        tableColumnAdjust.setCellValueFactory(e -> {
            BooleanProperty check = new SimpleBooleanProperty(e.getValue().getFillNameWithMonth());
            check.addListener((observableValue, aBoolean, t1) -> {
                e.getValue().setFillNameWithMonth(t1);

                String bulanTahun = LocalDateTime.parse(
                        Invoice.getOneData(e.getValue().getInvoice_id()).getTimeStamp(),
                        DateTimeFormatter.ISO_DATE_TIME
                ).format(formatter2);

                String nameOri = e.getValue().getName().replaceAll(" bulan .*$", "");
                if (t1){
                    e.getValue().setName(nameOri + " bulan "+bulanTahun);
                }else{
                    e.getValue().setName(nameOri);
                }
                Item.updateById(e.getValue());
                tableViewItem.refresh();
            });
            return check;
        });
        tableColumnAdjust.setCellFactory(e -> new CheckBoxTableCell<>());

        tableColumnQty.setCellValueFactory(e -> new ReadOnlyObjectWrapper<>(e.getValue().getQty()));
        tableColumnName.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getName()));

        tableColumnPrice.setCellValueFactory(e -> new ReadOnlyObjectWrapper<>(numberFormat.format(e.getValue().getPrice())));
        tableColumnPriceAll.setCellValueFactory(e -> new ReadOnlyObjectWrapper<>(numberFormat.format(e.getValue().getTotal_price_item())));

        tableColumnDelete.setCellFactory(e -> new TableCell<Item, Void>() {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("icon/circle-minus.png")));
            private final HBox container = new HBox();
            {
                icon.setFitHeight(20);
                icon.setFitWidth(20);

                container.setAlignment(Pos.CENTER_LEFT);
                container.getChildren().add(icon);
                container.setPadding(new Insets(0,0,0,20));

                container.setOnMouseClicked(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    handleDelete(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    private void handleDelete(Item item) {
        if (helloController.showConfirmationDialog("Are you really want to delete this "+item.getName()+"?")){
            Item.deleteOneById(item.getId());
            reloadTableItem();
            reloadTotalPrice();

            String json = HelloApplication.gson.toJson(HelloController.customJSON, CustomJSON.class);
            System.out.println(json);
            // update json
            helloController.getJsonDataController().setJsonText(json);
            helloController.getInvoiceSelected().setJsonData(json);
            Invoice.updateById(helloController.getInvoiceSelected());
        }else{

        }
    }



    private void openInvoiceSelection() {
        helloController.getAnchorPaneMain().getChildren().removeFirst();
        helloController.getAnchorPaneMain().getChildren().add(helloController.getSetupController().getRootPane());
    }

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    public void setupFloatTextField(TextField textField) {
        Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (pattern.matcher(newText).matches()) {
                return change;
            } else {
                return null;
            }
        });

        textField.setTextFormatter(formatter);
    }
}
