package com.example.runtimeManagement.Services.Exec;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public interface ExecTerminal {
    public V1Pod getPod(String namespace, String podName) throws ApiException, IOException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException ;
    public String podexec(String podName, String command) throws ApiException, IOException ;
    public String podexecit(String namespace, String podName, String container, String command) throws ApiException, IOException ;

    }
