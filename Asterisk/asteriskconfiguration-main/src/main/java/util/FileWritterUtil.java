/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import com.zilogic.asteriskconfiguration.App;
import com.zilogic.asteriskconfiguration.FileUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javafx.stage.FileChooser;
import model.GroupedSpeakers;
import model.Speaker;
import org.ini4j.Wini;

/**
 *
 * @author sridhar
 */
public class FileWritterUtil {

    /**
     * To create extension for Speakers and grouped Speakers
     *
     * @param filename
     * @param groupedSpeakers
     * @param speakers
     * @throws IOException
     */
    public static void writeExtension(String filename, List<GroupedSpeakers> groupedSpeakers, List<Speaker> speakers) throws IOException {
        
        FileChooser fileChooser = FileUtil.createFileChooser();
        File file = FileUtil.saveFileChooser(fileChooser, App.mainstage);

        file.createNewFile();
        LoadFiles.setExtFile(file);

        BufferedWriter writter = new BufferedWriter(new FileWriter(file));

        // For creating speakers 
        writter.write("[zilogic] \r\n");

        for (Speaker sp : speakers) {
            String userName = sp.getUserName();
            writter.write("exten => " + userName + ",1,Dial(PJSIP/" + userName + ")" + "\r\n");
            writter.write("exten => " + userName + ",2,Playback(tt-weasels)" + "\r\n");
            writter.write("exten => " + userName + ",3,Voicemail(44)" + "\r\n");
            writter.write("exten => " + userName + ",4,Hangup" + "\r\n");
            writter.write("\r\n");
            writter.write("\r\n");
            writter.write("\r\n");
        }

        for (GroupedSpeakers grp : groupedSpeakers) {
            String groupName = grp.getGroupName();
            String page = "Page(";
            for (Speaker groupSpeakers : grp.getSpeakers()) {
                String userName = groupSpeakers.getUserName();
                page += "PJSIP/" + userName;
                page += "&";
            }
            StringBuffer sb = new StringBuffer(page);
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");

            writter.write("exten => " + groupName + ",1," + sb + "\r\n");
            writter.write("exten => " + groupName + ",2,Playback(tt-weasels)" + "\r\n");
            writter.write("exten => " + groupName + ",3,Voicemail(44)" + "\r\n");
            writter.write("exten => " + groupName + ",4,Hangup" + "\r\n");
            writter.write("\r\n");
            writter.write("\r\n");
        }
        writter.close();

    }

}
