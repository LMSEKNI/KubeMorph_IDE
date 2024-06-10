package com.example.runtimeManagement.Controller;

import com.example.runtimeManagement.Services.Exec.ExecImpl;
import com.example.runtimeManagement.Services.Logs.LogsImpl;
import io.kubernetes.client.openapi.ApiException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/runtime")
public class LogsController {

    @Autowired
    private LogsImpl logs;

    @PostMapping("/podlogs")
    public ResponseEntity<String> getPodLogs(@RequestBody Map<String, String> request) {
        String namespace = request.get("namespace");
        String podName = request.get("podName");

        try {
            String log = logs.getPodLogs14(namespace, podName); // Ensure this method returns the actual logs
            return ResponseEntity.ok().body(log); // Return the actual logs content
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }



    @GetMapping("/pods")
    public String getPods(@RequestParam String namespace) throws ApiException, IOException{
        return logs.getPods(namespace).toString();
    }

    @GetMapping("/logs")
    public String getPodLogs(
            @RequestParam String namespace,
            @RequestParam String podName,
            @RequestParam(required = false) String containerName) {
        try {
            return logs.getPodLogs(namespace, podName);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }


    @GetMapping("/logs3")
    public String getPodLogs3(
            @RequestParam String namespace,
            @RequestParam String podName,
            @RequestParam(required = false) String containerName) {
        try {
            return logs.retrieveLogsFromPod3(namespace, podName, containerName);

        } catch (ApiException e) {
            e.printStackTrace();
            return "Error retrieving logs: " + e.getMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}
