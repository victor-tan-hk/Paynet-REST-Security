package com.workshop.basics;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;

import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class CryptoUtils {

  // For use with AES GCM encryption mode
  public static final int TAG_LENGTH_BIT = 128;
  
  // For use with PBKDF key generation
  public static final String PBKDF_KEY_ALGO = "AES";
  

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
  
  // Normal RSA keysize is 1024, 2048 and 4096 bits
  public static KeyPair createKeyPair(String algorithm, int keyBitSize) {
    
    KeyPairGenerator generator;
    KeyPair keypair = null;
    try {
      generator = KeyPairGenerator.getInstance(algorithm);
      generator.initialize(keyBitSize);
      keypair = generator.generateKeyPair();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return keypair;

    
  }
  
  
  public static SecretKey createKeyFromPassword(String algorithm, int keyLength, int iterations, String password, byte[] saltSequence) {
    
    SecretKey secretKey = null;
    try {
      SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
      KeySpec spec = new PBEKeySpec(password.toCharArray(), saltSequence, iterations, keyLength);
      secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), PBKDF_KEY_ALGO);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    }

    return secretKey;
    
  }
  
  public static PrivateKey createPrivateKey(String algorithm, byte[] keySequence) {
    
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keySequence);
    PrivateKey privateKey = null;
    try {
      KeyFactory kf = KeyFactory.getInstance(algorithm);
      privateKey = kf.generatePrivate(keySpec);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    }
    return privateKey;
    
  }
  
  public static PublicKey createPublicKey(String algorithm, byte[] keySequence) {
    
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keySequence);
    PublicKey publicKey = null;
    try {
      KeyFactory kf = KeyFactory.getInstance(algorithm);
      publicKey = kf.generatePublic(keySpec);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    }
    return publicKey;
    
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
  public static byte[] encryptMessage(String algorithmTransform, byte[] message, SecretKey secretKey,
      IvParameterSpec iv) {

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

  // encryption for GCM
  public static byte[] encryptMessage(String algorithmTransform, byte[] message, SecretKey secretKey,
      byte[] ivSequence) {

    Cipher cipher;
    byte[] encryptedMessage = null;
    try {

      cipher = Cipher.getInstance(algorithmTransform);
      GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, ivSequence);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
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

  // decryption for GCM
  public static byte[] decryptMessage(String algorithmTransform, byte[] message, SecretKey secretKey,
      byte[] ivSequence) {

    Cipher cipher;
    byte[] decryptedMessage = null;
    try {

      cipher = Cipher.getInstance(algorithmTransform);
      GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, ivSequence);
      cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
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
      e.printStackTrace();
    } finally {

    }
    return decryptedMessage;

  }

  // Encrypt an object in CBC mode
  public static SealedObject encryptObject(String algorithm, Serializable object, SecretKey key, IvParameterSpec iv) {

    Cipher cipher;
    SealedObject sealedObject = null;
    try {
      cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.ENCRYPT_MODE, key, iv);
      sealedObject = new SealedObject(object, cipher);
      return sealedObject;
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return sealedObject;

  }

// Decrypt an object in CBC mode
  public static Serializable decryptObject(String algorithm, SealedObject sealedObject, SecretKey key,
      IvParameterSpec iv) {

    Cipher cipher;
    Serializable unsealedObject = null;
    try {
      cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.DECRYPT_MODE, key, iv);
      unsealedObject = (Serializable) sealedObject.getObject(cipher);
      return unsealedObject;
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }

    return sealedObject;

  }
  
  // Asymmetric Encryption using Private Key
  public static byte[] encryptWithPrivateKey(String algorithmTransform, byte[] message, PrivateKey privateKey) {
    Cipher cipher;
    byte[] encryptedMessage = null;
    try {

      cipher = Cipher.getInstance(algorithmTransform);
      cipher.init(Cipher.ENCRYPT_MODE, privateKey);
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
  
  //Asymmetric Encryption using Public Key
  public static byte[] encryptWithPublicKey(String algorithmTransform, byte[] message, PublicKey publicKey) {
    Cipher cipher;
    byte[] encryptedMessage = null;
    try {

      cipher = Cipher.getInstance(algorithmTransform);
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
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
 
  //Asymmetric Decryption using Private Key
  public static byte[] decryptWithPrivateKey(String algorithmTransform, byte[] message, PrivateKey privateKey) {

    Cipher cipher;
    byte[] decryptedMessage = null;
    try {

      cipher = Cipher.getInstance(algorithmTransform);
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      decryptedMessage = cipher.doFinal(message);

    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } 
    return decryptedMessage;

  }

  
  //Asymmetric Decryption using Public Key
  public static byte[] decryptWithPublicKey(String algorithmTransform, byte[] message, PublicKey publicKey) {

    Cipher cipher;
    byte[] decryptedMessage = null;
    try {

      cipher = Cipher.getInstance(algorithmTransform);
      cipher.init(Cipher.DECRYPT_MODE, publicKey);
      decryptedMessage = cipher.doFinal(message);

    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } 
    return decryptedMessage;

  }
  
  
  
  public static byte[] signMessage(String algorithm, PrivateKey privateKey, byte[] message) {
    
    byte[] signatureSequence = new byte[0];
    try {
      Signature signature = Signature.getInstance(algorithm);
      signature.initSign(privateKey);
      signature.update(message);
      signatureSequence = signature.sign();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (SignatureException e) {
      e.printStackTrace();
    }
    
    return signatureSequence;
    
  }
  
  public static boolean verifySignedMessage(String algorithm, PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes) {
    
    boolean isCorrect = false;    
    try {
      Signature signature = Signature.getInstance(algorithm);
      signature.initVerify(publicKey);
      signature.update(messageBytes);
      isCorrect = signature.verify(signatureBytes);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (SignatureException e) {
      e.printStackTrace();
    }
    
    return isCorrect;
    
  }

  
  // Helper method to demonstrate length of
  // encrypted messages
  public static void listMessageLengths(String algorithm, int sizeLimit, SecretKey keyToUse) {

    System.out.println(
        "\n" + algorithm + " encrypted message lengths for key length of " + keyToUse.getEncoded().length + " bytes");

    System.out.println(String.format("%-25s %-25s", "Original message length", "Encrypted message length"));
    for (int i = 0; i < sizeLimit; i++) {

      byte[] tempArray = new byte[i + 1];
      for (int j = 0; j < i + 1; j++) {
        tempArray[j] = 56; // number 8 in ASCII
      }
      byte[] encryptedSequence = CryptoUtils.encryptMessage(algorithm, tempArray, keyToUse);
      System.out.println(String.format("%-25s %-25s", "" + tempArray.length, "" + encryptedSequence.length));
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
