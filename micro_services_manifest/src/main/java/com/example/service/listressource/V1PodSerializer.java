package com.example.service.listressource;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.kubernetes.client.openapi.models.*;

import java.io.IOException;
import java.util.Map;

public class V1PodSerializer extends StdSerializer<V1Pod> {

    public V1PodSerializer() {
        this(null);
    }

    public V1PodSerializer(Class<V1Pod> t) {
        super(t);
    }

    @Override
    public void serialize(V1Pod pod, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        // Serialize apiVersion and kind
        jsonGenerator.writeStringField("apiVersion", pod.getApiVersion());
        jsonGenerator.writeStringField("kind", pod.getKind());

        // Serialize metadata
        jsonGenerator.writeObjectFieldStart("metadata");
        jsonGenerator.writeStringField("name", pod.getMetadata().getName());
        jsonGenerator.writeStringField("namespace", pod.getMetadata().getNamespace());
        jsonGenerator.writeStringField("uid", pod.getMetadata().getUid());

        // Serialize labels if present
        if (pod.getMetadata().getLabels() != null && !pod.getMetadata().getLabels().isEmpty()) {
            jsonGenerator.writeObjectFieldStart("labels");
            for (Map.Entry<String, String> entry : pod.getMetadata().getLabels().entrySet()) {
                jsonGenerator.writeStringField(entry.getKey(), entry.getValue());
            }
            jsonGenerator.writeEndObject(); // End labels
        }

        jsonGenerator.writeEndObject(); // End metadata

        // Serialize spec
        jsonGenerator.writeObjectFieldStart("spec");
        jsonGenerator.writeArrayFieldStart("containers");
        for (V1Container container : pod.getSpec().getContainers()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", container.getName());
            jsonGenerator.writeStringField("image", container.getImage());
            jsonGenerator.writeStringField("imagePullPolicy", container.getImagePullPolicy());

            // Serialize environment variables if present
            if (container.getEnv() != null && !container.getEnv().isEmpty()) {
                jsonGenerator.writeArrayFieldStart("env");
                for (V1EnvVar envVar : container.getEnv()) {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeStringField("name", envVar.getName());
                    if (envVar.getValue() != null) {
                        jsonGenerator.writeStringField("value", envVar.getValue());
                    } else if (envVar.getValueFrom() != null) {
                        jsonGenerator.writeObjectField("valueFrom", envVar.getValueFrom());
                    }
                    jsonGenerator.writeEndObject();
                }
                jsonGenerator.writeEndArray(); // End env
            }

            // Serialize ports if present
            if (container.getPorts() != null && !container.getPorts().isEmpty()) {
                jsonGenerator.writeArrayFieldStart("ports");
                for (V1ContainerPort port : container.getPorts()) {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeNumberField("containerPort", port.getContainerPort());
                    jsonGenerator.writeStringField("name", port.getName());
                    jsonGenerator.writeStringField("protocol", port.getProtocol());
                    jsonGenerator.writeEndObject();
                }
                jsonGenerator.writeEndArray(); // End ports
            }

            jsonGenerator.writeEndObject(); // End container
        }
        jsonGenerator.writeEndArray(); // End containers
        jsonGenerator.writeEndObject(); // End spec

        // Serialize status if needed
        if (pod.getStatus() != null) {
            jsonGenerator.writeObjectFieldStart("status");
            // Add status fields as needed
            jsonGenerator.writeEndObject(); // End status
        }

        jsonGenerator.writeEndObject(); // End main object
    }


}
