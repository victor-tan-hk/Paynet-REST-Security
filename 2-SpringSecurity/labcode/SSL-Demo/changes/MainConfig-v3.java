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


  @Bean
  public RestTemplate restTemplate() {

    SSLContext sslContext = null;
    try {
    	
      // Here we explicitly specify the trust store we want to use
      // which should hold the root CA certificate to validate any 
      // certificate we received from a server during the TLS handshake	
      sslContext = SSLContextBuilder.create()
          .loadTrustMaterial(ResourceUtils.getFile(trustStore), trustStorePasswd.toCharArray()).build();
    } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException
        | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    HttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

    return new RestTemplate(requestFactory);

  }

}  

