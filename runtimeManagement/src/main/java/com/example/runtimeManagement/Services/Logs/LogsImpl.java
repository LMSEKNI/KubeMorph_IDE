package com.example.runtimeManagement.Services.Logs;

import com.example.runtimeManagement.K8sConfig.KubernetesConfig;
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

@Service
public class LogsImpl implements Logs {

    @Autowired
    private KubernetesConfig kubeconfig;

    private static final Logger logger = LoggerFactory.getLogger(LogsImpl.class);



    public String retrieveLogsFromPod3(String namespace, String podName, String containerName) throws ApiException, IOException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException {
        // Configure Kubernetes client
        ApiClient client = kubeconfig.configureKubernetesAccess();
        CoreV1Api coreApi = new CoreV1Api(client);

        // Fetch the pod object
        logger.debug("Fetching pod: {} in namespace: {}", podName, namespace);
        V1Pod pod = coreApi.readNamespacedPod( podName,namespace, null);

        // Instantiate PodLogs and stream logs from the pod
        PodLogs logs = new PodLogs(client);
        InputStream is = logs.streamNamespacedPodLog(namespace,podName,containerName);

        // Read the logs into a string
        StringBuilder logsBuilder = new StringBuilder(100);
        try {
            if (logsBuilder != null) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    logsBuilder.append(new String(buffer, 0, bytesRead));
                }
            }

        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        } finally {
            // Close the input stream
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String logsString = logsBuilder.toString();

        logsBuilder.setLength(0);

        return logsString;
    }



    public String retrieveLogsFromPod(String namespace, String podName) throws ApiException, UnrecoverableKeyException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // Fetch the pod object
        ApiClient client = KubernetesConfig.configureKubernetesAccess();
        CoreV1Api coreApi = new CoreV1Api(client);

        V1Pod pod = coreApi.readNamespacedPod(podName, namespace, null);

        // Instantiate PodLogs
        PodLogs logs = new PodLogs();
        InputStream is = logs.streamNamespacedPodLog(pod);

        // Read the logs into a string
        StringBuilder logsBuilder = new StringBuilder();
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                logsBuilder.append(new String(buffer, 0, bytesRead));
            }
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        } finally {
            // Close the input stream
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return logsBuilder.toString();
    }

    @Override
    public String getPodLogs(String namespace, String podName) throws Exception {
        logger.info("Fetching logs for pod: {} in namespace: {}", podName, namespace);

        try {
            ApiClient client = kubeconfig.configureKubernetesAccess();
            logger.debug("Kubernetes access configured successfully.");

            //CoreV1Api coreApi = new CoreV1Api();
            PodLogs logs = new PodLogs(client);

            logger.debug("Attempting to stream logs for pod: {}", podName);
            InputStream is = logs.streamNamespacedPodLog(namespace, podName, null);
            String podLogs = new String(is.readAllBytes());
            logger.info("Successfully fetched logs for pod: {}", podName);

            return podLogs;
        } catch (ApiException e) {
            logger.error("API exception occurred while fetching logs for pod: {} in namespace: {}", podName, namespace, e.getResponseBody());
            throw e;
        } catch (IOException e) {
            logger.error("I/O exception occurred while reading logs for pod: {} in namespace: {}", podName, namespace, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while fetching logs for pod: {} in namespace: {}", podName, namespace, e);
            throw e;
        }
    }
    @Override
    public List<V1Pod> getPods(String namespace) throws ApiException, IOException{
        kubeconfig.configureKubernetesAccess();
        CoreV1Api coreApi = new CoreV1Api();

        return coreApi.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null, null).getItems();
    }

    public String getPodLogs14(String namespace, String podName) throws ApiException, IOException {
        ApiClient client = kubeconfig.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);

        String log = api.readNamespacedPodLog(
                podName,
                namespace,
                null, // container name
                null, // follow
                false, // insecureSkipTLSVerifyBackend
                null, // limitBytes
                null, // pretty
                null, // previous
                Integer.MAX_VALUE, // sinceSeconds
                null, // tailLines
                false // timestamps
        );

        return log;
    }

}
