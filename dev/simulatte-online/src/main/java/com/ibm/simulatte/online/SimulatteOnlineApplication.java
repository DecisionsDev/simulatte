package com.ibm.simulatte.online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*"com.ibm.simulatte.core.configs",
        "com.ibm.simulatte.core.datamodels",
        "com.ibm.simulatte.core.dto",
        "com.ibm.simulatte.core.exception",
        "com.ibm.simulatte.core.execution.online",
        "com.ibm.simulatte.core.execution.common",
        "com.ibm.simulatte.core.execution.analytic",
        "com.ibm.simulatte.core.repositories",
        "com.ibm.simulatte.core.services",
        "com.ibm.simulatte.core.utils",*/
@SpringBootApplication(scanBasePackages = {"com.ibm.simulatte.online"})
@EntityScan(basePackages = {"com.ibm.simulatte.core.datamodels"})
@ComponentScan(basePackages = {
        "com.ibm.simulatte.core",
        "com.ibm.simulatte.online"
})
public class SimulatteOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimulatteOnlineApplication.class, args);
    }

}
