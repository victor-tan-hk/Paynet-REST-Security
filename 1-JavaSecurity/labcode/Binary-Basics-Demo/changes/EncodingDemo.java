package com.workshop.basics;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class EncodingDemo {
  
  
  public static void main(String[] args) {
    
    // Byte data type ranges from -128 to 127
    byte[] firstArray = {-128, -85, -13, 0, 13, 85, 127};
    System.out.println ("Contents of firstArray");
    System.out.println (Arrays.toString(firstArray));
    
    BasicUtils.displayBinaryArray(firstArray);
    
    
    String normalCharString = "#$% 123 abc |}";
    
    System.out.println ("String to display : " + normalCharString);
    System.out.println ("Displaying byte array using ASCII encoding");
    BasicUtils.displayBinaryArray(normalCharString.getBytes(StandardCharsets.US_ASCII));
    
    System.out.println ("String to display : " + normalCharString);
    System.out.println ("Displaying byte array using ISO 8859-1 encoding");
    BasicUtils.displayBinaryArray(normalCharString.getBytes(StandardCharsets.ISO_8859_1));


    String extendedCharString = "£¤ ØÙ þÿ";
    
    System.out.println ("String to display : " + extendedCharString);
    System.out.println ("Displaying byte array for string using ASCII encoding");
    BasicUtils.displayBinaryArray(extendedCharString.getBytes(StandardCharsets.US_ASCII));

    System.out.println ("String to display : " + extendedCharString);
    System.out.println ("Displaying byte array for string using ISO 8859-1 encoding");
    BasicUtils.displayBinaryArray(extendedCharString.getBytes(StandardCharsets.ISO_8859_1));

    System.out.println ("String to display : " + extendedCharString);
    System.out.println ("Displaying byte array for string using UTF-8 encoding");
    BasicUtils.displayBinaryArray(extendedCharString.getBytes(StandardCharsets.UTF_8));
    
    String arabCharString = "؆ ؈";
    
    System.out.println ("String to display : " + arabCharString);
    System.out.println ("Displaying byte array for string using ISO 8859-1 encoding");
    BasicUtils.displayBinaryArray(arabCharString.getBytes(StandardCharsets.ISO_8859_1));
        
    System.out.println ("String to display : " + arabCharString);
    System.out.println ("Displaying byte array for string using UTF-8 encoding");
    BasicUtils.displayBinaryArray(arabCharString.getBytes(StandardCharsets.UTF_8));


    
    System.out.println ("Encoding string : " + extendedCharString + " with default platform encoding");
    byte[] encodedArray = extendedCharString.getBytes();
    System.out.println ("Decoding byte array back to String using UTF8");
    System.out.println (new String(encodedArray, StandardCharsets.UTF_8));
    System.out.println ("Decoding byte array back to String using ASCII");
    System.out.println (new String(encodedArray, StandardCharsets.US_ASCII));
    System.out.println ("Decoding byte array back to String using ISO 8859-1");
    System.out.println (new String(encodedArray, StandardCharsets.ISO_8859_1));

    
  }
  
  




}
