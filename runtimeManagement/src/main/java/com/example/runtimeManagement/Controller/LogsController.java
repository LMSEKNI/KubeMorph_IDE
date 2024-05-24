package com.example.runtimeManagement.Controller;

import com.example.runtimeManagement.K8sConfig.KubernetesConfigurationImpl;
import com.example.runtimeManagement.Services.LogsImpl;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
//@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/runtime")
public class LogsController {

    @Autowired
    private LogsImpl logs;

    @GetMapping("/pods/{namespace}")
    public List<V1Pod> getPods(@PathVariable String namespace) throws ApiException, IOException {
        return logs.getPods(namespace);
    }

    @GetMapping("/logs/{namespace}/{podName}")
    public String getPodLogs(@PathVariable String namespace, @PathVariable String podName) throws Exception {
        return logs.getPodLogs(namespace, podName);
    }
}
