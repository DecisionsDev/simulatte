package com.ibm.simulatte.online.configs.modelMapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ModelMapperConfig {
    @Bean
    public static ModelMapper modelMapper() {
        return new ModelMapper();
    }

}