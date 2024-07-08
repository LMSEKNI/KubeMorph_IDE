package com.example.service.listressource;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.*;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.KubernetesConfig.KubernetesConfigService;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListerServiceImpl implements ListerService {

    @Autowired
    private KubernetesConfigService kubernetesConfigService;

    @Override
    public List<V1Pod> getAllPods() throws IOException, ApiException {
        CoreV1Api api = new CoreV1Api(kubernetesConfigService.configureKubernetesAccess());

        try {
            V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
            List<V1Pod> pods = podList.getItems();

            pods.forEach(pod -> {
                System.out.println("Pod: " + pods.toString());
            });

            return pods;
        } catch (ApiException e) {
            System.err.println("Exception fetching pods: " + e.getMessage());
            throw e;
        }
    }


    @Override
    public List<V1Namespace> getAllNamespaces() throws ApiException, FileNotFoundException, IOException {
        kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api coreV1Api = new CoreV1Api();
        V1NamespaceList namespaceList = coreV1Api.listNamespace(null, null, null, null, null, null, null, null, null, null);
        return namespaceList.getItems();
    }

    @Override
    public List<V1Service> getAllServices() throws IOException, ApiException {
        kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api();

        try {
            V1ServiceList serviceList = api.listServiceForAllNamespaces(null,null, null, null, null, null, null, null, null, null);
            return serviceList.getItems();
        } catch (ApiException e) {
            System.err.println("Exception fetching services: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<V1Deployment> getAllDeployments() throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        AppsV1Api api = new AppsV1Api(client);

        try {
            V1DeploymentList deploymentList = api.listDeploymentForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
            List<V1Deployment> deployments = deploymentList.getItems();

            deployments.forEach(deployment -> {
                System.out.println("Deployment: " + deployment.toString());
            });

            return deployments;
        } catch (ApiException e) {
            System.err.println("Exception fetching deployments: " + e.getMessage());
            throw e;
        }
    }
    @Override
    public List<V1DaemonSet> getAllDaemonSets() throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        AppsV1Api api = new AppsV1Api(client);

        try {
            V1DaemonSetList daemonSetList = api.listDaemonSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
            return daemonSetList.getItems();
        } catch (ApiException e) {
            System.err.println("Exception fetching daemon sets: " + e.getMessage());
            throw e;
        }
    }

    public List<V1ReplicaSet> getAllReplicaSets() throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        AppsV1Api api = new AppsV1Api(client);

        try {
            V1ReplicaSetList replicaSetList = api.listReplicaSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
            return replicaSetList.getItems();
        } catch (ApiException e) {
            System.err.println("Exception fetching replica sets: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<V1Job> getAllJobs() throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        BatchV1Api api = new BatchV1Api(client);
        V1JobList jobList = api.listJobForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        return jobList.getItems();
    }

    @Override
    public List<V1Node> getAllNodes() throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);
        V1NodeList nodeList = api.listNode(null, null, null, null, null, null, null, null, null, null);
        return nodeList.getItems();
    }

    @Override
    public List<V1ConfigMap> getAllConfigMaps() throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);
        V1ConfigMapList configMapList = api.listConfigMapForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        return configMapList.getItems();
    }

    @Override
    public List<V1Ingress> getAllIngresses() throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        NetworkingV1Api api = new NetworkingV1Api(client);
        V1IngressList ingressList = api.listIngressForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        return ingressList.getItems();
    }

    @Override
    public List<V1Endpoints> getAllEndpoints() throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);
        V1EndpointsList endpointsList = api.listEndpointsForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        return endpointsList.getItems();
    }



    @Override
    public List<V1PersistentVolumeClaim> getAllPersistentVolumeClaims() throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);
        V1PersistentVolumeClaimList pvcList = api.listPersistentVolumeClaimForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        return pvcList.getItems();
    }

    @Override
    public List<V1PersistentVolume> getAllPersistentVolumes() throws ApiException, IOException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);
        V1PersistentVolumeList pvList = api.listPersistentVolume(null, null, null, null, null, null, null, null, null, null);
        return pvList.getItems();
    }

    @Override
    public List<V1StorageClass> getAllStorageClasses() throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        StorageV1Api api = new StorageV1Api(client);
        V1StorageClassList storageClassList = api.listStorageClass(null, null, null, null, null, null, null, null, null, null);
        return storageClassList.getItems();
    }

    @Override
    public List<V1StatefulSet> getAllStatefulSets() throws IOException, ApiException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        AppsV1Api api = new AppsV1Api(client);
        V1StatefulSetList statefulSetList = api.listStatefulSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        return statefulSetList.getItems();
    }
}
