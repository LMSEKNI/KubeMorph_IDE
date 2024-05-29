package com.example.monitoring.service;

import com.example.monitoring.config.PrometheusConfiguration;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PrometheusMetricsService {
    private static final Logger logger = Logger.getLogger(PrometheusMetricsService.class.getName());

    private final CoreV1Api api;
    private final PrometheusConfiguration prometheusConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public PrometheusMetricsService(PrometheusConfiguration prometheusConfig) throws IOException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        this.api = new CoreV1Api();
        this.prometheusConfig = prometheusConfig;
        this.restTemplate = new RestTemplate();
    }

    public String getMetrics() {
        String url = prometheusConfig.getPrometheusURL() + "/api/v1/query";
        String query = "up"; // Example query
        String queryUrl = url + "?query=" + query;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(queryUrl, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                logger.log(Level.SEVERE, "Failed to get metrics: HTTP " + response.getStatusCode());
                return "Error: Failed to get metrics from Prometheus";
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception while fetching metrics", e);
            return "Error: Exception while fetching metrics from Prometheus";
        }
    }
}
