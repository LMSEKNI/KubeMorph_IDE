package com.example.monitoring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "prometheus")
public class PrometheusConfiguration {

    private String prometheusURL;
    private AlertRule alertingRules;

    // Getters and Setters
    public String getPrometheusURL() {
        return prometheusURL;
    }

    public void setPrometheusURL(String prometheusURL) {
        this.prometheusURL = prometheusURL;
    }

    public AlertRule getAlertingRules() {
        return alertingRules;
    }

    public void setAlertingRules(AlertRule alertingRules) {
        this.alertingRules = alertingRules;
    }
}
