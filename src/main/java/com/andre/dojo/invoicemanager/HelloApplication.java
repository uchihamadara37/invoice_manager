package com.andre.dojo.invoicemanager;

import com.andre.dojo.Adapter.*;
import com.andre.dojo.Models.*;
import com.andre.dojo.Utils.DatabaseManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class HelloApplication extends Application {

    public static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Personal.class, new PersonAdapter())
            .registerTypeAdapter(Customer.class, new CustomerAdapter())
            .registerTypeAdapter(CustomJSON.class, new CustomJSONAdapter())
            .registerTypeAdapter(InvoiceAdapter.class, new InvoiceAdapter())
            .registerTypeAdapter(Item.class, new ItemAdapter())
            .registerTypeAdapter(Organization.class, new OrganizationAdapter())
            .create();

    private static Stage mainStage;
    public static String dirSource;
    public static String dirImage;
    public static String dirPdf;
    public static String dirLogo;
    public static File filePropDir;
    public static String dirExport;

    public static Organization organization = Organization.getOneData(1723509861951L);
    public static List<KodeSurat> kodeSurats = KodeSurat.getAllData();

    @Override
    public void start(Stage stage) throws IOException {

//        Organization org = Organization.getOneData(1723509861951L);
//        org.setTahunOperasi(2024);
//        Organization.updateById(org);

//        DatabaseManager.dropTable();

//        Invoice inv = new Invoice();
//        inv.setId(Instant.now().toEpochMilli());
//        inv.setTimestamp(Instant.now().toString());
//
//        inv.setDescription("ok description");
//        inv.setTotalPriceAll(1000);
//        inv.setInvoiceMarkText("okok/okok/ok");
//        inv.setDate(LocalDate.now().getMonth().toString());
//        System.out.println(inv.getTimestamp());
//        Invoice.addToDB(inv);


        mainStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Invoice Manager!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // memilih direktory file.properties
        String userHome = System.getProperty("user.home");
        filePropDir = new File(userHome, ".invoiceGenerator");

        if (!filePropDir.exists()) {
            filePropDir.mkdir();
        }
        File fileProp = new File(filePropDir, "config.properties");

        if (!fileProp.exists()) {
            try {
                fileProp.createNewFile();
//                System.out.println("File config.properties baru telah dibuat.");
            } catch (IOException e) {
                showAlert("Gagal membuat file properties baru!");
                e.printStackTrace();
                return;
            }
        }

        Properties prop = new Properties();

        for (int i = 0; i < args.length; i++){
            switch (args[i]){
                case "--help" :
//                    System.out.println("Help center :");
//                    System.out.println("--set-src-directory : example >> '--set-src-directory C:\\your_folder'");
//                    System.out.println("                      it`s a url directory or folder which used to save your local pdf or image, and just needed on the first running configuration");
//                    System.out.println("--help              : to provide all syntax you can used to configure something");
                    break;
                case "--set-src-directory" :
                    // jika next string ada input
                    if (i + 1 < args.length){
                        String input = args[i+1];

                        // menghapus backslash dan slash terakhir
                        while (!input.isEmpty() && (input.endsWith("\\") || input.endsWith("/"))) {
                            input = input.substring(0, input.length() - 1);
                        }

                        // mulai membaca config.file
                        File fileConfig = new File(input);
                        if (fileConfig.exists()){

                            try (FileInputStream in = new FileInputStream(fileProp)) {
                                prop.load(in);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            File dirImage = new File(fileConfig, "image");
                            File dirPdf = new File(fileConfig, "pdf");
                            File dirLogo = new File(fileConfig, "logo");
                            if (!dirImage.exists()){
                                dirImage.mkdir();
                            }
                            if (!dirPdf.exists()){
                                dirPdf.mkdir();
                            }
                            if (!dirLogo.exists()){
                                dirLogo.mkdir();
                            }
                            if (!prop.containsKey("dir.src")  ){
                                // ganti val
                                // confirmasi
                                prop.setProperty("dir.src", fileConfig.getAbsolutePath());
                                prop.setProperty("dir.image", dirImage.getAbsolutePath());
                                prop.setProperty("dir.pdf", dirPdf.getAbsolutePath());
                                prop.setProperty("dir.logo", dirLogo.getAbsolutePath());

                                try {
                                    // Simpan ke file config.properties
                                    try (FileOutputStream out = new FileOutputStream(fileProp)) {
                                        prop.store(out, "Invoice Generator Configuration");
//                                        System.out.println("Properties berhasil disimpan ke " + fileProp.getAbsolutePath());
                                    }
                                } catch (IOException e) {
                                    Platform.runLater(() -> {
                                        showAlert("Gagal menyimpan properties ke file!");
                                    });
                                    e.printStackTrace();
                                }

                            }else{
                                // belum diset perbedaannya
                                prop.setProperty("dir.src", fileConfig.getAbsolutePath());
                                prop.setProperty("dir.image", dirImage.getAbsolutePath());
                                prop.setProperty("dir.pdf", dirPdf.getAbsolutePath());
                                prop.setProperty("dir.logo", dirLogo.getAbsolutePath());

                                try {
                                    // Simpan ke file config.properties
                                    try (FileOutputStream out = new FileOutputStream(fileProp)) {
                                        prop.store(out, "Invoice Generator Configuration");
//                                        System.out.println("Properties berhasil disimpan ke " + fileProp.getAbsolutePath());
                                    }
                                } catch (IOException e) {
                                    Platform.runLater(() -> {
                                        showAlert("Gagal menyimpan properties ke file!");
                                    });
                                    e.printStackTrace();
                                }

                            }
                        }else{
                            Platform.runLater(() -> {
                                HelloApplication.showAlert("this "+fileConfig+" directory is not exist! please fill with correct path directory!");
                            });
//                            System.out.println("this "+fileConfig+" directory is not exist! please fill with correct path directory!");
                            return;
                        }
                        i++;
                    }else{
                        Platform.runLater(() -> {
                            HelloApplication.showAlert("--url-source \"need_some_input\" please use --help for more information");
                        });
//                        System.out.println("--url-source \"need_some_input\" please use --help for more information");
                        return;
                    }
                    break;
                default:
//                    System.out.println(" Please use --help to show all syntax configuration!");
                    return;

            }
        }

        // membaca ulang prop
        try (FileInputStream in = new FileInputStream(fileProp)){
            prop.load(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        System.out.println("okokok");
//        System.out.println(fileProp.getAbsolutePath());
        if (prop.containsKey("dir.image") && prop.containsKey("dir.pdf") && prop.containsKey("dir.logo") && prop.containsKey("dir.src")){
            dirImage = prop.getProperty("dir.image");
            dirPdf = prop.getProperty("dir.pdf");
            dirLogo = prop.getProperty("dir.logo");
            dirSource = prop.getProperty("dir.src");
            File fileDirImage = new File(dirImage);
            File fileDirPdf = new File(dirPdf);
            File fileDirLogo = new File(dirLogo);
            File fileDirSrc = new File(dirSource);
            if (!fileDirSrc.exists()){
                fileDirSrc.mkdir();
            }
            if (!fileDirImage.exists()){
                fileDirImage.mkdir();
            }
            if (!fileDirPdf.exists()){
                fileDirPdf.mkdir();
            }
            if (!fileDirLogo.exists()){
                fileDirLogo.mkdir();
            }
            launch();

        }else{
//            System.out.println("jangkrik malah rung enek");
            Platform.runLater(() -> {
                showAlert("Nilai directory tidak ditemukan dalam file config.properties!");
            });

        }

    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.showAndWait();
    }
}