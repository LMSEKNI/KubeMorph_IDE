package com.example.runtimeManagement.Services;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;

import java.io.IOException;
import java.util.List;

public interface Logs {
    public List<V1Pod> getPods(String namespace) throws ApiException, IOException ;
    public String getPodLogs(String namespace, String podName) throws Exception ;

    }
