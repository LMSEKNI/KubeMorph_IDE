package com.example.service.createressource.yml;

import java.io.FileReader;
import java.io.IOException;

import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.*;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Yaml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.KubernetesConfig.KubernetesConfigService;

@Service
public class CreateServiceImpl implements CreateService {

    private final KubernetesConfigService kubernetesConfigService;

    @Autowired
    public CreateServiceImpl(KubernetesConfigService kubernetesConfigService) {
        this.kubernetesConfigService = kubernetesConfigService;
    }
    @Override
    public void createHPAFromYaml(String yamlContent) throws IOException, ApiException {
        kubernetesConfigService.configureKubernetesAccess();
        AutoscalingV1Api api = new AutoscalingV1Api();

        V1HorizontalPodAutoscaler yamlHPA = (V1HorizontalPodAutoscaler) Yaml.load(yamlContent);
        if (yamlHPA.getMetadata() != null && yamlHPA.getMetadata().getNamespace() != null) {
            String namespace = yamlHPA.getMetadata().getNamespace();
            api.createNamespacedHorizontalPodAutoscaler(namespace, yamlHPA, null,null, null, null);
            System.out.println("The HPA is created successfully!");
        } else {
            System.out.println("Namespace not found in YAML content");
        }
    }
    //Pod Creation
    
    @Override
    public void createPodFromYaml(String yamlContent) throws IOException, ApiException {
        kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api();
        V1Pod yamlPod = (V1Pod) Yaml.load(yamlContent);
        if (yamlPod.getMetadata() != null && yamlPod.getMetadata().getNamespace() != null ) {
            String namespace = yamlPod.getMetadata().getNamespace();
            api.createNamespacedPod(namespace, yamlPod, null, null, null, null);
            System.out.println("THE Pod is created successufully !");
        } else {
            System.out.println("Namespace not found in YAML content");
        }
    }


      //Service Creation
      @Override
      public void createServiceFromYaml(String yamlContent) throws IOException, ApiException {
        kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api();
        V1Service yamlSvc = (V1Service) Yaml.load(yamlContent);
        if (yamlSvc.getMetadata() != null && yamlSvc.getMetadata().getNamespace() != null ) {
            String namespace = yamlSvc.getMetadata().getNamespace();
            api.createNamespacedService(namespace, yamlSvc, null, null, null, null);
            System.out.println("THE Service is created successufully !");
        } else {
            System.out.println("Namespace not found in YAML content");
            api.createNamespacedService("default", yamlSvc, null, null, null, null);
            System.out.println("Service created in default namespace");
        }

      }

      //deployment creation
      @Override
      public void createDeploymentFromYaml(String yamlContent) throws IOException, ApiException {
          kubernetesConfigService.configureKubernetesAccess();
          AppsV1Api api = new AppsV1Api();
          V1Deployment yamlDeployment = (V1Deployment) Yaml.load(yamlContent);
          String namespace = yamlDeployment.getMetadata() != null ? yamlDeployment.getMetadata().getNamespace() : "default";
          if (namespace != null) {
              api.createNamespacedDeployment(namespace, yamlDeployment, null, null, null, namespace);
              System.out.println("The Deployment is created successfully!");
          } else {
              System.out.println("Namespace not found in YAML content. Defaulting to 'default' namespace.");
              api.createNamespacedDeployment("default", yamlDeployment, null, null, null, namespace);
              System.out.println("Deployment created in default namespace");
          }
      }

    //configmap creation
    @Override
    public void createConfigMapFromYaml(String yamlContent) throws IOException, ApiException {
        kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api();
        V1ConfigMap yamlConfigMap = (V1ConfigMap) Yaml.load(yamlContent);
        String namespace = yamlConfigMap.getMetadata() != null ? yamlConfigMap.getMetadata().getNamespace() : "default";
        if (namespace != null) {
            api.createNamespacedConfigMap(namespace, yamlConfigMap, null, null, null, namespace);
            System.out.println("The ConfigMap is created successfully!");
        } else {
            System.out.println("Namespace not found in YAML content. Defaulting to 'default' namespace.");
            api.createNamespacedConfigMap("default", yamlConfigMap, null, null, null, namespace);
            System.out.println("ConfigMap created in default namespace");
        }
    }
    
    @Override
    public void createStatefulSetFromYaml(String yamlContent) throws IOException, ApiException {
        kubernetesConfigService.configureKubernetesAccess();
        AppsV1Api api = new AppsV1Api();
        V1StatefulSet yamlStatefulSet = (V1StatefulSet) Yaml.load(yamlContent);
        String namespace = yamlStatefulSet.getMetadata() != null ? yamlStatefulSet.getMetadata().getNamespace() : "default";
        if (namespace != null) {
            api.createNamespacedStatefulSet(namespace, yamlStatefulSet, null, null, null, namespace);
            System.out.println("The StatefulSet is created successfully!");
        } else {
            System.out.println("Namespace not found in YAML content. Defaulting to 'default' namespace.");
            api.createNamespacedStatefulSet("default", yamlStatefulSet, null, null, null, namespace);
            System.out.println("StatefulSet created in default namespace");
        }
    }
    @Override
    public void createReplicaSetFromYaml(String yamlContent) throws IOException, ApiException {
        kubernetesConfigService.configureKubernetesAccess();
        AppsV1Api api = new AppsV1Api();
        V1ReplicaSet yamlReplicaSet = (V1ReplicaSet) Yaml.load(yamlContent);
        String namespace = yamlReplicaSet.getMetadata() != null ? yamlReplicaSet.getMetadata().getNamespace() : "";
        
        if (namespace == null || namespace.isEmpty()) {
            throw new IllegalArgumentException("Namespace is required to create a ReplicaSet");
        }
        try {
            api.createNamespacedReplicaSet(namespace, yamlReplicaSet, null, null, null, "");
            System.out.println("The ReplicaSet is created successfully in namespace: " + namespace);
        } catch (ApiException e) {
            System.err.println("Failed to create ReplicaSet: " + e.getResponseBody());
        }
    }
    @Override
    public void createNodeFromYaml(String yamlContent) throws IOException, ApiException {
        kubernetesConfigService.configureKubernetesAccess();
        CoreV1Api api = new CoreV1Api();
        V1Node yamlNode = (V1Node) Yaml.load(yamlContent);
        String namespace = yamlNode.getMetadata() != null ? yamlNode.getMetadata().getNamespace() : "";
        
        
        try {
            api.createNode(yamlNode, namespace, null, null, null);
            System.out.println("The node is created successfully in namespace: " + namespace);
        } catch (ApiException e) {
            System.err.println("Failed to create node: " + e.getResponseBody());
        }   
    }
    
    @Override
    public void createJobFromYaml(String yamlContent) throws IOException, ApiException {
        kubernetesConfigService.configureKubernetesAccess();
        BatchV1Api api = new BatchV1Api();
        V1Job yamlJob = (V1Job) Yaml.load(yamlContent);

        // Vérifier si le champ "namespace" est présent dans le fichier YAML
        String namespace = extractNamespaceFromYamlJob(yamlJob);

        try {
            // Créer le Job
            api.createNamespacedJob(namespace, yamlJob, null, null, null, null);
            System.out.println("Le Job est créé avec succès dans l'espace de noms : " + namespace);
        } catch (ApiException e) {
            System.err.println("Échec de la création du Job : " + e.getResponseBody());
            e.printStackTrace(); // Ajouter cette ligne pour afficher la trace de la pile
        }
    }

    private String extractNamespaceFromYamlJob(V1Job yamlJob) {
        if (yamlJob.getMetadata() != null) {
            return yamlJob.getMetadata().getNamespace() != null ? yamlJob.getMetadata().getNamespace() : "default";
        }
        return "default";
    }
    @Override
public void createIngressFromYaml(String yamlContent) throws IOException, ApiException {
    ApiClient client = kubernetesConfigService.configureKubernetesAccess();
    NetworkingV1Api api = new NetworkingV1Api(client);
    V1Ingress yamlIngress = (V1Ingress) Yaml.load(yamlContent);

    // Extract namespace from the YAML Ingress or set it to default
    String namespace = extractNamespaceFromYamlIngress(yamlIngress);

    try {
        // Create the Ingress
        api.createNamespacedIngress(namespace, yamlIngress, null, null, null, null);
        System.out.println("The Ingress is created successfully in namespace: " + namespace);
    } catch (ApiException e) {
        System.err.println("Failed to create Ingress: " + e.getResponseBody());
        e.printStackTrace();
    }
}

private String extractNamespaceFromYamlIngress(V1Ingress yamlIngress) {
    if (yamlIngress.getMetadata() != null && yamlIngress.getMetadata().getNamespace() != null) {
        return yamlIngress.getMetadata().getNamespace();
    }
    return "default";
}

@Override
public void createEndpointFromYaml(String yamlContent) throws IOException, ApiException {
    ApiClient client = kubernetesConfigService.configureKubernetesAccess();
    CoreV1Api api = new CoreV1Api(client);
    V1Endpoints yamlEndpoint = (V1Endpoints) Yaml.load(yamlContent);

    // Extract namespace from the YAML Endpoint or set it to default
    String namespace = extractNamespaceFromYamlEndpoint(yamlEndpoint);

    try {
        // Create the Endpoint
        api.createNamespacedEndpoints(namespace, yamlEndpoint, null, null, null, null);
        System.out.println("The Endpoint is created successfully in namespace: " + namespace);
    } catch (ApiException e) {
        System.err.println("Failed to create Endpoint: " + e.getResponseBody());
        e.printStackTrace();
    }
}

private String extractNamespaceFromYamlEndpoint(V1Endpoints yamlEndpoint) {
    if (yamlEndpoint.getMetadata() != null && yamlEndpoint.getMetadata().getNamespace() != null) {
        return yamlEndpoint.getMetadata().getNamespace();
    }
    return "default";
}

@Override
public void createDaemonSetFromYaml(String yamlContent) throws IOException, ApiException {
    ApiClient client = kubernetesConfigService.configureKubernetesAccess();
    AppsV1Api api = new AppsV1Api(client);
    V1DaemonSet yamlDaemonSet = (V1DaemonSet) Yaml.load(yamlContent);

    // Extract namespace from the YAML DaemonSet or set it to default
    String namespace = extractNamespaceFromYamlDaemonSet(yamlDaemonSet);

    try {
        // Create the DaemonSet
        api.createNamespacedDaemonSet(namespace, yamlDaemonSet, null, null, null, null);
        System.out.println("The DaemonSet is created successfully in namespace: " + namespace);
    } catch (ApiException e) {
        System.err.println("Failed to create DaemonSet: " + e.getResponseBody());
        e.printStackTrace();
    }
}

private String extractNamespaceFromYamlDaemonSet(V1DaemonSet yamlDaemonSet) {
    if (yamlDaemonSet.getMetadata() != null && yamlDaemonSet.getMetadata().getNamespace() != null) {
        return yamlDaemonSet.getMetadata().getNamespace();
    }
    return "default";
}

@Override
public void createPersistentVolumeFromYaml(String yamlContent) throws IOException, ApiException {
    ApiClient client = kubernetesConfigService.configureKubernetesAccess();
    CoreV1Api api = new CoreV1Api(client);
    V1PersistentVolume yamlPersistentVolume = (V1PersistentVolume) Yaml.load(yamlContent);

    String namespace = extractNamespaceFromYamlPersistentVolume(yamlPersistentVolume);

    try {
        api.createPersistentVolume(yamlPersistentVolume, null, null, null, null);
        System.out.println("The PersistentVolume is created successfully in namespace: " + namespace);
    } catch (ApiException e) {
        System.err.println("Failed to create PersistentVolume: " + e.getResponseBody());
        e.printStackTrace();
    }
}

private String extractNamespaceFromYamlPersistentVolume(V1PersistentVolume yamlPersistentVolume) {
    if (yamlPersistentVolume.getMetadata() != null && yamlPersistentVolume.getMetadata().getNamespace() != null) {
        return yamlPersistentVolume.getMetadata().getNamespace();
    }
    return "default";
}

@Override
public void createPersistentVolumeClaimFromYaml(String yamlContent) throws IOException, ApiException {
    ApiClient client = kubernetesConfigService.configureKubernetesAccess();
    CoreV1Api api = new CoreV1Api(client);
    V1PersistentVolumeClaim yamlPersistentVolumeClaim = (V1PersistentVolumeClaim) Yaml.load(yamlContent);

    String namespace = extractNamespaceFromYamlPersistentVolumeClaim(yamlPersistentVolumeClaim);

    try {
        api.createNamespacedPersistentVolumeClaim(namespace, yamlPersistentVolumeClaim, null, null, null, null);
        System.out.println("The V1PersistentVolumeClaim is created successfully in namespace: " + namespace);
    } catch (ApiException e) {
        System.err.println("Failed to create PersistentVolumeClaim: " + e.getResponseBody());
        e.printStackTrace();
    }
}

private String extractNamespaceFromYamlPersistentVolumeClaim (V1PersistentVolumeClaim yamlPersistentVolumeClaim) {
    if (yamlPersistentVolumeClaim.getMetadata() != null && yamlPersistentVolumeClaim.getMetadata().getNamespace() != null) {
        return yamlPersistentVolumeClaim.getMetadata().getNamespace();
    }
    return "default";
}

    
@Override
public void createStorageClassFromYaml(String yamlContent) throws IOException, ApiException {
    ApiClient client = kubernetesConfigService.configureKubernetesAccess();
    StorageV1Api api = new StorageV1Api(client);
    V1StorageClass yamlStorageClass = (V1StorageClass) Yaml.load(yamlContent);

    // Extract namespace from the YAML StorageClass
    String namespace = extractNamespaceFromYamlStorageClass(yamlStorageClass);

    try {
        // Create the StorageClass
        api.createStorageClass(yamlStorageClass, null, null, null, null);
        System.out.println("The StorageClass is created successfully in namespace: " + namespace);
    } catch (ApiException e) {
        System.err.println("Failed to create StorageClass: " + e.getResponseBody());
        e.printStackTrace();
    }
}

private String extractNamespaceFromYamlStorageClass(V1StorageClass yamlStorageClass) {
    if (yamlStorageClass.getMetadata() != null && yamlStorageClass.getMetadata().getNamespace() != null) {
        return yamlStorageClass.getMetadata().getNamespace();
    }
    return null; // Return null if namespace is not specified in the YAML
}

      

}
