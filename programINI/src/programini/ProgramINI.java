/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package programini;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.*;
import org.ini4j.Profile.Section;

/**
 *
 * @author user
 */
public class ProgramINI {

    static String sectionName[] = new String[10];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File file = new File("pjsip.ini");
        try {
            boolean bool = file.createNewFile();
            System.out.println(file.getName());
            Wini ini = new Wini(file);
            ini.getConfig().setMultiSection(false);
            if (file.exists()) {
                ini.add("GUNA", "native", "Ramnad");
                ini.add("village", "alagankulam", "fufbfbfb");
                ini.add("GUNA", "dbdgdbd", "guna");
                System.out.println("successFully file is Created");
                ini.put("GUNA", "name", String.class);
                ini.put("password", "password", 0);
//               

                for (String data : ini.keySet()) {
                    int i = 0;
                    sectionName[i] = data;
                    i++;
                    System.out.println("Values in ini File are : " + data);
                }
                ini.store();
                ini.forEach((t, u) -> {
                    System.out.println("t : " + t + " u " + u);
                });

                Collection<Profile.Section> con = ini.values();
                Iterator itr = con.iterator();
                if (itr.hasNext()) {
                    System.out.println("Using iterator : " + itr.next());
                }
                
                con.forEach((a) -> {
                    System.out.println(" a : " + a);
                });

                Profile.Section section = null;
                int count = 1;
                for (Profile.Section sections : con) {
                    System.out.println(" Section  Object : " + sections);
                    System.out.println(" Section get Name : " + ini.get(section.getName()));
                    section = ini.get(sections.getName());
                    System.out.println(" Section Object : " + section);
//               } Profile.Section section = null;
//                System.out.println(" section for getName : " + section.getName());

//                System.out.println("Ini Values Method Is Invoked : " + ini.values());
                }
            }
            //Key are all denoted as t and Values are all denoted as u
        } catch (Exception e) {
            try {
                throw new Exception("enter correct file Path");
            } catch (Exception ex) {
                Logger.getLogger(ProgramINI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
