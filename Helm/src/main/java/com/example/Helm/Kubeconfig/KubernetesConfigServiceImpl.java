package com.example.Helm.Kubeconfig;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

@Service
public class KubernetesConfigServiceImpl implements KubernetesConfigService{

    public ApiClient configureKubernetesAccess() throws IOException {
        String kubeConfigPath = System.getenv("HOME") + "/.kube/config";
        ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        Configuration.setDefaultApiClient(client);
        return client;
    }
}

