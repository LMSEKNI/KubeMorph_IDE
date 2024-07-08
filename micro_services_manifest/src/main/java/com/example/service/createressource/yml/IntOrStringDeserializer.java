package com.example.service.createressource.yml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.kubernetes.client.custom.IntOrString;

import java.io.IOException;

public class IntOrStringDeserializer extends JsonDeserializer<IntOrString> {

    @Override
    public IntOrString deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        if (node.isInt()) {
            return new IntOrString(node.asInt());
        } else if (node.isTextual()) {
            return new IntOrString(node.asText());
        } else {
            throw ctxt.mappingException("Unexpected JSON node type for IntOrString");
        }
    }
}
