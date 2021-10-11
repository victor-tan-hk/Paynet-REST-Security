package com.workshop.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.ssl.SSLContextBuilder;

import org.springframework.util.ResourceUtils;

@Configuration
public class MainConfig {

  @Value("${trust.store.location}")
  private String trustStore;

  @Value("${trust.store.password}")
  private String trustStorePasswd;
  
  @Value("${key.store.location}")
  private String keyStore;

  @Value("${key.store.password}")
  private String keyStorePasswd;

  @Bean
  public RestTemplate restTemplate() {

    SSLContext sslContext = null;
    try {
    
      //  We now load both the trust store and the key store for mutual authentication
    	
      sslContext = SSLContextBuilder.create()
          .loadTrustMaterial(ResourceUtils.getFile(trustStore), trustStorePasswd.toCharArray())
          .loadKeyMaterial(ResourceUtils.getFile(keyStore), keyStorePasswd.toCharArray(), keyStorePasswd.toCharArray())
          .build();
    } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException
        | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnrecoverableKeyException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    HttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

    return new RestTemplate(requestFactory);

  }

}

