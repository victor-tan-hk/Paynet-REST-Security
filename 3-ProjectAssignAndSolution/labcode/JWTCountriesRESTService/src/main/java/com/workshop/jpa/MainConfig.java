package com.workshop.jpa;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MainConfig {

  // First approach to instantiate a RestTemplate object
  // using SimpleClientHttpRequestFactory
  @Bean
  public RestTemplate restTemplate() {

    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

    factory.setConnectTimeout(3000);
    factory.setReadTimeout(3000);

    return new RestTemplate(factory);
  }

  // Second approach to instantiate a RestTemplate object
  // using RestTemplateBuilder
  @Bean
  @Primary
  public RestTemplate restTemplate(RestTemplateBuilder builder) {

    return builder.setConnectTimeout(Duration.ofMillis(3000)).setReadTimeout(Duration.ofMillis(3000)).build();
  }

}