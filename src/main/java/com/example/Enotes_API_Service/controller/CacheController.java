package com.example.Enotes_API_Service.controller;

import com.example.Enotes_API_Service.endpoint.CacheEndpoint;
import com.example.Enotes_API_Service.service.CacheManagerService;
import com.example.Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class CacheController implements CacheEndpoint {

    @Autowired
    private CacheManagerService cacheManagerService;

    @Override
    public ResponseEntity<?> getAllCache() {
        Collection<String> cache = cacheManagerService.getCache();
        return CommonUtil.createBuildResponse(cache, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getCache(String cacheName) {
        Cache cache = cacheManagerService.getCacheName(cacheName);
        return CommonUtil.createBuildResponse(cache, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> removeAllCache() {
        cacheManagerService.removeAllCache();
        return CommonUtil.createBuildResponseMessage("Removed all cache", HttpStatus.OK);
    }
}
