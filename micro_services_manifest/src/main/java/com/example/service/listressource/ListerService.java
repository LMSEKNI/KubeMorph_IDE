package com.example.service.listressource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;

public interface ListerService {
    public V1PodList getAllPods() throws FileNotFoundException, IOException, ApiException;
    public List<String> getAllNamespaces() throws FileNotFoundException, IOException, ApiException;
    public V1ServiceList getAllServices() throws IOException, ApiException ;
    public V1DeploymentList listAllDeployments() throws IOException, ApiException ;
    public List<String> getAllReplicaSets() throws IOException, ApiException ;
    public List<String> getAllJobs() throws IOException, ApiException ;
    public List<String> getAllNodes() throws IOException, ApiException ;
    public List<String> getAllConfigMaps() throws IOException, ApiException ;
    public List<String> getAllIngresses() throws IOException, ApiException ;
    public List<String> getAllEndpoints() throws IOException, ApiException ;
    public List<String> getAllDaemonSets() throws IOException, ApiException ;
    public List<String> getAllPersistentVolumeClaims() throws IOException, ApiException ;
    public List<String> getAllStorageClasses() throws IOException, ApiException ;
    public List<String> getAllStatefulSets() throws IOException, ApiException ;
    public List<String> getPodDeploymentConnections() throws ApiException, FileNotFoundException, IOException ;  
    public List<String> getAllPersistentVolumes() throws ApiException, IOException ;
    
}
