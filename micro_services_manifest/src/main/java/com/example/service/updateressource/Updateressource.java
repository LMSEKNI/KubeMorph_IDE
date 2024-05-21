package com.example.service.updateressource;

import java.io.IOException;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Pod;

public interface Updateressource {
    // public String getPodDescription(String namespace, String podName) throws ApiException, IOException ;

    // public V1Pod updatePod(String namespace, String podName, V1Pod updatedPod) throws ApiException, IOException ;
    // public V1Deployment updateDeployment(String namespace, String deploymentName, V1Deployment updatedDeployment) throws ApiException, IOException ;
    public void updateResourcePatch(String resourceName, V1Pod updatedResource) throws ApiException, IOException ;
}
