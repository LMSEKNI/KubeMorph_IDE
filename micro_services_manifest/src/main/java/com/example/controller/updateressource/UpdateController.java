package com.example.controller.updateressource;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.updateressource.UpdateressourceImpl;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/update")

@RestController
public class UpdateController {
    
    @Autowired
    private UpdateressourceImpl podUpdateService;


    @GetMapping("/{resourceType}/{resourceName}")
    public ResponseEntity<V1Pod> editPod(@PathVariable String resourceType, @PathVariable String resourceName) {
        try {
            V1Pod pod = podUpdateService.editPod(resourceType, resourceName);
            return ResponseEntity.ok(pod);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{resourceType}/{resourceName}")
    public ResponseEntity<?> updateResource(
            @PathVariable String resourceType,
            @PathVariable String resourceName,
            @RequestBody String updatedResourceJson) {
        try {
            V1Pod updatedPod = podUpdateService.updateResource(resourceType, resourceName, updatedResourceJson);
            return ResponseEntity.ok(updatedPod);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating resource: " + e.getMessage());
        }
    }

    @PostMapping("/{resourceName}")
    public ResponseEntity<String> updatePod(@PathVariable String resourceName, @RequestBody V1Pod updatedPod) {
        try {
            podUpdateService.updateResourcePatch(resourceName, updatedPod);
            return ResponseEntity.ok("Pod updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating pod: " + e.getMessage());
        }
    }

//     @PutMapping("/{namespace}/{podName}")
//     public ResponseEntity<String> updatePod(@PathVariable String namespace, @PathVariable String podName) {
//         try {
//             String description =podUpdateService.updatePoddd(namespace, podName);
//             return ResponseEntity.ok("Pod updated successfully: \n" + description );
//         } catch (ApiException | IOException e) {
//             e.printStackTrace();
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating pod: " + e.getMessage());
//         }
//     }
// }
}