package com.workshop.basics;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

public class HashesForKeysDemo {
  
  private static  final String PBKDF_ALGORITHM = "PBKDF2WithHmacSHA256";  
  public static final String ALGORITHM_NAME = "AES";
  public static final String ALGORITHM_TRANSFORMATION = "AES/ECB/PKCS5Padding";
  public static final int PBKDF_ITERATION = 65536;

  
  public static void main(String[] args) {
    
    String password = "spiderman";
    
    byte[] md5Sequence = DigestUtils.md5(password);
    System.out.println ("MD5 byte length : " + md5Sequence.length);
    
    byte[]sha256Sequence = DigestUtils.sha256(password);
    System.out.println ("SHA256 byte length : " + sha256Sequence.length);

    
    SecretKey md5Key = new SecretKeySpec(md5Sequence, ALGORITHM_NAME);
    SecretKey sha256Key = new SecretKeySpec(sha256Sequence, ALGORITHM_NAME);

    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    
    SecretKey pbkdfKey = CryptoUtils.createKeyFromPassword(PBKDF_ALGORITHM, 256, PBKDF_ITERATION, password, salt);
    System.out.println ("PBKDF2 byte length : " + pbkdfKey.getEncoded().length);
    
    
    String plainText = "I love Java. Its the most awesome programming language in the world !";
    System.out.println("\nPlain text to be encrypted : " + plainText);
    
    
    System.out.println ("Encrypting using the 3 different keys in ECB mode");
    byte[] md5Encryption = CryptoUtils.encryptMessage(ALGORITHM_TRANSFORMATION, plainText.getBytes(), md5Key);

    byte[] sha256Encryption = CryptoUtils.encryptMessage(ALGORITHM_TRANSFORMATION, plainText.getBytes(), sha256Key);
    
    byte[] pbkdfEncryption = CryptoUtils.encryptMessage(ALGORITHM_TRANSFORMATION, plainText.getBytes(), pbkdfKey);

    System.out.println ("\nEncrypted byte sequence for md5Key");
    System.out.println (Arrays.toString(md5Encryption));

    System.out.println ("\nnEncrypted byte sequence for sha256Key");
    System.out.println (Arrays.toString(sha256Encryption));
    
    System.out.println ("\nnEncrypted byte sequence for pbkdfKey");
    System.out.println (Arrays.toString(pbkdfEncryption));

  }

}
