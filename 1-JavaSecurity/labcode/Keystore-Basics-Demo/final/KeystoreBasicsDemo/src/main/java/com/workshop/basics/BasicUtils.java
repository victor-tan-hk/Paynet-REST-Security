package com.workshop.basics;

import java.util.Scanner;

public class BasicUtils {


  @SuppressWarnings("resource")
  public static void waitKeyPress() {
    System.out.println("\nPress enter to continue ....");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
    System.out.println();
    // scanner.close();
  }

}
