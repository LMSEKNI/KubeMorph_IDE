package com.example.runtimeManagement.Services.Logs;

import com.example.runtimeManagement.K8sConfig.KubernetesConfig;
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
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

    public List<V1Pod> getPods(String namespace) throws ApiException, IOException{
        kubeconfig.configureKubernetesAccess();
        CoreV1Api coreApi = new CoreV1Api();

        return coreApi.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null, null).getItems();
    }

    public String getPodLogs14(String podName) throws ApiException, IOException {
        ApiClient client = kubeconfig.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);

        // Retrieve the list of all pods in all namespaces
        V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null,null);

        // Find the namespace for the specified pod
        String namespace = null;
        for (V1Pod pod : podList.getItems()) {
            if (pod.getMetadata().getName().equals(podName)) {
                namespace = pod.getMetadata().getNamespace();
                break;
            }
        }

        if (namespace == null) {
            throw new ApiException("Pod not found: " + podName);
        }

        // Retrieve the logs for the specified pod
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
