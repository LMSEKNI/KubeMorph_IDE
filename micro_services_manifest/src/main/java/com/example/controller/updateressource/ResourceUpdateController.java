package com.example.controller.updateressource;

import com.example.service.updateressource.ResourceUpdateService;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resources")
public class ResourceUpdateController {

    private final ResourceUpdateService resourceUpdateService;

    @Autowired
    public ResourceUpdateController(ResourceUpdateService resourceUpdateService) {
        this.resourceUpdateService = resourceUpdateService;
    }

    @PutMapping("/{namespace}/{kind}/{name}")
    public ResponseEntity<?> updateResource(
            @PathVariable String namespace,
            @PathVariable String kind,
            @PathVariable String name,
            @RequestBody String updatedYaml
    ) {
        try {
            resourceUpdateService.updateResource(namespace, kind, name, updatedYaml);
            return ResponseEntity.ok("Resource updated successfully");
        } catch (ApiException e) {
            return ResponseEntity.status(e.getCode()).body(e.getResponseBody());
        }
    }
    @GetMapping("/{namespace}/{kind}/{name}/json")
    public ResponseEntity<?> getResourceAsJson(
            @PathVariable String namespace,
            @PathVariable String kind,
            @PathVariable String name
    ) {
        try {
            String resourceJson = resourceUpdateService.getResourceAsJson(namespace, kind, name);
            return ResponseEntity.ok(resourceJson);
        } catch (ApiException e) {
            return ResponseEntity.status(e.getCode()).body(e.getResponseBody());
        }
    }

    @GetMapping("/{namespace}/{kind}/{name}/yaml")
    public ResponseEntity<?> getResourceAsYaml(
            @PathVariable String namespace,
            @PathVariable String kind,
            @PathVariable String name
    ) {
        try {
            String resourceYaml = resourceUpdateService.getResourceAsYaml(namespace, kind, name);
            return ResponseEntity.ok(resourceYaml);
        } catch (ApiException e) {
            return ResponseEntity.status(e.getCode()).body(e.getResponseBody());
        }
    }
}
