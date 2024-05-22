package com.example.controller.createressource.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.createressource.form.CreateResourceFormImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/create")

@RestController
public class CreateControllerForm {


    @Autowired
    private  CreateResourceFormImpl createResource;
    

    @PostMapping("/resource/form")
    public ResponseEntity<String> handleResourceResponse(@RequestBody String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response); 
            
            // Check if 'kind' property exists in the JSON
            JsonNode kindNode = jsonNode.get("kind");
            if (kindNode == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing 'kind' property in JSON");
            }
            
            String kind = kindNode.asText(); 
            if ("Pod".equals(kind)) {
                createResource.createPod(response);
                return ResponseEntity.ok("Pod created successfully ");
            } 
            else if ("ConfigMap".equals(kind)) {
                createResource.createConfigMap(response);
                return ResponseEntity.ok("ConfigMap created successfully ");
            } 
            else if ("Deployment".equals(kind)) {
                createResource.createDeployment(response);
                return ResponseEntity.ok("Deployment created successfully ");
            }
            else if ("Namespace".equals(kind)) {
                createResource.createNamespace(response);
                return ResponseEntity.ok("Namespace created successfully ");
            }
            else if ("Service".equals(kind)) {
                createResource.createService(response);
                return ResponseEntity.ok("Service created successfully ");
            }
            else if ("Job".equals(kind)) {
                createResource.createJob(response);
                return ResponseEntity.ok("Job created successfully ");
            }
            else if ("Ingress".equals(kind)) {
                createResource.createIngress(response);
                return ResponseEntity.ok("Ingress created successfully ");
            }
            else if ("StatefulSet".equals(kind)) {
                createResource.createStatefulSet(response);
                return ResponseEntity.ok("StatefulSet created successfully ");
            }
            else if ("DaemonSet".equals(kind)) {
                createResource.createDaemonSet(response);
                return ResponseEntity.ok("DaemonSet created successfully ");
            }
            else if ("PersistentVolume".equals(kind)) {
                createResource.createPersistentVolume(response);
                return ResponseEntity.ok("PersistentVolume created successfully ");
            }
            else if ("PersistentVolumeClaim".equals(kind)) {
                createResource.createPersistentVolumeClaim(response);
                return ResponseEntity.ok("PersistentVolumeClaim created successfully ");
            }
            else if ("StorageClass".equals(kind)) {
                createResource.createStorageClass(response);
                return ResponseEntity.ok("StorageClass created successfully ");
            }
            else if ("ReplicaSet".equals(kind)) {
                createResource.createReplicaSet(response);
                return ResponseEntity.ok("ReplicaSet created successfully ");
            }
             else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported resource kind: " + kind);
            }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process response: " + e.getMessage());
            }
    }

}
