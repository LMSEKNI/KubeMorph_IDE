package com.example.controller.describeressource;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.example.service.describeressource.DescService;
import io.kubernetes.client.openapi.models.V1Pod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.describeressource.DescServiceImpl;

import io.kubernetes.client.openapi.ApiException;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/desc")
public class DescController {
    @Autowired
    private DescService descService;

    @GetMapping("/{resourceType}/{ressourceName}")
    public ResponseEntity<String> getResourceDescriptions(@PathVariable String ressourceName, @PathVariable String resourceType) throws IOException, ApiException {
        String description = descService.getResourceDescriptions(ressourceName, resourceType);
        return ResponseEntity.ok(description);
    }


}

