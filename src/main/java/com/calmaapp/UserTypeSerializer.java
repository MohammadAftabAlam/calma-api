package com.calmaapp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class UserTypeSerializer extends StdSerializer<UserType> {

    public UserTypeSerializer() {
        super(UserType.class);
    }

    @Override
    public void serialize(UserType userType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(userType.name());
    }
}
