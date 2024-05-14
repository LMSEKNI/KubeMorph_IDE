package com.example.service.describeressource;

import java.io.FileNotFoundException;
import java.io.IOException;

import io.kubernetes.client.openapi.ApiException;

public  interface DescService {

    // public String getPodDescriptions( String podName) throws ApiException, FileNotFoundException, IOException ;
    // public String getServiceDescriptions(String serviceName) throws ApiException, IOException ;
    public String getResourceDescriptions(String resourceName, String resourceType) throws ApiException, IOException ;
} 
