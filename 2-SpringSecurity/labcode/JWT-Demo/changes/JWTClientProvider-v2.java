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
public class JWTClientProvider {
	
	public static final String KEYSTORE_PASSWD = "changeit";
	public static final String SHARED_KEYSTORE_FILE = "shared-keystore.p12";
	public static final String CLIENT_TRUSTSTORE_FILE = "client-truststore.p12";
	
	public static final String SECRETKEY_ALIAS = "shared-secretKey";
	public static final String CERT_ALIAS = "server-public";
	
	public static final String CLAIMS_SUBJECT = "Wonder Woman";
	public static final String CLAIMS_ISSUER = "DC Universe";

	private KeyStore keyStore;
	private SecretKey secretKey = null;
	private PublicKey publicKey = null;
	
	private MySigningKeyResolver signingKeyResolver;
	
	public JWTClientProvider() {
		
		signingKeyResolver = new MySigningKeyResolver();
		
		try {

			log.info("Loading the keystore instance with the contents of the keystore file : " + SHARED_KEYSTORE_FILE
					+ " using password : " + KEYSTORE_PASSWD);

			keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(new FileInputStream(SHARED_KEYSTORE_FILE), KEYSTORE_PASSWD.toCharArray());

			log.info("Extracting the secret key with the alias of : " + SECRETKEY_ALIAS);

			secretKey = (SecretKey) keyStore.getKey(SECRETKEY_ALIAS, KEYSTORE_PASSWD.toCharArray());
			
			// Adding this secretKey for to key resolver so that is used for signature verification for HS256
			signingKeyResolver.addKeyMapping(SignatureAlgorithm.HS256, secretKey);
			
			log.info("Loading the keystore instance with the contents of the keystore file : " + CLIENT_TRUSTSTORE_FILE
					+ " using password : " + KEYSTORE_PASSWD);

			keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(new FileInputStream(CLIENT_TRUSTSTORE_FILE), KEYSTORE_PASSWD.toCharArray());

			log.info("Extracting the public key from the certificate with the alias of : " + CERT_ALIAS);
			Certificate certificate = keyStore.getCertificate(CERT_ALIAS);
			publicKey = certificate.getPublicKey();
			
			
			// Adding this public key to key resolver so that it is used for signature verification for RS256
			signingKeyResolver.addKeyMapping(SignatureAlgorithm.RS256, publicKey);
			

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
	
	public Key getPublicKey() {
		return publicKey;
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

	public void decodeJWT(String jwtString) {

		try {

			JwtParser parser = Jwts.parserBuilder()
					.setSigningKeyResolver(signingKeyResolver)
					.build();

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
