package com.workshop.basics;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class SignatureDemo {

  public static final String PLAINTEXTFILENAME = "plaintext.txt";
  public static final String ALGORITHM_NAME = "RSA";
  public static final String DIGEST_ALGO = "SHA-256";
  public static final String SIGNING_ALGO = "SHA256withRSA";
  
  public static final String ENCRYPTEDHASH_FILE = "encryptedhash.bin";
  public static final String DIGITALSIGNATURE_FILE = "digitalsignature.bin";
  
  public static void main(String[] args) {
    
    
    // Creating a 2048 bit keypair 
    KeyPair myKeyPair = CryptoUtils.createKeyPair(ALGORITHM_NAME, 2048);
    PrivateKey privateKey = myKeyPair.getPrivate();
    PublicKey publicKey = myKeyPair.getPublic();
    
    
    byte[] fileBytes = FileUtils.readBytesFromFile(PLAINTEXTFILENAME);
    
    System.out.println ("Computing encrypted hash and digital signature on file : " + PLAINTEXTFILENAME);
    
    // Approach 1 to generate digital signature: 
    // Don't use in production code, just to demo concept
    // First, hash the contents of the message / file 
    // Next, encrypt the hash with the private key
    byte[] fileHash = HashUtils.performDigest(DIGEST_ALGO, fileBytes);
    byte[] encryptedHashBytes = CryptoUtils.encryptWithPrivateKey(ALGORITHM_NAME, fileHash, privateKey);
    
    // Approach 2 to generate digital signature: The correct approach
    // Obtaining digital signature by directly using the Signature class
    byte[] digSigBytes = CryptoUtils.signMessage(SIGNING_ALGO, privateKey, fileBytes);

    
    String encryptedHashHex = Hex.encodeHexString(encryptedHashBytes);
    List<String> stringsToWrite = new ArrayList<String>();
    stringsToWrite.add(encryptedHashHex);
    FileUtils.writeStringsToFile(ENCRYPTEDHASH_FILE, stringsToWrite);
    System.out.println ("Encrypted hash written to : " + ENCRYPTEDHASH_FILE);

    String digitalSignatureHex =  Hex.encodeHexString(digSigBytes);
    stringsToWrite = new ArrayList<String>();
    stringsToWrite.add(digitalSignatureHex);
    FileUtils.writeStringsToFile(DIGITALSIGNATURE_FILE, stringsToWrite);
    System.out.println ("Digital signature written to : " + DIGITALSIGNATURE_FILE);

    BasicUtils.waitKeyPress();
    
    
    // Read the plaintext file again for the signature verification process
    
    fileBytes = FileUtils.readBytesFromFile(PLAINTEXTFILENAME);
    
    
    // Approach 1: To verify
    // Perform hash on file contents (fileHash)
    // Read encrypted hash from file and decrypt it (decryptedHash)
    // Compare both hashes, if same, the signature is validated
    System.out.println ("Verification of signature in approach #1");
    fileHash = HashUtils.performDigest(DIGEST_ALGO, fileBytes);
    encryptedHashHex = FileUtils.readStringsFromFile(ENCRYPTEDHASH_FILE).get(0);
    try {
      encryptedHashBytes = Hex.decodeHex(encryptedHashHex);
    } catch (DecoderException e) {
      e.printStackTrace();
    }
    byte[] decryptedHash = CryptoUtils.decryptWithPublicKey(ALGORITHM_NAME, encryptedHashBytes, publicKey);

    System.out.println ("Hash on file   : " + Hex.encodeHexString(fileHash));
    System.out.println ("Decrypted hash : " + Hex.encodeHexString(decryptedHash));
    if (Hex.encodeHexString(fileHash).contentEquals(Hex.encodeHexString(decryptedHash)))
      System.out.println ("Hashes match. The signature is validated");
    else
      System.out.println ("Hashes do NOT match. The signature is not valid or contents have been altered");
      
 
    // Approach 2: To verify
    // Read file contents and signature 
    // Use Signature class in verification mode
    System.out.println ("\nVerification of signature in approach #2");
    
    digitalSignatureHex = FileUtils.readStringsFromFile(DIGITALSIGNATURE_FILE).get(0);
    try {
      digSigBytes = Hex.decodeHex(digitalSignatureHex);
    } catch (DecoderException e) {
      e.printStackTrace();
    }

  
    boolean signatureValid = CryptoUtils.verifySignedMessage(SIGNING_ALGO, publicKey, fileBytes, digSigBytes);
    if (signatureValid) 
      System.out.println ("Digital signature validated");
    else
      System.out.println ("The signature is not valid or contents have been altered");
      
  }


}
