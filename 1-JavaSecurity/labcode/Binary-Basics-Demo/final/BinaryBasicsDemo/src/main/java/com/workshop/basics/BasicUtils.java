package com.workshop.basics;

import java.util.Scanner;

import org.apache.commons.codec.binary.Hex;

public class BasicUtils {

  // For displaying the contents of a byte array in different formats
  public static void displayBinaryArray(byte[] binArray) {
    
    System.out.println ("\n");
    System.out.println(String.format("%-10s %-10s %-10s %-10s %-10s", "position", "binary", "signed", "unsigned", "hex"));
    
    for (int i = 0; i < binArray.length; i++) {
      
      // Get an unsigned representation of a binary sequence
      // since a byte is a signed 2's complement number
      // by masking with 0xff to get the last 8 bits
      int unsignedNum = binArray[i] & 0xff;
      
      // Use the Hex class from Commons Code
      // to encode a byte array into a Hex string
      byte[] tempArray = new byte[1];
      tempArray[0] = binArray[i];
      String hexSequence = new String(Hex.encodeHex(tempArray));
      
      // Use the toBinaryString method and masking
      // to get the last 8 bits
      String binSequence = String.format("%8s", Integer.toBinaryString(binArray[i] & 0xFF)).replace(' ', '0');
      
      System.out.println(String.format("%-10s %-10s %-10s %-10s %-10s", ""+i, binSequence, ""+binArray[i], ""+unsignedNum, hexSequence));
    }
    waitKeyPress();
    
  }
  
  @SuppressWarnings("resource")
  public static void waitKeyPress() {
    System.out.println("\nPress enter to continue ....");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
    System.out.println("\n");
    // scanner.close();
  }
  
  
}
