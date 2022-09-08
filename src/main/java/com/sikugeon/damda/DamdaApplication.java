package com.sikugeon.damda;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DamdaApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:secret.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(DamdaApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}
