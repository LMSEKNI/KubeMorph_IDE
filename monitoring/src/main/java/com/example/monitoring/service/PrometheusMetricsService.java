package com.example.monitoring.service;

import com.example.monitoring.config.PrometheusConfiguration;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PrometheusMetricsService {
    private static final Logger logger = Logger.getLogger(PrometheusMetricsService.class.getName());

    private final CoreV1Api api;

    @Autowired
    public PrometheusMetricsService(PrometheusConfiguration prometheusConfig) throws IOException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        this.api = new CoreV1Api();

    }

    public String setupMonitoringStack() {
        try {
            //Add the Dashboard configmap
            runCommand("kubectl create configmap -n monitoring grafana-dashboard-configmap --from-file=monitoring/src/main/resources/grafanaDashboard.json");
            // Install Prometheus with custom values
            runCommand("helm install -n monitoring prometheus prometheus-community/prometheus -f monitoring/src/main/resources/prometheus-values.yaml");
            // Install Grafana with custom values
            runCommand("helm install -n monitoring grafana grafana/grafana -f monitoring/src/main/resources/values.yaml");

            return(getGrafanaAdminPassword());



        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Error while setting up monitoring stack", e);
        }
        return "Error while setting up monitoring stack";
    }

    private String runCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        process.waitFor();
        return output.toString();
    }
    
    private String getGrafanaAdminPassword() throws IOException, InterruptedException {
        String grafanaPassword = runCommand("kubectl get secret --namespace monitoring grafana -o jsonpath=\"{.data.admin-password}\"").trim();
        return new String(Base64.getDecoder().decode(grafanaPassword));
    }

}
