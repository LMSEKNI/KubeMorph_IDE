package com.example.service.listressource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.kubernetes.client.openapi.models.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationJackson {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        SimpleModule module = new SimpleModule();
        module.addSerializer(V1Deployment.class, new V1DeploymentSerializer());
        module.addSerializer(V1ReplicaSet.class, new V1ReplicaSetSerializer());
        module.addSerializer(V1DaemonSet.class, new V1DaemonSetSerializer());
        module.addSerializer(V1Service.class, new V1ServiceSerializer());
        module.addSerializer(V1Pod.class, new V1PodSerializer());

        objectMapper.registerModule(module);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        return objectMapper;
    }
}
