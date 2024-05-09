package org.ferhat.vetmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.ferhat.vetmanagement.core.config.swagger", "org.ferhat.vetmanagement"})
public class VetManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(VetManagementApplication.class, args);
    }

}
