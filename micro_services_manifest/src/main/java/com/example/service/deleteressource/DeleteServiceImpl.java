package com.example.service.deleteressource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.KubernetesConfig.KubernetesConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1StatefulSetList;
import io.kubernetes.client.openapi.models.V1EndpointsList;
import io.kubernetes.client.openapi.models.V1JobList;
import io.kubernetes.client.openapi.models.V1DaemonSetList;

@Service
public class DeleteServiceImpl implements DeleteService {

    @Autowired
    private KubernetesConfigService kubernetesConfigService;

    @Override
    public String deleteService(String resourceType, String resourceName) throws IOException {
        ApiClient client = kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);

        try {
            switch (resourceType.toLowerCase()) {
                case "service":
                    api.deleteNamespacedService(resourceName, "default", null, null, null, null, null, null);
                    return listServices(api);
                case "pod":
                    api.deleteNamespacedPod(resourceName, "default", null, null, null, null, null, null);
                    return listPods(api);
                case "node":
                    api.deleteNode(resourceName, null, null, null, null, null, null);
                    return listNodes(api);
                case "deployment":
                    AppsV1Api deploymentApi = new AppsV1Api(client);
                    deploymentApi.deleteNamespacedDeployment(resourceName, "default", null, null, null, null, null, null);
                    return listDeployments(deploymentApi);
                case "statefulset":
                    AppsV1Api statefulSetApi = new AppsV1Api(client);
                    statefulSetApi.deleteNamespacedStatefulSet(resourceName, "default", null, null, null, null, null, null);
                    return listStatefulSets(statefulSetApi);
                case "endpoint":
                    api.deleteNamespacedEndpoints(resourceName, "default", null, null, null, null, null, null);
                    return listEndpoints(api);
                case "job":
                    BatchV1Api jobApi = new BatchV1Api(client);
                    jobApi.deleteNamespacedJob(resourceName, "default", null, null, null, null, null, null);
                    return listJobs(jobApi);
                case "daemonset":
                    AppsV1Api daemonSetApi = new AppsV1Api(client);
                    daemonSetApi.deleteNamespacedDaemonSet(resourceName, "default", null, null, null, null, null, null);
                    return listDaemonSets(daemonSetApi);
                default:
                    throw new IllegalArgumentException("Unsupported resource type: " + resourceType);
            }
        } catch (ApiException e) {
            e.printStackTrace();
            return "Error occurred while deleting resource: " + e.getResponseBody();
        }
    }

    private String listServices(CoreV1Api api) throws ApiException {
        V1ServiceList serviceList = api.listServiceForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        List<String> serviceNames = new ArrayList<>();
        for (var item : serviceList.getItems()) {
            serviceNames.add(item.getMetadata().getName());
        }
        return convertToJson(serviceNames);
    }

    private String listPods(CoreV1Api api) throws ApiException {
        V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        List<String> podNames = new ArrayList<>();
        for (var item : podList.getItems()) {
            podNames.add(item.getMetadata().getName());
        }
        return convertToJson(podNames);
    }

    private String listNodes(CoreV1Api api) throws ApiException {
        V1NodeList nodeList = api.listNode(null, null, null, null, null, null, null, null, null, null);
        List<String> nodeNames = new ArrayList<>();
        for (var item : nodeList.getItems()) {
            nodeNames.add(item.getMetadata().getName());
        }
        return convertToJson(nodeNames);
    }

    private String listDeployments(AppsV1Api api) throws ApiException {
        V1DeploymentList deploymentList = api.listDeploymentForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        List<String> deploymentNames = new ArrayList<>();
        for (var item : deploymentList.getItems()) {
            deploymentNames.add(item.getMetadata().getName());
        }
        return convertToJson(deploymentNames);
    }

    private String listStatefulSets(AppsV1Api api) throws ApiException {
        V1StatefulSetList statefulSetList = api.listStatefulSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        List<String> statefulSetNames = new ArrayList<>();
        for (var item : statefulSetList.getItems()) {
            statefulSetNames.add(item.getMetadata().getName());
        }
        return convertToJson(statefulSetNames);
    }

    private String listEndpoints(CoreV1Api api) throws ApiException {
        V1EndpointsList endpointsList = api.listEndpointsForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        List<String> endpointNames = new ArrayList<>();
        for (var item : endpointsList.getItems()) {
            endpointNames.add(item.getMetadata().getName());
        }
        return convertToJson(endpointNames);
    }

    private String listJobs(BatchV1Api api) throws ApiException {
        V1JobList jobList = api.listJobForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        List<String> jobNames = new ArrayList<>();
        for (var item : jobList.getItems()) {
            jobNames.add(item.getMetadata().getName());
        }
        return convertToJson(jobNames);
    }

    private String listDaemonSets(AppsV1Api api) throws ApiException {
        V1DaemonSetList daemonSetList = api.listDaemonSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        List<String> daemonSetNames = new ArrayList<>();
        for (var item : daemonSetList.getItems()) {
            daemonSetNames.add(item.getMetadata().getName());
        }
        return convertToJson(daemonSetNames);
    }

    private String convertToJson(List<String> names) {
        try {
            ObjectMapper objectMapper = JsonMapper.builder()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .build();
            return objectMapper.writeValueAsString(names);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error converting list to JSON: " + e.getMessage();
        }
    }
}
