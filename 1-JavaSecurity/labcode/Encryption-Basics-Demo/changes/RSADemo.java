package com.workshop.basics;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;


public class RSADemo {
  
  public static final String ALGORITHM_NAME = "RSA";
  
  public static final String PUBLICKEYBINFILENAME = "publickey.bin";
  public static final String PRIVATEKEYBINFILENAME = "privatekey.bin";

  
  public static void main(String[] args) {
    
    // Creating a 2048 bit keypair 
    KeyPair myKeyPair = CryptoUtils.createKeyPair(ALGORITHM_NAME, 2048);
    PrivateKey privateKey = myKeyPair.getPrivate();
    PublicKey publicKey = myKeyPair.getPublic();
    
    System.out.println("private key contents are : ");
    System.out.println(Arrays.toString(privateKey.getEncoded()));
    System.out.println ("private key length is : " + privateKey.getEncoded().length + " bytes");
    
    System.out.println("\npublic key contents are : ");
    System.out.println(Arrays.toString(publicKey.getEncoded()));
    System.out.println ("public key length is : " + publicKey.getEncoded().length + " bytes");


    BasicUtils.waitKeyPress();
    
    String plainText = "I love Java. Its the most awesome programming language in the world !";
    System.out.println ("Plaintext to be encrypted : " + plainText);
    System.out.println ("\nEncrypting plaintext with private key");
    byte[] encryptedSequence = CryptoUtils.encryptWithPrivateKey(ALGORITHM_NAME, plainText.getBytes(), privateKey);

    System.out.println("Ciphertext as a string : ");
    System.out.println(new String(encryptedSequence));
    System.out.println("Ciphertext as a Base64 encoded string : ");
    String base64String = Base64.getEncoder().encodeToString(encryptedSequence);
    System.out.println(base64String);
    
    System.out.println ("\nDecrypting ciphertext with public key");
    byte[] decryptedSequence = CryptoUtils.decryptWithPublicKey(ALGORITHM_NAME, encryptedSequence, publicKey);
    System.out.println("\nDecrypted text as a string : ");
    System.out.println(new String(decryptedSequence));
    
    BasicUtils.waitKeyPress();

    // This will result in an error
    // as we are attempting to decrypt with the same key that we encrypted with
    // Comment this out to remove run time exception
    System.out.println ("\nAttempting to decrypt ciphertext with private key");
    decryptedSequence = CryptoUtils.decryptWithPrivateKey(ALGORITHM_NAME, encryptedSequence, privateKey);
    
    BasicUtils.waitKeyPress();

    System.out.println ("\nEncrypting plaintext with public key");
    encryptedSequence = CryptoUtils.encryptWithPublicKey(ALGORITHM_NAME, plainText.getBytes(), publicKey);

    System.out.println("Ciphertext as a string : ");
    System.out.println(new String(encryptedSequence));
    System.out.println("Ciphertext as a Base64 encoded string : ");
    base64String = Base64.getEncoder().encodeToString(encryptedSequence);
    System.out.println(base64String);
    
    System.out.println ("\nDecrypting ciphertext with private key");
    decryptedSequence = CryptoUtils.decryptWithPrivateKey(ALGORITHM_NAME, encryptedSequence, privateKey);
    System.out.println("\nDecrypted text as a string : ");
    System.out.println(new String(decryptedSequence));
    
    BasicUtils.waitKeyPress();
    
    
    System.out.println("Writing private key byte sequence to " + PRIVATEKEYBINFILENAME + " in binary format");
    FileUtils.writeBytesToFile(PRIVATEKEYBINFILENAME, privateKey.getEncoded());
    System.out.println("Writing public key byte sequence to " + PUBLICKEYBINFILENAME + " in binary format");
    FileUtils.writeBytesToFile(PUBLICKEYBINFILENAME, publicKey.getEncoded());


    BasicUtils.waitKeyPress();

   
    System.out.println ("Reading back byte sequence from  : " + PRIVATEKEYBINFILENAME + " and creating a private key from it");
    byte[] bytesFromFile = FileUtils.readBytesFromFile(PRIVATEKEYBINFILENAME);
    PrivateKey newPrivateKey = CryptoUtils.createPrivateKey(ALGORITHM_NAME, bytesFromFile);
    System.out.println ("private key length is : " + privateKey.getEncoded().length + " bytes");
    
    
    System.out.println ("Reading back byte sequence from  : " + PUBLICKEYBINFILENAME + " and creating a public key from it");
    bytesFromFile = FileUtils.readBytesFromFile(PUBLICKEYBINFILENAME);
    PublicKey newPublicKey = CryptoUtils.createPublicKey(ALGORITHM_NAME, bytesFromFile);
    System.out.println ("public key length is : " + publicKey.getEncoded().length + " bytes");

    BasicUtils.waitKeyPress();

    System.out.println("Performing encryption and decryption with the newly created keys");
    System.out.println ("Plaintext to be encrypted : " + plainText);
    System.out.println ("\nEncrypting plaintext with private key");
    encryptedSequence = CryptoUtils.encryptWithPrivateKey(ALGORITHM_NAME, plainText.getBytes(), newPrivateKey);

    System.out.println("Ciphertext as a string : ");
    System.out.println(new String(encryptedSequence));
    System.out.println("Ciphertext as a Base64 encoded string : ");
    base64String = Base64.getEncoder().encodeToString(encryptedSequence);
    System.out.println(base64String);
    
    System.out.println ("\nDecrypting ciphertext with public key");
    decryptedSequence = CryptoUtils.decryptWithPublicKey(ALGORITHM_NAME, encryptedSequence, newPublicKey);
    System.out.println("\nDecrypted text as a string : ");
    System.out.println(new String(decryptedSequence));
    

    
  }
}
