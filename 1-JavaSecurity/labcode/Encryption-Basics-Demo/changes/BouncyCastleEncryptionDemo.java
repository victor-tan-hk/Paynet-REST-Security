package com.workshop.basics;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class BouncyCastleEncryptionDemo {
  
  public static final String ALGORITHM_TRANSFORMATION = "AES/CBC/PKCS5Padding";
  public static final String ALGORITHM_NAME = "AES";
  public static final String PROVIDER_NAME = "BC";

  
  // CBC Encryption with specification of a provider
  public static byte[] encryptMessage(String algorithmTransform, String provider, byte[] message, SecretKey secretKey, IvParameterSpec iv) {

    Cipher cipher;
    byte[] encryptedMessage = null;
    try {

      cipher = Cipher.getInstance(algorithmTransform, provider);
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
      e.printStackTrace();
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
    } finally {

    }
    return encryptedMessage;

  }
  
  
  // CBC Decryption with specification of a provider
  public static byte[] decryptMessage(String algorithmTransform, String provider, byte[] message, SecretKey secretKey, IvParameterSpec iv) {

    Cipher cipher;
    byte[] decryptedMessage = null;
    try {

      cipher = Cipher.getInstance(algorithmTransform, provider);
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
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    } finally {

    }
    return decryptedMessage;

  }


  
  
  public static void main(String[] args) {
    
    Security.addProvider(new BouncyCastleProvider());
    
    CryptoUtils.listAlgorithms();
    
    BasicUtils.waitKeyPress();
    
    System.out.println ("Generated secret key");
    SecretKey secretKey = CryptoUtils.createKey(ALGORITHM_NAME, 256);
    
    
    String plainText = "I love Java. Its the most awesome programming language in the world !";
    System.out.println("Original plaintext as a string : ");
    System.out.println(plainText);
    
    // Generate IV required for CBC mode of operation
    // Here we use 16 bytes
    IvParameterSpec iv = CryptoUtils.createIv(16);
    
    byte[] encryptedCBCSequence = encryptMessage(ALGORITHM_TRANSFORMATION, PROVIDER_NAME, plainText.getBytes(), secretKey, iv);
    System.out.println ("\nByte sequence for encrypted ciphertext using CBC");
    System.out.println (Arrays.toString(encryptedCBCSequence));
    System.out.println("CBC Ciphertext as a string : ");
    System.out.println(new String(encryptedCBCSequence));
    System.out.println("CBC Ciphertext as a Base64 encoded string : ");
    String base64String = Base64.getEncoder().encodeToString(encryptedCBCSequence);
    System.out.println(base64String);
    
    byte[] decryptedSequence = decryptMessage(ALGORITHM_TRANSFORMATION, PROVIDER_NAME, encryptedCBCSequence, secretKey, iv);
    System.out.println("\nDecrypted text as a string : ");
    System.out.println(new String(decryptedSequence));
    
    
  }


}
