package com.workshop.basics;

import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESCBCDemo {
  
  public static final String ALGORITHM_NAME = "AES";
  public static final String CBC_TRANSFORMATION = "AES/CBC/PKCS5Padding";
  public static final String ECB_TRANSFORMATION = "AES/ECB/PKCS5Padding";


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
    
    
    byte[] encryptedECBSequence = CryptoUtils.encryptMessage(ECB_TRANSFORMATION, plainText.getBytes(), firstKey);
    System.out.println ("\nByte sequence for encrypted ciphertext using ECB");
    System.out.println (Arrays.toString(encryptedECBSequence));
    System.out.println("ECB Ciphertext as a string : ");
    System.out.println(new String(encryptedECBSequence));
    System.out.println("ECB Ciphertext as a Base64 encoded string : ");
    String base64String = Base64.getEncoder().encodeToString(encryptedECBSequence);
    System.out.println(base64String);
    
    // Generate IV required for CBC mode of operation
    // Here we use 16 bytes
    IvParameterSpec iv = CryptoUtils.createIv(16);
    
    byte[] encryptedCBCSequence = CryptoUtils.encryptMessage(CBC_TRANSFORMATION, plainText.getBytes(), firstKey, iv);
    System.out.println ("\nByte sequence for encrypted ciphertext using CBC");
    System.out.println (Arrays.toString(encryptedCBCSequence));
    System.out.println("CBC Ciphertext as a string : ");
    System.out.println(new String(encryptedCBCSequence));
    System.out.println("CBC Ciphertext as a Base64 encoded string : ");
    base64String = Base64.getEncoder().encodeToString(encryptedCBCSequence);
    System.out.println(base64String);
    
    byte[] decryptedSequence = CryptoUtils.decryptMessage(CBC_TRANSFORMATION, encryptedCBCSequence, firstKey, iv);
    System.out.println("\nDecrypted text as a string : ");
    System.out.println(new String(decryptedSequence));
    


  }

}
