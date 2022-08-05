/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Speaker;
import org.ini4j.Profile;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FileUtil {

    /**
     * This method is used to create File.
     *
     * @param file The file is used to assign the created file.
     * @param fileName The fileName is used as the name of the file.
     * @param fileName and creates the file with the return File with the use of
     * Parameter file Name with the Specfied path also
     * @return Returns File
     */
    public static File createFile(File file, String fileName) {
        file = new File(fileName);
        return file;
    }

    /**
     * This method is used to validate file creation.
     *
     * @param file File is passed as parameter for validation.
     *
     * @param its takes the file and validate the boolean Value using the file
     * Class method as file.exist(); Which return the Boolean value
     * @return Returns Boolean
     */
    public static Boolean validateFileCreation(File file) {
        try {
            //return file.createNewFile();
            return file.exists();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method is used to create iniFile.
     *
     * @param iniFile The iniFile is used to create ini File.
     * @param file The file parameter is used to create file into ini file. Wini
     * class is the class is created inside the package ini org.ini4j.Wini Class
     * the class which takes the argument as file inputStream or file name with
     * the Path
     * @return Returns Wini
     */
    public static Wini createIniFile(Wini iniFile, File file) {
        try {
            iniFile = new Wini(file);
            return iniFile;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * This method is used to write data into the ini File.
     *
     * @param ini The ini file is used for writing sectionName, attributeName,
     * attributeText.
     * @param sectionName The sectionName is passed as parameter to be written
     * into the ini file.
     * @param attributeName The attributeName is passed as parameter to be
     * written into the ini file it takes the Argument as key
     * @param attributeText The attributeText is passed as parameter to be
     * written into the ini file. as denotes the Value for the Key
     * @return Returns Wini [SECTION NAME] KEY = VALUE
     */
    public static Wini writeToIniFile(Wini ini, String sectionName, String attributeName, String attributeText) {

        try {
            ini.getConfig().setMultiSection(true);
            ini.put(sectionName, attributeName, attributeText);
            ini.store();
            return ini;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method is used to read contents from iniFile.
     *
     * @param iniFile The iniFile is used to create ini File.
     * @param sectionName The sectionName is passed as parameter to be read from
     * the ini file.
     * @param attributeName The attributeName is passed as parameter to be read
     * from the ini file
     *
     * @return Returns String
     */
    public static String readFromIniFile(Wini ini, String sectionName, String attributeName) {
        String data = ini.get(sectionName, attributeName, String.class);
        return data;
    }

    /**
     * This method is used to read contents from config file.
     *
     * @param filePath The String specifies the path of the file
     * @param speakers The list of speakers which contains the nodes read from
     * the config file.
     *
     * @return Returns Boolean, return true if config file not present, return
     * false if config file already exists.
     */
    public static Boolean readconfigFile(String filePath, List<Speaker> speakers) throws Exception {
        File file = null;
        Boolean fileCreationStatus;

        //file = FileUtil.createFile(file, "nodeDetails.conf");
        file = FileUtil.createFile(file, filePath);

        fileCreationStatus = FileUtil.validateFileCreation(file);
        if (!fileCreationStatus) {
            return true;
        }
        Wini ini = null;

        ini = FileUtil.createIniFile(ini, file);

        List<Section> sectionList = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        /*
[GUNA]
native = Ramnad
dbdgdbd = guna
name = class java.lang.String

[village]
alagankulam = fufbfbfb

[password]
password = 0

         */
        Collection<Profile.Section> list = ini.values();
        Profile.Section section = null;
        int count = 0;
        for (Profile.Section sections : list) {

            if (count == 0) {
                count = count + 1;
                continue;
            }
            section = ini.get(sections.getName());
            sectionList.add(section);
            for (String key : section.keySet()) {

                keys.add(key);
            }
            for (String value : section.values()) {
                values.add(value);

            }
        }

        int i = 0;

        int z = 1;
        for (i = 0; i < sectionList.size(); i++) {
            Speaker speaker = new Speaker();

            speaker.setType("endpoint");
            speaker.setContext(values.get(z++));
            speaker.setCodecDisallow(values.get(z++));
            speaker.setCodecAllow(values.get(z++));
            speaker.setAuth(values.get(z++));
            speaker.setAor(values.get(z++));
            speaker.setType("auth");

            speaker.setAuth_type(values.get(z++));
            speaker.setPassword(values.get(z++));
            speaker.setUserName(values.get(z++));
            speaker.setType("aor");

            speaker.setMax_contacts(values.get(z++));
            speakers.add(speaker);
            z = z + 1;
        }
        return false;
    }

    public static FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser;
    }

    public static File openFileChooser(FileChooser fileChooser, Stage stage) {
        File selectedFile = fileChooser.showOpenDialog(stage);
        return selectedFile;
    }

    public static File saveFileChooser(FileChooser fileChooser, Stage stage) {
        File selectedFile = fileChooser.showSaveDialog(stage);
        return selectedFile;
    }

    public static FileWriter writeToFile(FileWriter fileWriter, String fileContents) throws Exception {
        fileWriter.write(fileContents);
        return fileWriter;
    }

    public static Boolean writeToConfigFile(String filePath, List<Speaker> speakers) throws Exception {
        File file = null;
        Boolean fileCreationStatus;

        file = FileUtil.createFile(file, filePath);

        FileOutputStream fooStream = new FileOutputStream(file, false);

        fooStream.write("".getBytes());
        fooStream.close();
        fileCreationStatus = FileUtil.validateFileCreation(file);
        if (!fileCreationStatus) {
            return true;
        }

        FileWriter fw = new FileWriter(file);

        Wini ini = null;

        ini = FileUtil.createIniFile(ini, file);
        ini.getConfig().setMultiSection(true);
        int i = 0;

        fw = writeToFile(fw, "[" + "transport-udp" + "]\r\n");
        fw = writeToFile(fw, "type" + "=" + "transport\r\n");
        fw = writeToFile(fw, "protocol" + "=" + "udp\r\n");
        fw = writeToFile(fw, "bind" + "=" + "0.0.0.0\r\n\n");
        for (i = 0; i < speakers.size(); i++) {

            fw = writeToFile(fw, "[" + speakers.get(i).getUserName() + "]\r\n");
            fw = writeToFile(fw, "type" + "=" + "endpoint\r\n");
            fw = writeToFile(fw, "context" + "=" + "zilogic\r\n");
            fw = writeToFile(fw, "disallow" + "=" + "all\r\n");
            if (speakers.get(i).getCodecAllow().contentEquals("High Quality")) {
                speakers.get(i).setCodecAllow("speex32");
            } else if (speakers.get(i).getCodecAllow().contentEquals("Medium")) {
                speakers.get(i).setCodecAllow("speex16");
            } else if (speakers.get(i).getCodecAllow().contentEquals("Low")) {
                speakers.get(i).setCodecAllow("speex");
            }
            fw = writeToFile(fw, "allow" + "=" + speakers.get(i).getCodecAllow() + "\r\n");
            fw = writeToFile(fw, "auth" + "=" + speakers.get(i).getUserName() + "\r\n");
            fw = writeToFile(fw, "aors" + "=" + speakers.get(i).getUserName() + "\r\n\n");
            fw = writeToFile(fw, "[" + speakers.get(i).getUserName() + "]\r\n");
            fw = writeToFile(fw, "type" + "=" + "auth\r\n");
            fw = writeToFile(fw, "auth_type" + "=" + "userpass\r\n");
            fw = writeToFile(fw, "password" + "=" + speakers.get(i).getPassword() + "\r\n");
            fw = writeToFile(fw, "username" + "=" + speakers.get(i).getUserName() + "\r\n\n");

            fw = writeToFile(fw, "[" + speakers.get(i).getUserName() + "]\r\n");
            fw = writeToFile(fw, "type" + "=" + "aor\r\n");
            fw = writeToFile(fw, "max_contacts" + "=" + "1\r\n\n");

        }
        fw.close();
        return false;
    }
}
