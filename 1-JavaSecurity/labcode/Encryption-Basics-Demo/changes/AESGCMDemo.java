package com.workshop.basics;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKey;

public class AESGCMDemo {
  
  public static final String ALGORITHM_NAME = "AES";
  public static final String ALGORITHM_TRANSFORMATION = "AES/GCM/NoPadding";
  public static final int GCM_IV_LENGTH_BYTE = 12;

  public static void main(String[] args) {
    
    // Creating a 256 bit key (strongest encryption possible)
    SecretKey firstKey = CryptoUtils.createKey(ALGORITHM_NAME, 256);

    System.out.println("firstKey contents are : ");
    System.out.println(Arrays.toString(firstKey.getEncoded()));
    
    BasicUtils.waitKeyPress();
    
    String plainText = "I love Java. Its the most awesome programming language in the world !";
    System.out.println ("\nByte sequence for original plaintext");
    System.out.println (Arrays.toString(plainText.getBytes()));
    System.out.println("Original plaintext as a string : ");
    System.out.println(plainText);
    
    // Generate IV / Nonce required for GCM mode of operation
    // Here we use 12 bytes
    byte[] ivSequence = new byte[GCM_IV_LENGTH_BYTE];
    SecureRandom random = new SecureRandom();
    random.nextBytes(ivSequence);
    
    byte[] encryptedGCMSequence = CryptoUtils.encryptMessage(ALGORITHM_TRANSFORMATION, plainText.getBytes(), firstKey, ivSequence);
    System.out.println ("\nByte sequence for encrypted ciphertext using GCM");
    System.out.println (Arrays.toString(encryptedGCMSequence));
    System.out.println("GCM Ciphertext as a string : ");
    System.out.println(new String(encryptedGCMSequence));
    System.out.println("GCM Ciphertext as a Base64 encoded string : ");
    String base64String = Base64.getEncoder().encodeToString(encryptedGCMSequence);
    System.out.println(base64String);
    
    
    byte[] decryptedSequence = CryptoUtils.decryptMessage(ALGORITHM_TRANSFORMATION, encryptedGCMSequence, firstKey, ivSequence);
    System.out.println("\nDecrypted text as a string : ");
    System.out.println(new String(decryptedSequence));


  }

}
