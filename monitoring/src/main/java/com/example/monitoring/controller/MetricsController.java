package com.example.monitoring.controller;

import com.example.monitoring.service.KubernetesMetricsService;
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

    private final KubernetesMetricsService monitoringService;

    @Autowired
    public MetricsController(KubernetesMetricsService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @GetMapping("/k8s/nodes")
    public NodeMetricsList getKubernetesNodeMetrics() throws ApiException {
        return monitoringService.getNodeMetrics();
    }

    @GetMapping("/k8s/pods")
    public PodMetricsList getKubernetesPodMetrics() throws ApiException {
        return monitoringService.getPodMetrics("default");
    }
}
