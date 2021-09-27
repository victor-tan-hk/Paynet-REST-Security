package com.workshop.basics;

import java.util.Arrays;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class ObjectEncryptionDemo {
  
  public static final String ALGORITHM_NAME = "AES";
  public static final String ALGORITHM_TRANSFORMATION = "AES/CBC/PKCS5Padding";

  public static void main(String[] args) {
    
    // Creating a 256 bit key (strongest encryption possible)
    SecretKey firstKey = CryptoUtils.createKey(ALGORITHM_NAME, 256);
    
    System.out.println("firstKey contents are : ");
    System.out.println(Arrays.toString(firstKey.getEncoded()));
    
    BasicUtils.waitKeyPress();
    
    Developer dev = new Developer("Spiderman", 33, true);
    System.out.println ("Contents of original object to be encrypted");
    System.out.println(dev);
    
    // Generate IV required for CBC mode of operation
    // Here we use 16 bytes
    IvParameterSpec iv = CryptoUtils.createIv(16);
    
    SealedObject so = CryptoUtils.encryptObject(ALGORITHM_TRANSFORMATION, dev, firstKey, iv);
    
    System.out.println ("\nContents of encrypted object");
    System.out.println(so.toString());

    Developer decryptedDev = (Developer) CryptoUtils.decryptObject(ALGORITHM_TRANSFORMATION, so, firstKey, iv);
    System.out.println ("\nContents of decrypted object");
    System.out.println(decryptedDev);

  }

}
