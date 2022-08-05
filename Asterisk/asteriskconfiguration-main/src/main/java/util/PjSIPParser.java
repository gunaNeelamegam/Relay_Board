/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import model.Speaker;
import org.ini4j.*;
import org.ini4j.Profile.Section;

/**
 *
 * @author sridhar
 */
public class PjSIPParser {

    /**
     *
     * @param pjsipFile
     * @return List<Speaker> of nodes configured in pjsip.conf file
     * @throws IOException
     */
    public static List<Speaker> getSpeakers(File pjsipFile) throws IOException {
        List<Speaker> speakerList = new ArrayList<Speaker>();
        FileReader reader = new FileReader(pjsipFile);
//        FileOutputStream out = new FileOutputStream(pjsipFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        // PrintWriter writer = new PrintWriter(out);
        Wini ini = new Wini(pjsipFile);

        for (String sectionName : ini.keySet()) {
            System.out.println("ini-keyset :" + ini.keySet());
            Speaker speaker = new Speaker();
            System.out.println("[" + sectionName + "]");
            Section section = ini.get(sectionName);
            //    System.out.println("\t" + optionKey + "=" + section.get(optionKey));
            System.out.println(section.toString());
            speaker.setType(section.get("type"));
            speaker.setContext(section.get("context"));
            speaker.setCodecDisallow(section.get("disallow"));
            speaker.setCodecAllow(section.get("allow"));
            speaker.setAuth(section.get("auth"));
            speaker.setAor(section.get("aors"));
            speaker.setCallerid(section.get("callerid"));
            speaker.setAuth_type(section.get("auth_type"));
            speaker.setPassword(section.get("password"));
            speaker.setUserName(section.get("username"));
            speaker.setMax_contacts(section.get("max_contacts"));
            if (speaker.getUserName() != null) {
                speakerList.add(speaker);
            }
        }
        return speakerList;
    }
}
