package com.workshop.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
	
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        
    	registry.addViewController("/").setViewName("index");

        registry.addViewController("/view/all").setViewName("viewall");
        registry.addViewController("/view/some").setViewName("viewsome");
        registry.addViewController("/view/single").setViewName("viewsingle");
        
        registry.addViewController("/delete/all").setViewName("deleteall");
        registry.addViewController("/delete/some").setViewName("deletesome");
        registry.addViewController("/delete/single").setViewName("deletesingle");
        
        registry.addViewController("/add").setViewName("add");
        registry.addViewController("/update").setViewName("update");


        
    }

}
