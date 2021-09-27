package com.workshop.basics;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.codec.binary.Hex;


public class MD5SHADemo {
  
  public static final String PLAINTEXTFILENAME = "plaintext.txt";

  
  public static void main(String[] args) {
    
    String message = "I love Java. Its the most awesome programming language in the world !";
    System.out.println ("Message to perform MD5 hash on : " + message);
    
    byte[] hashSequence = HashUtils.performDigest("MD5", message.getBytes());
    System.out.println ("\nByte sequence for MD5 hash");
    System.out.println (Arrays.toString(hashSequence));
    System.out.println ("Size of MD5 hash in bytes : " + hashSequence.length);
    System.out.println("MD5 Hash as a Base64 encoded string : ");
    String base64String = Base64.getEncoder().encodeToString(hashSequence).toUpperCase();
    System.out.println(base64String);
    System.out.println("MD5 Hash as a Hex encoded string : ");
    String hexString = Hex.encodeHexString(hashSequence).toUpperCase();
    System.out.println (hexString);    
    
    
    BasicUtils.waitKeyPress();
    
    // Reading from plaintext file
    List<String> fileStrings = FileUtils.readStringsFromFile(PLAINTEXTFILENAME);
    String combinedString = "";
    System.out.println ("Read from file : " + PLAINTEXTFILENAME);
    for (String str : fileStrings) {
      combinedString += str + "\n";
    }

    hashSequence = HashUtils.performDigest("MD5", combinedString.getBytes());
    System.out.println("MD5 Hash on the file contents as a Hex encoded string : ");
    hexString = Hex.encodeHexString(hashSequence).toUpperCase();
    System.out.println (hexString);
    
    BasicUtils.waitKeyPress();
    
    System.out.println("Performing SHA-256 on the message");
    hashSequence = HashUtils.performDigest("SHA-256", message.getBytes());
    System.out.println ("Size of hash in bytes : " + hashSequence.length);
    System.out.println("SHA-256 Hash as a Hex encoded string : ");
    hexString = Hex.encodeHexString(hashSequence).toUpperCase();
    System.out.println (hexString);

    BasicUtils.waitKeyPress();

    System.out.println("Performing SHA-512 on the message");
    hashSequence = HashUtils.performDigest("SHA-512", message.getBytes());
    System.out.println ("Size of hash in bytes : " + hashSequence.length);
    System.out.println("SHA-512 Hash as a Hex encoded string : ");
    hexString = Hex.encodeHexString(hashSequence).toUpperCase();
    System.out.println (hexString);

    BasicUtils.waitKeyPress();
    
    System.out.print("Name of file to compute hash on (press enter to skip ) : ");
    Scanner scanner = new Scanner(System.in);
    String fileToVerify = scanner.nextLine();
    
    if (fileToVerify.length() > 1) {
      
      System.out.print("Name of hash algorithm to use (press enter to use SHA-512) : ");
      String algorithm = scanner.nextLine();
      if (algorithm.length() < 1) 
        algorithm = "SHA-512";
      System.out.print("Precomputed hash provided (enter for none) : ");
      String providedHash = scanner.nextLine();
      
      byte[] fileBytes = FileUtils.readBytesFromFile(fileToVerify);
      hashSequence = HashUtils.performDigest(algorithm, fileBytes);
      System.out.println(algorithm + " Hash on " + fileToVerify + " as a Hex encoded string : ");
      hexString = Hex.encodeHexString(hashSequence).toUpperCase();
      System.out.println (hexString);
      if (providedHash.length() > 1) {
        if (providedHash.toUpperCase().equals(hexString))
          System.out.println ("Computed and provided hashes match");
        else
          System.out.println ("Computed and provided hashes DO NOT match !");
      }
      
      
    }
    
    BasicUtils.waitKeyPress();
    
    BasicUtils.listDigestLengths("MD5",32);
    
    BasicUtils.listDigestLengths("SHA-256",32);

    BasicUtils.listDigestLengths("SHA-512",32);
    
    scanner.close();

    
  }
  
  
}
