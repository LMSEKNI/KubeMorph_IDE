package com.example.service.listressource;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.kubernetes.client.openapi.models.V1DaemonSet;

import java.io.IOException;

public class V1DaemonSetSerializer extends StdSerializer<V1DaemonSet> {

    public V1DaemonSetSerializer() {
        this(null);
    }
    public V1DaemonSetSerializer(Class<V1DaemonSet> t) {
        super(t);
    }

    @Override
    public void serialize(V1DaemonSet daemonSet, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        // Serialize apiVersion and kind
        jsonGenerator.writeStringField("apiVersion", "apps/v1");
        jsonGenerator.writeStringField("kind", "DaemonSet");

        // Serialize metadata
        jsonGenerator.writeObjectFieldStart("metadata");
        jsonGenerator.writeStringField("name", daemonSet.getMetadata().getName());
        jsonGenerator.writeStringField("namespace", daemonSet.getMetadata().getNamespace());
        jsonGenerator.writeStringField("uid", daemonSet.getMetadata().getUid());
        jsonGenerator.writeStringField("creationTimestamp", String.valueOf(daemonSet.getMetadata().getCreationTimestamp()));
        jsonGenerator.writeObjectField("annotations", daemonSet.getMetadata().getAnnotations()); // Serialize annotations
        // Add other metadata fields as needed
        jsonGenerator.writeEndObject(); // End metadata

        // Serialize spec
        jsonGenerator.writeObjectFieldStart("spec");
        jsonGenerator.writeObjectField("selector", daemonSet.getSpec().getSelector());
        jsonGenerator.writeObjectField("template", daemonSet.getSpec().getTemplate());
        // Add other spec fields as needed
        jsonGenerator.writeEndObject(); // End spec

        // Serialize status
        jsonGenerator.writeObjectFieldStart("status");
        jsonGenerator.writeNumberField("currentNumberScheduled", daemonSet.getStatus().getCurrentNumberScheduled());
        jsonGenerator.writeNumberField("desiredNumberScheduled", daemonSet.getStatus().getDesiredNumberScheduled());
        jsonGenerator.writeNumberField("numberAvailable", daemonSet.getStatus().getNumberAvailable());
        jsonGenerator.writeNumberField("numberMisscheduled", daemonSet.getStatus().getNumberMisscheduled());
        jsonGenerator.writeNumberField("numberReady", daemonSet.getStatus().getNumberReady());
        jsonGenerator.writeNumberField("numberUnavailable", daemonSet.getStatus().getNumberUnavailable());
        // Add other status fields as needed
        jsonGenerator.writeEndObject(); // End status

        jsonGenerator.writeEndObject(); // End main object
    }
}

