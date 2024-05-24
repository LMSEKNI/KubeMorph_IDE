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
    private ObjectMapper mapper;
    @Override
    public V1Pod updatePod(String oldName, String newJson) throws ApiException, IOException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);

        // Parse new JSON pod
        JsonNode newPodJson = mapper.readTree(newJson);

        String namespace = newPodJson.path("metadata").path("namespace").asText("default");

        try {
            // Retrieve the existing pod
            V1Pod existingPod = api.readNamespacedPod(oldName, namespace, null);

            // Ensure the new pod JSON has the same name as the old pod
            ((ObjectNode) newPodJson.path("metadata")).put("name", oldName);

            // Create a JSON patch
            ArrayNode patchJson = mapper.createArrayNode();

            JsonNode newContainerImage = newPodJson.path("spec").path("containers").get(0).path("image");
            if (!newContainerImage.isMissingNode()) {
                ObjectNode imagePatch = mapper.createObjectNode();
                imagePatch.put("op", "replace");
                imagePatch.put("path", "/spec/containers/0/image");
                imagePatch.put("value", newContainerImage.asText());
                patchJson.add(imagePatch);
            }

            JsonNode activeDeadlineSeconds = newPodJson.path("spec").path("activeDeadlineSeconds");
            if (!activeDeadlineSeconds.isMissingNode()) {
                ObjectNode adsPatch = mapper.createObjectNode();
                adsPatch.put("op", "add");
                adsPatch.put("path", "/spec/activeDeadlineSeconds");
                adsPatch.put("value", activeDeadlineSeconds.asInt());
                patchJson.add(adsPatch);
            }

            JsonNode terminationGracePeriodSeconds = newPodJson.path("spec").path("terminationGracePeriodSeconds");
            if (!terminationGracePeriodSeconds.isMissingNode()) {
                ObjectNode tgpsPatch = mapper.createObjectNode();
                tgpsPatch.put("op", "add");
                tgpsPatch.put("path", "/spec/terminationGracePeriodSeconds");
                tgpsPatch.put("value", terminationGracePeriodSeconds.asInt());
                patchJson.add(tgpsPatch);
            }

            // Convert patch to JSON string
            String patchStr = mapper.writeValueAsString(patchJson);

            // Apply the patch
            V1Patch patch = new V1Patch(patchStr);
            V1Pod updatedPod = PatchUtils.patch(
                    V1Pod.class,
                    () -> api.patchNamespacedPodCall(oldName, namespace, patch, null, null, null, null, null,null),
                    V1Patch.PATCH_FORMAT_JSON_PATCH,
                    api.getApiClient()
            );

            System.out.println("Pod updated successfully: " + updatedPod.getMetadata().getName());
            return updatedPod;
        } catch (ApiException e) {
            System.err.println("Failed to update pod: " + e.getResponseBody());
            throw e;
        } catch (IOException e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
            throw e;
        }
    }


    private JsonNode createPatch(JsonNode original, JsonNode updated) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode patchNode = mapper.createObjectNode();

        updated.fields().forEachRemaining(field -> {
            String fieldName = field.getKey();
            JsonNode originalValue = original.get(fieldName);
            JsonNode updatedValue = field.getValue();

            if (originalValue == null || !originalValue.equals(updatedValue)) {
                patchNode.set(fieldName, updatedValue);
            }
        });

        return patchNode;
    }


    @Override
    public void updateResourcePatch(String resourceName, V1Pod updatedResource) throws ApiException, IOException {
        String patchedPodJSON = Yaml.dump(updatedResource);
        CoreV1Api api = new CoreV1Api();
        PatchUtils.patch(
            V1Pod.class,
            () -> api.patchNamespacedPodCall(
                resourceName,
                "default", // assuming default namespace, you can change as needed
                new V1Patch(patchedPodJSON),
                null,
                null,
                null,
                patchedPodJSON, true,
                null
            ),
            V1Patch.PATCH_FORMAT_JSON_MERGE_PATCH,
            api.getApiClient()
        );
    }




    public void patchReplicaSet(String namespace, String name, String patchJson) throws IOException {
        ApiClient apiClient = kubernetesConfigService.configureKubernetesAccess();
        AppsV1Api api = new AppsV1Api(apiClient);

        logger.info("Patching ReplicaSet {} in namespace {} with patch: {}", name, namespace, patchJson);

        try {
            V1ReplicaSet result = PatchUtils.patch(
                    V1ReplicaSet.class,
                    () -> api.patchNamespacedReplicaSetCall(
                            name,
                            namespace,
                            new V1Patch(patchJson),
                            null,
                            null,
                            null,
                            null,
                            null,null),
                    V1Patch.PATCH_FORMAT_JSON_PATCH,
                    apiClient
            );
            logger.info("ReplicaSet patched successfully: {} in namespace: {}", name, namespace);
            logger.debug("Patch response: {}", result);
        } catch (ApiException e) {
            logger.error("ApiException during resource patch, Status code: {}, Reason: {}, Response body: {}", e.getCode(), e.getResponseBody(), e);
        } catch (Exception e) {
            logger.error("Unexpected error during resource patch", e);
        }
    }

    public V1Pod getPod(String namespace, String podName) throws ApiException, IOException {
        logger.info("Entering getPod method with namespace: {} and podName: {}", namespace, podName);
        kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api();
        V1Pod pod = null;
        try {
            pod = api.readNamespacedPod(podName, namespace, null);
            logger.info("Successfully retrieved pod: {} in namespace: {}", podName, namespace);
        } catch (ApiException e) {
            logger.error("ApiException while retrieving pod: {} in namespace: {}: {}", podName, namespace, e.getResponseBody(), e);
            throw e;
        }
        return pod;
    }

    public V1Pod updateResourcenew(String resourceType, String resourceName, String updatedResourceJson) throws ApiException, IOException {
        kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api();

        // Convert the JSON string to a V1Pod object
        V1Pod updatedPod = api.getApiClient().getJSON().deserialize(updatedResourceJson, V1Pod.class);

        // Replace the existing pod with the updated pod
        return api.replaceNamespacedPod(
                resourceName,
                "default", // assuming default namespace, you can change as needed
                updatedPod,
                null,
                null,
                null,
                null
        );
    }

    // public V1Pod editPod(String namespace , String nameressource ) throws ApiException, IOException {
    //     kubernetesConfigService.configureKubernetesAccess();
    //     CoreV1Api api = new CoreV1Api();

    //     // Retrieve the existing pod
    //     V1Pod existingPod = api.readNamespacedPod(nameressource, namespace, null);

    //     // Update pod metadata (labels in this example)
    //     V1ObjectMeta metadata = existingPod.getMetadata();
    //     if (metadata.getLabels() == null) {
    //         metadata.setLabels(new HashMap<>());
    //     }
    //     metadata.getLabels();

    //     // Update the pod
    //     return api.replaceNamespacedPod(
    //             nameressource,
    //             namespace,
    //             existingPod,
    //             null,
    //             null,
    //             null,
    //             null);
    // }

   
    // @Override
    // public String getPodDescription(String namespace, String podName) throws ApiException, IOException {
    //     ApiClient client = Config.defaultClient();
    //     CoreV1Api api = new CoreV1Api(client);

    //     V1Pod pod = api.readNamespacedPod(podName, namespace, null);
    //     return pod.toString();
    // }

    // @Override
    // public V1Pod updatePod(String namespace, String podName, V1Pod updatedPod) throws ApiException, IOException {
    //     kubernetesConfigService.configureKubernetesAccess();
    //     CoreV1Api api = new CoreV1Api(Config.defaultClient());
    //     return api.replaceNamespacedPod(podName, namespace, updatedPod, null, null, null, null);
    // }
    // @Override
    // public V1Deployment updateDeployment(String namespace, String deploymentName, V1Deployment updatedDeployment) throws ApiException, IOException {
    //     kubernetesConfigService.configureKubernetesAccess();
    //     AppsV1Api api = new AppsV1Api();
    //     return api.replaceNamespacedDeployment(deploymentName, namespace, updatedDeployment, null, null, null, null);
    // }
    
   

    // public String updatePoddd(String namespace, String podName) throws ApiException, IOException {
    //     kubernetesConfigService.configureKubernetesAccess();
    //     CoreV1Api api = new CoreV1Api(Config.defaultClient());
    //     ObjectMapper mapper = new ObjectMapper();

    //     // Fetch the existing Pod
    //     V1Pod existingPod;

    //     try {
    //         existingPod = api.readNamespacedPod(podName, namespace, null);
    //     } catch (ApiException e) {
    //         return "Error fetching Pod: " + e.getResponseBody();
    //     }

    //     // Fetch the description of the Pod
    //     String podDescription;
    //     try {
    //         podDescription = descServiceImpl.getResourceDescriptions(podName, "pod");
    //         System.out.println("Pod description: " + podDescription);
    //     } catch (Exception e) {
    //         return "Error fetching Pod description: " + e.getMessage();

    //     }


    //     // Update the Pod in the Kubernetes cluster
    //     try {
    //         api.replaceNamespacedPod(podName, namespace, existingPod, null, null, null, null);
    //         System.out.println("Pod updated successfully: " + podName );
    //     } catch (ApiException e) {
    //         return "Error updating Pod: " + e.getResponseBody();
    //     }
    //     return podDescription;
    // }
   
    
}
