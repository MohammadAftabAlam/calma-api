package com.calmaapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(UserType.class, new UserTypeSerializer());
        module.addDeserializer(UserType.class, new UserTypeDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}

