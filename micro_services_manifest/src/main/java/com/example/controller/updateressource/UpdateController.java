package com.example.controller.updateressource;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service.updateressource.UpdateressourceImpl;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/update")

@RestController
public class UpdateController {
    private static final Logger logger = LoggerFactory.getLogger(UpdateController.class);

    @Autowired
    private UpdateressourceImpl podUpdateService;
    @PatchMapping("/pod/{oldName}")
    public V1Pod updatePod2(@PathVariable String oldName, @RequestBody String newJson) {
        logger.info("Received request to update pod with oldName: {} and newJson: {}", oldName, newJson);
        try {
            V1Pod updatedPod = podUpdateService.updatePod(oldName, newJson);
            logger.info("Pod updated successfully: {}", updatedPod.getMetadata().getName());
            return updatedPod;
        } catch (Exception e) {
            logger.error("Failed to update pod with oldName: {}", oldName, e);
            throw new RuntimeException("Failed to update pod", e);
        }
    }
    @PutMapping("/{name}")
    public ResponseEntity<String> updatePod(@PathVariable String name, @RequestBody String yamlContent) {
        try {
            podUpdateService.updatePod( name,yamlContent);
            return ResponseEntity.ok("Pod updated successfully: " + name);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating Pod: " + e.getMessage());
        }
    }
    @GetMapping("/{namespace}/{resourceName}")
    public ResponseEntity<V1Pod> getPod(@PathVariable String namespace, @PathVariable String resourceName) {
        try {
            V1Pod pod = podUpdateService.getPod(namespace, resourceName);
            return ResponseEntity.ok(pod);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/{resourceType}/{resourceName}")
    public ResponseEntity<?> updatePod(
            @PathVariable String resourceType,
            @PathVariable String resourceName,
            @RequestBody String updatedResourceJson) {
        try {
            V1Pod updatedPod = podUpdateService.updateResourcenew(resourceType, resourceName, updatedResourceJson);
            return ResponseEntity.ok(updatedPod);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating resource: " + e.getMessage());
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



    @PutMapping("/replica/{namespace}/{name}")
    public String patchReplicaSet(@PathVariable String namespace, @PathVariable  String name,@RequestBody String jsondesc){
        try {
            podUpdateService.patchReplicaSet( namespace,  name,  jsondesc);
            return "Resource updated successfully";
        } catch (IOException e) {
            throw new RuntimeException(e);
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