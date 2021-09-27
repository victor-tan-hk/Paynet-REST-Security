package com.workshop.basics;

import java.util.Arrays;
import java.util.Base64;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Base64HexDemo {
  
  public static void main(String[] args) {
    
    
    byte[] visibleArray = {49, 50, 51};
    System.out.println ("Decoding visibleArray using UTF-8");
    System.out.println (new String(visibleArray));
    
    byte[] invisibleArray = {0, 6, 12, 18, 24, 30};
    System.out.println ("Invisible Byte Array");
    System.out.println (Arrays.toString(invisibleArray));
    
    System.out.println ("\nDecoding invisibleArray using UTF-8");
    System.out.println (new String(invisibleArray));

    System.out.println ("\nEncoding invisibleArray using Base64");
    String base64String = Base64.getEncoder().encodeToString(invisibleArray);
    System.out.println (base64String);
    
    System.out.println ("\nDecoding base64String back to a byte array");
    byte[] decodedArray = Base64.getDecoder().decode(base64String);
    System.out.println (Arrays.toString(decodedArray));

    System.out.println ("\nEncoding invisibleArray using Hex String");
    String hexString = Hex.encodeHexString(invisibleArray);
    System.out.println (hexString);
    
    try {
      System.out.println ("\nDecoding hexString back to a byte array");
      decodedArray = Hex.decodeHex(hexString);
      System.out.println (Arrays.toString(decodedArray));
    } catch (DecoderException e) {
      e.printStackTrace();
    }


    System.out.println ("\n\nDemonstrating base64 strings");
    
    System.out.println(String.format("%-15s %-25s %-10s", "Array length", "Base64String", "length"));
    for (int i = 0; i < 15; i++) {
      
      byte[] tempArray = new byte[i+1];
      for (int j = 0; j < i+1; j++) {
        tempArray[j] = 56; // number 8 in ASCII 
      }
      base64String = Base64.getEncoder().encodeToString(tempArray);

      System.out.println(String.format("%-15s %-25s %-10s", ""+tempArray.length, base64String,""+base64String.length()));
      
    }
    
    BasicUtils.waitKeyPress();
    
    System.out.println ("Demonstrating Hex strings");
    
    System.out.println(String.format("%-15s %-35s %-10s", "Array length", "Hex String", "length"));

    for (int i = 0; i < 15; i++) {
      
      byte[] tempArray = new byte[i+1];
      for (int j = 0; j < i+1; j++) {
        tempArray[j] = 56; // number 8 in ASCII
      }
      hexString = Hex.encodeHexString(tempArray);

      System.out.println(String.format("%-15s %-35s %-10s", ""+tempArray.length,hexString,""+hexString.length()));
      
    }

    
  }


}
