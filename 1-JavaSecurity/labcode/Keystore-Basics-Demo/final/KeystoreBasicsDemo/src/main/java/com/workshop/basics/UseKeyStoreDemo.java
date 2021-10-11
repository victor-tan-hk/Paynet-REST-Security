package com.workshop.basics;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class UseKeyStoreDemo {
  
  public static final String KEYSTORE_PASSWD = "changeit";
  public static final String KEYSTORE_FILE = "firstkeystore.p12";
  
  public static final String SECRETKEY_ALIAS = "firstAESkey";
  
  // There are several keypair / certificates in the keystore
  // We will just use one of them to demonstrate here
  public static final String KEYPAIR_ALIAS = "awesome";

  public static final String ALGORITHM_TRANSFORMATION = "AES/ECB/PKCS5Padding";
  public static final String PLAINTEXTFILENAME = "plaintext.txt";
  public static final String SIGNING_ALGO = "SHA256withRSA";
  
  public static final String DIGITALSIGNATURE_FILE = "digitalsignature.bin";


  
  public static void main(String[] args) {
    
    KeyStore keyStore;
    SecretKey secretKey = null;
    PrivateKey privateKey = null;
    PublicKey publicKey = null;
    try {
      
      System.out.println ("Loading the keystore instance with the contents of the keystore file : " + KEYSTORE_FILE + " using password : " + KEYSTORE_PASSWD);

      keyStore = KeyStore.getInstance("PKCS12");
      keyStore.load(new FileInputStream(KEYSTORE_FILE), KEYSTORE_PASSWD.toCharArray());
      
      System.out.println ("Extracting the secret key with the alias of : " + SECRETKEY_ALIAS);
      
      secretKey = (SecretKey) keyStore.getKey(SECRETKEY_ALIAS, KEYSTORE_PASSWD.toCharArray());
      

      System.out.println ("Extracting the public and private keys from the keypair with the alias of : " + KEYPAIR_ALIAS);

      privateKey = (PrivateKey) keyStore.getKey(KEYPAIR_ALIAS, KEYSTORE_PASSWD.toCharArray());
      Certificate certificate = keyStore.getCertificate(KEYPAIR_ALIAS);
      publicKey = certificate.getPublicKey();
     
      
    } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnrecoverableKeyException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    BasicUtils.waitKeyPress();
    
    System.out.println ("Demonstrating simple ECB encryption using the extracted secret key");
    

    String plainText = "I love Java. Its the most awesome programming language in the world !";
    System.out.println("Original plaintext as a string : ");
    System.out.println(plainText);
    
    
    byte[] encryptedSequence = CryptoUtils.encryptMessage(ALGORITHM_TRANSFORMATION, plainText.getBytes(), secretKey);
    System.out.println("Ciphertext as a string : ");
    System.out.println(new String(encryptedSequence));

    byte[] decryptedSequence = CryptoUtils.decryptMessage(ALGORITHM_TRANSFORMATION, encryptedSequence, secretKey);
    System.out.println("\nDecrypted text as a string : ");
    System.out.println(new String(decryptedSequence));
    
    BasicUtils.waitKeyPress();
    
    System.out.println ("Demonstrating signature creation and verification using the extracted private and public keys");
    
    System.out.println ("Computing digital signature on file : " + PLAINTEXTFILENAME);

    
    byte[] fileBytes = FileUtils.readBytesFromFile(PLAINTEXTFILENAME);
    
    byte[] digSigBytes = CryptoUtils.signMessage(SIGNING_ALGO, privateKey, fileBytes);
    
    String digitalSignatureHex =  Hex.encodeHexString(digSigBytes);
    List<String> stringsToWrite = new ArrayList<String>();
    stringsToWrite.add(digitalSignatureHex);
    FileUtils.writeStringsToFile(DIGITALSIGNATURE_FILE, stringsToWrite);
    System.out.println ("Digital signature written to : " + DIGITALSIGNATURE_FILE);

    BasicUtils.waitKeyPress();
    
    // Read the plaintext file again for the signature verification process
    
    fileBytes = FileUtils.readBytesFromFile(PLAINTEXTFILENAME);
    

    System.out.println ("\nVerifying signature on file ");
    
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
