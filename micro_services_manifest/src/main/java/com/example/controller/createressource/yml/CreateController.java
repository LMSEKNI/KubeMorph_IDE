package com.example.controller.createressource.yml;

import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.createressource.yml.CreateService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.util.Yaml;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/create")
public class CreateController {
    @Autowired
    private CreateService CreateService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CreateController(CreateService CreateService,
                            ObjectMapper objectMapper) {
        this.CreateService = CreateService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/resource")
    public ResponseEntity<String> createResourceFromYaml(MultipartFile yamlFile) {
        
        try {
            // Read the YAML content from the file
            String yamlContent = new String(yamlFile.getBytes());
            Yaml yaml = new Yaml();
            Object obj = Yaml.load(yamlContent);
            String jsonContent = objectMapper.writeValueAsString(obj);
            JsonNode rootNode = objectMapper.readTree(jsonContent);
            String kind = rootNode.get("kind").asText();

            if ("Pod".equals(kind)) {
                CreateService.createPodFromYaml(yamlContent);
                return ResponseEntity.ok("Pod created successfully from YAML");
            } else if ("Service".equals(kind)) {
                CreateService.createServiceFromYaml(yamlContent);
                return ResponseEntity.ok("Service created successfully from YAML");
            } else if ("Deployment".equals(kind)) {
                CreateService.createDeploymentFromYaml(yamlContent);
                return ResponseEntity.ok("Deployment created successfully from YAML");
            }else if ("ConfigMap".equals(kind)) {
                CreateService.createConfigMapFromYaml(yamlContent);
                return ResponseEntity.ok("ConfigMap created successfully from YAML");
            }
            else if ("StatefulSet".equals(kind)) {
                CreateService.createStatefulSetFromYaml(yamlContent);
                return ResponseEntity.ok("StatefulSet created successfully from YAML");
            }
            else if ("ReplicaSet".equals(kind)) {
                CreateService.createReplicaSetFromYaml(yamlContent);
                return ResponseEntity.ok("ReplicaSet created successfully from YAML");
            } 
            else if ("Node".equals(kind)) {
                CreateService.createNodeFromYaml(yamlContent);
                return ResponseEntity.ok("Node created successfully from YAML");
            } 
            else if ("Job".equals(kind)) {
                CreateService.createJobFromYaml(yamlContent);
                return ResponseEntity.ok("Job created successfully from YAML");
            } else if ("Ingress".equals(kind)) {
                CreateService.createIngressFromYaml(yamlContent);
                return ResponseEntity.ok("Ingress created successfully from YAML");
            }
            else if ("Endpoint".equals(kind)) {
                CreateService.createEndpointFromYaml(yamlContent);
                return ResponseEntity.ok("Endpoint created successfully from YAML");
            } 
            else if ("DaemonSet".equals(kind)) {
                CreateService.createDaemonSetFromYaml(yamlContent);
                return ResponseEntity.ok("DaemonSet created successfully from YAML");
            } 
            else if ("PersistentVolume".equals(kind)) {
                CreateService.createPersistentVolumeFromYaml(yamlContent);
                return ResponseEntity.ok("PersistentVolume created successfully from YAML");
            }
            else if ("PersistentVolumeClaim".equals(kind)) {
                CreateService.createPersistentVolumeClaimFromYaml(yamlContent);
                return ResponseEntity.ok("PersistentVolumeClaim created successfully from YAML");
            } 
            else if ("StorageClass".equals(kind)) {
                CreateService.createStorageClassFromYaml(yamlContent);
                return ResponseEntity.ok("StorageClass created successfully from YAML");
            }
            else if ("HorizontalPodAutoscaler".equals(kind)) {
                CreateService.createHPAFromYaml(yamlContent);
                return ResponseEntity.ok("HPA created successfully from YAML");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported resource kind: " + kind);
            }
        } catch (IOException | ApiException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create resource from YAML: " + e.getMessage());
        }
    }

}
