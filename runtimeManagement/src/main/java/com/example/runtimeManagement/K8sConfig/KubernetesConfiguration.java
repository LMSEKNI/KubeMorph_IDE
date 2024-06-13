package com.example.runtimeManagement.K8sConfig;

import io.kubernetes.client.openapi.ApiClient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public interface KubernetesConfiguration {
    public ApiClient configureKubernetesAccess() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException;

    }
