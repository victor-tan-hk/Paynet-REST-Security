package com.workshop.basics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
  
  
  public static byte[] performDigest (String algorithm, byte[] message) {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance(algorithm);
      md.update(message);
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return md.digest();
  }
  

}
