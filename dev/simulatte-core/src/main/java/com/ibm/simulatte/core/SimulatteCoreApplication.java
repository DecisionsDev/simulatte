package com.ibm.simulatte.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SimulatteCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimulatteCoreApplication.class, args);
    }

}
