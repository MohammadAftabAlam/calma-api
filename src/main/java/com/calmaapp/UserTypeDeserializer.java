package com.calmaapp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class UserTypeDeserializer extends StdDeserializer<UserType> {

    public UserTypeDeserializer() {
        super(UserType.class);
    }

    @Override
    public UserType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return UserType.valueOf(node.asText());
    }
}
