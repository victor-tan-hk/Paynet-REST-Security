package com.workshop.basics;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SessionKeyDemo {
    
  public static final String PUBLICKEY_ALGO = "RSA";
  public static final String SYMMETRIC_ALGO = "AES";
  public static final String ECB_TRANSFORMATION = "AES/ECB/PKCS5Padding";


  public static final String ALICE_PUBLICKEY = "alice.publickey";
  public static final String BOB_SECRETKEY = "bob.secretkey";
  
  public static final String ALICE_MESSAGE = "alice.encrypted";
  public static final String BOB_MESSAGE = "bob.encrypted";
  
  
  public static void main(String[] args) {
    
    /*
     * This program simulates the session key exchange that happens during a SSL /
     * TLS handshake. Consider two parties Alice and Bob that wish to communicate securely
     * using symmetric key encryption.
     */
    
    System.out.println ("Alice first creates a public / private keypair");
    KeyPair myKeyPair = CryptoUtils.createKeyPair(PUBLICKEY_ALGO, 2048);
    PrivateKey alicePrivateKey = myKeyPair.getPrivate();
    PublicKey alicePublicKey = myKeyPair.getPublic();
    
    System.out.println ("Alice distributes her public key to the entire world");
    FileUtils.writeBytesToFile(ALICE_PUBLICKEY, alicePublicKey.getEncoded());
    
    BasicUtils.waitKeyPress();
    
    System.out.println ("Bob reads Alice's public key");
    byte[] bytesFromFile = FileUtils.readBytesFromFile(ALICE_PUBLICKEY);
    PublicKey newPublicKey = CryptoUtils.createPublicKey(PUBLICKEY_ALGO, bytesFromFile);
    
    System.out.println ("Bob generates a secret key of his own");
    SecretKey bobSecretKey = CryptoUtils.createKey(SYMMETRIC_ALGO, 256);
    byte[] secretKeyBytes = bobSecretKey.getEncoded();
    System.out.println (Arrays.toString(secretKeyBytes));
    
    System.out.println ("Bob encrypts his secret key with Alice's public key");
    byte[] encryptedKeyBytes = CryptoUtils.encryptWithPublicKey(PUBLICKEY_ALGO, secretKeyBytes, newPublicKey);
    
    System.out.println ("Bob sends this encrypted secret key to Alice");
    FileUtils.writeBytesToFile(BOB_SECRETKEY, encryptedKeyBytes);
    
    BasicUtils.waitKeyPress();
    
    System.out.println ("Alice decrypts the encrypted content with her private key");
    bytesFromFile = FileUtils.readBytesFromFile(BOB_SECRETKEY);
    byte[] decryptedContent = CryptoUtils.decryptWithPrivateKey(PUBLICKEY_ALGO, bytesFromFile, alicePrivateKey);
    System.out.println ("Alice recreates Bob's secret key");
    System.out.println (Arrays.toString(decryptedContent));
    SecretKey aliceSecretKey = new SecretKeySpec(decryptedContent, SYMMETRIC_ALGO);

    BasicUtils.waitKeyPress();
    
    String messageFromAlice = "Hey, do you wanna hang out at Starbucks after work ?";
    System.out.println ("Alice is encrypting this message to send to Bob : " + messageFromAlice);
    byte[] encryptedMessage = CryptoUtils.encryptMessage(ECB_TRANSFORMATION, messageFromAlice.getBytes(), aliceSecretKey);
    
    FileUtils.writeBytesToFile(ALICE_MESSAGE, encryptedMessage);

    BasicUtils.waitKeyPress();
    
    System.out.println ("Bob retrieves Alice's encrypted message and decrypts it with his secret key");
    bytesFromFile = FileUtils.readBytesFromFile(ALICE_MESSAGE);
    decryptedContent = CryptoUtils.decryptMessage(ECB_TRANSFORMATION, bytesFromFile, bobSecretKey);
    System.out.println ("Bob received from Alice : " + new String(decryptedContent));

    String messageFromBob = "Awesome ! Lets meet at 5 p.m ok ? Make sure you have your mask on";
    System.out.println ("Bob is encrypting this response to send to Alice : " + messageFromBob);
    encryptedMessage = CryptoUtils.encryptMessage(ECB_TRANSFORMATION, messageFromBob.getBytes(), bobSecretKey);
    
    FileUtils.writeBytesToFile(BOB_MESSAGE, encryptedMessage);

    BasicUtils.waitKeyPress();
    
    System.out.println ("Alice retrieves Bob's encrypted message and decrypts it with her secret key");
    bytesFromFile = FileUtils.readBytesFromFile(BOB_MESSAGE);
    decryptedContent = CryptoUtils.decryptMessage(ECB_TRANSFORMATION, bytesFromFile, aliceSecretKey);
    System.out.println ("Alice received from Bob : " + new String(decryptedContent));
    
    BasicUtils.waitKeyPress();
    
    System.out.println ("After an extended period of secret communication, Bob proposes to Alice");
    System.out.println ("They marry, live happily ever after and have 2 kids who go on to become CEOs of BitDefender and Kaspersky");
    System.out.println ("***** THE END *******");

  }

}
