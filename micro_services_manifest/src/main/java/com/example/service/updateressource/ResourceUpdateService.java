package com.example.service.updateressource;

import io.kubernetes.client.openapi.ApiException;

public interface ResourceUpdateService {
    void updateResource(String namespace, String kind, String name, String updatedYaml) throws ApiException;
    String getResourceAsJson(String namespace, String kind, String name) throws ApiException;
    String getResourceAsYaml(String namespace, String kind, String name) throws ApiException;

}
