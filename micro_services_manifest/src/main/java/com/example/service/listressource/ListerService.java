package com.example.service.listressource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;

public interface ListerService {
    public List<V1Pod> getAllPods() throws FileNotFoundException, IOException, ApiException;
    public List<V1Namespace> getAllNamespaces() throws FileNotFoundException, IOException, ApiException;
    public List<V1Service> getAllServices() throws FileNotFoundException, IOException, ApiException ;
    public List<V1Deployment> getAllDeployments() throws IOException, ApiException;
    public List<V1ReplicaSet> getAllReplicaSets() throws IOException, ApiException ;
    public List<V1Job> getAllJobs() throws IOException, ApiException ;
    public List<V1Node> getAllNodes() throws IOException, ApiException ;
    public List<V1ConfigMap> getAllConfigMaps() throws IOException, ApiException ;
    public List<V1Ingress> getAllIngresses() throws IOException, ApiException ;
    public List<V1Endpoints> getAllEndpoints() throws IOException, ApiException ;
    public List<V1DaemonSet> getAllDaemonSets() throws IOException, ApiException ;
    public List<V1PersistentVolumeClaim> getAllPersistentVolumeClaims() throws IOException, ApiException ;
    public List<V1StorageClass> getAllStorageClasses() throws IOException, ApiException ;
    public List<V1StatefulSet> getAllStatefulSets() throws IOException, ApiException ;
    public List<V1PersistentVolume> getAllPersistentVolumes() throws ApiException, IOException ;
    
}
