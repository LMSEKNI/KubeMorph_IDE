package com.example.runtimeManagement.Services;

import com.example.runtimeManagement.K8sConfig.KubernetesConfigurationImpl;
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class LogsImpl implements Logs {

    @Autowired
    private KubernetesConfigurationImpl KubernetesConfig;

    private static final Logger logger = LoggerFactory.getLogger(LogsImpl.class);

    @Override
    public String getPodLogs(String namespace, String podName) throws Exception {
        logger.info("Fetching logs for pod: {} in namespace: {}", podName, namespace);

        try {
            KubernetesConfig.configureKubernetesAccess();
            logger.debug("Kubernetes access configured successfully.");

            CoreV1Api coreApi = new CoreV1Api();
            PodLogs logs = new PodLogs();

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
    public List<V1Pod> getPods(String namespace) throws ApiException, IOException {
        KubernetesConfig.configureKubernetesAccess();
        CoreV1Api coreApi = new CoreV1Api();

        return coreApi.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null, null)
                .getItems();
    }

}
