package com.example.controller.listressource;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service.listressource.ListerServiceImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/list")
public class ListerController {

    @Autowired
    private ListerServiceImpl listerService;

    @GetMapping("/pods")
    public ResponseEntity<List<V1Pod>> listPods() {
        try {
            List<V1Pod> pods = listerService.getAllPods();
            return new ResponseEntity<>(pods, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/namespaces")
    public ResponseEntity<List<V1Namespace>> getAllNamespaces() {
        try {
            List<V1Namespace> namespaces = listerService.getAllNamespaces();
            return new ResponseEntity<>(namespaces, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/services")
    public ResponseEntity<List<V1Service>> listServices() {
        try {
            List<V1Service> services = listerService.getAllServices();
            return new ResponseEntity<>(services, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/deployments")
    public ResponseEntity<List<V1Deployment>> listDeployments() {
        try {
            List<V1Deployment> deployments = listerService.getAllDeployments();
            return new ResponseEntity<>(deployments, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/replicasets")
    public ResponseEntity<List<V1ReplicaSet>> listReplicaSets() {
        try {
            List<V1ReplicaSet> replicaSets = listerService.getAllReplicaSets();
            return new ResponseEntity<>(replicaSets, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<V1Job>> listJobs() {
        try {
            List<V1Job> jobs = listerService.getAllJobs();
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/nodes")
    public ResponseEntity<List<V1Node>> listNodes() {
        try {
            List<V1Node> nodes = listerService.getAllNodes();
            return new ResponseEntity<>(nodes, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/configmaps")
    public ResponseEntity<List<V1ConfigMap>> listConfigMaps() {
        try {
            List<V1ConfigMap> configMaps = listerService.getAllConfigMaps();
            return new ResponseEntity<>(configMaps, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ingresses")
    public ResponseEntity<List<V1Ingress>> listIngresses() {
        try {
            List<V1Ingress> ingresses = listerService.getAllIngresses();
            return new ResponseEntity<>(ingresses, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/endpoints")
    public ResponseEntity<List<V1Endpoints>> listEndpoints() {
        try {
            List<V1Endpoints> endpoints = listerService.getAllEndpoints();
            return new ResponseEntity<>(endpoints, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/daemonsets")
    public ResponseEntity<List<V1DaemonSet>> listDaemonSets() {
        try {
            List<V1DaemonSet> daemonSets = listerService.getAllDaemonSets();
            return new ResponseEntity<>(daemonSets, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/persistentvolumeclaims")
    public ResponseEntity<List<V1PersistentVolumeClaim>> listPersistentVolumeClaims() {
        try {
            List<V1PersistentVolumeClaim> pvClaims = listerService.getAllPersistentVolumeClaims();
            return new ResponseEntity<>(pvClaims, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/storageclasses")
    public ResponseEntity<List<V1StorageClass>> listStorageClasses() {
        try {
            List<V1StorageClass> storageClasses = listerService.getAllStorageClasses();
            return new ResponseEntity<>(storageClasses, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statefulsets")
    public ResponseEntity<List<V1StatefulSet>> listStatefulSets() {
        try {
            List<V1StatefulSet> statefulSets = listerService.getAllStatefulSets();
            return new ResponseEntity<>(statefulSets, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/persistentvolumes")
    public ResponseEntity<List<V1PersistentVolume>> listPersistentVolumes() {
        try {
            List<V1PersistentVolume> persistentVolumes = listerService.getAllPersistentVolumes();
            return new ResponseEntity<>(persistentVolumes, HttpStatus.OK);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
