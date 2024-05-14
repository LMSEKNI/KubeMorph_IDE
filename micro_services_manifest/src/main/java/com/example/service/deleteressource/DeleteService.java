package com.example.service.deleteressource;

import java.io.IOException;
import java.util.List;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1ServiceList;

public interface DeleteService {
    public String deleteService( String resourceType,String serviceName) throws IOException, ApiException ;    }
