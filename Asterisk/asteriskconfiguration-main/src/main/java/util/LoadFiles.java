/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import com.zilogic.asteriskconfiguration.App;
import com.zilogic.asteriskconfiguration.FileUtil;
import java.io.File;
import javafx.stage.FileChooser;

/**
 *
 * @author sridhar
 */
public class LoadFiles {

    static LoadFiles loadfiles = null;
    static File pjsipFile = null;
    static File extFile = null;

    public static LoadFiles getInstance() {
        if (loadfiles == null) {
            loadfiles = new LoadFiles();
            return loadfiles;

        } else {
            return loadfiles;
        }

    }

    public static File getPjsipFile() {
        if (pjsipFile == null) {
            FileChooser chooser = FileUtil.createFileChooser();
            pjsipFile = FileUtil.openFileChooser(chooser, App.mainstage);
        }

        return pjsipFile;
    }

    public static void setPjsipFile(File pjsipFile) {

        LoadFiles.pjsipFile = pjsipFile;
    }

    public static File getExtFile() {
        if (extFile == null) {

            FileChooser chooser = FileUtil.createFileChooser();
            extFile = FileUtil.openFileChooser(chooser, App.mainstage);
        }

        return extFile;
    }

    public static void setExtFile(File extFile) {
        LoadFiles.extFile = extFile;
    }

}
