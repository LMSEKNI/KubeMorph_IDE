package com.example.service.updateressource;

import java.io.IOException;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1Pod;

public interface Updateressource {

    public V1Namespace replaceNamespace(String oldNamespaceName, String newNamespaceJson) throws IOException, ApiException ;
    public V1Pod updatePod(String namespace, String podName, String podJson) throws IOException, ApiException ;

    }
