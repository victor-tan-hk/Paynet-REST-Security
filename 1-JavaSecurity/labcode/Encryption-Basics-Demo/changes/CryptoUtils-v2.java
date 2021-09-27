package com.workshop.basics;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import java.security.Provider;
import java.security.Security;

public class CryptoUtils {
  

  // Note that keyBitSize must be 128, 192 or 256 for AES
  public static SecretKey createKey(String algorithm, int keyBitSize) {
    KeyGenerator keyGenerator = null;
    try {
      keyGenerator = KeyGenerator.getInstance(algorithm);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    SecureRandom secureRandom = new SecureRandom();

    keyGenerator.init(keyBitSize, secureRandom);
    return keyGenerator.generateKey();

  }
  
  // Generate Initialization Vector 
  // which is required for the use with AES CBC / AES GCM
  public static IvParameterSpec createIv(int byteLength) {
    byte[] iv = new byte[byteLength];
    new SecureRandom().nextBytes(iv);
    return new IvParameterSpec(iv);
  }
  
  
  // encryption for ECB
  public static byte[] encryptMessage(String algorithmTransform, byte[] message, SecretKey secretKey) {

    Cipher cipher;
    byte[] encryptedMessage = null;
    try {
      
      cipher = Cipher.getInstance(algorithmTransform);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      encryptedMessage = cipher.doFinal(message);

    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } finally {

    }
    return encryptedMessage;

  }
  
  // encryption for CBC
  public static byte[] encryptMessage(String algorithmTransform, byte[] message, SecretKey secretKey, IvParameterSpec iv) {

    Cipher cipher;
    byte[] encryptedMessage = null;
    try {
      
      cipher = Cipher.getInstance(algorithmTransform);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
      encryptedMessage = cipher.doFinal(message);

    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {

    }
    return encryptedMessage;

  }

  
  // decryption for CBC
  public static byte[] decryptMessage(String algorithmTransform, byte[] message, SecretKey secretKey, IvParameterSpec iv) {

    Cipher cipher;
    byte[] decryptedMessage = null;
    try {
      
      cipher = Cipher.getInstance(algorithmTransform);
      cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
      decryptedMessage = cipher.doFinal(message);

    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {

    }
    return decryptedMessage;

  }

  // decryption for ECB
  public static byte[] decryptMessage(String algorithmTransform, byte[] message, SecretKey secretKey) {

    Cipher cipher;
    byte[] decryptedMessage = null;
    try {
      
      cipher = Cipher.getInstance(algorithmTransform);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      decryptedMessage = cipher.doFinal(message);

    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } finally {

    }
    return decryptedMessage;

  }
  

  
  
  // Helper method to demonstrate length of
  // encrypted messages
  public static void listMessageLengths(String algorithm, int sizeLimit, SecretKey keyToUse) {
    
    System.out.println ("\n" + algorithm + " encrypted message lengths for key length of " + keyToUse.getEncoded().length + " bytes");
    
    System.out.println(String.format("%-25s %-25s", "Original message length", "Encrypted message length"));
    for (int i = 0; i < sizeLimit; i++) {
      
      byte[] tempArray = new byte[i+1];
      for (int j = 0; j < i+1; j++) {
        tempArray[j] = 56; // number 8 in ASCII 
      }
      byte[] encryptedSequence = CryptoUtils.encryptMessage(algorithm, tempArray, keyToUse);
      System.out.println(String.format("%-25s %-25s", ""+tempArray.length, ""+encryptedSequence.length));
    }

  }
  
  // List all the providers and the algorithms they support
  // on the given platform
  public static void listAlgorithms() {

    for (Provider provider : Security.getProviders()) {
      System.out.println("\n\nProvider : " + provider.getName());
      System.out.println("*** Services provided **** ");
      for (Provider.Service service : provider.getServices()) {
        String algo = service.getAlgorithm();
        System.out.print(algo + " , ");
      }
    }

  }
  


}
