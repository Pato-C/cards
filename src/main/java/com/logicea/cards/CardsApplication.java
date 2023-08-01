package com.logicea.cards;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class CardsApplication {
    public static Logger logger = LogManager.getLogger("com.logicea.cards");

    public static String appVersion = "1.0";
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(new Class[]{CardsApplication.class});
        Properties properties = new Properties();
        properties.put("server.port", 8088);
        application.setDefaultProperties(properties);
        logger.debug("Starting Cards API with version:" + appVersion);
        application.run(args);
    }

}
