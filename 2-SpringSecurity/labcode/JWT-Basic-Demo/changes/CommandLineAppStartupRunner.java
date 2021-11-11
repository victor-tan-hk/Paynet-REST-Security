package com.workshop.security;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import java.util.Date;
import java.util.Scanner;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;




@Component
@Slf4j
public class CommandLineAppStartupRunner implements CommandLineRunner {
	


    @Override
    public void run(String...args) throws Exception {
      
      // This should be a unique value
      // so technically must be UUID or a nonce from a CSPRNG
      String id = "123456";
      
      // Hardcode some dummy values for demo
      String issuer = "Marvel Universe";
      String subject = "Spiderman";
      
      String base64encodedKey = null;
      String jwtString = null;
      
      log.info("Using JJWT library for generating secret key for HMAC-SHA256");
      Key firstSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
      
      base64encodedKey = Encoders.BASE64.encode(firstSecretKey.getEncoded());
      log.info("Key used : " + base64encodedKey);
      
      jwtString = createJWT(id,issuer,subject,firstSecretKey);
      log.info("Generated JWT : " + jwtString);

      waitKeyPress();
      
      log.info("Decoding and verifying JWS using the same secret key that was used in signing");
      decodeJWT(jwtString, firstSecretKey);
      
      waitKeyPress();

      
      log.info("Using JCA library for generating secret key for HMAC-SHA256");
      // HS 256 uses HmacSHA256
      // Key length needs to be minimum of 32 bytes (256 bits)
      // Create random byte sequence using SecureRandom

      SecureRandom secureRandom = new SecureRandom();
      byte[] randomByteSequence = new byte[32];
      secureRandom.nextBytes(randomByteSequence);

      Key secondSecretKey = new SecretKeySpec(randomByteSequence, SignatureAlgorithm.HS256.getJcaName());

      base64encodedKey = Encoders.BASE64.encode(secondSecretKey.getEncoded());
      log.info("Key used : " + base64encodedKey);
      jwtString = createJWT(id,issuer,subject,secondSecretKey);
      log.info("Generated JWT : " + jwtString);

      waitKeyPress();
      
      log.info("Decoding and verifying JWS using the same secret key that was used in signing");
      decodeJWT(jwtString, secondSecretKey);
      
      waitKeyPress();      
      
      log.info("Attempting to verify JWS using a different secret key from the one used in signing");
      decodeJWT(jwtString, firstSecretKey);
      
      waitKeyPress();      
      
      log.info("Using JJWT library for generating key pair for RSA-SHA256 and signing with private key");
      KeyPair firstKeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
      PrivateKey firstPrivateKey = firstKeyPair.getPrivate();
      PublicKey firstPublicKey = firstKeyPair.getPublic();
      jwtString = createJWT(id,issuer,subject,firstPrivateKey);
      log.info("Generated JWT : " + jwtString);
      
      waitKeyPress();
      
      log.info("Decoding and verifying JWS using the public key corresonding to the private key that was used in signing");
      base64encodedKey = Encoders.BASE64.encode(firstPublicKey.getEncoded());
      log.info("Public key used : " + base64encodedKey);      
      decodeJWT(jwtString, firstPublicKey);
      
      waitKeyPress();

      log.info("Using JCA library for generating key pair for RSA-SHA256 and signing with private key");
      KeyPair secondKeyPair = null;
      try {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        // The key size here will determine whether it is RS256 (2048), 
        // RS384 (3072) or RS512 (4096)
        generator.initialize(2048);
        secondKeyPair = generator.generateKeyPair();
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }
      PrivateKey secondPrivateKey = secondKeyPair.getPrivate();
      PublicKey secondPublicKey = secondKeyPair.getPublic();
      jwtString = createJWT(id,issuer,subject,secondPrivateKey);
      log.info("Generated JWT : " + jwtString);
      
      waitKeyPress();
      
      log.info("Decoding and verifying JWS using the public key corresonding to the private key that was used in signing");
      base64encodedKey = Encoders.BASE64.encode(secondPublicKey.getEncoded());
      log.info("Public key used : " + base64encodedKey);      
      decodeJWT(jwtString, secondPublicKey);
      
      waitKeyPress();

      log.info("Attempting to verify JWS using a public key that is not matched to the private key used in signing");
      decodeJWT(jwtString, firstPublicKey);
      
      waitKeyPress(); 

    }
    
    public void testing() {
      log.info("Testing now");
    }
    
    // Include here only 3 standard claims just for demo purposes
    // Of course, can also include all the other standard claims - aud, exp, nbf 
    // and also other non-standard claims if necessary
    public String createJWT(String id, String issuer, String subject, Key keyToUse) {

      Date now = new Date(System.currentTimeMillis());

      JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now)
          .setSubject(subject).setIssuer(issuer)
          .signWith(keyToUse);

      String jwtString = builder.compact();
      return jwtString;

    }

    public void decodeJWT(String jwtString, Key keyToUse) {

      try {

        JwtParser parser = Jwts.parserBuilder().setSigningKey(keyToUse).build();
        Claims claims = parser.parseClaimsJws(jwtString).getBody();

        log.info("JWT ID : " + claims.getId());
        log.info("Subject : " + claims.getSubject());
        log.info("Issuer : " + claims.getIssuer());
        log.info("Issued at : " + claims.getIssuedAt());

      } catch (JwtException e) {
        
        log.info("Error in signature validation");
        log.info(e.getMessage());

      }
     }
    
    private void waitKeyPress() {
      log.info("Press enter to continue ....");
      Scanner scanner = new Scanner(System.in);
      scanner.nextLine();
      log.info("\n");
      // scanner.close();
    }
}