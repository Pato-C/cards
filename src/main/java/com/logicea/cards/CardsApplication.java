package com.logicea.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class CardsApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(new Class[]{CardsApplication.class});
        Properties properties = new Properties();
        properties.put("server.port", 8088);
        application.setDefaultProperties(properties);
        application.run(args);
    }

}
