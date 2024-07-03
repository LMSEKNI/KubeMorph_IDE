package com.example.service.listressource;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.kubernetes.client.openapi.models.*;

import java.io.IOException;

public class V1DeploymentSerializer extends StdSerializer<V1Deployment> {

    public V1DeploymentSerializer() {
        this(V1Deployment.class);
    }

    public V1DeploymentSerializer(Class<V1Deployment> t) {
        super(t);
    }

    @Override
    public void serialize(V1Deployment deployment, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        // Serialize apiVersion and kind
        jsonGenerator.writeStringField("apiVersion", deployment.getApiVersion());
        jsonGenerator.writeStringField("kind", deployment.getKind());

        // Serialize metadata
        jsonGenerator.writeObjectFieldStart("metadata");
        jsonGenerator.writeStringField("name", deployment.getMetadata().getName());
        jsonGenerator.writeStringField("namespace", deployment.getMetadata().getNamespace());
        jsonGenerator.writeStringField("uid", deployment.getMetadata().getUid()); // Serialize UID
        jsonGenerator.writeStringField("creationTimestamp", String.valueOf(deployment.getMetadata().getCreationTimestamp()));

        // Serialize annotations
        jsonGenerator.writeObjectFieldStart("annotations");
        deployment.getMetadata().getAnnotations().forEach((key, value) -> {
            try {
                jsonGenerator.writeStringField(key, value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndObject(); // End annotations

        jsonGenerator.writeEndObject(); // End metadata

        // Serialize spec
        jsonGenerator.writeObjectFieldStart("spec");
        jsonGenerator.writeNumberField("replicas", deployment.getSpec().getReplicas());

        // Serialize template
        jsonGenerator.writeObjectFieldStart("template");
        jsonGenerator.writeObjectFieldStart("spec");
        jsonGenerator.writeArrayFieldStart("containers");
        deployment.getSpec().getTemplate().getSpec().getContainers().forEach(container -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("image", container.getImage());
                jsonGenerator.writeStringField("imagePullPolicy", container.getImagePullPolicy());
                jsonGenerator.writeStringField("name", container.getName());

                if (container.getEnv() != null) {
                    jsonGenerator.writeArrayFieldStart("env");
                    container.getEnv().forEach(envVar -> {
                        try {
                            jsonGenerator.writeStartObject();
                            jsonGenerator.writeStringField("name", envVar.getName());
                            // Handle valueFrom or value as needed
                            if (envVar.getValueFrom() != null) {
                                jsonGenerator.writeObjectField("valueFrom", envVar.getValueFrom());
                            } else {
                                jsonGenerator.writeStringField("value", envVar.getValue());
                            }
                            jsonGenerator.writeEndObject();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    jsonGenerator.writeEndArray(); // End env
                }

                // Serialize ports
                jsonGenerator.writeArrayFieldStart("ports");
                container.getPorts().forEach(port -> {
                    try {
                        jsonGenerator.writeStartObject();
                        jsonGenerator.writeNumberField("containerPort", port.getContainerPort());
                        jsonGenerator.writeStringField("name", port.getName());
                        jsonGenerator.writeStringField("protocol", port.getProtocol());
                        jsonGenerator.writeEndObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                jsonGenerator.writeEndArray(); // End ports

                jsonGenerator.writeEndObject(); // End container
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndArray(); // End containers
        jsonGenerator.writeEndObject(); // End spec
        jsonGenerator.writeEndObject(); // End template
        jsonGenerator.writeEndObject(); // End spec

        // Serialize status
        jsonGenerator.writeObjectFieldStart("status");
        jsonGenerator.writeNumberField("availableReplicas", deployment.getStatus().getAvailableReplicas());

        // Serialize conditions
        jsonGenerator.writeArrayFieldStart("conditions");
        deployment.getStatus().getConditions().forEach(condition -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("lastTransitionTime", String.valueOf(condition.getLastTransitionTime()));
                jsonGenerator.writeStringField("lastUpdateTime", String.valueOf(condition.getLastUpdateTime()));
                jsonGenerator.writeStringField("message", condition.getMessage());
                jsonGenerator.writeStringField("reason", condition.getReason());
                jsonGenerator.writeStringField("status", condition.getStatus());
                jsonGenerator.writeStringField("type", condition.getType());
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndArray(); // End conditions

        jsonGenerator.writeNumberField("observedGeneration", deployment.getStatus().getObservedGeneration());
        jsonGenerator.writeNumberField("readyReplicas", deployment.getStatus().getReadyReplicas());
        jsonGenerator.writeNumberField("replicas", deployment.getStatus().getReplicas());
        jsonGenerator.writeNumberField("updatedReplicas", deployment.getStatus().getUpdatedReplicas());

        jsonGenerator.writeEndObject(); // End status

        jsonGenerator.writeEndObject(); // End main object
    }
}
