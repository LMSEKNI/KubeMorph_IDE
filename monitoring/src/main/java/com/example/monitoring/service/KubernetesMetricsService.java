package com.example.monitoring.service;

import io.kubernetes.client.Metrics;
import io.kubernetes.client.custom.NodeMetricsList;
import io.kubernetes.client.custom.PodMetricsList;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.util.Config;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class KubernetesMetricsService {

    private ApiClient apiClient;
    private Metrics metrics;

    public KubernetesMetricsService() throws IOException {
        try {
            // Initialize ApiClient
            this.apiClient = Config.defaultClient();
        } catch (IOException e) {
            // Handle exception and provide meaningful error message
            throw new RuntimeException("Failed to initialize Kubernetes ApiClient", e);
        }
    }

    @PostConstruct
    public void init() {
        // Initialize Metrics after ApiClient is created
        this.metrics = new Metrics(apiClient);
    }

    public NodeMetricsList getNodeMetrics() throws ApiException {
        return metrics.getNodeMetrics();
    }

    public PodMetricsList getPodMetrics(String namespace) throws ApiException {
        return metrics.getPodMetrics(namespace);
    }
}
