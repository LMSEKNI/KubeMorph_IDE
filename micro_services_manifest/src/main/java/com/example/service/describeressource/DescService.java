package com.example.service.describeressource;

import java.io.FileNotFoundException;
import java.io.IOException;

import io.kubernetes.client.openapi.ApiException;

public  interface DescService {

       public String getResourceDescriptions(String resourceName, String resourceType) throws ApiException, IOException ;
} 
