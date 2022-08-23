package com.ibm.simulatte.online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.ibm.simulatte.online"})
@EntityScan(basePackages = {"com.ibm.simulatte.core.datamodels"})
@ComponentScan(basePackages = {"com.ibm.simulatte.core", "com.ibm.simulatte.online"})
public class SimulatteOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimulatteOnlineApplication.class, args);
    }

}
