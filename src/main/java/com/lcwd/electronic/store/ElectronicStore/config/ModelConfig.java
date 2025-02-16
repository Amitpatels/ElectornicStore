package com.lcwd.electronic.store.ElectronicStore.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {

    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
