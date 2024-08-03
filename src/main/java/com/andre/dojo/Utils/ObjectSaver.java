package com.andre.dojo.Utils;


import com.andre.dojo.Adapter.MetadataSaveAdapter;
import com.andre.dojo.Adapter.OrganizationAdapter;
import com.andre.dojo.Adapter.PersonAdapter;
import com.andre.dojo.Models.MetadataSave;
import com.andre.dojo.Models.Organization;
import com.andre.dojo.Models.PersonManagement;
import com.andre.dojo.invoicemanager.HelloApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.stage.FileChooser;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class ObjectSaver implements Serializable{
    public static Gson gsonPretty = new GsonBuilder()
            .setPrettyPrinting()
//            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(PersonManagement.class, new PersonAdapter())
            .registerTypeAdapter(Organization.class, new OrganizationAdapter())
            .registerTypeAdapter(MetadataSave.class, new MetadataSaveAdapter())
            .create();
    public static Gson gson = new GsonBuilder()
//            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(PersonManagement.class, new PersonAdapter())
            .registerTypeAdapter(Organization.class, new OrganizationAdapter())
            .registerTypeAdapter(MetadataSave.class, new MetadataSaveAdapter())
            .create();

    public static String fileNameAddress = "/home/andre/Documents/data_text/invoice.txt";
    public static String fileCacheAddress;

    public static void SaveData(MetadataSave allData) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameAddress))) {
            String dataJson = gsonPretty.toJson(allData);
            System.out.println(dataJson);

            String enkripsi = Base64.getEncoder().encodeToString(dataJson.getBytes());

////            String encodedS = HelloApplication.makeVigenereString(enkripsi);

            writer.write(enkripsi);
            writer.newLine();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static MetadataSave retrieveData(){
        MetadataSave data = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileNameAddress))) {
            String line;
            if ((line = reader.readLine()) != null){

                data = new MetadataSave();

                String encodeEnkripsi = new String(Base64.getDecoder().decode(line));
                System.out.println("retrive person\n"+encodeEnkripsi);
                // Definisikan tipe untuk List<PersonManagement>
                Type listType = new TypeToken<List<MetadataSave>>(){}.getType();
                System.out.println(listType);
                // Parse JSON ke List<PersonManagement>
                data = ObjectSaver.gsonPretty.fromJson(encodeEnkripsi, MetadataSave.class);
//                persons = PersonManagementJSONConverter.jsonToObservableList(encodeEnkripsi);

            }
        }catch (IOException e){
            System.out.println("error baca file mase");
            throw new RuntimeException(e);
        }
        return data;
    }

















    public static boolean saveFileCache(String fileNameAddress){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./fileCache.txt"))) {
            String enkripsi = Base64.getEncoder().encodeToString(fileNameAddress.getBytes());
            writer.write(enkripsi);
            writer.newLine();
            return true;
        }catch (IOException e1){
            System.out.println("error save fileCache mase \n "+e1);
            return false;
//            throw new RuntimeException(e1);
        }
    }
    public static String readFileCache(){
        try (BufferedReader reader = new BufferedReader(new FileReader("./fileCache.txt"))) {
            String line;
            if ((line = reader.readLine()) != null){
                return new String(Base64.getDecoder().decode(line));
            }else{
                System.out.println("isi file kosong");
                return "";
            }
        }catch (IOException e1){
            System.out.println("error save fileCache mase \n "+e1);
            return "";
//            throw new RuntimeException(e1);
        }
    }

    public static File chooseFileSave() {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        fileChooser.setInitialDirectory(getUserDocumentsDirectory());
        fileChooser.setInitialFileName("akun.txt");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        return fileChooser.showSaveDialog(HelloApplication.getMainStage());
    }
    public static File chooseFileSave(String addressRecentFile, String fileName) {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        fileChooser.setInitialFileName(fileName);
        fileChooser.setInitialDirectory(new File(addressRecentFile));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        return fileChooser.showSaveDialog(HelloApplication.getMainStage());
    }

    public static File chooseFileSelectOpen() {

        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        fileChooser.setInitialDirectory(getUserDocumentsDirectory());
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        return fileChooser.showOpenDialog(HelloApplication.getMainStage());
    }
    public static File getUserDocumentsDirectory() {
        String userHome = System.getProperty("user.home");
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows
            return new File(userHome + "\\Documents");
        } else if (os.contains("mac")) {
            // macOS
            return new File(userHome + "/Documents");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("unix")) {
            // Linux/Unix
            return new File(userHome + "/Documents");
        } else {
            // Fallback ke user home jika OS tidak dikenali
            return new File(userHome);
        }
    }

}
