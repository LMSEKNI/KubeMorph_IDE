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

    public String podexec (String namespace, String podName,String container,String command) throws ApiException, IOException{

        ApiClient client = kubeconfig.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);

        String[] commandArray = command.split(" ");
        Exec exec = new Exec();
        Process process= exec.exec(namespace,podName,commandArray,container,true,true);
        // Read output from the process (example: reading from InputStream)
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // Optionally, you can wait for the process to complete
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return output.toString();
    }




    /////////////////////
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



