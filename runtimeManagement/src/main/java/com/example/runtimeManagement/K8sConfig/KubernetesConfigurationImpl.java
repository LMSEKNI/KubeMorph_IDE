package com.example.runtimeManagement.K8sConfig;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

@Service
public class KubernetesConfigurationImpl implements  KubernetesConfiguration{

    public ApiClient configureKubernetesAccess() throws IOException {
        String kubeConfigPath = System.getenv("HOME") + "/.kube/config";
        ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        Configuration.setDefaultApiClient(client);
        return client;
    }
}
