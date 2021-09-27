package com.workshop.basics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Hex;

public class HMACCRCDemo {

  private static  final String MAC_ALGORITHM = "HmacSHA512";  
  public static final String PLAINTEXTFILENAME = "plaintext.txt";

  public static void main(String[] args) {
    
    SecretKey secretKey = CryptoUtils.createKey(MAC_ALGORITHM, 256);
    String message = "I love Java. Its the most awesome programming language in the world !";
    System.out.println ("Message to perform HMAC hash on : " + message);

    
    byte[] hmacSequence = HashUtils.computeMac(MAC_ALGORITHM, message.getBytes(), secretKey);
    System.out.println ("Size of HMAC in bytes : " + hmacSequence.length);
    System.out.println("HMAC as a Hex encoded string : ");
    String hexString = Hex.encodeHexString(hmacSequence).toUpperCase();
    System.out.println (hexString);    
    
    BasicUtils.waitKeyPress();
    
    // Reading from plaintext file
    byte[] fileBytes = FileUtils.readBytesFromFile(PLAINTEXTFILENAME);
    System.out.println ("Read from file : " + PLAINTEXTFILENAME);
    
    long crcNumber = HashUtils.computeCRC32(fileBytes);
    System.out.println ("CRC32 computed on file : " + crcNumber);
    
    BasicUtils.waitKeyPress();

    // Reading from plaintext file using CheckedInputStream 
    System.out.println ("Reading using CheckedInputStream from file : " + PLAINTEXTFILENAME);
    try {
      FileInputStream inputStream = new FileInputStream(PLAINTEXTFILENAME);
      crcNumber = HashUtils.computeCRC32(inputStream, 32);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    System.out.println ("CRC32 computed on file : " + crcNumber);

    
  }

    
}
