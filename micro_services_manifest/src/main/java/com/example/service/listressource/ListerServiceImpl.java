package com.example.service.listressource;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.openapi.apis.StorageV1Api;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1ConfigMapList;
import io.kubernetes.client.openapi.models.V1DaemonSet;
import io.kubernetes.client.openapi.models.V1DaemonSetList;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1Endpoints;
import io.kubernetes.client.openapi.models.V1EndpointsList;
import io.kubernetes.client.openapi.models.V1Ingress;
import io.kubernetes.client.openapi.models.V1IngressList;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1JobList;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1OwnerReference;
import io.kubernetes.client.openapi.models.V1PersistentVolume;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaim;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaimList;
import io.kubernetes.client.openapi.models.V1PersistentVolumeList;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1ReplicaSet;
import io.kubernetes.client.openapi.models.V1ReplicaSetList;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.openapi.models.V1StatefulSet;
import io.kubernetes.client.openapi.models.V1StatefulSetList;
import io.kubernetes.client.openapi.models.V1StorageClass;
import io.kubernetes.client.openapi.models.V1StorageClassList;
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
public class ListerServiceImpl implements ListerService{
    
    @Autowired
    private KubernetesConfigService KubernetesConfigService;

//connection 
    public List<String> getPodDeploymentConnections() throws ApiException, FileNotFoundException, IOException {
        List<String> connections = new ArrayList<>();

        // Retrieve list of deployments
        

        String kubeConfigPath = System.getenv("HOME") + "/.kube/config";
        ApiClient apiClient = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        Configuration.setDefaultApiClient(apiClient);

        AppsV1Api appsV1Api = new AppsV1Api(apiClient);
        V1DeploymentList deploymentList = appsV1Api.listDeploymentForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        List<V1Deployment> deployments = deploymentList.getItems();

        // Retrieve list of pods
        CoreV1Api coreV1Api = new CoreV1Api(apiClient);
        V1PodList podList = coreV1Api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        List<V1Pod> pods = podList.getItems();

        // Match pods with their corresponding deployments
        for (V1Pod pod : pods) {
            String podName = pod.getMetadata().getName();
            V1OwnerReference ownerReference = getDeploymentOwnerReference(pod, deployments);
            if (ownerReference != null) {
                String deploymentName = ownerReference.getName();
                connections.add("Pod: " + podName + " - Deployment: " + deploymentName);
            }
        }

        return connections;
    }

    private V1OwnerReference getDeploymentOwnerReference(V1Pod pod, List<V1Deployment> deployments) {
        String podUid = pod.getMetadata().getUid();
        for (V1Deployment deployment : deployments) {
            List<V1OwnerReference> ownerReferences = deployment.getMetadata().getOwnerReferences();
            if (ownerReferences != null) { // Vérification de nullité
                for (V1OwnerReference ownerReference : ownerReferences) {
                    if ("ReplicaSet".equals(ownerReference.getKind()) && podUid.equals(ownerReference.getUid())) {
                        return ownerReference;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<String> getAllPods() throws FileNotFoundException, IOException, ApiException {

        KubernetesConfigService.configureKubernetesAccess();
        
        CoreV1Api api = new CoreV1Api();

        V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        
        List<String> podNames = new ArrayList<>();
        for (V1Pod item : podList.getItems()) {
            podNames.add(item.getMetadata().getName());
        }
        return podNames;
    }

    @Override
    public List<String> getAllNamespaces() throws ApiException, FileNotFoundException, IOException {
        KubernetesConfigService.configureKubernetesAccess();

        CoreV1Api coreV1Api = new CoreV1Api();
        // List namespaces
        V1NamespaceList namespaceList = coreV1Api.listNamespace(null, null, null, null, null, null, null, null, null, null);
        List<String> namespaceNames = new ArrayList<>();
        for (V1Namespace namespace : namespaceList.getItems()) {
            namespaceNames.add(namespace.getMetadata().getName());
        }

        return namespaceNames;
    }

    @Override
    public List<String> getAllServices() throws FileNotFoundException, IOException, ApiException {
        KubernetesConfigService.configureKubernetesAccess();

        CoreV1Api api = new CoreV1Api();

        V1ServiceList serviceList = api.listServiceForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        // Extract service names
        List<String> serviceNames = new ArrayList<>();
        for (V1Service item : serviceList.getItems()) {
            serviceNames.add(item.getMetadata().getName());
        }

        return serviceNames;
    }

    @Override
    public List<String> getAllDeployments() throws IOException, ApiException {
        
        ApiClient client =KubernetesConfigService.configureKubernetesAccess();
        // CoreV1Api api = new CoreV1Api();
        AppsV1Api api = new AppsV1Api(client);
        V1DeploymentList deploymentList = api.listDeploymentForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        List<String> deploymentNames = new ArrayList<>();
        for (V1Deployment deployment : deploymentList.getItems()) {
            deploymentNames.add(deployment.getMetadata().getName());
        }
        return deploymentNames;
    }

    public List<String> getAllReplicaSets() throws IOException, ApiException {
        ApiClient client =KubernetesConfigService.configureKubernetesAccess();

        // CoreV1Api api = new CoreV1Api();
        AppsV1Api api = new AppsV1Api(client);
        V1ReplicaSetList replicaSetList = api.listReplicaSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        List<String> replicaSetNames = new ArrayList<>();
        for (V1ReplicaSet replicaSet : replicaSetList.getItems()) {
            replicaSetNames.add(replicaSet.getMetadata().getName());
        }
        return replicaSetNames;
    }

    public List<String> getAllJobs() throws IOException, ApiException {
        ApiClient client =KubernetesConfigService.configureKubernetesAccess();

        BatchV1Api api = new BatchV1Api(client);
        V1JobList jobList = api.listJobForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        List<String> jobNames = new ArrayList<>();
        for (V1Job job : jobList.getItems()) {
            jobNames.add(job.getMetadata().getName());
        }
        return jobNames;
    }

    public List<String> getAllNodes() throws IOException, ApiException {
        ApiClient client =KubernetesConfigService.configureKubernetesAccess();

        CoreV1Api api = new CoreV1Api(client);
        V1NodeList nodeList = api.listNode(null, null, null, null, null, null, null, null, null, null);

        List<String> nodeNames = new ArrayList<>();
        for (V1Node node : nodeList.getItems()) {
            nodeNames.add(node.getMetadata().getName());
        }
        return nodeNames;
    }
    public List<String> getAllConfigMaps() throws IOException, ApiException {
        ApiClient client =KubernetesConfigService.configureKubernetesAccess();

        CoreV1Api api = new CoreV1Api(client);
        V1ConfigMapList configMapList = api.listConfigMapForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        List<String> configMapNames = new ArrayList<>();
        for (V1ConfigMap configMap : configMapList.getItems()) {
            configMapNames.add(configMap.getMetadata().getName());
        }
        return configMapNames;
    }
    public List<String> getAllIngresses() throws IOException, ApiException {
        ApiClient client =KubernetesConfigService.configureKubernetesAccess();

        NetworkingV1Api api = new NetworkingV1Api(client);
        V1IngressList ingressList = api.listIngressForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        List<String> ingressNames = new ArrayList<>();
        for (V1Ingress ingress : ingressList.getItems()) {
            ingressNames.add(ingress.getMetadata().getName());
        }
        return ingressNames;
    }
    public List<String> getAllEndpoints() throws IOException, ApiException {
        ApiClient client =KubernetesConfigService.configureKubernetesAccess();

        CoreV1Api api = new CoreV1Api(client);

        V1EndpointsList endpointsList = api.listEndpointsForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        List<String> endpointNames = new ArrayList<>();
        for (V1Endpoints endpoints : endpointsList.getItems()) {
            endpointNames.add(endpoints.getMetadata().getName());
        }
        return endpointNames;
    }
    public List<String> getAllDaemonSets() throws IOException, ApiException {
        ApiClient client =KubernetesConfigService.configureKubernetesAccess();

        AppsV1Api api = new AppsV1Api(client);

        V1DaemonSetList daemonSetList = api.listDaemonSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        List<String> daemonSetNames = new ArrayList<>();
        for (V1DaemonSet daemonSet : daemonSetList.getItems()) {
            daemonSetNames.add(daemonSet.getMetadata().getName());
        }
        return daemonSetNames;
    }
    public List<String> getAllPersistentVolumeClaims() throws IOException, ApiException {
        ApiClient client =KubernetesConfigService.configureKubernetesAccess();

        CoreV1Api api = new CoreV1Api(client);

        V1PersistentVolumeClaimList pvcList = api.listPersistentVolumeClaimForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        List<String> pvcNames = new ArrayList<>();
        for (V1PersistentVolumeClaim pvc : pvcList.getItems()) {
            pvcNames.add(pvc.getMetadata().getName());
        }
        return pvcNames;
    }

    public List<String> getAllPersistentVolumes() throws ApiException, IOException {
        
            ApiClient client = KubernetesConfigService.configureKubernetesAccess();
            CoreV1Api api = new CoreV1Api(client);

            V1PersistentVolumeList pvList = api.listPersistentVolume(null, null, null, null, null, null, null, null, null, null);

            List<String> pvNames = new ArrayList<>();
            for (V1PersistentVolume pv : pvList.getItems()) {
                pvNames.add(pv.getMetadata().getName());
            }
            return pvNames;
      
    }
    

    public List<String> getAllStorageClasses() throws IOException, ApiException {
        ApiClient client =KubernetesConfigService.configureKubernetesAccess();
        StorageV1Api api = new StorageV1Api(client);

        V1StorageClassList storageClassList = api.listStorageClass(null, null, null, null, null, null, null, null, null, null);

        List<String> storageClassNames = new ArrayList<>();
        for (V1StorageClass storageClass : storageClassList.getItems()) {
            storageClassNames.add(storageClass.getMetadata().getName());
        }
        return storageClassNames;
    }
    public List<String> getAllStatefulSets() throws IOException, ApiException {
        ApiClient client = io.kubernetes.client.util.Config.defaultClient();
        AppsV1Api api = new AppsV1Api(client);

        V1StatefulSetList statefulSetList = api.listStatefulSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        List<String> statefulSetNames = new ArrayList<>();
        for (V1StatefulSet statefulSet : statefulSetList.getItems()) {
            statefulSetNames.add(statefulSet.getMetadata().getName());
        }
        return statefulSetNames;
    }

    
}
