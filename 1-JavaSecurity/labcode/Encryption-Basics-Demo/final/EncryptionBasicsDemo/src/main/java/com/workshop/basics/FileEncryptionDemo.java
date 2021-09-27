package com.workshop.basics;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryptionDemo {
  
  public static final String ALGORITHM_NAME = "AES";
  public static final String ALGORITHM_TRANSFORMATION = "AES/CBC/PKCS5Padding";
  public static final int KEY_LENGTH = 128;
  
  public static final String PLAINTEXTFILENAME = "plaintext.txt";
  public static final String DECRYPTEDTEXTFILENAME = "decryptedtext.txt";

  public static final String KEYBINFILENAME = "secretkey.bin";
  public static final String KEYBASE64FILENAME = "secretkey.txt";
  public static final String CIPHERTEXTBINFILENAME = "encryptedtext.bin";
  public static final String CIPHERTEXTBASE64FILENAME = "encryptedtext.txt";   
  
  public static final String OBJECTFILENAME = "object.bin";

  public static void main(String[] args) {
    
    System.out.println("Reading from original plaintext file " + PLAINTEXTFILENAME + " as a list of strings");
    List<String> fileStrings = FileUtils.readStringsFromFile(PLAINTEXTFILENAME);
    String combinedString = "";
    for (String str : fileStrings) {
      combinedString += str + "\n";
    }
    BasicUtils.waitKeyPress();

    System.out.println (combinedString);

    BasicUtils.waitKeyPress();


    SecretKey secretKey = CryptoUtils.createKey(ALGORITHM_NAME, KEY_LENGTH);
    byte[] keySequence = secretKey.getEncoded();
    
    System.out.println ("Secret key byte sequence:");
    System.out.println (Arrays.toString(keySequence));
    
    System.out.println("Writing secret key byte sequence to " + KEYBINFILENAME + " in binary format");
    FileUtils.writeBytesToFile(KEYBINFILENAME, keySequence);
    
    System.out.println("Writing secret key byte sequence to " + KEYBASE64FILENAME + " in Base64 format");
    String base64String = Base64.getEncoder().encodeToString(keySequence);
    List<String> stringsToWrite = new ArrayList<String>();
    stringsToWrite.add(base64String);
    FileUtils.writeStringsToFile(KEYBASE64FILENAME, stringsToWrite);

    // Generate IV required for CBC mode of operation
    // Here we use 16 bytes
    IvParameterSpec iv = CryptoUtils.createIv(16);
    
    System.out.println ("Encrypting plaintext file contents");
    byte[] encryptedCBCSequence = CryptoUtils.encryptMessage(ALGORITHM_TRANSFORMATION, combinedString.getBytes(), secretKey, iv);
    
    System.out.println("Writing encrypted contents to " + CIPHERTEXTBINFILENAME + " in binary format");
    FileUtils.writeBytesToFile(CIPHERTEXTBINFILENAME, encryptedCBCSequence);
    
    System.out.println("Writing encrypted contents to " + CIPHERTEXTBASE64FILENAME + " in Base64 format");
    base64String = Base64.getEncoder().encodeToString(encryptedCBCSequence);
    stringsToWrite = new ArrayList<String>();
    stringsToWrite.add(base64String);
    FileUtils.writeStringsToFile(CIPHERTEXTBASE64FILENAME, stringsToWrite);

    BasicUtils.waitKeyPress();
    
    // We could also have read from KEYBINFILENAME
    System.out.println ("Reading key byte sequence from " + KEYBASE64FILENAME);
    List<String> stringsToRead = FileUtils.readStringsFromFile(KEYBASE64FILENAME);
    byte[] decodedKeySequence = Base64.getDecoder().decode(stringsToRead.get(0));
    
    System.out.println ("Secret key byte sequence:");
    System.out.println (Arrays.toString(decodedKeySequence));
    
    SecretKey keyFromFile = new SecretKeySpec(decodedKeySequence, ALGORITHM_NAME);
    
    BasicUtils.waitKeyPress();
    
    System.out.println ("Reading encrypted contents from " + CIPHERTEXTBASE64FILENAME);
    fileStrings = FileUtils.readStringsFromFile(CIPHERTEXTBASE64FILENAME);
    byte[] encryptedTextSequence = Base64.getDecoder().decode(fileStrings.get(0));
    
    System.out.println ("Decrypting file contents");
    byte[] decryptedSequence = CryptoUtils.decryptMessage(ALGORITHM_TRANSFORMATION, encryptedTextSequence, keyFromFile, iv);
    System.out.println ("Decrypted contents are :");

    BasicUtils.waitKeyPress();
    
    System.out.println (new String(decryptedSequence));
    
    BasicUtils.waitKeyPress();
    
    System.out.println ("Performing incremental encryption by reading from : " +  PLAINTEXTFILENAME + " part by part");
    System.out.println ("then encrypting each part and writing this to : " + CIPHERTEXTBINFILENAME);
    
    File plaintextFile = new File(PLAINTEXTFILENAME);
    File ciphertextFile = new File(CIPHERTEXTBINFILENAME);

    FileUtils.encryptFile(ALGORITHM_TRANSFORMATION, secretKey, iv, plaintextFile, ciphertextFile, 64);

    System.out.println ("Performing incremental decryption by reading from file : " + CIPHERTEXTBINFILENAME + " part by part");
    System.out.println ("then decrypting each part and writing this to " + DECRYPTEDTEXTFILENAME);

    File decryptedtextFile = new File(DECRYPTEDTEXTFILENAME);

    FileUtils.decryptFile(ALGORITHM_TRANSFORMATION, secretKey, iv, ciphertextFile, decryptedtextFile, 64);
    
    
    BasicUtils.waitKeyPress();
    
    System.out.println("Reading from original file " + PLAINTEXTFILENAME + " as a byte array");
    
    byte[] contentToEncrypt = FileUtils.readBytesFromFile(PLAINTEXTFILENAME);

    System.out.println("Encrypting plaintext content using CipherOutputStream and writing encrypted content to : " + CIPHERTEXTBINFILENAME);
    
    ciphertextFile = new File(CIPHERTEXTBINFILENAME);

    FileUtils.encryptWithCipherOutputStream(ALGORITHM_TRANSFORMATION, secretKey, iv, contentToEncrypt, ciphertextFile);
    
    System.out.println("Decrypting encrypted file " + CIPHERTEXTBINFILENAME + " using CipherInputStream");
    
    String decryptedContent = FileUtils.decryptWithCipherInputStream(ALGORITHM_TRANSFORMATION, secretKey, 16, ciphertextFile);

    System.out.println ("Showing decrypted content : ");
    BasicUtils.waitKeyPress();
    
    System.out.println (decryptedContent);

    BasicUtils.waitKeyPress();

    Developer dev = new Developer("Ironman", 22, false);
    System.out.println ("Contents of original object to be encrypted");
    System.out.println(dev);

    System.out.println ("Encrypting object and writing to encrypted content to : " + OBJECTFILENAME);
    SealedObject so = CryptoUtils.encryptObject(ALGORITHM_TRANSFORMATION, dev, secretKey, iv);
    FileUtils.writeObjectToFile(OBJECTFILENAME, so);
    
    BasicUtils.waitKeyPress();

    System.out.println ("Reading encrypted object from : " + OBJECTFILENAME + " and decrypting it");
    SealedObject readObject = (SealedObject) FileUtils.readObjectFromFile(OBJECTFILENAME);
    Developer decryptedDev = (Developer) CryptoUtils.decryptObject(ALGORITHM_TRANSFORMATION, readObject, secretKey, iv);
    System.out.println ("Contents of decrypted object");
    System.out.println(decryptedDev);
    
    
    

    
    
  }
  
  

}
