package javaapplication1;
//import javafx.application.Application; 
//In string lreavdealr, two strings level and radar are interleaved, write a code to deinterleave the level and radar

import java.util.Scanner;

public class Interchanging {

    static Scanner sc = new Scanner(System.in);

    static String splitingString(String str) {
        char[] ch = str.toCharArray();
        str = "";
        String str1 = "";
        for (int i = 0; i < ch.length; i++) {
            if (i % 2 == 0) {
                str = str + ch[i];
            } else {
                str1 = str1 + ch[i];
                
            }
        }
          System.out.println(str);
          System.out.println(str1);
        return str;
    }

    public static void main(String[] args) {

        // getting the String
        System.out.println("Enter the String ");
        String str = sc.next();
        splitingString(str);

    }
}
