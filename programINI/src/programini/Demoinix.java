/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programini;

import java.io.File;
import org.ini4j.Wini;

/**
 *
 * @author user
 */
public class Demoinix {

    public static void main(String[] args) throws Exception {

        Wini ini = new Wini(new File("pjsip.ini"));
        String arr[] = ini.getConfig().toString().split("@", 0);

        for (String a : arr) {

            System.out.println("a : " + a.trim());
        }

        //ini.setConfig();
    }
}
//4617c264
/*
package programini;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.*;

/**
 *

public class ProgramINI {


    public static void main(String[] args) {
        File file = new File("pjsip.ini");

        try {

            boolean bool = file.createNewFile();
            File file1 = null;
            file1 = new File("pjsipa2.ini") {
                @Override
                public boolean createNewFile() throws IOException {
                    return super.createNewFile(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
                }
            };
            Wini ini = new Wini(file);
            System.out.println(file.getName());
            System.out.println("file Name : " + file.getName() + "file Path : " + file.getPath() + "file adsolute path : " + file.getAbsolutePath());
            if (file.exists()) {
                Speaker spe1 = new Speaker("guna", "codec", "guna");
                Speaker sp2 = new Speaker("gsdgd", "gggg", "ggg");
                ArrayList<Speaker> spea = new ArrayList<Speaker>();
                spea.add(sp2);
                spea.add(spe1);

                for (Speaker s : spea) {
                    ini.setComment("trying t ini creteinig  ini file formating ");
                    ini.add(s.toString());
                 
                    ini.put(s.getName(), s.getName(), "Name");
                    ini.put(s.getName(), s.getPassword(), "Password");
                    ini.put(s.getName(), s.getCodec(), "codec");
                    System.out.println("Guna details : " + ini.get("guna"));
                    System.out.println(s.toString());

                }
            } else {
                System.out.println("file is not There :");
            }
        } catch (Exception e) {
            try {
                throw new Exception("enter correct file Path");
            } catch (Exception ex) {
                Logger.getLogger(ProgramINI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

*/
