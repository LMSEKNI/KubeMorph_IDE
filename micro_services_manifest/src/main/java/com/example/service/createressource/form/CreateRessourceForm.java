package com.example.service.createressource.form;

import java.io.IOException;

import io.kubernetes.client.openapi.ApiException;

public interface CreateRessourceForm {
    public void createPod(String response) throws ApiException, IOException ;
    public void createDeployment(String response) throws ApiException, IOException ;
    public void createConfigMap(String response) throws ApiException, IOException ;
    public void createNamespace(String response) throws ApiException, IOException ;
    public void createService(String response) throws ApiException, IOException ;
    public void createJob(String response) throws ApiException, IOException ;
    public void createIngress(String response) throws ApiException, IOException ;
    public void createStatefulSet(String response) throws ApiException, IOException ;
    public void createDaemonSet(String response) throws ApiException, IOException ;
    public void createPersistentVolume(String response) throws ApiException, IOException ;
    public void createPersistentVolumeClaim(String response) throws ApiException, IOException ;
    public void createStorageClass(String response) throws ApiException, IOException ;
    public void createReplicaSet(String response) throws ApiException, IOException ;

    }
