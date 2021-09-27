package com.workshop.basics;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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
  
  // perform a HMAC computation
  public static byte[] computeMac(String algorithm, byte[] message, SecretKey secretKey) {
    
    byte [] digest = new byte[0];
    try {
      Mac mac = Mac.getInstance(algorithm);
      mac.init(new SecretKeySpec(secretKey.getEncoded(), algorithm)); 
      mac.update(message);
      digest = mac.doFinal();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    }
    return digest;
    
  }
  
  public static long computeCRC32(byte[] message) {
    
    Checksum crc32 = new CRC32();
    crc32.update(message, 0, message.length);
    return crc32.getValue();
    
  }
  
  public static long computeCRC32(InputStream stream, int bufferSize) {
    
        CheckedInputStream checkedInputStream = new CheckedInputStream(stream, new CRC32());
        byte[] buffer = new byte[bufferSize];
        try {
          while (checkedInputStream.read(buffer, 0, buffer.length) >= 0) {}
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        return checkedInputStream.getChecksum().getValue();
    }
  

}
