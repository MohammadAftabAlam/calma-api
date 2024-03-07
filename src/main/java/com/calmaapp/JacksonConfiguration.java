package com.calmaapp;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register UserType serializer and deserializer
        SimpleModule userTypeModule = new SimpleModule();
        userTypeModule.addSerializer(UserType.class, new UserTypeSerializer());
        userTypeModule.addDeserializer(UserType.class, new UserTypeDeserializer());
        objectMapper.registerModule(userTypeModule);

    
        SimpleModule module = new SimpleModule();
        module.addSerializer(HibernateProxy.class, new HibernateProxySerializer());
        objectMapper.registerModule(module);
        objectMapper.addMixIn(HibernateProxy.class, HibernateProxyMixin.class);


        // return objectMapper;
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
    }



// import com.calmaapp.UserType;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.module.SimpleModule;
// import org.hibernate.proxy.HibernateProxy;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// @Configuration
// public class JacksonConfiguration {
    
//     private static final Logger logger = LoggerFactory.getLogger(JacksonConfiguration.class);

//     @Bean
//     public ObjectMapper objectMapper() {
//         ObjectMapper objectMapper = new ObjectMapper();

//         // Register UserType serializer and deserializer
//         SimpleModule userTypeModule = new SimpleModule();
//         userTypeModule.addSerializer(UserType.class, new UserTypeSerializer());
//         userTypeModule.addDeserializer(UserType.class, new UserTypeDeserializer());
//         objectMapper.registerModule(userTypeModule);

//         // Register custom module to ignore Hibernate proxy objects
//         objectMapper.registerModule(new HibernateProxyModule());
//         logger.info("HibernateProxyModule registered with ObjectMapper."); // Add this log statement

//         return objectMapper;
//     }
   




