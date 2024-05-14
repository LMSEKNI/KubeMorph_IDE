package com.example.service.deleteressource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.KubernetesConfig.KubernetesConfigService;
import com.example.KubernetesConfig.KubernetesConfigServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
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

@Service
public class DeleteServiceImpl  implements DeleteService{

    @Autowired
    private  KubernetesConfigService kubernetesConfigService;
    
    @Override
    public String deleteService( String resourceType,String resourceName) throws IOException, ApiException {
        ApiClient client=kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api();

        switch (resourceType) {
            case "service":
                api.deleteNamespacedService(resourceName, "default", null, null, null, null, null, null);
                // return api.listNamespacedService("default", null, null, null, null, null, null, null, null, null, null);
                V1ServiceList serviceList = api.listServiceForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

                List<String> serviceNames = new ArrayList<>();
                for (V1Service item : serviceList.getItems()) {
                    serviceNames.add(item.getMetadata().getName());
                }
                // Convert serviceNames list to JSON
                ObjectMapper objectMapper = JsonMapper.builder()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
                String jsonServiceNames = objectMapper.writeValueAsString(serviceNames);
                return jsonServiceNames;
            case "pod":
                api.deleteNamespacedPod(resourceName, "default", null, null, null, null, null, null);
                V1PodList podList =api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
                List<String> podNames = new ArrayList<>();
                for (V1Pod item : podList.getItems()) {
                    podNames.add(item.getMetadata().getName());
                }
                // Convert serviceNames list to JSON
                ObjectMapper objectMapper1 = JsonMapper.builder()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
                String jsonPodNames = objectMapper1.writeValueAsString(podNames);
                return jsonPodNames;
            case "node":
                api.deleteNode(resourceName, null, null, null, null, null, null);
                V1NodeList nodeList = api.listNode(null, null, null, null, null, null, null, null, null, null);
                List<String> nodeNames = new ArrayList<>();
                for (V1Node item : nodeList.getItems()) {
                    nodeNames.add(item.getMetadata().getName());
                }
                // Convert serviceNames list to JSON
                ObjectMapper objectMapper2 = JsonMapper.builder()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
                String jsonNodeNames = objectMapper2.writeValueAsString(nodeNames);
                return jsonNodeNames;
                
            case "deployment":
                AppsV1Api deploymentApi = new AppsV1Api(client);
                deploymentApi.deleteNamespacedDeployment(resourceName, "default", null, null, null, null, null, null);
                V1DeploymentList deploymentList = deploymentApi.listDeploymentForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
                List<String> deploymentNames = new ArrayList<>();
                for (V1Deployment item : deploymentList.getItems()) {
                    deploymentNames.add(item.getMetadata().getName());
                }
                // Convert serviceNames list to JSON
                ObjectMapper objectMapper3 = JsonMapper.builder()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
                String jsonDeploymentNames = objectMapper3.writeValueAsString(deploymentNames);
                return jsonDeploymentNames;

            case "stateful":
                AppsV1Api statefulSetApi = new AppsV1Api(client);
                statefulSetApi.deleteNamespacedStatefulSet(resourceName, "default", null, null, null, null, null, null);
                V1StatefulSetList statefulSetList = statefulSetApi.listStatefulSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
                List<String> statefulsetNames = new ArrayList<>();
                for (V1StatefulSet item : statefulSetList.getItems()) {
                    statefulsetNames.add(item.getMetadata().getName());
                }
                // Convert serviceNames list to JSON
                ObjectMapper objectMapper4 = JsonMapper.builder()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
                String jsonStatefulsetNames = objectMapper4.writeValueAsString(statefulsetNames);
                return jsonStatefulsetNames;
            case "endpoint":
                CoreV1Api endpointApi = new CoreV1Api(client);
                endpointApi.deleteNamespacedEndpoints(resourceName, "default", null, null, null, null, null, null);
                V1EndpointsList endpointsList = api.listEndpointsForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
                List<String> endpointNames = new ArrayList<>();
                for (V1Endpoints item : endpointsList.getItems()) {
                    endpointNames.add(item.getMetadata().getName());
                }
                // Convert serviceNames list to JSON
                ObjectMapper objectMapper5 = JsonMapper.builder()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
                String jsonendpointNames = objectMapper5.writeValueAsString(endpointNames);
                return jsonendpointNames;
            case "job":
                BatchV1Api jobApi = new BatchV1Api(client);
                jobApi.deleteNamespacedJob(resourceName, "default", null, null, null, null, null, null);
                V1JobList jobList = jobApi.listJobForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
                List<String> jobNames = new ArrayList<>();
                for (V1Job item : jobList.getItems()) {
                    jobNames.add(item.getMetadata().getName());
                }
                // Convert serviceNames list to JSON
                ObjectMapper objectMapper7 = JsonMapper.builder()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
                String jsonjobNames = objectMapper7.writeValueAsString(jobNames);
                return jsonjobNames;
            case "deamonset":
                AppsV1Api daemonSetApi = new AppsV1Api(client);
                daemonSetApi.deleteNamespacedDaemonSet(resourceName, "default", null, null, null, null, null, null);
                
                // List remaining DaemonSets
                V1DaemonSetList daemonSetList = daemonSetApi.listDaemonSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
                List<String> daemonSetNames = new ArrayList<>();
                for (V1DaemonSet item : daemonSetList.getItems()) {
                    daemonSetNames.add(item.getMetadata().getName());
                }
                
                // Convert DaemonSet names list to JSON
                ObjectMapper objectMapper8 = JsonMapper.builder()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .build();
                String jsonDaemonSetNames = objectMapper8.writeValueAsString(daemonSetNames);
                return jsonDaemonSetNames;
            
            case "ingress":
                NetworkingV1Api ingressApi = new NetworkingV1Api(client);
                ingressApi.deleteNamespacedIngress(resourceName, "default", null, null, null, null, null, null);
                V1IngressList IngressList = ingressApi.listIngressForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
                List<String> ingressNames = new ArrayList<>();
                for (V1Ingress item : IngressList.getItems()) {
                    ingressNames.add(item.getMetadata().getName());
                }
                
                // Convert DaemonSet names list to JSON
                ObjectMapper objectMapper9 = JsonMapper.builder()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .build();
                String jsoningressNames = objectMapper9.writeValueAsString(ingressNames);
                return jsoningressNames;
            case "persistentvolume":
                CoreV1Api persistentVolumeApi = new CoreV1Api(client);
                V1PersistentVolumeList persistentVolumeList = persistentVolumeApi.listPersistentVolume(null, null, null, null, null, null, null, null, null, null);

                persistentVolumeApi.deletePersistentVolume(resourceName, null, null, null, null, null, null);
                List<String> persistentvolumeNames = new ArrayList<>();
                for (V1PersistentVolume item : persistentVolumeList.getItems()) {
                    persistentvolumeNames.add(item.getMetadata().getName());
                }
                // Convert serviceNames list to JSON
                ObjectMapper objectMapper6= JsonMapper.builder()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
                String jsonpersistentVolumeNames = objectMapper6.writeValueAsString(persistentvolumeNames);
                return jsonpersistentVolumeNames;
            case "pvc":
                CoreV1Api persistentVolumeClaimApi = new CoreV1Api(client);
                persistentVolumeClaimApi.deleteNamespacedPersistentVolumeClaim(resourceName, "default", null, null, null, null, null, null);
                V1PersistentVolumeClaimList persistentVolumeClaimList = persistentVolumeClaimApi.listPersistentVolumeClaimForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
                
                List<String> persistentvolumeClaimNames = new ArrayList<>();
                for (V1PersistentVolumeClaim item : persistentVolumeClaimList.getItems()) {
                    persistentvolumeClaimNames.add(item.getMetadata().getName());
                }
                // Convert serviceNames list to JSON
                ObjectMapper objectMapper11= JsonMapper.builder()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
                String jsonpersistentVolumeCNames = objectMapper11.writeValueAsString(persistentvolumeClaimNames);
                return jsonpersistentVolumeCNames;
            case "namespace":
                CoreV1Api namespaceApi = new CoreV1Api(client);
                namespaceApi.deleteNamespace(resourceName, null, null, null, null, null, null);
                V1NamespaceList namespaceList = namespaceApi.listNamespace(null, null, null, null, null, null, null, null, null, null);
                List<String> namespaceNames = new ArrayList<>();
                for (V1Namespace item : namespaceList.getItems()) {
                    namespaceNames.add(item.getMetadata().getName());
                }
                ObjectMapper objectMapper12 = JsonMapper.builder()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .build();
                String jsonNamespaceNames = objectMapper12.writeValueAsString(namespaceNames);
                return jsonNamespaceNames;
            
            case "replicaset":
                AppsV1Api replicasetApi = new AppsV1Api(client);
                replicasetApi.deleteNamespacedReplicaSet(resourceName, "default", null, null, null, null, null, null);
                V1ReplicaSetList replicasetList = replicasetApi.listReplicaSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
                List<String> replicasetNames = new ArrayList<>();
                for (V1ReplicaSet item : replicasetList.getItems()) {
                    replicasetNames.add(item.getMetadata().getName());
                }
                ObjectMapper objectMapper13 = JsonMapper.builder()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .build();
                String jsonReplicaSetNames = objectMapper13.writeValueAsString(replicasetNames);
                return jsonReplicaSetNames;
            
            case "configmap":
                CoreV1Api configmapApi = new CoreV1Api(client);
                configmapApi.deleteNamespacedConfigMap(resourceName, "default", null, null, null, null, null, null);
                V1ConfigMapList configmapList = configmapApi.listConfigMapForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
                List<String> configmapNames = new ArrayList<>();
                for (V1ConfigMap item : configmapList.getItems()) {
                    configmapNames.add(item.getMetadata().getName());
                }
                ObjectMapper objectMapper14 = JsonMapper.builder()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .build();
                String jsonConfigMapNames = objectMapper14.writeValueAsString(configmapNames);
                return jsonConfigMapNames;
            
            case "sc":
                StorageV1Api storageClassApi = new StorageV1Api(client);
                storageClassApi.deleteStorageClass(resourceName, null, null, null, null, null, null);
                V1StorageClassList storageClassList = storageClassApi.listStorageClass(null, null, null, null, null, null, null, null, null, null);
                List<String> storageClassNames = new ArrayList<>();
                for (V1StorageClass item : storageClassList.getItems()) {
                    storageClassNames.add(item.getMetadata().getName());
                }
                ObjectMapper objectMapper15 = JsonMapper.builder()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .build();
                String jsonStorageClassNames = objectMapper15.writeValueAsString(storageClassNames);
                return jsonStorageClassNames;
            
            default:
                throw new IllegalArgumentException("Unsupported resource type: " + resourceType);
        }
    }
}
