package com.example.controller.listressource;

import io.kubernetes.client.openapi.ApiException;
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
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/list")
public class ListerController {
 
    @Autowired
    private  ListerServiceImpl ListerService;

    
    @GetMapping("/pods")
    public ResponseEntity<List<String>> listPods() throws FileNotFoundException, IOException, ApiException {
        List<String> pods = ListerService.getAllPods();
        return new ResponseEntity<>(pods, HttpStatus.OK);   
    }

    @GetMapping("/namespaces")
    public ResponseEntity<List<String> >getAllNamespace() throws FileNotFoundException, IOException {
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
    public ResponseEntity<List<String>> listServices() {
        try {
            List<String> services = ListerService.getAllServices();
            return new ResponseEntity<>(services, HttpStatus.OK);
        } catch (IOException | ApiException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/deployments")
    public ResponseEntity<List<String>> listDeployments() throws IOException, ApiException {
        List<String> deploymnts = ListerService.getAllDeployments();
        return new ResponseEntity<>(deploymnts,HttpStatus.OK);
    }
    @GetMapping("/replicasets")
    public ResponseEntity<List<String>> listReplicaSets() throws IOException, ApiException {
        List<String> deploymnts = ListerService.getAllReplicaSets();
        return new ResponseEntity<>(deploymnts,HttpStatus.OK);
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<String>> listJobs() throws IOException, ApiException {
        List<String> jobs = ListerService.getAllJobs();
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
