package com.example.service.updateressource;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.*;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ResourceUpdateServiceImpl implements ResourceUpdateService {

    private final ApiClient apiClient;


    public ResourceUpdateServiceImpl() throws IOException {
        this.apiClient = Config.defaultClient();

    }

    @Override
    public void updateResource(String namespace, String kind, String name, String updatedYaml) throws ApiException {
        CoreV1Api coreV1Api = new CoreV1Api(apiClient);
        AppsV1Api appsV1Api = new AppsV1Api(apiClient);
        BatchV1Api batchV1Api = new BatchV1Api(apiClient);
        StorageV1Api storageV1Api = new StorageV1Api(apiClient);
        NetworkingV1Api networkingV1Api = new NetworkingV1Api(apiClient);

        switch (kind.toLowerCase()) {
            case "pod":
                V1Pod updatedPod = Yaml.loadAs(updatedYaml, V1Pod.class);
                coreV1Api.replaceNamespacedPod(name, namespace, updatedPod, null, null, null, null);
                break;
            case "deployment":
                V1Deployment updatedDeployment = Yaml.loadAs(updatedYaml, V1Deployment.class);
                appsV1Api.replaceNamespacedDeployment(name, namespace, updatedDeployment, null, null, null, null);
                break;
            case "service":
                V1Service updatedService = Yaml.loadAs(updatedYaml, V1Service.class);
                coreV1Api.replaceNamespacedService(name, namespace, updatedService, null, null, null, null);
                break;
            case "configmap":
                V1ConfigMap updatedConfigMap = Yaml.loadAs(updatedYaml, V1ConfigMap.class);
                coreV1Api.replaceNamespacedConfigMap(name, namespace, updatedConfigMap, null, null, null, null);
                break;
            case "secret":
                V1Secret updatedSecret = Yaml.loadAs(updatedYaml, V1Secret.class);
                coreV1Api.replaceNamespacedSecret(name, namespace, updatedSecret, null, null, null, null);
                break;
            case "daemonset":
                V1DaemonSet updatedDaemonSet = Yaml.loadAs(updatedYaml, V1DaemonSet.class);
                appsV1Api.replaceNamespacedDaemonSet(name, namespace, updatedDaemonSet, null, null, null, null);
                break;
            case "statefulset":
                V1StatefulSet updatedStatefulSet = Yaml.loadAs(updatedYaml, V1StatefulSet.class);
                appsV1Api.replaceNamespacedStatefulSet(name, namespace, updatedStatefulSet, null, null, null, null);
                break;
            case "job":
                V1Job updatedJob = Yaml.loadAs(updatedYaml, V1Job.class);
                batchV1Api.replaceNamespacedJob(name, namespace, updatedJob, null, null, null, null);
                break;
            case "persistentvolume":
                V1PersistentVolume updatedPv = Yaml.loadAs(updatedYaml, V1PersistentVolume.class);
                coreV1Api.replacePersistentVolume(name, updatedPv, null, null,null,null);
                break;
            case "persistentvolumeclaim":
                V1PersistentVolumeClaim updatedPvc = Yaml.loadAs(updatedYaml, V1PersistentVolumeClaim.class);
                coreV1Api.replaceNamespacedPersistentVolumeClaim(name, namespace, updatedPvc, null, null, null, null);
                break;
            case "storageclass":
                V1StorageClass updatedStorageClass = Yaml.loadAs(updatedYaml, V1StorageClass.class);
                storageV1Api.replaceStorageClass(name, updatedStorageClass, null, null,null,null);
                break;
            case "networkpolicy":
                V1NetworkPolicy updatedNetworkPolicy = Yaml.loadAs(updatedYaml, V1NetworkPolicy.class);
                networkingV1Api.replaceNamespacedNetworkPolicy(name, namespace, updatedNetworkPolicy, null, null, null, null);
                break;
            case "serviceaccount":
                V1ServiceAccount updatedServiceAccount = Yaml.loadAs(updatedYaml, V1ServiceAccount.class);
                coreV1Api.replaceNamespacedServiceAccount(name, namespace, updatedServiceAccount, null, null, null, null);
                break;
            default:
                throw new ApiException(400, "Unsupported resource kind: " + kind);
        }
    }

    @Override
    public String getResourceAsJson(String namespace, String kind, String name) throws ApiException {
        Object resource = getResource(namespace, kind, name);
        return apiClient.getJSON().serialize(resource);
    }

    @Override
    public String getResourceAsYaml(String namespace, String kind, String name) throws ApiException {
        Object resource = getResource(namespace, kind, name);
        return Yaml.dump(resource);
    }

    private Object getResource(String namespace, String kind, String name) throws ApiException {
        CoreV1Api coreV1Api = new CoreV1Api(apiClient);
        AppsV1Api appsV1Api = new AppsV1Api(apiClient);
        BatchV1Api batchV1Api = new BatchV1Api(apiClient);
        StorageV1Api storageV1Api = new StorageV1Api(apiClient);
        NetworkingV1Api networkingV1Api = new NetworkingV1Api(apiClient);

        switch (kind.toLowerCase()) {
            case "pod":
                return coreV1Api.readNamespacedPod(name, namespace, null);
            case "deployment":
                return appsV1Api.readNamespacedDeployment(name, namespace, null);
            case "service":
                return coreV1Api.readNamespacedService(name, namespace, null);
            case "configmap":
                return coreV1Api.readNamespacedConfigMap(name, namespace, null);
            case "secret":
                return coreV1Api.readNamespacedSecret(name, namespace, null);
            case "daemonset":
                return appsV1Api.readNamespacedDaemonSet(name, namespace, null);
            case "statefulset":
                return appsV1Api.readNamespacedStatefulSet(name, namespace, null);
            case "job":
                return batchV1Api.readNamespacedJob(name, namespace, null);
            case "persistentvolume":
                return coreV1Api.readPersistentVolume(name, null);
            case "persistentvolumeclaim":
                return coreV1Api.readNamespacedPersistentVolumeClaim(name, namespace, null);
            case "storageclass":
                return storageV1Api.readStorageClass(name, null);
            case "networkpolicy":
                return networkingV1Api.readNamespacedNetworkPolicy(name, namespace, null);
            case "serviceaccount":
                return coreV1Api.readNamespacedServiceAccount(name, namespace, null);
            default:
                throw new ApiException(400, "Unsupported resource kind: " + kind);

        }
    }

}
