package com.example.runtimeManagement.K8sConfig;

import io.kubernetes.client.openapi.ApiClient;

import java.io.IOException;

public interface KubernetesConfiguration {
    public ApiClient configureKubernetesAccess() throws IOException ;

    }
