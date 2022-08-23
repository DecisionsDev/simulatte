package com.ibm.simulatte.offline.configs.modelMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ModelMapperConfig {
    @Bean
    public static ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public static ObjectMapper objectMapper() { return new ObjectMapper(); }

}