package com.example.service.describeressource;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.kubernetes.client.openapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.KubernetesConfig.KubernetesConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.openapi.apis.StorageV1Api;

@Service
public class DescServiceImpl<ExtensionsV1beta1Api> implements DescService {


    @Autowired
    private KubernetesConfigService KubernetesConfigService;

    public V1Pod getPod(String resourceType, String podName) throws ApiException, IOException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        ApiClient client = KubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api(client);
        V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        String namespace = null;
        for (V1Pod pod : podList.getItems()) {
            if (pod.getMetadata().getName().equals(podName)) {
                namespace = pod.getMetadata().getNamespace();
                break;
            }
        }
        V1PodList pods = api.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null,null);
        for (V1Pod pod : pods.getItems()) {
            if (pod.getMetadata().getName().equals(podName)) {
                return pod;
            }
        }
        return null; // Pod not found
    }

    @Override
    public String getResourceDescriptions(String resourceName, String resourceType) throws ApiException, IOException {
        // Create a client to interact with the Kubernetes API
        ApiClient client = KubernetesConfigService.configureKubernetesAccess();

        // Create an API instance based on the resource type
        switch (resourceType) {
            case "pod":
                return getPodDescription(client, resourceName);
            case "service":
                return getServiceDescription(client, resourceName);
            case "deployment":
                return getDeploymentDescription(client, resourceName);
            case "configmap":
                return getConfigMapDescription(client, resourceName);
            case "node":
                return getNodeDescription(client, resourceName);
            case "stateful":
                return getStatefulSetDescription(client, resourceName);
            case "persistentvolume":
                return getPersistentVolumeDescription(client, resourceName);
            case "pvc":
                return getPersistentVolumeClaimDescription(client, resourceName);
            case "job":
                return getJobDescription(client, resourceName);
            case "namespace":
                return getNamespaceDescription(client, resourceName);
            case "deamonset":
                return getDaemonSetDescription(client, resourceName);
            case "replicaset":
                return getReplicaSetDescription(client, resourceName);
            case "endpoint":
                return getEndpointDescription(client, resourceName);
            case "ingress":
                return getIngressDescription(client, resourceName);
            case "sc":
                return getStorageClassDescription(client, resourceName);
            default:
                throw new IllegalArgumentException("Unsupported resource type: " + resourceType);
        }
    }

    public V1Pod getPod(ApiClient client,  String podName) throws ApiException, IOException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        CoreV1Api api = new CoreV1Api(client);
        V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        String namespace = null;
        for (V1Pod pod : podList.getItems()) {
            if (pod.getMetadata().getName().equals(podName)) {
                namespace = pod.getMetadata().getNamespace();
                return pod;
            }
        }

        return null; // Pod not found
    }
    private String getPodDescription(ApiClient client, String podName) throws ApiException, IOException {
        CoreV1Api api = new CoreV1Api(client);
    
        // Fetch the namespace dynamically from the pod metadata
        V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        String namespace = null;
        for (V1Pod pod : podList.getItems()) {
            if (pod.getMetadata().getName().equals(podName)) {
                namespace = pod.getMetadata().getNamespace();
                break;
            }
        }
    
        if (namespace == null) {
            throw new ApiException("Namespace not found for pod: " + podName);
        }
    
        V1Pod pod = api.readNamespacedPod(podName, namespace, null);
    
        return generatePodDescription(pod);
    }


    public String generatePodDescription(V1Pod pod) {
        StringBuilder descriptionBuilder = new StringBuilder();

        // API Version and Kind
        descriptionBuilder.append("API Version: ").append(pod.getApiVersion()).append("\n");
        descriptionBuilder.append("Kind: ").append(pod.getKind()).append("\n");

        // Metadata
        descriptionBuilder.append("Metadata:\n");
        descriptionBuilder.append("\tName: ").append(pod.getMetadata().getName()).append("\n");

        // Labels
        Map<String, String> labels = pod.getMetadata().getLabels();
        if (labels != null && !labels.isEmpty()) {
            descriptionBuilder.append("\tLabels:\n");
            for (Map.Entry<String, String> entry : labels.entrySet()) {
                descriptionBuilder.append("\t\t").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }

        descriptionBuilder.append("\tCreation Timestamp: ").append(pod.getMetadata().getCreationTimestamp()).append("\n");
        descriptionBuilder.append("\tOwner References: ");
        if (pod.getMetadata().getOwnerReferences() != null && !pod.getMetadata().getOwnerReferences().isEmpty()) {
            for (V1OwnerReference ownerReference : pod.getMetadata().getOwnerReferences()) {
                descriptionBuilder.append(ownerReference.getKind()).append("/").append(ownerReference.getName());
                if (!ownerReference.equals(pod.getMetadata().getOwnerReferences().get(pod.getMetadata().getOwnerReferences().size() - 1))) {
                    descriptionBuilder.append(", ");
                }
            }
        }
        descriptionBuilder.append("\n");

        // Spec
        descriptionBuilder.append("Spec:\n");
        descriptionBuilder.append("\tNode Name: ").append(pod.getSpec().getNodeName()).append("\n");
        descriptionBuilder.append("\tService Account Name: ").append(pod.getSpec().getServiceAccountName()).append("\n");

        // Containers
        List<V1Container> containers = pod.getSpec().getContainers();
        descriptionBuilder.append("\tContainers:\n");
        for (V1Container container : containers) {
            descriptionBuilder.append("\t\tName: ").append(container.getName()).append("\n");
            descriptionBuilder.append("\t\tImage: ").append(container.getImage()).append("\n");
            // Iterate through container ports
            List<V1ContainerPort> ports = container.getPorts();
            if (ports != null && !ports.isEmpty()) {
                for (V1ContainerPort port : ports) {
                    descriptionBuilder.append("\t\tContainer Port: ").append(port.getContainerPort()).append("\n");
                    descriptionBuilder.append("\t\tName: ").append(port.getName()).append("\n");
                }
            }
            descriptionBuilder.append("\n");
        }

        return descriptionBuilder.toString();
    }

    private String getServiceDescription(ApiClient client, String serviceName) throws ApiException, IOException {
        CoreV1Api api = new CoreV1Api(client);
        V1ServiceList serviceList =api.listServiceForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        String namespace = null;
        for (V1Service service : serviceList.getItems()) {
            if (service.getMetadata().getName().equals(serviceName)) {
                namespace = service.getMetadata().getNamespace();
                break;
            }
        }
    
        if (namespace == null) {
            throw new ApiException("Namespace not found for pod: " + serviceName);
        }
        V1Service service = api.readNamespacedService(serviceName, namespace, null);

        // Generate and return the service description
        return generateServiceDescription(service);
    }

    private String generateServiceDescription(V1Service service) {
        StringBuilder descriptionBuilder = new StringBuilder();
    
        descriptionBuilder.append("Name: ").append(service.getMetadata().getName()).append("\n");
        descriptionBuilder.append("Namespace: ").append(service.getMetadata().getNamespace()).append("\n");
        descriptionBuilder.append("Labels: ").append(service.getMetadata().getLabels()).append("\n");
        descriptionBuilder.append("Status: ").append(service.getStatus()).append("\n");
        descriptionBuilder.append("Annotations: ").append(service.getMetadata().getAnnotations()).append("\n");
        descriptionBuilder.append("Creation Timestamp: ").append(service.getMetadata().getCreationTimestamp()).append("\n");
        descriptionBuilder.append("Owner References: ").append(service.getMetadata().getOwnerReferences()).append("\n");
    
        // Service specific attributes
        descriptionBuilder.append("Type: ").append(service.getSpec().getType()).append("\n");
        descriptionBuilder.append("Cluster IP: ").append(service.getSpec().getClusterIP()).append("\n");
        descriptionBuilder.append("Session Affinity: ").append(service.getSpec().getSessionAffinity()).append("\n");
    
        // Ports
        List<V1ServicePort> ports = service.getSpec().getPorts();
        if (!ports.isEmpty()) {
            descriptionBuilder.append("Ports:").append("\n");
            for (V1ServicePort port : ports) {
                descriptionBuilder.append("\tName: ").append(port.getName()).append("\n");
                descriptionBuilder.append("\tProtocol: ").append(port.getProtocol()).append("\n");
                descriptionBuilder.append("\tPort: ").append(port.getPort()).append("\n");
                descriptionBuilder.append("\tTarget Port: ").append(port.getTargetPort()).append("\n");
                descriptionBuilder.append("\tNode Port: ").append(port.getNodePort()).append("\n");
            }
        }
    
        // Selector
        Map<String, String> selector = service.getSpec().getSelector();
        if (selector != null && !selector.isEmpty()) {
            descriptionBuilder.append("Selector: ").append(selector).append("\n");
        }
    
        // External IP
        List<String> externalIPs = service.getSpec().getExternalIPs();
        if (externalIPs != null && !externalIPs.isEmpty()) {
            descriptionBuilder.append("External IPs: ").append(externalIPs).append("\n");
        }
    
        // Load Balancer details
        V1LoadBalancerStatus loadBalancer = service.getStatus().getLoadBalancer();
        if (loadBalancer != null) {
            descriptionBuilder.append("Load Balancer: ").append(loadBalancer).append("\n");
        }
    
        
    
        return descriptionBuilder.toString();
    }
    
    

    private String getDeploymentDescription(ApiClient client, String deploymentName) throws ApiException, IOException {
        AppsV1Api api = new AppsV1Api(client);
        V1DeploymentList deploymentList = api.listDeploymentForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        String namespace = null;
        for (V1Deployment deployment : deploymentList.getItems()) {
            if (deployment.getMetadata().getName().equals(deploymentName)) {
                namespace = deployment.getMetadata().getNamespace();
                break;
            }
        }
    
        if (namespace == null) {
            throw new ApiException("Namespace not found for pod: " + deploymentName);
        }

        V1Deployment deployment = api.readNamespacedDeployment(deploymentName, namespace, null);

        // Generate and return the deployment description
        return generateDeploymentDescription(deployment);
    }

    private String generateDeploymentDescription(V1Deployment deployment) {
     
        StringBuilder descriptionBuilder = new StringBuilder();

        descriptionBuilder.append("\tName: ").append(deployment.getMetadata().getName()).append("\n");
        descriptionBuilder.append("\tNamespace: ").append(deployment.getMetadata().getNamespace()).append("\n");
        descriptionBuilder.append("\tLabels: ").append(deployment.getMetadata().getLabels()).append("\n");
        // descriptionBuilder.append("\tAnnotations: ").append(deployment.getMetadata().getAnnotations()).append("\n");
        // Annotations
        Map<String, String> annotations = deployment.getMetadata().getAnnotations();
        // descriptionBuilder.append("\tAnnotations:\n");
        if (annotations != null && !annotations.isEmpty()) {
            for (Map.Entry<String, String> entry : annotations.entrySet()) {
                String annotationKey = entry.getKey();
                String annotationValue = entry.getValue();
                descriptionBuilder.append("\t\t").append(annotationKey).append(": ").append(annotationValue).append("\n");
            }
            // Check if the lastAppliedConfig annotation exists
            String lastAppliedConfig = annotations.get("kubectl.kubernetes.io/last-applied-configuration");
            if (lastAppliedConfig != null && !lastAppliedConfig.isEmpty()) {
                descriptionBuilder.append("\tLast Applied Configuration:\n");
                // Parse the JSON string to a readable format with line breaks
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode jsonNode = (ObjectNode) mapper.readTree(lastAppliedConfig);
                    descriptionBuilder.append("\t\t").append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode)).append("\n");
                } catch (IOException e) {
                    descriptionBuilder.append("\t\tFailed to parse last applied configuration JSON\n");
                }
            } else {
                descriptionBuilder.append("\t\tNo last applied configuration\n");
            }
        } else {
            descriptionBuilder.append("\t\tNo annotations\n");
        }

        
        descriptionBuilder.append("\tCreation Timestamp: ").append(deployment.getMetadata().getCreationTimestamp()).append("\n");
        descriptionBuilder.append("\tOwner References: ").append(deployment.getMetadata().getOwnerReferences()).append("\n");

        // Replicas
        descriptionBuilder.append("\tReplicas: ").append(deployment.getSpec().getReplicas()).append("\n");

        // Selector
        descriptionBuilder.append("Selector: ").append(deployment.getSpec().getSelector().getMatchLabels()).append("\n");

        // Pod Template
        V1PodTemplateSpec podTemplate = deployment.getSpec().getTemplate();
        descriptionBuilder.append("Pod Template:\n");
        descriptionBuilder.append("\tLabels: ").append(podTemplate.getMetadata().getLabels()).append("\n");

        // Containers
        List<V1Container> containers = podTemplate.getSpec().getContainers();
        descriptionBuilder.append("\tContainers:\n");
        for (V1Container container : containers) {
            descriptionBuilder.append("\t\tName: ").append(container.getName()).append("\n");
            descriptionBuilder.append("\t\tImage: ").append(container.getImage()).append("\n");
            // Include more container details as needed
        }

        // Owner References
        List<V1OwnerReference> ownerReferences = deployment.getMetadata().getOwnerReferences();
        if (ownerReferences != null && !ownerReferences.isEmpty()) {
            descriptionBuilder.append("Controlled By: ");
            for (V1OwnerReference ownerReference : ownerReferences) {
                descriptionBuilder.append(ownerReference.getKind()).append("/").append(ownerReference.getName());
                if (!ownerReference.equals(ownerReferences.get(ownerReferences.size() - 1))) {
                    descriptionBuilder.append(", ");
                }
            }
        }


        return descriptionBuilder.toString();
    }

    private String getConfigMapDescription(ApiClient client, String configMapName) throws ApiException, IOException {
        CoreV1Api api = new CoreV1Api(client);
       
        V1ConfigMapList configMapList = api.listConfigMapForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        String namespace = null;
        for (V1ConfigMap configMap : configMapList.getItems()) {
            if (configMap.getMetadata().getName().equals(configMapName)) {
                namespace = configMap.getMetadata().getNamespace();
                break;
            }
        }
    
        if (namespace == null) {
            throw new ApiException("Namespace not found for config map: " + configMapName);
        }
        V1ConfigMap configMap = api.readNamespacedConfigMap(configMapName, namespace, null);
        
        return generateConfigMapDescription(configMap);
    }
    
    private String generateConfigMapDescription(V1ConfigMap configMap) {
        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("Name: ").append(configMap.getMetadata().getName()).append("\n");
        descriptionBuilder.append("Namespace: ").append(configMap.getMetadata().getNamespace()).append("\n");
        descriptionBuilder.append("Resource Version: ").append(configMap.getMetadata().getResourceVersion()).append("\n");
        descriptionBuilder.append("Labels: ").append(configMap.getMetadata().getLabels()).append("\n");
        descriptionBuilder.append("Creation Timestamp: ").append(configMap.getMetadata().getCreationTimestamp()).append("\n");
        descriptionBuilder.append("Annotations: ").append(configMap.getMetadata().getAnnotations()).append("\n");

          // Data
        Map<String, String> data = configMap.getData();
        if (!data.isEmpty()) {
            descriptionBuilder.append("Data Items: ").append(data.size()).append("\n");
            for (Map.Entry<String, String> entry : data.entrySet()) {
                descriptionBuilder.append("\tKey: ").append(entry.getKey()).append("\n");
                descriptionBuilder.append("\tValue: ").append(entry.getValue()).append("\n");
            }
        } else {
            descriptionBuilder.append("No Data Items").append("\n");
        }

        

            return descriptionBuilder.toString();
        }

    private String getNodeDescription(ApiClient client, String nodeName) throws ApiException, IOException {
        CoreV1Api api = new CoreV1Api(client);
        V1Node node = api.readNode(nodeName, null);
        return generateNodeDescription(node);
    }

    private String generateNodeDescription(V1Node node) {
        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("Name: ").append(node.getMetadata().getName()).append("\n");
        descriptionBuilder.append("Creation Timestamp: ").append(node.getMetadata().getCreationTimestamp()).append("\n");
        descriptionBuilder.append("Labels: ").append(node.getMetadata().getLabels()).append("\n");

        // Append capacity information
        descriptionBuilder.append("Capacity:\n");
        Map<String, Quantity> capacity = node.getStatus().getCapacity();
        if (capacity != null) {
            capacity.forEach((key, value) -> {
                descriptionBuilder.append("\t").append(key).append(": ").append(value).append("\n");
            });
        }
        // Append allocatable information
        descriptionBuilder.append("Allocatable:\n");
        Map<String, Quantity> allocatable = node.getStatus().getAllocatable();
        if (allocatable != null) {
            allocatable.forEach((key, value) -> {
                descriptionBuilder.append("\t").append(key).append(": ").append(value).append("\n");
            });
        }
        descriptionBuilder.append("Conditions:\n");
        List<V1NodeCondition> conditions = node.getStatus().getConditions();
        if (conditions != null) {
            conditions.forEach(condition -> {
                descriptionBuilder.append("\t").append(condition.getType()).append(": ").append(condition.getStatus()).append("\n");
            });
        }
         // Append node addresses
         descriptionBuilder.append("Addresses:\n");
         List<V1NodeAddress> addresses = node.getStatus().getAddresses();
         if (addresses != null) {
             addresses.forEach(address -> {
                 descriptionBuilder.append("\t").append(address.getType()).append(": ").append(address.getAddress()).append("\n");
             });
         }
        return descriptionBuilder.toString();
    }

    private String getStatefulSetDescription(ApiClient client, String statefulSetName) throws ApiException, IOException {
        AppsV1Api api = new AppsV1Api(client);
        V1StatefulSetList statefulSetList = api.listStatefulSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        String namespace = null;
        for (V1StatefulSet statefulSet : statefulSetList.getItems()) {
            if (statefulSet.getMetadata().getName().equals(statefulSetName)) {
                namespace = statefulSet.getMetadata().getNamespace();
                break;
            }
        }
    
        if (namespace == null) {
            throw new ApiException("Namespace not found for pod: " + statefulSetName);
        }
        V1StatefulSet statefulSet = api.readNamespacedStatefulSet(statefulSetName, "default", null);
        return generateStatefulSetDescription(statefulSet);
    }

    private String generateStatefulSetDescription(V1StatefulSet statefulSet) {
        StringBuilder descriptionBuilder = new StringBuilder();
        // Append basic information about the StatefulSet
    descriptionBuilder.append("Name: ").append(statefulSet.getMetadata().getName()).append("\n");
    descriptionBuilder.append("Namespace: ").append(statefulSet.getMetadata().getNamespace()).append("\n");
    descriptionBuilder.append("Creation Timestamp: ").append(statefulSet.getMetadata().getCreationTimestamp()).append("\n");
    descriptionBuilder.append("Labels: ").append(statefulSet.getMetadata().getLabels()).append("\n");

    // Append StatefulSet spec details
    descriptionBuilder.append("Replicas: ").append(statefulSet.getSpec().getReplicas()).append("\n");
    descriptionBuilder.append("Selector: ").append(statefulSet.getSpec().getSelector()).append("\n");
    descriptionBuilder.append("Service Name: ").append(statefulSet.getSpec().getServiceName()).append("\n");
    descriptionBuilder.append("Pod Management Policy: ").append(statefulSet.getSpec().getPodManagementPolicy()).append("\n");

    // Append information about pods controlled by the StatefulSet
    descriptionBuilder.append("Pods:\n");
    List<String> podNames = getPodNames(statefulSet);
    for (String podName : podNames) {
        descriptionBuilder.append("\t").append(podName).append("\n");
        // Include more pod details if needed
    }
    // Append information about volumes used by the StatefulSet
    descriptionBuilder.append("Volumes:\n");
    List<V1PersistentVolumeClaim> volumeClaimTemplates = statefulSet.getSpec().getVolumeClaimTemplates();
    if (volumeClaimTemplates != null && !volumeClaimTemplates.isEmpty()) {
        for (V1PersistentVolumeClaim claimTemplate : volumeClaimTemplates) {
            descriptionBuilder.append("\tClaim Template Name: ").append(claimTemplate.getMetadata().getName()).append("\n");
            descriptionBuilder.append("\tClaim Template Labels: ").append(claimTemplate.getMetadata().getLabels()).append("\n");
            descriptionBuilder.append("\tClaim Template Annotations: ").append(claimTemplate.getMetadata().getAnnotations()).append("\n");
            descriptionBuilder.append("\tClaim Template Storage Class: ").append(claimTemplate.getSpec().getStorageClassName()).append("\n");
            descriptionBuilder.append("\tClaim Template Access Modes: ").append(claimTemplate.getSpec().getAccessModes()).append("\n");
            descriptionBuilder.append("\tClaim Template Resources: ").append(claimTemplate.getSpec().getResources()).append("\n");
            // Include more details as needed
        }
    } else {
        descriptionBuilder.append("\tNo Volume Claim Templates\n");
    }

        return descriptionBuilder.toString();
    }

    private List<String> getPodNames(V1StatefulSet statefulSet) {
        List<String> podNames = new ArrayList<>();
        
        // Retrieve pod names controlled by the StatefulSet
        // This example assumes that you are retrieving pod names from the replicas field in the StatefulSet status.
        Integer replicas = statefulSet.getStatus().getReplicas();
        if (replicas != null) {
            // Iterate over the replicas to construct pod names based on the naming convention
            for (int i = 0; i < replicas; i++) {
                // Construct the pod name using the StatefulSet's naming convention
                String podName = statefulSet.getMetadata().getName() + "-" + i;
                podNames.add(podName);
            }
        }
        
        // Alternatively, you can extract pod names directly from the pods field in the StatefulSet status.
        // List<V1Pod> pods = statefulSet.getStatus().getPods();
        // Iterate over pods and extract pod names as needed.
    
        return podNames;
    }
    
    // private String getIngressDescription(ApiClient client, String resourceName) throws ApiException {
    //     // Create an instance of the ExtensionsV1beta1Api
    //     ExtensionsV1beta1Api api = new ExtensionsV1beta1Api(client);

    //     // Retrieve the Ingress object by name
    //     V1Ingress ingress = api.readNamespacedIngress(resourceName, "default", null, null, null);
        
    //     // Create a StringBuilder to construct the description
    //     StringBuilder descriptionBuilder = new StringBuilder();
    
    //     // Append details about the Ingress
    //     descriptionBuilder.append("Name: ").append(ingress.getMetadata().getName()).append("\n");
    //     descriptionBuilder.append("Namespace: ").append(ingress.getMetadata().getNamespace()).append("\n");
    //     descriptionBuilder.append("Labels: ").append(ingress.getMetadata().getLabels()).append("\n");
    //     descriptionBuilder.append("Annotations: ").append(ingress.getMetadata().getAnnotations()).append("\n");
    //     // Append more information as needed
    
    //     return descriptionBuilder.toString();
    // }
    
    private String getPersistentVolumeDescription(ApiClient client, String persistentvolumeName) throws ApiException {
        // Create an instance of the CoreV1Api
        CoreV1Api api = new CoreV1Api(client);
        // V1PersistentVolumeList persistentVolumeList = api.listPersistentVolume(null, null, null, null, null, null, null, null, null, null);
        // String namespace = null;
        // for (V1PersistentVolume persistentVolume : persistentVolumeList.getItems()) {
        //     if (persistentVolume.getMetadata().getName().equals(persistentvolumeName)) {
        //         namespace = persistentVolume.getMetadata().getNamespace();
        //         break;
        //     }
        // }
    
        // if (namespace == null) {
        //     throw new ApiException("Namespace not found for pod: " + persistentvolumeName);
        // }
        V1PersistentVolume persistentVolume = api.readPersistentVolume(persistentvolumeName, null);
            return generatePersistentVolumeDescription(persistentVolume);
    }
    private String generatePersistentVolumeDescription(V1PersistentVolume persistentVolume){
        StringBuilder descriptionBuilder = new StringBuilder();

        // Append details about the PersistentVolume
        descriptionBuilder.append("Name: ").append(persistentVolume.getMetadata().getName()).append("\n");
        descriptionBuilder.append("Labels: ").append(persistentVolume.getMetadata().getLabels()).append("\n");
        descriptionBuilder.append("Capacity: ").append(persistentVolume.getSpec().getCapacity()).append("\n");
        // Append more information as needed
    
        return descriptionBuilder.toString();
    }
    
    private String getPersistentVolumeClaimDescription(ApiClient client, String PVCName) throws ApiException {
        // Create an instance of the CoreV1Api
        CoreV1Api api = new CoreV1Api(client);
    
        // Retrieve the PersistentVolumeClaim object by name
        V1PersistentVolumeClaim persistentVolumeClaim = api.readNamespacedPersistentVolumeClaim(PVCName, "default", null);
        return generatePersistentVolumeClaimDescription(persistentVolumeClaim);
    }
    private String generatePersistentVolumeClaimDescription( V1PersistentVolumeClaim persistentVolumeClaim){
        // Create a StringBuilder to construct the description
        StringBuilder descriptionBuilder = new StringBuilder();
    
        // Append details about the PersistentVolumeClaim
        descriptionBuilder.append("Name: ").append(persistentVolumeClaim.getMetadata().getName()).append("\n");
        descriptionBuilder.append("Namespace: ").append(persistentVolumeClaim.getMetadata().getNamespace()).append("\n");
        descriptionBuilder.append("Labels: ").append(persistentVolumeClaim.getMetadata().getLabels()).append("\n");
        descriptionBuilder.append("Annotations: ").append(persistentVolumeClaim.getMetadata().getAnnotations()).append("\n");
        // Append more information as needed
    
        return descriptionBuilder.toString();
    }
    
    private String getJobDescription(ApiClient client, String jobName) throws ApiException {
        // Create an instance of the BatchV1Api
        BatchV1Api api = new BatchV1Api(client);
        V1JobList jobList = api.listJobForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        String namespace = null;
        for (V1Job job : jobList.getItems()) {
            if (job.getMetadata().getName().equals(jobName)) {
                namespace = job.getMetadata().getNamespace();
                break;
            }
        }
    
        if (namespace == null) {
            throw new ApiException("Namespace not found for pod: " + jobName);
        }
        V1Job job = api.readNamespacedJob(jobName, namespace, null);
        return generateJobDescription(job);

        } 
       private String generateJobDescription(V1Job job){

        // Create a StringBuilder to construct the description
        StringBuilder descriptionBuilder = new StringBuilder();
    
        // Append details about the Job
        descriptionBuilder.append("Name: ").append(job.getMetadata().getName()).append("\n");
        descriptionBuilder.append("Namespace: ").append(job.getMetadata().getNamespace()).append("\n");
        descriptionBuilder.append("Labels: ").append(job.getMetadata().getLabels()).append("\n");
        descriptionBuilder.append("Annotations: ").append(job.getMetadata().getAnnotations()).append("\n");
        // Append more information as needed
    
        return descriptionBuilder.toString();
    }
    
    private String getNamespaceDescription(ApiClient client, String resourceName) throws ApiException {
        // Create an instance of the CoreV1Api
        CoreV1Api api = new CoreV1Api(client);
    
        // Retrieve the Namespace object by name
        V1Namespace namespace = api.readNamespace(resourceName, null);
        return generateNamespaceDescription(namespace);
    }
    private String generateNamespaceDescription( V1Namespace namespace ){
        // Create a StringBuilder to construct the description
        StringBuilder descriptionBuilder = new StringBuilder();
    
        // Append details about the Namespace
        descriptionBuilder.append("Name: ").append(namespace.getMetadata().getName()).append("\n");
        descriptionBuilder.append("Labels: ").append(namespace.getMetadata().getLabels()).append("\n");
        descriptionBuilder.append("Annotations: ").append(namespace.getMetadata().getAnnotations()).append("\n");
        // Append more information as needed
    
        return descriptionBuilder.toString();
    }
    
    private String getDaemonSetDescription(ApiClient client, String resourceName) throws ApiException {
        AppsV1Api api = new AppsV1Api(client);
        V1DaemonSetList daemonSetList = api.listDaemonSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        String namespace = null;
        for (V1DaemonSet deamon : daemonSetList.getItems()) {
            if (deamon.getMetadata().getName().equals(resourceName)) {
                namespace = deamon.getMetadata().getNamespace();
                break;
            }
        }
    
        if (namespace == null) {
            throw new ApiException("Namespace not found for pod: " + resourceName);
        }
        V1DaemonSet daemonSet = api.readNamespacedDaemonSet(resourceName, namespace, null);
        return generateDaemonSetDescription(daemonSet);
    }

        private String generateDaemonSetDescription(V1DaemonSet daemonSet){
        // Create a StringBuilder to construct the description
        StringBuilder descriptionBuilder = new StringBuilder();
    
        // Append details about the DaemonSet
        descriptionBuilder.append("Name: ").append(daemonSet.getMetadata().getName()).append("\n");
        descriptionBuilder.append("Namespace: ").append(daemonSet.getMetadata().getNamespace()).append("\n");
        descriptionBuilder.append("Labels: ").append(daemonSet.getMetadata().getLabels()).append("\n");
        descriptionBuilder.append("Annotations: ").append(daemonSet.getMetadata().getAnnotations()).append("\n");
        // Append more information as needed
    
        return descriptionBuilder.toString();
    }
    private String getEndpointDescription(ApiClient client, String resourceName) throws ApiException, IOException {
        // Create an instance of the CoreV1Api
        CoreV1Api api = new CoreV1Api(client);
        V1EndpointsList endpointsList = api.listEndpointsForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        String namespace = null;
        for (V1Endpoints end : endpointsList.getItems()) {
            if (end.getMetadata().getName().equals(resourceName)) {
                namespace = end.getMetadata().getNamespace();
                break;
            }
        }
    
        if (namespace == null) {
            throw new ApiException("Namespace not found for pod: " + resourceName);
        }
        V1Endpoints endpoints = api.readNamespacedEndpoints(resourceName, namespace, null);
    
        return generateEndpointDescription(endpoints);
    }
    
    private String generateEndpointDescription(V1Endpoints endpoints) {
        StringBuilder descriptionBuilder = new StringBuilder();
    
        descriptionBuilder.append("Name: ").append(endpoints.getMetadata().getName()).append("\n");
        descriptionBuilder.append("Namespace: ").append(endpoints.getMetadata().getNamespace()).append("\n");
        descriptionBuilder.append("Labels: ").append(endpoints.getMetadata().getLabels()).append("\n");
        descriptionBuilder.append("Annotations: ").append(endpoints.getMetadata().getAnnotations()).append("\n");
        descriptionBuilder.append("Creation Timestamp: ").append(endpoints.getMetadata().getCreationTimestamp()).append("\n");
        descriptionBuilder.append("Owner References: ").append(endpoints.getMetadata().getOwnerReferences()).append("\n");
    
        // Include more details as needed
    
        return descriptionBuilder.toString();
    }
    private String getReplicaSetDescription(ApiClient client, String resourceName) throws ApiException, IOException {
        // Create an instance of the AppsV1Api
        AppsV1Api api = new AppsV1Api(client);
        V1ReplicaSetList replicaSetList = api.listReplicaSetForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        String namespace = null;
        for (V1ReplicaSet replica : replicaSetList.getItems()) {
            if (replica.getMetadata().getName().equals(resourceName)) {
                namespace = replica.getMetadata().getNamespace();
                break;
            }
        }
    
        if (namespace == null) {
            throw new ApiException("Namespace not found for pod: " + resourceName);
        }
        V1ReplicaSet replicaset = api.readNamespacedReplicaSet(resourceName ,namespace, null);
        
        return generateReplicaSetDescription(replicaset);
    }
    
    private String generateReplicaSetDescription(V1ReplicaSet replicaset) {
        StringBuilder descriptionBuilder = new StringBuilder();
    
        descriptionBuilder.append("Name: ").append(replicaset.getMetadata().getName()).append("\n");
        descriptionBuilder.append("Namespace: ").append(replicaset.getMetadata().getNamespace()).append("\n");
        descriptionBuilder.append("Labels: ").append(replicaset.getMetadata().getLabels()).append("\n");
        descriptionBuilder.append("Annotations: ").append(replicaset.getMetadata().getAnnotations()).append("\n");
        descriptionBuilder.append("Creation Timestamp: ").append(replicaset.getMetadata().getCreationTimestamp()).append("\n");
        descriptionBuilder.append("Owner References: ").append(replicaset.getMetadata().getOwnerReferences()).append("\n");
        
        // Include more details as needed
    
        return descriptionBuilder.toString();
    }
    
     
    private String getIngressDescription(ApiClient client, String resourceName) throws ApiException, IOException {
        // Create an instance of the NetworkingV1Api
        NetworkingV1Api api = new NetworkingV1Api(client);
    
        // Retrieve the Ingress object by name
        V1Ingress ingress = api.readNamespacedIngress(resourceName, "default", null);
    
        return generateIngressDescription(ingress);
    }
    
    
    private String generateIngressDescription(V1Ingress ingress) {
        StringBuilder descriptionBuilder = new StringBuilder();
    
        descriptionBuilder.append("Name: ").append(ingress.getMetadata().getName()).append("\n");
        descriptionBuilder.append("Namespace: ").append(ingress.getMetadata().getNamespace()).append("\n");
        descriptionBuilder.append("Labels: ").append(ingress.getMetadata().getLabels()).append("\n");
        descriptionBuilder.append("Annotations: ").append(ingress.getMetadata().getAnnotations()).append("\n");
        descriptionBuilder.append("Creation Timestamp: ").append(ingress.getMetadata().getCreationTimestamp()).append("\n");
        descriptionBuilder.append("Owner References: ").append(ingress.getMetadata().getOwnerReferences()).append("\n");
    
        // Include more details as needed
    
        return descriptionBuilder.toString();
    }
    
    private String getStorageClassDescription(ApiClient client, String resourceName) throws ApiException, IOException {
        // Create an instance of the StorageV1Api
        StorageV1Api api = new StorageV1Api(client);
    
        // Retrieve the StorageClass object by name
        V1StorageClass storageClass = api.readStorageClass(resourceName, null);
    
        return generateStorageClassDescription(storageClass);
    }
    
    private String generateStorageClassDescription(V1StorageClass storageClass) {
        StringBuilder descriptionBuilder = new StringBuilder();
    
        descriptionBuilder.append("Name: ").append(storageClass.getMetadata().getName()).append("\n");
        descriptionBuilder.append("Provisioner: ").append(storageClass.getProvisioner()).append("\n");
        descriptionBuilder.append("Reclaim Policy: ").append(storageClass.getReclaimPolicy()).append("\n");
        descriptionBuilder.append("Mount Options: ").append(storageClass.getMountOptions()).append("\n");
        descriptionBuilder.append("Allowed Topologies: ").append(storageClass.getAllowedTopologies()).append("\n");
        descriptionBuilder.append("Volume Binding Mode: ").append(storageClass.getVolumeBindingMode()).append("\n");
        // Include more details as needed
    
        return descriptionBuilder.toString();
    }
    
}
    // @Autowired
    // private KubernetesConfigService KubernetesConfigService;

   
    // @Override
    // public String getPodDescriptions(String podName) throws ApiException, IOException {
    //     // Créer un client pour interagir avec l'API Kubernetes
    //     ApiClient client = KubernetesConfigService.configureKubernetesAccess();
    
    //     // Créer une instance de l'API CoreV1Api
    //     CoreV1Api api = new CoreV1Api(client);
    
    //     // Get the namespace of the pod by name
    //     V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
    //     String namespace = null;
    //     for (V1Pod pod : podList.getItems()) {
    //         if (pod.getMetadata().getName().equals(podName)) {
    //             namespace = pod.getMetadata().getNamespace();
    //             break;
    //         }
    //     }
    
    //     if (namespace == null) {
    //         throw new ApiException("Namespace not found for pod: " + podName);
    //     }
    
    //     // Retrieve the pod by name and namespace
    //     V1Pod pod = api.readNamespacedPod(podName, namespace, null);
    
    //     // Liste pour stocker les descriptions des pods
    //     return generatePodDescription(pod);
    // }

    // private String generatePodDescription(V1Pod pod) {
    //     // Générer la description du pod en fonction de ses attributs
    //     // Vous pouvez personnaliser cette méthode pour inclure plus d'informations si
    //     // nécessaire
    //     StringBuilder descriptionBuilder = new StringBuilder();

        // descriptionBuilder.append("Name: ").append(pod.getMetadata().getName()).append("\n");
        // descriptionBuilder.append("Namespace: ").append(pod.getMetadata().getNamespace()).append("\n");
        // descriptionBuilder.append("Labels: ").append(pod.getMetadata().getLabels()).append("\n");
        // descriptionBuilder.append("Status: ").append(pod.getStatus().getPhase()).append("\n");
        // descriptionBuilder.append("Annotations: ").append(pod.getMetadata().getAnnotations()).append("\n");
        // descriptionBuilder.append("Creation Timestamp: ").append(pod.getMetadata().getCreationTimestamp()).append("\n");
        // descriptionBuilder.append("Owner References: ").append(pod.getMetadata().getOwnerReferences()).append("\n");

        // descriptionBuilder.append("Node Name: ").append(pod.getSpec().getNodeName()).append("\n");
        // descriptionBuilder.append("Service Account Name: ").append(pod.getSpec().getServiceAccountName()).append("\n");
        // descriptionBuilder.append("Host Network: ").append(pod.getSpec().getHostNetwork()).append("\n");
        // descriptionBuilder.append("Host PID: ").append(pod.getSpec().getHostPID()).append("\n");
        // descriptionBuilder.append("Host IPC: ").append(pod.getSpec().getHostIPC()).append("\n");
        // descriptionBuilder.append("Containers: \n");

        // // Ajouter plus d'informations au besoin
        // descriptionBuilder.append("Pod IP: ").append(pod.getStatus().getPodIP()).append("\n");

        // // Include container details
        //     // Containers
        // List<V1Container> containers = pod.getSpec().getContainers();
        // for (V1Container container : containers) {
        //     descriptionBuilder.append("\tName: ").append(container.getName()).append("\n");
        //     descriptionBuilder.append("\tImage: ").append(container.getImage()).append("\n");
        //     descriptionBuilder.append("\tCommand: ").append(container.getCommand()).append("\n");
        //     descriptionBuilder.append("\tArgs: ").append(container.getArgs()).append("\n");
        //     descriptionBuilder.append("\tWorking Directory: ").append(container.getWorkingDir()).append("\n");
        //     descriptionBuilder.append("\tPorts: ").append(container.getPorts()).append("\n");
        //     descriptionBuilder.append("\tVolume Mounts: ").append(container.getVolumeMounts()).append("\n");
        //     descriptionBuilder.append("\tResources: ").append(container.getResources()).append("\n");
        //     descriptionBuilder.append("\tEnvironment Variables: ").append(container.getEnv()).append("\n");
        //     descriptionBuilder.append("\n");
        // }
        // descriptionBuilder.append("Status: ").append(pod.getStatus().getPhase()).append("\n");
        // descriptionBuilder.append("Pod IP: ").append(pod.getStatus().getPodIP()).append("\n");
        // descriptionBuilder.append("Pod Host IP: ").append(pod.getStatus().getHostIP()).append("\n");
        // descriptionBuilder.append("Conditions: ").append(pod.getStatus().getConditions()).append("\n");
        // descriptionBuilder.append("Message: ").append(pod.getStatus().getMessage()).append("\n");
        // descriptionBuilder.append("Reason: ").append(pod.getStatus().getReason()).append("\n");
        // descriptionBuilder.append("StartTime: ").append(pod.getStatus().getStartTime()).append("\n");
        // descriptionBuilder.append("Deletion Timestamp: ").append(pod.getMetadata().getDeletionTimestamp()).append("\n");
        
        // if (pod.getMetadata().getOwnerReferences() != null && !pod.getMetadata().getOwnerReferences().isEmpty()) {
        //     descriptionBuilder.append("Controlled By: ");
        //     // Iterate through ownerReferences
        //     for (V1OwnerReference ownerReference : pod.getMetadata().getOwnerReferences()) {
        //         descriptionBuilder.append(ownerReference.getKind()).append("/").append(ownerReference.getName());
        //         // If there are multiple ownerReferences, separate them with commas
        //         if (!ownerReference.equals(pod.getMetadata().getOwnerReferences().get(pod.getMetadata().getOwnerReferences().size() - 1))) {
        //             descriptionBuilder.append(", ");
        //         }
        //     }
        // }
    //     return descriptionBuilder.toString();
    // }

   

    // @Override
    // public String getServiceDescriptions(String serviceName) throws ApiException, IOException {
    //     // Créer un client pour interagir avec l'API Kubernetes
    //     ApiClient client = KubernetesConfigService.configureKubernetesAccess();
        
    //     // Créer une instance de l'API CoreV1Api
    //     CoreV1Api api = new CoreV1Api(client);
        
    //     // Récupérer le service par son nom
    //     V1Service service = api.readNamespacedService(serviceName, "default", null);
        
    //     // Générer la description du service
    //     return generateServiceDescription(service);
    // }

    // private String generateServiceDescription(V1Service service) {
    //     // Générer la description du service en fonction de ses attributs
    //     StringBuilder descriptionBuilder = new StringBuilder();

    //     descriptionBuilder.append("Name: ").append(service.getMetadata().getName()).append("\n");
    //     descriptionBuilder.append("Namespace: ").append(service.getMetadata().getNamespace()).append("\n");
    //     descriptionBuilder.append("Labels: ").append(service.getMetadata().getLabels()).append("\n");
    //     descriptionBuilder.append("Creation Timestamp: ").append(service.getMetadata().getCreationTimestamp()).append("\n");
    //     descriptionBuilder.append("Annotations: ").append(service.getMetadata().getAnnotations()).append("\n");
        
    //     // Inclure plus d'informations au besoin
        
    //     return descriptionBuilder.toString();
    // }

    

