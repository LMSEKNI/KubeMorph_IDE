package com.example.controller.updateressource;

import java.io.IOException;
import java.util.Map;

import com.example.KubernetesConfig.KubernetesConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service.updateressource.UpdateressourceImpl;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/update")

@RestController
public class UpdateController {
    private static final Logger logger = LoggerFactory.getLogger(UpdateController.class);

    @Autowired
    private UpdateressourceImpl podUpdateService;
    @Autowired
    private KubernetesConfigService kubernetesConfigService;
    @Autowired
    private ObjectMapper objectMapper;
    @PatchMapping("/{namespace}/pods/{podName}")
    public V1Pod updatePod3(@PathVariable String namespace, @PathVariable String podName, @RequestBody String podJson) throws IOException, ApiException {
        return podUpdateService.updatePod(namespace, podName, podJson);
    }

    @PatchMapping("/namespace/{oldNamespaceName}")
    public V1Namespace replaceNamespace(@PathVariable String oldNamespaceName, @RequestBody String newNamespaceJson) throws IOException, ApiException {
        return podUpdateService.replaceNamespace(oldNamespaceName, newNamespaceJson);
    }



}
