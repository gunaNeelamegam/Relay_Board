/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.GroupedSpeakers;
import model.Speaker;
import org.ini4j.Profile;
import org.ini4j.Wini;

/**
 *
 * @author sridhar
 */
public class ExtensionFileParser {

    private static Map extParser(File extFile) throws IOException {

        FileReader reader = new FileReader(extFile);
        BufferedReader bufferedReader = new BufferedReader(reader);

        Map<String, List<String>> map = new HashMap();

        while (bufferedReader.ready()) {
            String str = bufferedReader.readLine();
//            if (str.contains("Dial")) {
//                String[] strarray = str.split(",");
//                String spk = strarray[0].replaceAll("exten => ", "");
//                List<String> lt = new ArrayList<>();
//                for (String s : str.split("exten =>.*,1,Dial\\(PJSIP/")) {
//                    s = s.replaceAll("\\)", "");
//                    s = s.trim();
//                    if (!s.equals("")) {
//                        lt.add(s);
//                    }
//                }
//                map.put(spk, lt);
//
//            }

            if (str.contains("Page")) {
                List<String> nodes = new ArrayList<>();
                String[] strarray = str.split(",");
                String spk = strarray[0].replaceAll("exten => ", "");

                for (String s : str.split("exten =>.*,1,Page\\(PJSIP/")) {
                    s = s.replaceAll("\\)", "");
                    s = s.replace("&PJSIP/", ",");
                    s = s.trim();
                    if (!s.equals("")) {
                        nodes.add(s);
                    }

                }

                map.put(spk, nodes);

            }

        }

        return map;
    }

    public static List<GroupedSpeakers> parseExtensionFile(File extFile, File pjsipFile) throws IOException {

        Map<String, List<String>> map = ExtensionFileParser.extParser(extFile);
        List<Speaker> speakers = PjSIPParser.getSpeakers(pjsipFile);

        List<GroupedSpeakers> gp = new ArrayList<GroupedSpeakers>();

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            GroupedSpeakers group = new GroupedSpeakers();
            group.setGroupName(entry.getKey());

            List<Speaker> groupedSpeakers = null;

            List<String> lt = entry.getValue();

            for (int i = 0; i < lt.size(); i++) {

                groupedSpeakers = new ArrayList<>();

                String[] str = lt.get(i).split(",");

                if ((str.length != 0)) {
                    for (String s : str) {
                        System.out.println(s);
                        Speaker sp = getSpeaker(s, speakers);
                        groupedSpeakers.add(sp);

                    }
                }
            }

            group.getSpeakers().addAll(groupedSpeakers);
            gp.add(group);

        }

        return gp;
    }

    private static Speaker getSpeaker(String speakerName, List<Speaker> speakers) {

        for (Speaker sp : speakers) {

            if (sp.getUserName().equals(speakerName)) {
                return sp;
            }
        }

        return null;
    }

   public static Map getDialList(File extFile) throws FileNotFoundException, IOException {

        FileReader reader;
        BufferedReader bufferedReader;
        String[] keywords = {"Dial", "Page"};

        Map<String, List<String>> map = new HashMap();
        List<String> singleSpeaker = new ArrayList<>();
        List<String> multipleSpeaker = new ArrayList<>();

        for (String k : keywords) {
            reader = new FileReader(extFile);
            bufferedReader = new BufferedReader(reader);
            System.out.println("-------------");
            System.out.println(k);
            System.out.println("-------------");
            while (bufferedReader.ready()) {

                String str = bufferedReader.readLine();
                if (str.contains(k)) {
                    List<String> nodes = new ArrayList<>();
                    String[] strarray = str.split(",");
                    String spk = strarray[0].replaceAll("exten => ", "");

                    for (String s : str.split("exten =>.*,1," + k + "\\(PJSIP/")) {
                        s = s.replaceAll("\\)", "");
                        s = s.replace("&PJSIP/", ",");
                        s = s.trim();
                        if (!s.equals("")) {
                            nodes.add(s);
                        }

                    }

                    if (nodes.get(0).split(",").length == 1) {
                        singleSpeaker.add(spk);
                    } else {
                        multipleSpeaker.add(spk);
                    }

                }

            }
            bufferedReader.close();
            reader.close();

        }
        map.put("Single", singleSpeaker);
        map.put("Multiple", multipleSpeaker);
        return map;

    }


}
