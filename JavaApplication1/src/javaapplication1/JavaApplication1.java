package javaapplication1;

import java.util.Scanner;

public class JavaApplication1 {

    public static void factorial(int arr[]) {

        for (int i = 0; i < arr.length; i++) {
            int k = 2;
            int fact = arr[i];
            boolean flag = false;
            int count = 1;

            while (fact != 1) {
                count++;
                fact = fact / k;
                System.out.println("factorial for "+arr[i]+" "+fact);
                if (fact == 1) {
                    System.out.println("win " + count);
                    flag = true;
                    break;
                }
                k++;

            }
            if (flag == false) {
                System.out.println("lost ");
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int size = 0;
        System.err.println("Enter the  size of the factorial...");
        size = scanner.nextInt();
        int arr[] = new int[size];

        for (int i = 0; i < arr.length; i++) {
            System.out.println("enter the element's  in the array ");
            arr[i] = scanner.nextInt();
        }
        factorial(arr);

    }

}
