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
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JWTServiceProvider {
	
	public static final String KEYSTORE_PASSWD = "changeit";
	public static final String SHARED_KEYSTORE_FILE = "shared-keystore.p12";
	public static final String SERVER_KEYSTORE_FILE = "server-keystore.p12";

	public static final String SECRETKEY_ALIAS = "shared-secretKey";
	public static final String KEYPAIR_ALIAS = "server-keypair";
	
	public static final String CLAIMS_SUBJECT = "Thanos";
	public static final String CLAIMS_ISSUER = "Marvel";

	
	private KeyStore keyStore;
	private SecretKey secretKey = null;
	private PrivateKey privateKey = null;
	
	
	
	public JWTServiceProvider() {
		
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

		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public Key getSecretKey() {
		return secretKey;
	}
	
	public Key getPrivateKey() {
		return privateKey;
	}
	
	public String createJWT(Developer developer, Key keyToUse) {

		Date now = new Date(System.currentTimeMillis());

		JwtBuilder builder = Jwts.builder()
				.setIssuedAt(now)
				.setSubject(CLAIMS_SUBJECT)
				.setIssuer(CLAIMS_ISSUER)
				.claim("name", developer.getName())
				.claim("age", developer.getAge())
				.claim("married", developer.getMarried())
				.signWith(keyToUse);

		String jwtString = builder.compact();
		return jwtString;

	}

	public void decodeJWT(String jwtString, Key keyToUse) {

		try {

			JwtParser parser = Jwts.parserBuilder().setSigningKey(keyToUse).build();
			Claims claims = parser.parseClaimsJws(jwtString).getBody();

			log.info("Subject : " + claims.getSubject());
			log.info("Issuer : " + claims.getIssuer());
			log.info("Issued at : " + claims.getIssuedAt());
			log.info("Name : " + claims.get("name", String.class));
			log.info("Age : " + claims.get("age",Integer.class));
			log.info("Married : " + claims.get("married",Boolean.class));


		} catch (JwtException e) {

			log.info("Error in signature validation");
			log.info(e.getMessage());

		}
	}	
	

}
