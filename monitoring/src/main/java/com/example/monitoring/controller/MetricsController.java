package com.example.monitoring.controller;

import com.example.monitoring.service.KubernetesMetricsService;
import com.example.monitoring.service.PrometheusMetricsService;
import io.kubernetes.client.custom.NodeMetricsList;
import io.kubernetes.client.custom.PodMetricsList;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {
    private final KubernetesMetricsService kubernetesMetricsService;
    private final PrometheusMetricsService prometheusMetricsService;

    @Autowired
    public MetricsController(KubernetesMetricsService kubernetesMetricsService, PrometheusMetricsService prometheusMetricsService) {
        this.kubernetesMetricsService = kubernetesMetricsService;
        this.prometheusMetricsService = prometheusMetricsService;
    }

    @GetMapping("/k8s/nodes")
    public NodeMetricsList getKubernetesNodeMetrics() throws ApiException {
        return kubernetesMetricsService.getNodeMetrics();
    }

    @GetMapping("/k8s/pods")
    public PodMetricsList getKubernetesPodMetrics() throws ApiException {
        String namespace = "default";
        return kubernetesMetricsService.getPodMetrics(namespace);
    }

    @GetMapping("/prometheus")
    public String getPrometheusMetrics() throws ApiException {
       return prometheusMetricsService.setupMonitoringStack();

    }
    @GetMapping("/grafana")
    public String getGrafanaServiceUrl() throws ApiException {
        return prometheusMetricsService.getGrafanaServiceUrl();

    }
    
}
