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

        String podName = request.get("podName");
        try {
            String log = logs.getPodLogs14( podName); // Ensure this method returns the actual logs
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
}
