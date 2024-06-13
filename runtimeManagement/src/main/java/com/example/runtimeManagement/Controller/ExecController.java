package com.example.runtimeManagement.Controller;

import com.example.runtimeManagement.K8sConfig.KubernetesConfig;
//import com.example.runtimeManagement.K8sConfig.KubernetesConfigurationImpl;
import com.example.runtimeManagement.Services.Exec.ExecImpl;
import com.example.runtimeManagement.Services.Logs.LogsImpl;
import io.kubernetes.client.Exec;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.proto.V1;
import io.kubernetes.client.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.*;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/runtime")
public class ExecController {

    @Autowired
    private ExecImpl execImpl;

    private static final Logger logger = LoggerFactory.getLogger(ExecController.class);

    @GetMapping("/getpod")
    public String getPod(@RequestParam String namespace, @RequestParam String podName) {
        try {
            V1Pod pod = execImpl.getPod(namespace, podName);
            if (pod != null) {
                return pod.toString();
            } else {
                return "Pod not found.";
            }
        } catch (ApiException | UnrecoverableKeyException | CertificateException | IOException | KeyStoreException |
                 NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return "Error retrieving pod details: " + e.getMessage();
        }
    }

    @PostMapping("/podexec")
    public String podexec(@RequestBody Map<String, String> request) {
        {
            String podName = request.get("podName");
            String command = request.get("command");
            try {
                System.out.println("Command executed successfully");

                return execImpl.podexec( podName, command);
                // Read the output of the process
                // You can implement your own logic to read and process the output here


            } catch (ApiException | IOException e) {
                e.printStackTrace();
                System.out.println("Error executing command: " + e.getMessage());
            }
        }
        return null;
    }
}
