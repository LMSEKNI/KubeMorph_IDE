package com.example.controller.listressource;

import com.example.KubernetesConfig.KubernetesConfigService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.listressource.ListerServiceImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/list")
public class ListerController {
 
    @Autowired
    private  ListerServiceImpl ListerService;

    
    @GetMapping("/pods")
    public ResponseEntity<V1PodList> listPods() throws FileNotFoundException, IOException, ApiException {
        V1PodList pods = ListerService.getAllPods();
        return new ResponseEntity<>(pods, HttpStatus.OK);   
    }

    @GetMapping("/namespaces")
    public ResponseEntity<List<String>>getAllNamespace() throws FileNotFoundException, IOException {
        try {
            List<String> namespaces= ListerService.getAllNamespaces();
            return new ResponseEntity<>(namespaces, HttpStatus.OK);
        } catch (ApiException e) {
            // Handle exceptions
            e.printStackTrace();
            return null; // Or return an empty list or handle the error as appropriate
        }
    }
    @GetMapping("/services")
    public ResponseEntity<V1ServiceList> listServices() {
        try {
            V1ServiceList services = ListerService.getAllServices();
            return new ResponseEntity<>(services, HttpStatus.OK);
        } catch (IOException | ApiException e) {
            // Log the exception or handle it accordingly
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /////////////////////////////////////////////////.::::::::::::::::
    @Autowired
    private KubernetesConfigService KubernetesConfigService;
    @GetMapping("/deploymentss")
    public ResponseEntity<String> listDeploymentss() throws IOException, ApiException {

            ApiClient client = KubernetesConfigService.configureKubernetesAccess();
            AppsV1Api api = new AppsV1Api(client);

            // Fetch deployments
            V1DeploymentList deploymentList = api.listDeploymentForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

            // Construct JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            List<V1Deployment> deployments = deploymentList.getItems();
            List<Object> items = new ArrayList<>();
            for (V1Deployment deployment : deployments) {
                items.add(objectMapper.convertValue(deployment, Object.class));
            }

            // Create the JSON structure
            String json = objectMapper.writeValueAsString(new KubernetesDeploymentList(items));

            return ResponseEntity.ok(json);

            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve deployments: " + e.getMessage());

    }

    // Helper class to wrap deployments in a list object
    private static class KubernetesDeploymentList {
        private String apiVersion = "v1";
        private String kind = "List";
        private List<Object> items;
        private KubernetesDeploymentList(List<Object> items) {
            this.items = items;
        }
    }
   ///////////////:::::::::::::::::::::::::
    @GetMapping("/deployments")
    public ResponseEntity<V1DeploymentList> listDeployments() {
        try {
            V1DeploymentList deployments = ListerService.listAllDeployments();
            return new ResponseEntity<>(deployments, HttpStatus.OK);
        } catch (IOException | ApiException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/replicasets")
    public ResponseEntity<List<String>> listReplicaSets() throws IOException, ApiException {
        List<String> deploymnts = ListerService.getAllReplicaSets();
        return new ResponseEntity<>(deploymnts,HttpStatus.OK);
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<String> > listJobs() throws IOException, ApiException {
        List<String>  jobs = ListerService.getAllJobs();
        return new ResponseEntity<>(jobs,HttpStatus.OK);
    }
    @GetMapping("/node")
    public ResponseEntity<List<String>> listNodes() throws IOException, ApiException {
        List<String> nodes = ListerService.getAllNodes();
        return new ResponseEntity<>(nodes,HttpStatus.OK);
    }
    @GetMapping("/configmaps")
    public ResponseEntity<List<String>> listConfigMap() throws IOException, ApiException {
        List<String> configmap = ListerService.getAllConfigMaps();
        return new ResponseEntity<>(configmap,HttpStatus.OK);
    }
    @GetMapping("/ingress")
    public ResponseEntity<List<String>> listIngress() throws IOException, ApiException {
        List<String> ingress = ListerService.getAllIngresses();
        return new ResponseEntity<>(ingress,HttpStatus.OK);
    }
    @GetMapping("/endpoints")
    public ResponseEntity<List<String>> listEndpoint() throws IOException, ApiException {
        List<String> endpoint = ListerService.getAllEndpoints();
        return new ResponseEntity<>(endpoint,HttpStatus.OK);
    }
    @GetMapping("/deamonsets")
    public ResponseEntity<List<String>> getAllDaemonSets() throws IOException, ApiException {
        List<String> deamonsets = ListerService.getAllDaemonSets();
        return new ResponseEntity<>(deamonsets,HttpStatus.OK);
    }
    @GetMapping("/pvc")
    public ResponseEntity<List<String>> getAllPersistentVolumeClaims() throws IOException, ApiException {
        List<String> deamonsets = ListerService.getAllPersistentVolumeClaims();
        return new ResponseEntity<>(deamonsets,HttpStatus.OK);
    }
    @GetMapping("/sc")
    public ResponseEntity<List<String>> getAllStorageClasses() throws IOException, ApiException {
        List<String> deamonsets = ListerService.getAllStorageClasses();
        return new ResponseEntity<>(deamonsets,HttpStatus.OK);
    }
    @GetMapping("/stateful")
    public ResponseEntity<List<String>> getAllStateful() throws IOException, ApiException {
        List<String> stateful = ListerService.getAllStatefulSets();
        return new ResponseEntity<>(stateful,HttpStatus.OK);
    }
    @GetMapping("/persistentvolume")
    public ResponseEntity<List<String>> getAllPersistentVolumes() throws IOException, ApiException {
        List<String> persistentvolume = ListerService.getAllPersistentVolumes();
        return new ResponseEntity<>(persistentvolume,HttpStatus.OK);
    }
    @GetMapping("/connection")
    public ResponseEntity<List<String>> getPodDeploymentConnections() throws IOException, ApiException {
        List<String> connection = ListerService.getPodDeploymentConnections();
        return new ResponseEntity<>(connection,HttpStatus.OK);
    }
    
    
    
}
