package com.calmaapp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.hibernate.proxy.HibernateProxy;

import java.io.IOException;

public class HibernateProxySerializer extends JsonSerializer<HibernateProxy> {

    @Override
    public void serialize(HibernateProxy value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Exclude Hibernate proxy properties from serialization
        gen.writeStartObject();
        gen.writeEndObject();
    }
}

