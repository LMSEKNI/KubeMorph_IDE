package com.example.monitoring.config;

public class AlertRule {

    private String ruleName;
    private String expression;
    private String forDuration;

    // Getters and Setters
    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getForDuration() {
        return forDuration;
    }

    public void setForDuration(String forDuration) {
        this.forDuration = forDuration;
    }
}
