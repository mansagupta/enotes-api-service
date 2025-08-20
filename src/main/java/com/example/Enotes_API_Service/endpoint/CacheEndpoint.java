package com.example.Enotes_API_Service.endpoint;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Caching", description = "All the caching APIs")
@RequestMapping("/api/v1/cache")
public interface CacheEndpoint {

    @GetMapping("/get")
    ResponseEntity<?> getAllCache();

    @GetMapping("/{cacheName}")
    ResponseEntity<?> getCache(@PathVariable String cacheName);

    @DeleteMapping("/remove-cache")
    ResponseEntity<?> removeAllCache();
}
