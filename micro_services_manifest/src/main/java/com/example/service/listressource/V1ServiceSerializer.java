package com.example.service.listressource;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServicePort;

import java.io.IOException;

public class V1ServiceSerializer extends StdSerializer<V1Service> {

    public V1ServiceSerializer() {
        this(null);
    }

    public V1ServiceSerializer(Class<V1Service> t) {
        super(t);
    }

    @Override
    public void serialize(V1Service service, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        // Serialize apiVersion and kind
        jsonGenerator.writeStringField("apiVersion", "v1");
        jsonGenerator.writeStringField("kind", "Service");

        // Serialize metadata
        jsonGenerator.writeObjectFieldStart("metadata");
        jsonGenerator.writeStringField("name", service.getMetadata().getName());
        jsonGenerator.writeStringField("namespace", service.getMetadata().getNamespace());
        jsonGenerator.writeStringField("creationTimestamp", String.valueOf(service.getMetadata().getCreationTimestamp()));
        jsonGenerator.writeObjectField("annotations", service.getMetadata().getAnnotations()); // Serialize annotations
        // Add other metadata fields as needed
        jsonGenerator.writeEndObject(); // End metadata

        // Serialize spec
        jsonGenerator.writeObjectFieldStart("spec");
        jsonGenerator.writeObjectField("selector", service.getSpec().getSelector());
        jsonGenerator.writeArrayFieldStart("ports");
        service.getSpec().getPorts().forEach(port -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("port", port.getPort());
                jsonGenerator.writeStringField("name", port.getName());
                jsonGenerator.writeStringField("protocol", port.getProtocol());
                // Check if nodePort is null before writing
                if (port.getNodePort() != null) {
                    jsonGenerator.writeNumberField("nodePort", port.getNodePort());
                } else {
                    jsonGenerator.writeNullField("nodePort");
                }
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndArray();
        // Add other spec fields as needed
        jsonGenerator.writeEndObject(); // End spec

        // Serialize status
        jsonGenerator.writeObjectFieldStart("status");
        if (service.getStatus() != null && service.getStatus().getLoadBalancer() != null) {
            jsonGenerator.writeStringField("loadBalancer", service.getStatus().getLoadBalancer().toString());
        } else {
            jsonGenerator.writeNullField("loadBalancer");
        }
        // Serialize status fields as needed
        jsonGenerator.writeEndObject(); // End status

        jsonGenerator.writeEndObject(); // End main object
    }
}
