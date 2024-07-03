package com.example.service.listressource;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.kubernetes.client.openapi.models.V1ReplicaSet;

import java.io.IOException;

public class V1ReplicaSetSerializer extends StdSerializer<V1ReplicaSet> {

    public V1ReplicaSetSerializer() {
        this(null);
    }

    public V1ReplicaSetSerializer(Class<V1ReplicaSet> t) {
        super(t);
    }

    @Override
    public void serialize(V1ReplicaSet replicaSet, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        // Serialize apiVersion and kind
        jsonGenerator.writeStringField("apiVersion", "apps/v1");
        jsonGenerator.writeStringField("kind", "ReplicaSet");

        // Serialize metadata
        jsonGenerator.writeObjectFieldStart("metadata");
        jsonGenerator.writeStringField("name", replicaSet.getMetadata().getName());
        jsonGenerator.writeStringField("namespace", replicaSet.getMetadata().getNamespace());
        jsonGenerator.writeStringField("uid", replicaSet.getMetadata().getUid()); // Serialize UID
        jsonGenerator.writeStringField("creationTimestamp", String.valueOf(replicaSet.getMetadata().getCreationTimestamp()));
        jsonGenerator.writeObjectField("annotations", replicaSet.getMetadata().getAnnotations()); // Serialize annotations
        // Add other metadata fields as needed
        jsonGenerator.writeEndObject(); // End metadata

        // Serialize spec
        jsonGenerator.writeObjectFieldStart("spec");
        jsonGenerator.writeNumberField("replicas", replicaSet.getSpec().getReplicas());
        jsonGenerator.writeObjectField("selector", replicaSet.getSpec().getSelector());
        jsonGenerator.writeObjectField("template", replicaSet.getSpec().getTemplate());
        // Add other spec fields as needed
        jsonGenerator.writeEndObject(); // End spec

        // Serialize status
        jsonGenerator.writeObjectFieldStart("status");
        jsonGenerator.writeNumberField("availableReplicas", replicaSet.getStatus().getAvailableReplicas());
        jsonGenerator.writeNumberField("fullyLabeledReplicas", replicaSet.getStatus().getFullyLabeledReplicas());
        jsonGenerator.writeNumberField("observedGeneration", replicaSet.getStatus().getObservedGeneration());
        jsonGenerator.writeNumberField("readyReplicas", replicaSet.getStatus().getReadyReplicas());
        jsonGenerator.writeNumberField("replicas", replicaSet.getStatus().getReplicas());
        // Add other status fields as needed
        jsonGenerator.writeEndObject(); // End status

        jsonGenerator.writeEndObject(); // End main object
    }
}

