package com.example.KubernetesConfig;

import java.io.IOException;

import io.kubernetes.client.openapi.ApiClient;

public interface KubernetesConfigService {
    
    public  ApiClient configureKubernetesAccess() throws IOException;

}
