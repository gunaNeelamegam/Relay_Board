package javaapplication1;

import java.util.*;

public class UnionAndIntersectionAndComplement {

    static int union[] = new int[20];
    static int size1;
    static int size2;
    static Scanner scanner;    //union Intersection Complement
//UNION FUCTION

    public static int[] union(int merge[]) {
        int freq[] = new int[merge.length];

        for (int i = 0; i < merge.length; i++) {
            int count = 1;
            for (int j = i + 1; j < merge.length; j++) {
                if (merge[i] == merge[j]) {
                    freq[j] = -1;
                    count++;
                }
                if (freq[i] != -1) {
                    freq[i] = count;
                }
            }
        }
        //displaying the Union elemnt's in the element's in the Array
        //SIZE OF THE ARRAY IS STATIC

        for (int i = 0; i < merge.length; i++) {
            if (freq[i] >= 1) {
                union[i] = merge[i];
            }
        }
        System.out.println(Arrays.toString(union));
        return freq;
    }
//INTERSECTION FUNCTION

    public static int[] intersection(int freq[], int merge[]) {
        int[] intersection = new int[merge.length];

        for (int i = 0; i < merge.length; i++) {
            for (int j = i + 1; j < merge.length; j++) {
                if (freq[i] >= 2) {
                    intersection[i] = merge[i];
                }
            }

        }
        System.out.println(Arrays.toString(intersection));
        return intersection;
    }
//COMPLEMENTS OF A

    public static void complementA(int arr[], int intersection[]) {
        int[] complA = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < intersection.length; j++) {
                if (arr[i] != intersection[j]) {
                    complA[i] = arr[i];
                }
            }
        }
        System.out.println("Complement's of A :" + Arrays.toString(complA));
    }
//COMPLEMENTS OF B

    public static void complementB(int arr1[], int intersection[]) {
        int complB[] = new int[arr1.length];
        for (int i = 0; i < arr1.length; i++) {

            for (int j = 0; j < intersection.length; j++) {
                if (arr1[i] != intersection[j]) {
                    complB[i] = arr1[i];
                }
            }
        }
        System.out.println("Complement's of B is : " + Arrays.toString(complB));
    }

//MERGEING THE DIFFERENT ARRAY
    static int[] merging(int arr[], int arr1[]) {
        int merge[] = new int[arr.length + arr1.length];

        for (int i = 0; i < arr.length; i++) {
            merge[i] = arr[i];
        }
        for (int i = 0; i < arr1.length; i++) {
            int k = arr.length + i;
            merge[k] = arr1[i];
        }

        return merge;

    }
//MAIN FUNCTION

    public static void main(String[] args) throws Exception {

        scanner = new Scanner(System.in);
        System.out.println("enter the size of 1'st Array ");
        size1 = scanner.nextInt();
        int arr[] = new int[size1];
        System.out.println("Enter the Size of 2nd Array ");
        size2 = scanner.nextInt();
        int arr1[] = new int[size2];
        for (int i = 0; i < arr.length; i++) {
            System.out.println("Enter the elements in the Array " + i);
            arr[i] = scanner.nextInt();
        }
        for (int i = 0; i < arr1.length; i++) {
            System.out.println("Enter the 2'nd elements in the Array " + i);
            arr1[i] = scanner.nextInt();
        }
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(arr1));
        int a = 0;
        while (true) {
            System.out.println("Enter the Operation tou what !..");
            try {
                a = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("THE NUMBER IS INVALID...");
                e.printStackTrace();
            } finally {

            }
            switch (a) {
                case 1:
                    int merge[] = merging(arr, arr1);
                    union(merge);
                    break;
                case 2:
                    int merg[] = merging(arr, arr1);
                    int freq[] = union(merg);
                    intersection(freq, merg);
                    break;
                case 3:
                    int mer[] = merging(arr, arr1);
                    int fre[] = union(mer);
                    int inter[] = intersection(fre, mer);
                    complementA(arr, inter);
                    complementB(arr1, inter);
                    break;
                default:
                    System.out.println("Invalid Number access");
            }
        }

    }
}
