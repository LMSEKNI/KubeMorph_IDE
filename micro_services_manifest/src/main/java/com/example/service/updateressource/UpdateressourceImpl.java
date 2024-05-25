package com.example.service.updateressource;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.generic.options.PatchOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.KubernetesConfig.KubernetesConfigService;
import com.example.controller.updateressource.UpdateController;
import com.example.service.describeressource.DescServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.PatchUtils;
import io.kubernetes.client.util.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UpdateressourceImpl implements Updateressource {

    
    private KubernetesConfigService kubernetesConfigService = null;
    private DescServiceImpl descServiceImpl;
    @Autowired
    public UpdateressourceImpl (KubernetesConfigService kubernetesConfigService){
        this.kubernetesConfigService =kubernetesConfigService;
    }
    private static final Logger logger = LoggerFactory.getLogger(UpdateressourceImpl.class);


    @Autowired
    private ObjectMapper mapper,objectMapper;
    @Override
    public V1Pod updatePod(String namespace, String podName, String podJson) throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        logger.info("Received request to update pod in namespace: {} with podName: {} and podJson: {}", namespace, podName, podJson);

        CoreV1Api apiInstance = new CoreV1Api(client);

        V1Pod newPod;
        try {
            newPod = objectMapper.readValue(podJson, V1Pod.class);

            // Ensure the new pod JSON has the same name and namespace as the old pod
            V1ObjectMeta metadata = newPod.getMetadata();
            if (metadata == null) {
                metadata = new V1ObjectMeta();
                newPod.setMetadata(metadata);
            }
            metadata.setName(podName);
            metadata.setNamespace(namespace);

            // Example of additional modifications:
            // Add or update labels
            if (metadata.getLabels() != null) {
                metadata.getLabels().put("new-label-key", "new-label-value");
            } else {
                metadata.setLabels(Map.of("new-label-key", "new-label-value"));
            }

            // Add or update annotations
            if (metadata.getAnnotations() != null) {
                metadata.getAnnotations().put("new-annotation-key", "new-annotation-value");
            } else {
                metadata.setAnnotations(Map.of("new-annotation-key", "new-annotation-value"));
            }

            // Only update mutable fields
            V1Pod currentPod = apiInstance.readNamespacedPod(podName, namespace, null);

            // Preserve immutable fields from the currentPod
            newPod.getSpec().setVolumes(currentPod.getSpec().getVolumes());
            for (int i = 0; i < newPod.getSpec().getContainers().size(); i++) {
                V1Container newContainer = newPod.getSpec().getContainers().get(i);
                V1Container currentContainer = currentPod.getSpec().getContainers().get(i);
                newContainer.setVolumeMounts(currentContainer.getVolumeMounts());
            }

        } catch (IOException e) {
            logger.error("Failed to parse new pod JSON", e);
            throw new RuntimeException("Failed to parse new pod JSON", e);
        }

        try {
            V1Pod result = apiInstance.replaceNamespacedPod(podName, namespace, newPod, null, null, null, null);
            logger.info("Pod updated successfully: {}", result.getMetadata().getName());
            return result;
        } catch (ApiException e) {
            if (e.getCode() == 422) {
                // Handle immutable field change by deleting and recreating the pod
                logger.warn("Attempting to delete and recreate the pod due to immutable field change");

                // Delete the existing pod
                apiInstance.deleteNamespacedPod(podName, namespace, null, null, null, null, null, null);

                // Recreate the pod
                V1Pod createdPod = apiInstance.createNamespacedPod(namespace, newPod, null, null, null, null);
                logger.info("Pod recreated successfully: {}", createdPod.getMetadata().getName());
                return createdPod;
            } else {
                logger.error("Exception when calling CoreV1Api#replaceNamespacedPod", e);
                logger.error("Status code: {}", e.getCode());
                logger.error("Reason: {}", e.getResponseBody());
                logger.error("Response headers: {}", e.getResponseHeaders());
                throw new RuntimeException("Failed to update pod", e);
            }
        }
    }

    @Override
    public V1Namespace replaceNamespace(String oldNamespaceName, String newNamespaceJson) throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        logger.info("Received request to replace namespace with oldNamespaceName: {} and newNamespaceJson: {}", oldNamespaceName, newNamespaceJson);

        CoreV1Api apiInstance = new CoreV1Api(client);

        V1Namespace newNamespace;
        try {
            newNamespace = objectMapper.readValue(newNamespaceJson, V1Namespace.class);

            // Ensure the new namespace JSON has the same name as the old namespace
            V1ObjectMeta metadata = newNamespace.getMetadata();
            if (metadata == null) {
                metadata = new V1ObjectMeta();
                newNamespace.setMetadata(metadata);
            }
            metadata.setName(oldNamespaceName);

            // Example of additional modifications:
            // Add or update labels
            if (metadata.getLabels() != null) {
                metadata.getLabels().put("new-label-key", "new-label-value");
            } else {
                metadata.setLabels(Map.of("new-label-key", "new-label-value"));
            }

            // Add or update annotations
            if (metadata.getAnnotations() != null) {
                metadata.getAnnotations().put("new-annotation-key", "new-annotation-value");
            } else {
                metadata.setAnnotations(Map.of("new-annotation-key", "new-annotation-value"));
            }

        } catch (IOException e) {
            logger.error("Failed to parse new namespace JSON", e);
            throw new RuntimeException("Failed to parse new namespace JSON", e);
        }

        try {
            V1Namespace result = apiInstance.replaceNamespace(oldNamespaceName, newNamespace, null, null, null, null);
            logger.info("Namespace replaced successfully: {}", result.getMetadata().getName());
            return result;
        } catch (ApiException e) {
            logger.error("Exception when calling CoreV1Api#replaceNamespace", e);
            logger.error("Status code: {}", e.getCode());
            logger.error("Reason: {}", e.getResponseBody());
            logger.error("Response headers: {}", e.getResponseHeaders());
            throw new RuntimeException("Failed to replace namespace", e);
        }
    }

   
    
}
