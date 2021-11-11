package com.workshop.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Scanner;

import javax.crypto.SecretKey;
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

	public static final String KEYSTORE_PASSWD = "changeit";
	public static final String SHARED_KEYSTORE_FILE = "shared-keystore.p12";
	public static final String SERVER_KEYSTORE_FILE = "server-keystore.p12";
	public static final String CLIENT_TRUSTSTORE_FILE = "client-truststore.p12";

	public static final String SECRETKEY_ALIAS = "shared-secretKey";
	public static final String KEYPAIR_ALIAS = "server-keypair";
	public static final String CERT_ALIAS = "server-public";

	@Override
	public void run(String... args) throws Exception {

		KeyStore keyStore;
		SecretKey secretKey = null;
		PrivateKey privateKey = null;
		PublicKey publicKey = null;
		try {

			log.info("Loading the keystore instance with the contents of the keystore file : " + SHARED_KEYSTORE_FILE
					+ " using password : " + KEYSTORE_PASSWD);

			keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(new FileInputStream(SHARED_KEYSTORE_FILE), KEYSTORE_PASSWD.toCharArray());

			log.info("Extracting the secret key with the alias of : " + SECRETKEY_ALIAS);

			secretKey = (SecretKey) keyStore.getKey(SECRETKEY_ALIAS, KEYSTORE_PASSWD.toCharArray());

			log.info("Loading the keystore instance with the contents of the keystore file : " + SERVER_KEYSTORE_FILE
					+ " using password : " + KEYSTORE_PASSWD);

			keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(new FileInputStream(SERVER_KEYSTORE_FILE), KEYSTORE_PASSWD.toCharArray());

			log.info("Extracting the private key from the keypair with the alias of : " + KEYPAIR_ALIAS);

			privateKey = (PrivateKey) keyStore.getKey(KEYPAIR_ALIAS, KEYSTORE_PASSWD.toCharArray());

			log.info("Loading the keystore instance with the contents of the keystore file : " + CLIENT_TRUSTSTORE_FILE
					+ " using password : " + KEYSTORE_PASSWD);

			keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(new FileInputStream(CLIENT_TRUSTSTORE_FILE), KEYSTORE_PASSWD.toCharArray());

			log.info("Extracting the public key from the certificate with the alias of : " + CERT_ALIAS);
			Certificate certificate = keyStore.getCertificate(CERT_ALIAS);
			publicKey = certificate.getPublicKey();

		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	     // This should be a unique value
	      // so technically must be UUID or a nonce from a CSPRNG
	      String id = "123456";
	      
	      // Hardcode some dummy values for demo
	      String issuer = "Marvel Universe";
	      String subject = "Spiderman";
	      
	      String jwtString = null;
	      
	      log.info("Signing JWT using secret key with alias of " + SECRETKEY_ALIAS + " from " + SHARED_KEYSTORE_FILE);
	      jwtString = createJWT(id,issuer,subject,secretKey);
	      log.info("Generated JWT : " + jwtString);

	      waitKeyPress();

	      log.info("Decoding and verifying JWS using the same secret key that was used in signing");
	      decodeJWT(jwtString, secretKey);
	      
	      waitKeyPress();  
	      
	      log.info("Signing JWT using private key with alias of " + KEYPAIR_ALIAS + " from " + SERVER_KEYSTORE_FILE);
	      jwtString = createJWT(id,issuer,subject,privateKey);
	      log.info("Generated JWT : " + jwtString);
	      
	      waitKeyPress();  

	      log.info("Verifying JWS using the public key with alias of " + CERT_ALIAS + " from " + CLIENT_TRUSTSTORE_FILE);
	      decodeJWT(jwtString, publicKey);


	}
	

	// Include here only 3 standard claims just for demo purposes
	// Of course, can also include all the other standard claims - aud, exp, nbf
	// and also other non-standard claims if necessary
	public String createJWT(String id, String issuer, String subject, Key keyToUse) {

		Date now = new Date(System.currentTimeMillis());

		JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setIssuer(issuer)
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