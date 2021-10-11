package com.workshop.basics;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;


import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;
import sun.security.util.ObjectIdentifier;

import sun.security.tools.keytool.CertAndKeyGen;

import javax.crypto.SecretKey;


public class CreateKeyStoreDemo {
  
  public static final String SYMMETRIC_ALGO = "AES";

  public static final String ASYMMETRIC_ALGO = "RSA";

  // This two values to identify the signing algo on the certificate
  // must match
  public static final ObjectIdentifier SIGNING_ALGO_OID = AlgorithmId.sha512WithRSAEncryption_oid;
  public static final String SIGNING_ALGO = "SHA512withRSA";           
      
  
  public static final String KEYSTORE_PASSWD = "changeit";
  public static final String KEYSTORE_FILE = "secondkeystore.p12";
  

  public static final String MOH_DN = "CN=moh.gov.my, O=Kementerian Kesihatan Malaysia, L=Putrajaya, S=Wilayah Persekutuan, C=MY";
  public static final String RHB_DN = "CN=www.rhbgroup.com, O=RHB Bank Berhad, OU=IT, L=Kuala Lumpur, S=Kuala Lumpur, C=MY";
  
  public static final String SECRETKEY_ALIAS = "coolkey";
  public static final String MOH_ALIAS = "moh";
  public static final String RHB_ALIAS = "rhb";
  
  

  public static void main(String[] args) {
    
    System.out.println ("The default keystore type is : " + KeyStore.getDefaultType());
    
    // We need to use PKCS12 type for our keystore because only this type
    // can store symmetric secret keys (JKS cannot)
    System.out.println ("Creating a new keystore PKCS12 type with password : " + KEYSTORE_PASSWD);
    KeyStore keyStore = null;
    try {
      keyStore = KeyStore.getInstance("PKCS12");
      keyStore.load(null, KEYSTORE_PASSWD.toCharArray());

    } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    BasicUtils.waitKeyPress();
    
    
    System.out.println ("Generating a secret key and storing it in keystore under alias : " + SECRETKEY_ALIAS );
    SecretKey secretKey = CryptoUtils.createKey(SYMMETRIC_ALGO, 256);

    KeyStore.SecretKeyEntry secret = new KeyStore.SecretKeyEntry(secretKey);
    KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(KEYSTORE_PASSWD.toCharArray());
    try {
      keyStore.setEntry(SECRETKEY_ALIAS, secret, password);
    } catch (KeyStoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    BasicUtils.waitKeyPress();

    
    System.out.println ("Generating a key pair and self-signed certificate for : " + MOH_DN);
    
    KeyPair mohKeyPair = CryptoUtils.createKeyPair(ASYMMETRIC_ALGO, 2048);
    
    // 1st approach to generating X509 certificate
    // more verbose with specification of all certificate fields
    X509Certificate mohCert = genCertWithDetails(MOH_DN, mohKeyPair, 365, SIGNING_ALGO_OID, SIGNING_ALGO);
    
    X509Certificate[] certificateChain = new X509Certificate[1];
    certificateChain[0] = mohCert;

    System.out.println ("Storing certificate and the corresponding private key under alias : " + MOH_ALIAS);

    try {

      keyStore.setCertificateEntry(MOH_ALIAS, mohCert);
      keyStore.setKeyEntry(MOH_ALIAS, mohKeyPair.getPrivate(), KEYSTORE_PASSWD.toCharArray(), certificateChain);
      
    } catch (KeyStoreException e) {
      e.printStackTrace();
    }
    
    
    BasicUtils.waitKeyPress();
    
    
    // 2nd approach to generating X509 certificate
    // slightly shorter
    
    System.out.println ("Generating a key pair and self-signed certificate for : " + RHB_DN);
    
    CertAndKeyGen certGen = null;
    try {
      certGen = new CertAndKeyGen(ASYMMETRIC_ALGO, SIGNING_ALGO, null);
      certGen.generate(2048);
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException e1) {
      e1.printStackTrace();
    }
    
    // prepare the validity of the certificate
    long validSecs = (long) 365 * 24 * 60 * 60; // valid for one year
    
    System.out.println ("Storing certificate and the corresponding private key under alias : " + RHB_ALIAS);
    
    try {
      X509Certificate rhbCert = certGen.getSelfCertificate(new X500Name(RHB_DN), validSecs);
      
      certificateChain = new X509Certificate[1];
      certificateChain[0] = rhbCert;
      keyStore.setKeyEntry(RHB_ALIAS, certGen.getPrivateKey(), null, certificateChain);
      
    } catch (InvalidKeyException | CertificateException | SignatureException | NoSuchAlgorithmException
        | NoSuchProviderException | IOException | KeyStoreException e1) {
      e1.printStackTrace();
    }


    
    System.out.println ("Writing contents of populated key store to : " + KEYSTORE_FILE);
    try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
      keyStore.store(fos, KEYSTORE_PASSWD.toCharArray());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (KeyStoreException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (CertificateException e) {
      e.printStackTrace();
    }
    
  }
  
  
  
  public static X509Certificate genCertWithDetails(String dname, KeyPair pair, int days, ObjectIdentifier signingAlgoId, String signingAlgo) 
    {

      X509CertInfo info = new X509CertInfo();

      
      X500Name owner = null;
      AlgorithmId algoID = null;
      X509CertImpl cert = null;
      
      try {
        
        // Set DN for owner
        owner = new X500Name(dname);
        
        // Set certificate validity period
        Date from = new Date();
        long milliSecondsInADay = 24L * 60L * 60L * 1000L;
        Date to = new Date(from.getTime() + days * milliSecondsInADay);
        CertificateValidity interval = new CertificateValidity(from, to);
        info.set(X509CertInfo.VALIDITY, interval);
        
        // Use a CSPRNG for a random number for certificate serial number
        // Alternatively, make sure you have a UUID generator, such as Java 8 UUID
        BigInteger serialNum = new BigInteger(64, new SecureRandom());
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(serialNum));
        
        // For self-signed certificate, subject and issuer are exactly the same
        info.set(X509CertInfo.SUBJECT, owner);
        info.set(X509CertInfo.ISSUER, owner);
        
        
        // The public key in the certificate is the public key from the key pair
        info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
        
        // Certificate version is nearly always X509 v3
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        
        // Specifies the signing algorithm to be applied on the certificate
        // using the private key of the issuer
        algoID = new AlgorithmId(signingAlgoId);
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algoID));

        // Implement the cert and sign it with the private key using the specified 
        // signing algorithm
        cert = new X509CertImpl(info);
        PrivateKey privKey = pair.getPrivate();
        cert.sign(privKey, signingAlgo);
        
      } catch (IOException e) {
        e.printStackTrace();
      } catch (CertificateException e) {
        e.printStackTrace();
      } catch (InvalidKeyException e) {
        e.printStackTrace();
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      } catch (NoSuchProviderException e) {
        e.printStackTrace();
      } catch (SignatureException e) {
        e.printStackTrace();
      }
     
      return cert;
    }   
  
  

}
