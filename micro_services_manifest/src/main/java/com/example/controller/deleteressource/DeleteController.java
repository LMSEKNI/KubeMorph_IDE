package com.example.controller.deleteressource;

import java.io.IOException;

import com.example.service.deleteressource.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.deleteressource.DeleteServiceImpl;

import io.kubernetes.client.openapi.ApiException;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/delete")
public class DeleteController {

    @Autowired
    private DeleteService deleteService;
    
    @DeleteMapping("/{resourceType}/{resourceName}")
    public String deleteService(@PathVariable String resourceType, @PathVariable String resourceName) {
        try {
            return deleteService.deleteService(resourceType,resourceName).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
}
