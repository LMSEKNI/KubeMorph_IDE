package com.example.service.updateressource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1JobBuilder;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodSpec;
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

  
    public V1Pod editPod(String ressourcetype, String ressourcename) throws ApiException, IOException {
        kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api();
    
        // Retrieve the existing pod
        V1Pod existingPod = api.readNamespacedPod(ressourcename, "default", null);
    
        // Update pod metadata (labels in this example)
        V1ObjectMeta metadata = existingPod.getMetadata();
        if (metadata.getLabels() == null) {
            metadata.setLabels(new HashMap<>());
        }
        metadata.getLabels();
    
        // Update the pod
        return api.replaceNamespacedPod(
                ressourcename,
                "default",
                existingPod,
                null,
                null,
                null,
                null);
    }

    public V1Pod updateResource(String resourceType, String resourceName, String updatedResourceJson) throws ApiException, IOException {
        Logger logger = LoggerFactory.getLogger(UpdateController.class);
        CoreV1Api api = new CoreV1Api();
        V1Pod updatedPod = api.getApiClient().getJSON().deserialize(updatedResourceJson, V1Pod.class);
    
        // Ensure the resourceName matches the name in the JSON payload
        // String jsonPayloadName = updatedPod.getMetadata().getName();
        // if (!resourceName.equals(jsonPayloadName)) {
        //     throw new ApiException(400, "The name of the object (" + jsonPayloadName + ") does not match the name on the URL (" + resourceName + ")");
        // }
    
        try {
            // Use the name from the JSON payload instead of resourceName
            
            V1Pod result = api.replaceNamespacedPod(
                resourceName, // Use the name from the JSON payload
                "default", // assuming default namespace, you can change as needed
                updatedPod,
                null,
                null,
                null,
                null
            );
            return result;
        } catch (ApiException e) {
            logger.error("Exception when calling CoreV1Api#replaceNamespacedPod");
            logger.error("Status code: " + e.getCode());
            logger.error("Reason: " + e.getResponseBody());
            logger.error("Response headers: " + e.getResponseHeaders());
            throw e;
        }
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
