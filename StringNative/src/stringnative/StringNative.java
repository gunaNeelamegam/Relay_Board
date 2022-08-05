/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package stringnative;

import java.util.Scanner;

/**
 *
 * @author user
 */
public class StringNative {

    static {
        System.loadLibrary("StringNative1");
    }

    public native static void println(String str);

    /**
     * @param args the command line arguments
     */
    public native static String println()
            ;

    public native static String println(String str, String str1);

    public static void main(String[] args) {
        // TODO code application logic here
        String str = "Guna";
//        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        Scanner sc = new Scanner(System.in);
        System.out.println("enter the String for Concat");
        String str1 = "hello";
        //Printing the String inside the C programming with the Use of JNI Method
        //StringNative.println(str);
        //creating and returning the String
//        System.out.println(StringNative.println());
        StringNative.println(str, str1);
//        System.out.println(StringNative.println(str, str1));
    }

}
