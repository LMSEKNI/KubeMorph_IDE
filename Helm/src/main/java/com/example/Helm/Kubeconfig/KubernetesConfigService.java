package com.example.Helm.Kubeconfig;

import io.kubernetes.client.openapi.ApiClient;

import java.io.IOException;

public interface KubernetesConfigService {
    public ApiClient configureKubernetesAccess() throws IOException ;

    }
