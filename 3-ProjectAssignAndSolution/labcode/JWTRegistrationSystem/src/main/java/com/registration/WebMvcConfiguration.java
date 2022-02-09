package com.registration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {

    // navigating to app home should redirect to login
    registry.addViewController("/").setViewName("login");

    registry.addViewController("/login").setViewName("login");

    registry.addViewController("/create-success").setViewName("create-success");

  }

}
