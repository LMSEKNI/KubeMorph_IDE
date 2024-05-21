package com.example.service.createressource.form;

import java.io.IOException;

import io.kubernetes.client.openapi.ApiException;

public interface CreateRessourceForm {
    public void createPod(String response) throws ApiException, IOException ;
    public void createDeployment(String response) throws ApiException, IOException ;
    public void createConfigMap(String response) throws ApiException, IOException ;
    public void createNamespace(String response) throws ApiException, IOException ;
    }
