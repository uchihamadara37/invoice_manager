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
import java.util.*;

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
    public static String addressConfig;
    public static String dirExport;
    public static Organization organization;
    public static List<KodeSurat> kodeSurats = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        mainStage.setResizable(false);

        DatabaseManager.createNewDatabase();
        DatabaseManager.createTableOrganization();
        DatabaseManager.createTableCustomer();
        DatabaseManager.createTableKodeSurat();
        DatabaseManager.createTablePerson();
        DatabaseManager.createTableInvoice();
        DatabaseManager.createTableDesign();
        DatabaseManager.createTableItem();
        DatabaseManager.createTableBank();

        // Membuat organization pertama
        if (Organization.getAllData().isEmpty()){
            Organization.addToDB(
                    new Organization("Siegan Dojo")
            );
            Personal.addToDB(new Personal());

            organization = Organization.getFirstData();
            organization.setPersonal(Personal.getFirstData());
        }else{
            organization = Organization.getFirstData();
            organization.setPersonal(Personal.getFirstData());
        }




        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Invoice Manager!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DatabaseManager.createTableConfig();
        if (Config.getAllData().isEmpty()){
            Config.addToDB(new Config(Instant.now().toEpochMilli()));
        }else{
            addressConfig = Config.getFirstData().getDir();
        }
        // memilih direktory file.properties
//        String userHome = System.getProperty("user.home");
//        filePropDir = new File(userHome, ".invoiceGenerator");

        if (!(addressConfig == null || addressConfig.isEmpty())) {
            File fileConfig =  new File(addressConfig);
            if (!fileConfig.exists()){
                Platform.runLater(() -> {
                   HelloApplication.showAlert("your file config on "+addressConfig+" is missing! please set the config properties again!");
                });
                return;
            }

            // menyiapkan var properties. Mencoba membaca file props
            Properties prop = new Properties();
            try (FileInputStream in = new FileInputStream(fileConfig)){
                prop.load(in);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

            }else{
                // berarti config isinya masih kosong
            }

        }else {
//            Platform.runLater(() -> {
//                HelloApplication.showAlert("this "+addressConfig+
//                        "directory is not exist! please fill with correct path directory!");
//            });

        }
        System.out.println("selesai pembacaan dir pertama");
        System.out.println("selesai config : "+addressConfig);

        for (int i = 0; i < args.length; i++){
            switch (args[i]){
                case "--help" :
                    System.out.println("Help center :");
                    System.out.println("--set-src-dir       : example >> '--set-src-dir C:\\your_folder'");
                    System.out.println("                      it`s a url directory or folder which used to save your local pdf or image,");
                    System.out.println("                      and just needed on the first running configuration");
                    System.out.println("--set-config-file   : example >> '--set-config-dir C:\\your_file_address' fileName.properties");
                    System.out.println("                      it`s a url directory or folder which used to make your config properties");
                    System.out.println("--help              : to provide all syntax you can used to configure something");
                    System.out.println("--show-dir          : to show current source or config directory address");
                    i++;
                    break;
                case "--show-dir" :

                    File fileDirSrc = new File(dirSource);
                    if (fileDirSrc.exists()){
                        System.out.println("Current Source Directory  : "+fileDirSrc.getAbsolutePath());
                    }else{
                        System.out.println("Current source directory still null.");
                    }
                    if (!(addressConfig == null || addressConfig.isEmpty()) && new File(addressConfig).exists()){
                        System.out.println("Current Config Directory  : "+addressConfig);
                    }else{
                        System.out.println("Current Config directory still null.");
                    }
                    i++;
                    break;
                case "--set-src-dir" :

                    // jika next string ada input
                    if (i + 1 < args.length){
                        String inputDirectory = args[i+1];

                        // jika wadahnya ada
                        if (!(addressConfig == null || addressConfig.isEmpty()) && new File(addressConfig).exists()){
                            // menghapus backslash dan slash terakhir
                            while (!inputDirectory.isEmpty() && (inputDirectory.endsWith("\\") || inputDirectory.endsWith("/"))) {
                                inputDirectory = inputDirectory.substring(0, inputDirectory.length() - 1);
                            }

                            // mulai membaca config.file
                            File srcDir = new File(inputDirectory);
                            if (srcDir.exists()){
                                Properties prop = new Properties();
                                try (FileInputStream in = new FileInputStream(addressConfig)) {
                                    prop.load(in);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                File dirImage = new File(srcDir, "image");
                                File dirPdf = new File(srcDir, "pdf");
                                File dirLogo = new File(srcDir, "logo");
                                if (!dirImage.exists()){
                                    dirImage.mkdir();
                                }
                                if (!dirPdf.exists()){
                                    dirPdf.mkdir();
                                }
                                if (!dirLogo.exists()){
                                    dirLogo.mkdir();
                                }

                                prop.setProperty("dir.src", srcDir.getAbsolutePath());
                                prop.setProperty("dir.image", dirImage.getAbsolutePath());
                                prop.setProperty("dir.pdf", dirPdf.getAbsolutePath());
                                prop.setProperty("dir.logo", dirLogo.getAbsolutePath());


                                try {
                                    // Simpan ke file config.properties
                                    try (FileOutputStream out = new FileOutputStream(addressConfig)) {
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
                                Platform.runLater(() -> {
                                    HelloApplication.showAlert("this "+srcDir.getAbsolutePath()+
                                            " directory is not exist! please fill with correct path directory!");
                                });
                                return;
                            }
                        }else{
                            Platform.runLater(() -> {
                                HelloApplication.showAlert("Please set the config directory first before you set source directory");
                            });

                        }
                        i++;
                    }else{
                        Platform.runLater(() -> {
                            HelloApplication.showAlert("--url-source \"need_some_input\" please use --help for more information");
                        });
                        return;
                    }
                    i++;
                    break;
                case "--set-config-file" :
                    // jika next string ada input
                    if (i + 1 < args.length){
                        String inputDirectory = args[i+1];

                        // jika wadahnya ada
                        if (!Objects.equals(inputDirectory, "")){
                            // menghapus backslash dan slash terakhir
                            while (!inputDirectory.isEmpty() && (inputDirectory.endsWith("\\") || inputDirectory.endsWith("/"))) {
                                inputDirectory = inputDirectory.substring(0, inputDirectory.length() - 1);
                            }

                            // mulai membaca config.file
                            File configFile = new File(inputDirectory);
                            if (configFile.exists() && configFile.getName().endsWith(".properties")){
                                Config config = Config.getFirstData();
                                config.setDir(inputDirectory);
                                Config.updateById(config);
                                addressConfig = configFile.getAbsolutePath();
                                Platform.runLater(() -> {
                                    HelloApplication.showAlert("Successfully update config file.properties! Lets go.");
                                });
                            }else{
                                Platform.runLater(() -> {
                                    HelloApplication.showAlert("this "+configFile.getAbsolutePath()+" file is not exist or not .properties extension! please fill with correct file.properties directory!");
                                });
                                return;
                            }
                        }else{
                            Platform.runLater(() -> {
                                HelloApplication.showAlert("Please set the config directory first before you set source directory");
                            });
                            return;
                        }
                        i++;
                    }else{
                        Platform.runLater(() -> {
                            HelloApplication.showAlert("--url-source \"need_some_input\" please use --help for more information");
                        });
                        return;
                    }
                    i++;
                    break;
                default:
            }
        }

        if (!(Objects.equals(addressConfig, "") || addressConfig == null)) {

            // membaca ulang prop
            File fileConfig = new File(addressConfig);
            if (fileConfig.exists()){
                Properties prop = new Properties();
                try (FileInputStream in = new FileInputStream(addressConfig)){
                    prop.load(in);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

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
                    System.out.println("udah siap launch");
                    launch();

                }else{
                    Platform.runLater(() -> {
                        showAlert("Your file config "+addressConfig+" not contains source directory, please set source directory via --help for more information!");
                    });
                }
            }else{
                Platform.runLater(() -> {
                    HelloApplication.showAlert("File config not found!");
                });
            }
        }else{
            Platform.runLater(() -> {
                HelloApplication.showAlert("File config not set! Please set file config.properties first");
            });
        }

        System.out.println("selesai pembacaan dir kedua");
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