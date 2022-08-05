/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication1;
import java.util.*;
import java.util.HashSet;
//Hashset does'nt not allow the duplicate element's in the array

public class IntersectionWithHashMap {

    public static Integer[] first={1,1,1,2,3,3354,6,64553,433};
    public static Integer[] second={1,24,6,7,8,9,9,94,4,4,4,4};
    public static void main(String[] args) {
HashSet<Integer> intersection=new HashSet<Integer>();

intersection.addAll(Arrays.asList(first));
intersection.retainAll(Arrays.asList(second));

        System.out.println(intersection);
        
        
        
    }
 
}
