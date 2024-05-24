package com.example.service.createressource.form;

import java.io.IOException;
import java.util.*;

import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.apis.*;
import io.kubernetes.client.openapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.KubernetesConfig.KubernetesConfigService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.kubernetes.client.openapi.ApiException;

@Service
public class CreateResourceFormImpl implements CreateRessourceForm {

    private KubernetesConfigService kubernetesConfigService = null;

    @Autowired
    public CreateResourceFormImpl(KubernetesConfigService kubernetesConfigService) {
        this.kubernetesConfigService = kubernetesConfigService;
    }

    @Override
    public void createPod(String response) throws ApiException, IOException {
        kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        JsonNode metadataNode = jsonNode.get("metadata");
        if (metadataNode == null) {
            System.out.println("Metadata node is missing in the JSON response.");
            return;
        }

        String name = metadataNode.get("name").asText();
        String namespace = metadataNode.get("namespace").asText();
        V1ObjectMeta metadata = new V1ObjectMetaBuilder()
                .withName(name)
                .withNamespace(namespace)
                .build();

        JsonNode specNode = jsonNode.get("spec");
        if (specNode == null) {
            System.out.println("Spec node is missing in the JSON response.");
            return;
        }

        JsonNode containersNode = specNode.get("containers");
        if (containersNode == null || containersNode.size() == 0) {
            System.out.println("Containers node is missing or empty in the JSON response.");
            return;
        }

        String image = containersNode.get(0).get("image").asText();

        V1Container container = new V1ContainerBuilder()
                .withName(name)
                .withImage(image)
                .build();

        V1PodSpec podSpec = new V1PodSpecBuilder()
                .addToContainers(container)
                .build();

        V1Pod pod = new V1PodBuilder()
                .withMetadata(metadata)
                .withSpec(podSpec)
                .build();

        api.createNamespacedPod(namespace, pod, null, null, null, null);
        System.out.println("Pod created successfully: " + name);
    }
    @Override
    public void createConfigMap(String response) throws ApiException, IOException {
        // Configure Kubernetes access
        kubernetesConfigService.configureKubernetesAccess();

        // Initialize Kubernetes API client
        CoreV1Api api = new CoreV1Api();

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract and validate required fields from the schema
        String apiVersion = jsonNode.get("apiVersion").asText();
        String kind = jsonNode.get("kind").asText();
        String name = jsonNode.get("metadata").get("name").asText();

        // Extract namespace if it exists, otherwise use default
        String namespace = jsonNode.get("metadata").has("namespace") ?
                jsonNode.get("metadata").get("namespace").asText() : "default";

        // Extract ConfigMap data, if present
        JsonNode dataNode = jsonNode.get("data");

        // Create ConfigMap object
        V1ConfigMap configMap = new V1ConfigMapBuilder()
                .withApiVersion(apiVersion)
                .withKind(kind)
                .withNewMetadata()
                .withName(name)
                .withNamespace(namespace)
                .endMetadata()
                .withData(dataNode != null ? toMap(dataNode) : null)
                .build();

        // Deploy ConfigMap to cluster
        api.createNamespacedConfigMap(namespace, configMap, null, null, null, null);
        System.out.println("ConfigMap deployed successfully: " + name);
    }
    
    private Map<String, String> toMap(JsonNode dataNode) {
        Map<String, String> dataMap = new HashMap<>();
        dataNode.fields().forEachRemaining(entry -> {
            dataMap.put(entry.getKey(), entry.getValue().asText());
        });
        return dataMap;
    }
    

    @Override
    public void createDeployment(String response) throws ApiException, IOException {

        kubernetesConfigService.configureKubernetesAccess();
        AppsV1Api api = new AppsV1Api();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);
    
        // Extract deployment metadata
        String name = jsonNode.get("metadata").get("name").asText();
        String namespace = jsonNode.get("metadata").get("namespace").asText();

        // Extract replicas
        int replicas = jsonNode.get("spec").get("replicas").asInt();
    
        // Extract labels
        Map<String, String> labels = new HashMap<>();
        JsonNode labelsNode = jsonNode.get("metadata").get("labels");
        labelsNode.fields().forEachRemaining(entry -> labels.put(entry.getKey(), entry.getValue().asText()));
    
        // Extract container details
        String image = jsonNode.get("spec").get("template").get("spec").get("containers").get(0).get("image").asText();
        int containerPort = jsonNode.get("spec").get("template").get("spec").get("containers").get(0).get("ports").get(0).get("containerPort").asInt();
    
        // Create deployment object
        V1Deployment deployment = new V1Deployment()
                .apiVersion("apps/v1")
                .kind("Deployment")
                .metadata(new V1ObjectMeta().name(name).namespace(namespace).labels(labels))
                .spec(new V1DeploymentSpec()
                        .replicas(replicas)
                        .selector(new V1LabelSelector().matchLabels(labels))
                        .template(new V1PodTemplateSpec()
                                .metadata(new V1ObjectMeta().labels(labels))
                                .spec(new V1PodSpec()
                                        .containers(List.of(
                                                new V1Container()
                                                        .name(name)
                                                        .image(image)
                                                        .ports(List.of(
                                                                new V1ContainerPort().containerPort(containerPort)
                                                        ))
                                        ))
                                )
                        )
                );
    
        // Create deployment in Kubernetes cluster
        api.createNamespacedDeployment(namespace, deployment, null, null, null,null);
    
        System.out.println("Deployment created successfully: " + name);
    }
    @Override
    public void createNamespace(String response) throws ApiException, IOException {
        // Configure Kubernetes access
        kubernetesConfigService.configureKubernetesAccess();

        // Initialize Kubernetes API client
        CoreV1Api api = new CoreV1Api();

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract and validate required fields from the schema
        String apiVersion = jsonNode.get("apiVersion").asText();
        String kind = jsonNode.get("kind").asText();
        String name = jsonNode.get("metadata").get("name").asText();

        try {
            // Check if namespace already exists
            V1Namespace existingNamespace = api.readNamespace(name, null);

            // If the namespace exists, notify and exit
            if (existingNamespace != null) {
                System.out.println("Namespace already exists: " + name);
                return;
            }
        } catch (ApiException e) {
            // If the error is not about the namespace not existing, rethrow it
            if (e.getCode() != 404) {
                throw e;
            }
            // If it's a 404 error, it means the namespace doesn't exist, so we can proceed
        }

        // Create Namespace object
        V1Namespace namespace = new V1NamespaceBuilder()
                .withApiVersion(apiVersion)
                .withKind(kind)
                .withNewMetadata()
                .withName(name)
                .endMetadata()
                .build();

        // Deploy Namespace to cluster
        api.createNamespace(namespace, null, null, null, null);
        System.out.println("Namespace created successfully: " + name);
    }
//    @Override
//    public void createService(String response) throws ApiException, IOException {
//        // Configure Kubernetes access
//        kubernetesConfigService.configureKubernetesAccess();
//        CoreV1Api api = new CoreV1Api();
//
//        // Parse JSON response
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonNode = mapper.readTree(response);
//
//        // Extract service metadata
//        String name = jsonNode.get("metadata").get("name").asText();
//        String namespace = jsonNode.get("metadata").get("namespace").asText();
//
////        // Extract labels (assuming "metadata.labels" is present in the input)
////        Map<String, String> labels = new HashMap<>();
////        JsonNode labelsNode = jsonNode.path("metadata").path("labels");
////        if (!labelsNode.isMissingNode()) {
////            labelsNode.fields().forEachRemaining(entry -> labels.put(entry.getKey(), entry.getValue().asText()));
////        }
//
//        // Extract the selector
//        JsonNode selectorNode = jsonNode.get("spec").get("selector");
//        Map<String, String> selector = new HashMap<>();
//        selectorNode.fields().forEachRemaining(entry -> selector.put(entry.getKey(), entry.getValue().asText()));
//
//        // Extract the ports
//        JsonNode portsNode = jsonNode.get("spec").get("ports");
//        List<V1ServicePort> ports = new ArrayList<>();
//        if (portsNode.isArray()) {
//            for (JsonNode portNode : portsNode) {
//                V1ServicePort port = new V1ServicePort()
//                        .protocol(portNode.get("protocol").asText())
//                        .port(portNode.get("port").asInt())
//                        .targetPort(new IntOrString(portNode.get("targetPort").asInt()));
//                ports.add(port);
//            }
//        }
//
//        // Check if service already exists
//        try {
//            V1Service existingService = api.readNamespacedService(name, namespace, null);
//
//            // If the service exists, notify and exit
//            if (existingService != null) {
//                System.out.println("Service already exists: " + name);
//                return;
//            }
//        } catch (ApiException e) {
//            // If the error is not about the service not existing, rethrow it
//            if (e.getCode() != 404) {
//                throw e;
//            }
//            // If it's a 404 error, it means the service doesn't exist, so we can proceed
//        }
//
//        // Create Service object
//        V1Service service = new V1Service()
//                .apiVersion("v1")
//                .kind("Service")
//                .metadata(new V1ObjectMeta().name(name).namespace(namespace))
//                .spec(new V1ServiceSpec()
//                        .selector(selector)
//                        .ports(ports)
//                );
//
//        // Deploy Service to cluster
//        api.createNamespacedService(namespace, service, null, null, null, null);
//        System.out.println("Service created successfully: " + name);
//    }

    @Override
    public void createJob(String response) throws ApiException, IOException {
        // Configure Kubernetes access
        kubernetesConfigService.configureKubernetesAccess();

        // Initialize Kubernetes API client
        BatchV1Api api = new BatchV1Api();

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract and validate required fields from the schema
        String apiVersion = jsonNode.get("apiVersion").asText();
        String kind = jsonNode.get("kind").asText();
        String name = jsonNode.get("metadata").get("name").asText();
        String namespace = jsonNode.get("metadata").get("namespace").asText();

        // Extract the template.spec.containers
        JsonNode containersNode = jsonNode.get("spec").get("template").get("spec").get("containers");
        List<V1Container> containers = new ArrayList<>();
        if (containersNode.isArray()) {
            for (JsonNode containerNode : containersNode) {
                List<String> command = new ArrayList<>();
                containerNode.get("command").forEach(cmd -> command.add(cmd.asText()));

                V1Container container = new V1Container()
                        .name(containerNode.get("name").asText())
                        .image(containerNode.get("image").asText())
                        .command(command);
                containers.add(container);
            }
        }

        // Extract the restartPolicy
        String restartPolicy = jsonNode.get("spec").get("template").get("spec").get("restartPolicy").asText();

        // Extract the backoffLimit
        int backoffLimit = jsonNode.get("spec").get("backoffLimit").asInt();

        // Create Job object
        V1Job job = new V1JobBuilder()
                .withApiVersion(apiVersion)
                .withKind(kind)
                .withNewMetadata()
                .withName(name)
                .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                .withBackoffLimit(backoffLimit)
                .withNewTemplate()
                .withNewSpec()
                .withContainers(containers)
                .withRestartPolicy(restartPolicy)
                .endSpec()
                .endTemplate()
                .endSpec()
                .build();

        // Check if job already exists
        try {
            V1Job existingJob = api.readNamespacedJob(name, namespace, null);
            if (existingJob != null) {
                System.out.println("Job already exists: " + name);
                return;
            }
        } catch (ApiException e) {
            if (e.getCode() != 404) {
                throw e;
            }
            // If it's a 404 error, it means the job doesn't exist, so we can proceed
        }

        // Deploy Job to cluster
        api.createNamespacedJob(namespace, job, null, null, null,null);
        System.out.println("Job created successfully: " + name);
    }
//    @Override
//    public void createIngress(String response) throws ApiException, IOException {
//        // Configure Kubernetes access
//        kubernetesConfigService.configureKubernetesAccess();
//
//        // Initialize Kubernetes API client
//        NetworkingV1Api api = new NetworkingV1Api();
//
//        // Parse JSON response
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonNode = mapper.readTree(response);
//
//        // Extract and validate required fields from the schema
//        String apiVersion = jsonNode.get("apiVersion").asText();
//        String kind = jsonNode.get("kind").asText();
//        String name = jsonNode.get("metadata").get("name").asText();
//        String namespace = jsonNode.get("metadata").get("namespace").asText();
//
//        // Extract annotations
//        Map<String, String> annotations = new HashMap<>();
//        JsonNode annotationsNode = jsonNode.get("metadata").get("annotations");
//        if (annotationsNode != null && annotationsNode.isObject()) {
//            annotationsNode.fields().forEachRemaining(entry -> {
//                annotations.put(entry.getKey(), entry.getValue().asText());
//            });
//        }
//
//        // Extract ingress class name
//        String ingressClassName = jsonNode.get("spec").get("ingressClassName").asText();
//
//        // Check if ingress already exists
//        try {
//            V1Ingress existingIngress = api.readNamespacedIngress(name, namespace,  null);
//            // If the ingress exists, notify and exit
//            if (existingIngress != null) {
//                System.out.println("Ingress already exists: " + name);
//                return;
//            }
//        } catch (ApiException e) {
//            // If the error is not about the ingress not existing, rethrow it
//            if (e.getCode() != 404) {
//                throw e;
//            }
//            // If it's a 404 error, it means the ingress doesn't exist, so we can proceed
//        }
//
//        // Create Ingress metadata
//        V1ObjectMeta metadata = new V1ObjectMeta()
//                .name(name)
//                .namespace(namespace)
//                .annotations(annotations);
//
//        // Create Ingress spec
//        V1IngressSpec ingressSpec = new V1IngressSpec()
//                .ingressClassName(ingressClassName);
//
//        // Create Ingress object
//        V1Ingress ingress = new V1Ingress()
//                .apiVersion(apiVersion)
//                .kind(kind)
//                .metadata(metadata)
//                .spec(ingressSpec);
//
//        // Deploy Ingress to cluster
//        api.createNamespacedIngress(namespace, ingress, null,null, null, null);
//        System.out.println("Ingress created successfully: " + name);
//    }


    @Override
    public void createStatefulSet(String response) throws ApiException, IOException {
        // Configure Kubernetes access
        kubernetesConfigService.configureKubernetesAccess();
        AppsV1Api api = new AppsV1Api();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract StatefulSet metadata
        String name = jsonNode.get("metadata").get("name").asText();
        String namespace = jsonNode.get("metadata").has("namespace") ?
                jsonNode.get("metadata").get("namespace").asText() : "default";

        // Extract replicas
        int replicas = jsonNode.get("spec").get("replicas").asInt();

        // Extract labels
        Map<String, String> labels = new HashMap<>();
        JsonNode labelsNode = jsonNode.get("spec").get("template").get("metadata").get("labels");
        labelsNode.fields().forEachRemaining(entry -> labels.put(entry.getKey(), entry.getValue().asText()));

        // Extract container details
        String image = jsonNode.get("spec").get("template").get("spec").get("containers").get(0).get("image").asText();
        int containerPort = jsonNode.get("spec").get("template").get("spec").get("containers").get(0).get("ports").get(0).get("containerPort").asInt();

        // Create StatefulSet object
        V1StatefulSet statefulSet = new V1StatefulSet()
                .apiVersion("apps/v1")
                .kind("StatefulSet")
                .metadata(new V1ObjectMeta().name(name).namespace(namespace).labels(labels))
                .spec(new V1StatefulSetSpec()
                        .replicas(replicas)
                        .selector(new V1LabelSelector().matchLabels(labels))
                        .serviceName(jsonNode.get("spec").get("serviceName").asText())
                        .template(new V1PodTemplateSpec()
                                .metadata(new V1ObjectMeta().labels(labels))
                                .spec(new V1PodSpec()
                                        .containers(List.of(
                                                new V1Container()
                                                        .name(name)
                                                        .image(image)
                                                        .ports(List.of(
                                                                new V1ContainerPort().containerPort(containerPort)
                                                        ))
                                        ))
                                )
                        )
                );

        // Create StatefulSet in Kubernetes cluster
        api.createNamespacedStatefulSet(namespace, statefulSet, null, null, null,null);

        System.out.println("StatefulSet created successfully: " + name);
    }
    @Override
    public void createDaemonSet(String response) throws ApiException, IOException {
        // Configure Kubernetes access
        kubernetesConfigService.configureKubernetesAccess();

        // Initialize Kubernetes API client
        AppsV1Api api = new AppsV1Api();

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract DaemonSet metadata
        String name = jsonNode.get("metadata").get("name").asText();
        String namespace = jsonNode.get("metadata").get("namespace").asText();

        // Extract labels
        Map<String, String> labels = new HashMap<>();
        JsonNode labelsNode = jsonNode.get("metadata").get("labels");
        labelsNode.fields().forEachRemaining(entry -> labels.put(entry.getKey(), entry.getValue().asText()));

        // Extract tolerations
        List<V1Toleration> tolerations = new ArrayList<>();
        JsonNode tolerationsNode = jsonNode.get("spec").get("template").get("spec").get("tolerations");
        for (JsonNode tolerationNode : tolerationsNode) {
            V1Toleration toleration = new V1Toleration()
                    .key(tolerationNode.get("key").asText())
                    .operator(tolerationNode.get("operator").asText())
                    .effect(tolerationNode.get("effect").asText());
            tolerations.add(toleration);
        }

        // Extract containers
        List<V1Container> containers = new ArrayList<>();
        JsonNode containersNode = jsonNode.get("spec").get("template").get("spec").get("containers");
        for (JsonNode containerNode : containersNode) {
            String containerName = containerNode.get("name").asText();
            String image = containerNode.get("image").asText();

            // Extract resources
            V1ResourceRequirements resources = new V1ResourceRequirements()
                    .limits(Collections.singletonMap("memory", new Quantity(containerNode.get("resources").get("limits").get("memory").asText())))
                    .requests(Collections.singletonMap("cpu", new Quantity(containerNode.get("resources").get("requests").get("cpu").asText())))
                    .requests(Collections.singletonMap("memory", new Quantity(containerNode.get("resources").get("requests").get("memory").asText())));

            // Extract volume mounts
            List<V1VolumeMount> volumeMounts = new ArrayList<>();
            JsonNode volumeMountsNode = containerNode.get("volumeMounts");
            for (JsonNode volumeMountNode : volumeMountsNode) {
                V1VolumeMount volumeMount = new V1VolumeMount()
                        .name(volumeMountNode.get("name").asText())
                        .mountPath(volumeMountNode.get("mountPath").asText());
                volumeMounts.add(volumeMount);
            }

            V1Container container = new V1Container()
                    .name(containerName)
                    .image(image)
                    .resources(resources)
                    .volumeMounts(volumeMounts);

            containers.add(container);
        }

        // Extract terminationGracePeriodSeconds
        int terminationGracePeriodSeconds = jsonNode.get("spec").get("template").get("spec").get("terminationGracePeriodSeconds").asInt();

        // Extract volumes
        List<V1Volume> volumes = new ArrayList<>();
        JsonNode volumesNode = jsonNode.get("spec").get("template").get("spec").get("volumes");
        for (JsonNode volumeNode : volumesNode) {
            V1Volume volume = new V1Volume()
                    .name(volumeNode.get("name").asText())
                    .hostPath(new V1HostPathVolumeSource().path(volumeNode.get("hostPath").get("path").asText()));
            volumes.add(volume);
        }

        // Create DaemonSet object
        V1DaemonSet daemonSet = new V1DaemonSet()
                .apiVersion(jsonNode.get("apiVersion").asText())
                .kind(jsonNode.get("kind").asText())
                .metadata(new V1ObjectMeta().name(name).namespace(namespace).labels(labels))
                .spec(new V1DaemonSetSpec()
                        .selector(new V1LabelSelector().matchLabels(Collections.singletonMap("name", name))) // Update selector to match the label "name" with the DaemonSet name
                        .template(new V1PodTemplateSpec()
                                .metadata(new V1ObjectMeta().labels(Collections.singletonMap("name", name))) // Update template metadata labels to match the label "name" with the DaemonSet name
                                .spec(new V1PodSpec()
                                        .tolerations(tolerations)
                                        .containers(containers)
                                        .terminationGracePeriodSeconds((long) terminationGracePeriodSeconds)
                                        .volumes(volumes)
                                )
                        )
                );

        // Create DaemonSet in Kubernetes cluster
        try {
            // Create DaemonSet in Kubernetes cluster
            api.createNamespacedDaemonSet(namespace, daemonSet, null, null, null, null);
            System.out.println("DaemonSet created successfully: " + name);
        } catch (ApiException e) {
            System.err.println("Exception when calling AppsV1Api#createNamespacedDaemonSet");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            throw e;
        }
    }

    @Override
    public void createPersistentVolume(String response) throws ApiException, IOException {
        // Configure Kubernetes access
        kubernetesConfigService.configureKubernetesAccess();

        // Initialize Kubernetes API client
        CoreV1Api api = new CoreV1Api();

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract PersistentVolume metadata
        String name = jsonNode.get("metadata").get("name").asText();

        // Extract labels
        Map<String, String> labels = new HashMap<>();
        JsonNode labelsNode = jsonNode.get("metadata").get("labels");
        labelsNode.fields().forEachRemaining(entry -> labels.put(entry.getKey(), entry.getValue().asText()));

        // Extract storageClassName
        String storageClassName = jsonNode.get("spec").get("storageClassName").asText();

        // Extract capacity
        Map<String, Quantity> capacity = new HashMap<>();
        String storage = jsonNode.get("spec").get("capacity").get("storage").asText();
        capacity.put("storage", new Quantity(storage));

        // Extract accessModes
        List<String> accessModes = new ArrayList<>();
        JsonNode accessModesNode = jsonNode.get("spec").get("accessModes");
        for (JsonNode accessModeNode : accessModesNode) {
            accessModes.add(accessModeNode.asText());
        }

        // Extract hostPath
        String hostPath = jsonNode.get("spec").get("hostPath").get("path").asText();

        V1PersistentVolumeList existingPVs = api.listPersistentVolume(null, null, null, null, null, null, null, null, null, null);
        for (V1PersistentVolume pv : existingPVs.getItems()) {
            if (pv.getMetadata().getName().equals(name)) {
                System.out.println("PersistentVolume with name " + name + " already exists.");
                return; // Exit the function if the PersistentVolume already exists
            }
        }


        // Create PersistentVolume object
        V1PersistentVolume persistentVolume = new V1PersistentVolume()
                .apiVersion(jsonNode.get("apiVersion").asText())
                .kind(jsonNode.get("kind").asText())
                .metadata(new V1ObjectMeta().name(name).labels(labels))
                .spec(new V1PersistentVolumeSpec()
                        .storageClassName(storageClassName)
                        .capacity(capacity)
                        .accessModes(accessModes)
                        .hostPath(new V1HostPathVolumeSource().path(hostPath))
                );

        // Create PersistentVolume in Kubernetes cluster
        try {
            api.createPersistentVolume(persistentVolume, null,null, null, null);
            System.out.println("PersistentVolume created successfully: " + name);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#createPersistentVolume");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            throw e;
        }
    }
    @Override
    public void createPersistentVolumeClaim(String response) throws ApiException, IOException {
        // Configure Kubernetes access
        kubernetesConfigService.configureKubernetesAccess();

        // Initialize Kubernetes API client
        CoreV1Api api = new CoreV1Api();

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract PersistentVolumeClaim metadata
        String name = jsonNode.get("metadata").get("name").asText();

        // Check if the PersistentVolumeClaim already exists
        V1PersistentVolumeClaimList existingPVCs = api.listPersistentVolumeClaimForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        for (V1PersistentVolumeClaim pvc : existingPVCs.getItems()) {
            if (pvc.getMetadata().getName().equals(name)) {
                System.out.println("PersistentVolumeClaim with name " + name + " already exists.");
                return; // Exit the function if the PersistentVolumeClaim already exists
            }
        }

        // Extract storageClassName
        String storageClassName = jsonNode.get("spec").get("storageClassName").asText();

        // Extract accessModes
        List<String> accessModes = new ArrayList<>();
        JsonNode accessModesNode = jsonNode.get("spec").get("accessModes");
        for (JsonNode accessModeNode : accessModesNode) {
            accessModes.add(accessModeNode.asText());
        }

        // Extract resources requests
        String storage = jsonNode.get("spec").get("resources").get("requests").get("storage").asText();
        Map<String, Quantity> requests = new HashMap<>();
        requests.put("storage", new Quantity(storage));

        // Create PersistentVolumeClaim object
        V1PersistentVolumeClaim persistentVolumeClaim = new V1PersistentVolumeClaim()
                .apiVersion(jsonNode.get("apiVersion").asText())
                .kind(jsonNode.get("kind").asText())
                .metadata(new V1ObjectMeta().name(name))
                .spec(new V1PersistentVolumeClaimSpec()
                        .storageClassName(storageClassName)
                        .accessModes(accessModes)
                        .resources(new V1ResourceRequirements().requests(requests))
                );

        // Create PersistentVolumeClaim in Kubernetes cluster
        try {
            api.createNamespacedPersistentVolumeClaim("default", persistentVolumeClaim, null, null, null, null);
            System.out.println("PersistentVolumeClaim created successfully: " + name);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#createNamespacedPersistentVolumeClaim");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            throw e;
        }
    }
    @Override
    public void createStorageClass(String response) throws ApiException, IOException {
        // Configure Kubernetes access
        kubernetesConfigService.configureKubernetesAccess();

        // Initialize Kubernetes API client
        StorageV1Api api = new StorageV1Api();

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract StorageClass metadata
        String name = jsonNode.get("metadata").get("name").asText();

        // Check if the StorageClass already exists
        V1StorageClassList existingStorageClasses = api.listStorageClass(null,null, null, null, null, null, null, null, null, null);
        for (V1StorageClass sc : existingStorageClasses.getItems()) {
            if (sc.getMetadata().getName().equals(name)) {
                System.out.println("StorageClass with name " + name + " already exists.");
                return; // Exit the function if the StorageClass already exists
            }
        }

        // Extract provisioner
        String provisioner = jsonNode.get("provisioner").asText();

        // Extract parameters
        Map<String, String> parameters = new HashMap<>();
        JsonNode parametersNode = jsonNode.get("parameters");
        parametersNode.fields().forEachRemaining(entry -> parameters.put(entry.getKey(), entry.getValue().asText()));

        // Extract allowVolumeExpansion
        boolean allowVolumeExpansion = jsonNode.get("allowVolumeExpansion").asBoolean();

        // Create StorageClass object
        V1StorageClass storageClass = new V1StorageClass()
                .apiVersion(jsonNode.get("apiVersion").asText())
                .kind(jsonNode.get("kind").asText())
                .metadata(new V1ObjectMeta().name(name))
                .provisioner(provisioner)
                .parameters(parameters)
                .allowVolumeExpansion(allowVolumeExpansion);

        // Create StorageClass in Kubernetes cluster
        try {
            api.createStorageClass(storageClass, null, null, null, null);
            System.out.println("StorageClass created successfully: " + name);
        } catch (ApiException e) {
            System.err.println("Exception when calling StorageV1Api#createStorageClass");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            throw e;
        }
    }
    @Override
    public void createReplicaSet(String response) throws ApiException, IOException {
        // Configure Kubernetes access
        kubernetesConfigService.configureKubernetesAccess();

        // Initialize Kubernetes API client
        AppsV1Api api = new AppsV1Api();

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract ReplicaSet metadata
        String name = jsonNode.get("metadata").get("name").asText();

        // Check if the ReplicaSet already exists
        V1ReplicaSetList existingReplicaSets = api.listNamespacedReplicaSet("default", null,null, null, null, null, null, null, null, null, null);
        for (V1ReplicaSet rs : existingReplicaSets.getItems()) {
            if (rs.getMetadata().getName().equals(name)) {
                System.out.println("ReplicaSet with name " + name + " already exists.");
                return; // Exit the function if the ReplicaSet already exists
            }
        }

        // Extract labels
        Map<String, String> labels = new HashMap<>();
        JsonNode labelsNode = jsonNode.get("metadata").get("labels");
        labelsNode.fields().forEachRemaining(entry -> labels.put(entry.getKey(), entry.getValue().asText()));

        // Extract selector matchLabels
        Map<String, String> matchLabels = new HashMap<>();
        JsonNode matchLabelsNode = jsonNode.get("spec").get("selector").get("matchLabels");
        matchLabelsNode.fields().forEachRemaining(entry -> matchLabels.put(entry.getKey(), entry.getValue().asText()));
        String namespace = jsonNode.has("metadata") && jsonNode.get("metadata").has("namespace") ?
                jsonNode.get("metadata").get("namespace").asText() : "default";

        // Extract containers
        List<V1Container> containers = new ArrayList<>();
        JsonNode containersNode = jsonNode.get("spec").get("template").get("spec").get("containers");
        for (JsonNode containerNode : containersNode) {
            String containerName = containerNode.get("name").asText();
            String image = containerNode.get("image").asText();

            V1Container container = new V1Container()
                    .name(containerName)
                    .image(image);

            containers.add(container);
        }

        // Extract replicas
        int replicas = jsonNode.get("spec").get("replicas").asInt();

        // Create ReplicaSet object
        V1ReplicaSet replicaSet = new V1ReplicaSet()
                .apiVersion(jsonNode.get("apiVersion").asText())
                .kind(jsonNode.get("kind").asText())
                .metadata(new V1ObjectMeta().name(name).namespace(namespace).labels(labels))
                .spec(new V1ReplicaSetSpec()
                        .replicas(replicas)
                        .selector(new V1LabelSelector().matchLabels(matchLabels))
                        .template(new V1PodTemplateSpec()
                                .metadata(new V1ObjectMeta().labels(matchLabels))
                                .spec(new V1PodSpec()
                                        .containers(containers)
                                )
                        )
                );

        // Create ReplicaSet in Kubernetes cluster
        try {
            api.createNamespacedReplicaSet(namespace, replicaSet, null, null, null, null);
            System.out.println("ReplicaSet created successfully: " + name);
        } catch (ApiException e) {
            System.err.println("Exception when calling AppsV1Api#createNamespacedReplicaSet");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            throw e;
        }
    }

    @Override
    public void createService(String response) throws ApiException, IOException {
        // Configure Kubernetes access
        kubernetesConfigService.configureKubernetesAccess();

        // Initialize Kubernetes API client
        CoreV1Api api = new CoreV1Api();

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract Service metadata
        String name = jsonNode.get("metadata").get("name").asText();

        // Determine the namespace
        String namespace = jsonNode.has("metadata") && jsonNode.get("metadata").has("namespace") ?
                jsonNode.get("metadata").get("namespace").asText() : "default";

        // Check if the Service already exists
        V1ServiceList existingServices = api.listNamespacedService(namespace,null, null, null, null, null, null, null, null, null, null);
        for (V1Service svc : existingServices.getItems()) {
            if (svc.getMetadata().getName().equals(name)) {
                System.out.println("Service with name " + name + " already exists in namespace " + namespace);
                return; // Exit the function if the Service already exists
            }
        }

        // Extract selector
        Map<String, String> selector = new HashMap<>();
        JsonNode selectorNode = jsonNode.get("spec").get("selector");
        selectorNode.fields().forEachRemaining(entry -> selector.put(entry.getKey(), entry.getValue().asText()));

        // Extract ports
        List<V1ServicePort> ports = new ArrayList<>();
        JsonNode portsNode = jsonNode.get("spec").get("ports");
        for (JsonNode portNode : portsNode) {
            String portName = portNode.get("name").asText();
            String protocol = portNode.get("protocol").asText();
            int port = portNode.get("port").asInt();
            String targetPort = portNode.get("targetPort").asText();

            V1ServicePort servicePort = new V1ServicePort()
                    .name(portName)
                    .protocol(protocol)
                    .port(port)
                    .targetPort(new IntOrString(targetPort));

            ports.add(servicePort);
        }

        // Create Service object
        V1Service service = new V1Service()
                .apiVersion(jsonNode.get("apiVersion").asText())
                .kind(jsonNode.get("kind").asText())
                .metadata(new V1ObjectMeta().name(name).namespace(namespace))
                .spec(new V1ServiceSpec()
                        .selector(selector)
                        .ports(ports)
                );

        // Create Service in Kubernetes cluster
        try {
            api.createNamespacedService(namespace, service, null, null, null, null);
            System.out.println("Service created successfully: " + name + " in namespace: " + namespace);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#createNamespacedService");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            throw e;
        }
    }
    @Override
    public void createIngress(String response) throws ApiException, IOException {
        // Configure Kubernetes access
        kubernetesConfigService.configureKubernetesAccess();

        // Initialize Kubernetes API client
        NetworkingV1Api api = new NetworkingV1Api();

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract Ingress metadata
        String name = jsonNode.get("metadata").get("name").asText();

        // Determine the namespace
        String namespace = jsonNode.has("metadata") && jsonNode.get("metadata").has("namespace") ?
                jsonNode.get("metadata").get("namespace").asText() : "default";

        // Check if the Ingress already exists
        V1IngressList existingIngresses = api.listNamespacedIngress(namespace, null,null, null, null, null, null, null, null, null, null);
        for (V1Ingress ingress : existingIngresses.getItems()) {
            if (ingress.getMetadata().getName().equals(name)) {
                System.out.println("Ingress with name " + name + " already exists in namespace " + namespace);
                return; // Exit the function if the Ingress already exists
            }
        }

        // Extract defaultBackend
        JsonNode defaultBackendNode = jsonNode.get("spec").get("defaultBackend").get("resource");
        V1TypedLocalObjectReference defaultBackendResource = new V1TypedLocalObjectReference()
                .apiGroup(defaultBackendNode.get("apiGroup").asText())
                .kind(defaultBackendNode.get("kind").asText())
                .name(defaultBackendNode.get("name").asText());

        // Extract rules
        List<V1IngressRule> rules = new ArrayList<>();
        JsonNode rulesNode = jsonNode.get("spec").get("rules");
        for (JsonNode ruleNode : rulesNode) {
            JsonNode httpNode = ruleNode.get("http");
            JsonNode pathsNode = httpNode.get("paths");
            for (JsonNode pathNode : pathsNode) {
                String path = pathNode.get("path").asText();
                String pathType = pathNode.get("pathType").asText();

                JsonNode backendNode = pathNode.get("backend").get("resource");
                V1TypedLocalObjectReference backendResource = new V1TypedLocalObjectReference()
                        .apiGroup(backendNode.get("apiGroup").asText())
                        .kind(backendNode.get("kind").asText())
                        .name(backendNode.get("name").asText());

                V1IngressBackend ingressBackend = new V1IngressBackend()
                        .resource(backendResource);

                V1HTTPIngressPath ingressPath = new V1HTTPIngressPath()
                        .path(path)
                        .pathType(pathType)
                        .backend(ingressBackend);

                V1IngressRule ingressRule = new V1IngressRule()
                        .http(new V1HTTPIngressRuleValue()
                                .paths(Arrays.asList(ingressPath)));

                rules.add(ingressRule);
            }
        }

        // Create Ingress object
        V1Ingress ingress = new V1Ingress()
                .apiVersion(jsonNode.get("apiVersion").asText())
                .kind(jsonNode.get("kind").asText())
                .metadata(new V1ObjectMeta().name(name).namespace(namespace))
                .spec(new V1IngressSpec()
                        .defaultBackend(new V1IngressBackend()
                                .resource(defaultBackendResource))
                        .rules(rules)
                );

        // Create Ingress in Kubernetes cluster
        try {
            api.createNamespacedIngress(namespace, ingress, null,null, null, null);
            System.out.println("Ingress created successfully: " + name + " in namespace: " + namespace);
        } catch (ApiException e) {
            System.err.println("Exception when calling NetworkingV1Api#createNamespacedIngress");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            throw e;
        }
    }
    @Override
    public void createHPA(String response) throws ApiException, IOException {
        // Configure Kubernetes access
        kubernetesConfigService.configureKubernetesAccess();

        // Initialize Kubernetes API client
        AutoscalingV1Api api = new AutoscalingV1Api();

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract HPA metadata
        String name = jsonNode.get("metadata").get("name").asText();
        String namespace = jsonNode.has("metadata") && jsonNode.get("metadata").has("namespace") ?
                jsonNode.get("metadata").get("namespace").asText() : "default";

        // Check if the HPA already exists
        try {
            api.readNamespacedHorizontalPodAutoscaler(name, namespace, null);
            System.out.println("HPA with name " + name + " already exists in namespace " + namespace);
            return; // Exit the function if the HPA already exists
        } catch (ApiException e) {
            if (e.getCode() != HttpStatus.NOT_FOUND.value()) {
                System.err.println("Exception when calling AutoscalingV1Api#readNamespacedHorizontalPodAutoscaler");
                System.err.println("Status code: " + e.getCode());
                System.err.println("Reason: " + e.getResponseBody());
                System.err.println("Response headers: " + e.getResponseHeaders());
                throw e;
            }
        }

        // Extract HPA spec
        int minReplicas = jsonNode.get("spec").get("minReplicas").asInt();
        int maxReplicas = jsonNode.get("spec").get("maxReplicas").asInt();
        int targetCPUUtilizationPercentage = jsonNode.get("spec").get("targetCPUUtilizationPercentage").asInt();
        String targetDeploymentName = jsonNode.get("spec").get("scaleTargetRef").get("name").asText();

        // Create HPA object
        V1HorizontalPodAutoscaler hpa = new V1HorizontalPodAutoscaler()
                .metadata(new V1ObjectMeta().name(name).namespace(namespace))
                .spec(new V1HorizontalPodAutoscalerSpec()
                        .minReplicas(minReplicas)
                        .maxReplicas(maxReplicas)
                        .scaleTargetRef(new V1CrossVersionObjectReference()
                                .kind("Deployment")
                                .name(targetDeploymentName)
                                .apiVersion("apps/v1"))
                        .targetCPUUtilizationPercentage(targetCPUUtilizationPercentage)
                );

        // Create HPA in Kubernetes cluster
        try {
            api.createNamespacedHorizontalPodAutoscaler(namespace, hpa, null, null, null, null);
            System.out.println("HPA created successfully: " + name + " in namespace " + namespace);
        } catch (ApiException e) {
            System.err.println("Exception when calling AutoscalingV1Api#createNamespacedHorizontalPodAutoscaler");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            throw e;
        }
    }


}
