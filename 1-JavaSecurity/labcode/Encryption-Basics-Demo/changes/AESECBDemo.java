package com.workshop.basics;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESECBDemo {
  
  public static final String ALGORITHM_NAME = "AES";
  public static final String ALGORITHM_TRANSFORMATION = "AES/ECB/PKCS5Padding";
  
  public static void main( String[] args )  {
    
    // Simplest way to create a secret key **NOT SECURE !!!**
    // Create a hardcoded byte array of the required length: 16, 24, or 32
    // and use SecretKeySpec
    byte[] simpleByteSequence = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    SecretKey firstKey = new SecretKeySpec(simpleByteSequence, ALGORITHM_NAME);
    
    // Correct way to create a secret key
    // Use the KeyGenerator class
    SecretKey secondKey = CryptoUtils.createKey(ALGORITHM_NAME, 128);
    
    
    // Another possible way is to use SecureRandom to generate 
    // a random byte array sequence
    SecureRandom secureRandom = null;
    SecretKey thirdKey = null; 
    try {
      secureRandom = SecureRandom.getInstance("SHA1PRNG");
      byte[] randBytes = new byte[16];
      secureRandom.nextBytes(randBytes);
      thirdKey = new SecretKeySpec(randBytes, ALGORITHM_NAME);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    
    System.out.println("firstKey contents are : ");
    System.out.println(Arrays.toString(firstKey.getEncoded()));
    
    System.out.println("\nsecondKey contents are : ");
    System.out.println(Arrays.toString(secondKey.getEncoded()));
    
    System.out.println("\nthirdKey contents are : ");
    System.out.println(Arrays.toString(thirdKey.getEncoded()));
    
    BasicUtils.waitKeyPress();
    
    CryptoUtils.listAlgorithms();
    
    BasicUtils.waitKeyPress();

    
    String plainText = "I love Java. Its the most awesome programming language in the world !";
    System.out.println ("\nByte sequence for original plaintext");
    System.out.println (Arrays.toString(plainText.getBytes()));
    System.out.println("Original plaintext as a string : ");
    System.out.println(plainText);
    
    
    byte[] encryptedSequence = CryptoUtils.encryptMessage(ALGORITHM_TRANSFORMATION, plainText.getBytes(), firstKey);
    System.out.println ("\nByte sequence for encrypted ciphertext");
    System.out.println (Arrays.toString(encryptedSequence));
    System.out.println("Ciphertext as a string : ");
    System.out.println(new String(encryptedSequence));
    System.out.println("Ciphertext as a Base64 encoded string : ");
    String base64String = Base64.getEncoder().encodeToString(encryptedSequence);
    System.out.println(base64String);

    byte[] decryptedSequence = CryptoUtils.decryptMessage(ALGORITHM_TRANSFORMATION, encryptedSequence, firstKey);
    System.out.println("\nDecrypted text as a string : ");
    System.out.println(new String(decryptedSequence));
    
    BasicUtils.waitKeyPress();

    // The two lines of code below will cause an exception
    // Comment it out to avoid this exception
    System.out.println ("\nAttempting to decrypt the ciphertext using an incorrect key");
    CryptoUtils.decryptMessage(ALGORITHM_TRANSFORMATION, encryptedSequence, secondKey);
    
    System.out.println ("\nAttempting to change the contents of the ciphertext before decryption");
    // changing one of the bytes in the decrypted byte sequence
    encryptedSequence[0] = 10;
    decryptedSequence = CryptoUtils.decryptMessage(ALGORITHM_TRANSFORMATION, encryptedSequence, firstKey);
    System.out.println("Decrypted text as a string : ");
    System.out.println(new String(decryptedSequence));
    
    BasicUtils.waitKeyPress();
    
    CryptoUtils.listMessageLengths(ALGORITHM_TRANSFORMATION, 63, firstKey);
    
    BasicUtils.waitKeyPress();
    
    firstKey = CryptoUtils.createKey(ALGORITHM_NAME, 192);

    CryptoUtils.listMessageLengths(ALGORITHM_TRANSFORMATION, 31, firstKey);
    
    BasicUtils.waitKeyPress();
    
    firstKey = CryptoUtils.createKey(ALGORITHM_NAME, 256);

    CryptoUtils.listMessageLengths(ALGORITHM_TRANSFORMATION, 31, firstKey);
    
    BasicUtils.waitKeyPress();

    
  }
  
  

}
