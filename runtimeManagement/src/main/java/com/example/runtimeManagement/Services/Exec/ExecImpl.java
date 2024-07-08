package com.example.runtimeManagement.Services.Exec;

import com.example.runtimeManagement.K8sConfig.KubernetesConfig;
//import com.example.runtimeManagement.K8sConfig.KubernetesConfigurationImpl;
import com.example.runtimeManagement.Services.Logs.LogsImpl;
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreApi;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import io.kubernetes.client.Exec;

@Service
public class ExecImpl implements ExecTerminal {

    @Autowired
    private KubernetesConfig kubeconfig;


    private static final Logger logger = LoggerFactory.getLogger(LogsImpl.class);

    @Override
    public V1Pod getPod(String namespace, String podName) throws ApiException, IOException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        ApiClient client = kubeconfig.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);

        V1PodList podList = api.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null,null);
        for (V1Pod pod : podList.getItems()) {
            if (pod.getMetadata().getName().equals(podName)) {
                return pod;
            }
        }
        return null; // Pod not found
    }
    @Override
    public String podexec(String podName, String command) throws ApiException, IOException {
        // Configure Kubernetes access
        ApiClient client = kubeconfig.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);

        // Fetch all namespaces and find the pod
        V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        V1Pod pod = null;
        for (V1Pod p : podList.getItems()) {
            if (podName.equals(p.getMetadata().getName())) {
                pod = p;
                break;
            }
        }

        if (pod == null) {
            throw new ApiException("Pod not found: " + podName);
        }

        // Extract namespace and container name
        String namespace = pod.getMetadata().getNamespace();
        String container = pod.getSpec().getContainers().get(0).getName(); // Assuming first container if not specified

        String[] commandArray = command.split(" ");
        Exec exec = new Exec();
        Process process = exec.exec(namespace, podName, commandArray, container, true, true);

        // Read output from the process
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // Optionally, wait for the process to complete
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return output.toString();
    }
    @Override
    public String podexecit(String namespace, String podName, String container, String command) throws ApiException, IOException {
        ApiClient client = kubeconfig.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);

        String[] commandArray = {"/bin/sh", command};

        Exec exec = new Exec();
        Process process = exec.exec(namespace, podName, commandArray, container, true, true);

        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return output.toString();
    }
}



