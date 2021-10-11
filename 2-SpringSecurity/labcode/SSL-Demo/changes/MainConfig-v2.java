package com.workshop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.HttpClient;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;


@Configuration
public class MainConfig {

  @Bean
  public RestTemplate restTemplate() {

    SSLContext sslContext = null;
    try {
      // Using TrustAllStrategy - validates all certificates
      // Can also have used TrustSelfSignedStrategy - validate all normal self-signed certificates
      sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustAllStrategy()).build();
    } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
      e.printStackTrace();
    }

    HttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

    return new RestTemplate(requestFactory);

  }

}

