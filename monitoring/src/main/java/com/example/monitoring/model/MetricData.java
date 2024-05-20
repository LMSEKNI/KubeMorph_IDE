package com.example.monitoring.model;

public class MetricData {

    private String metricName;
    private Number value;
    private String unit;

    // Constructor
    public MetricData(String metricName, Number value, String unit) {
        this.metricName = metricName;
        this.value = value;
        this.unit = unit;
    }

    // Getters and Setters
    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
