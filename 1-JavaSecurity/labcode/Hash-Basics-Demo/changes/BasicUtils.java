package com.workshop.basics;

import java.util.Scanner;

public class BasicUtils {

  // Demonstrating how the length of the digest changes with message length
  public static void listDigestLengths(String algorithm, int lengthLimit) {
    
    System.out.println ("Listing digest lengths for " + algorithm + " algorithm");
    System.out.println(String.format("%-15s %-25s", "Message length", "Digest length"));
    
    for (int i = 0; i < lengthLimit; i++) {

      byte[] messageBytes = new byte[i + 1];
      for (int j = 0; j < i + 1; j++) {
        messageBytes[j] = 56; // number 8 in ASCII
      }

      byte[] hashSequence = HashUtils.performDigest(algorithm, messageBytes);
      System.out.println(String.format("%-15s %-25s", "" + messageBytes.length, "" + hashSequence.length));

    }
    waitKeyPress();

  }

  @SuppressWarnings("resource")
  public static void waitKeyPress() {
    System.out.println("\nPress enter to continue ....");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
    System.out.println();
    // scanner.close();
  }

}
