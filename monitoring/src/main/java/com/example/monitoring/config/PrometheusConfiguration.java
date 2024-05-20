package com.example.monitoring.config;

public class PrometheusConfiguration {

    private String prometheusURL;
    private AlertRule alertingRules; // Assuming AlertRule is another class that you need to define

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
