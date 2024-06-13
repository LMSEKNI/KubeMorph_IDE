package com.example.runtimeManagement.Services.Logs;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

public interface Logs {
    public List<V1Pod> getPods(String namespace) throws ApiException, IOException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException;
    public String getPodLogs(String namespace, String podName) throws Exception ;

    }
