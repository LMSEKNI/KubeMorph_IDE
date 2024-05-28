package com.example.service.createressource.yml;

import java.io.IOException;


import io.kubernetes.client.openapi.ApiException;

public interface CreateService {
    public void createServiceFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createPodFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createDeploymentFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createConfigMapFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createStatefulSetFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createReplicaSetFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createNodeFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createJobFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createIngressFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createEndpointFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createDaemonSetFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createPersistentVolumeFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createPersistentVolumeClaimFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createStorageClassFromYaml(String yamlContent) throws IOException, ApiException ;
    public void createHPAFromYaml(String yamlContent) throws IOException, ApiException ;

    }