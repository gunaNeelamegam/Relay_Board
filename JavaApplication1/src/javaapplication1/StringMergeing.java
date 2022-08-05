package javaapplication1;

import java.util.Scanner;

//There are two strings level and radar, write a code to interleave and form them as as a single string lreavdealr
public class StringMergeing {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("enter the 1'st String ");
        String str = scanner.next();
        System.out.println("enter  the 2 'nd String ");
        String str1 = scanner.next();
        char[] ch = str.toCharArray();
        char[] ch1 = str1.toCharArray();

        String str3 = "";
        
         int size = ch.length <ch1.length ? ch.length : ch1.length;
        
        for (int i = 0, j = 0; i < size; i++, j++) {
            str3 = str3 + ch[i] + ch1[i];
        }
        System.out.println(str3);

    }

}
