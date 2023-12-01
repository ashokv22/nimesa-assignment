package com.example.demo.rest;

import com.example.demo.domain.Job;
import com.example.demo.service.S3Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class S3Resource {

    @Autowired private S3Service s3Service;

    @GetMapping("/discover")
    public ResponseEntity<String> getDiscoveries(@RequestBody List<String> services) {
        Job job = s3Service.intitlizeJob();
        s3Service.discoverServices(job, services);
        return ResponseEntity.ok(job.getId());
    }


    @GetMapping("/job-result/{jobId}")
    public CompletableFuture<String> getJobResult(@PathVariable String jobId) {
        return s3Service.getJobResult(jobId);
    }

    @GetMapping("/discovery-result/{service}")
    public CompletableFuture<List<String>> getDiscoveryResult(@PathVariable String service) {
        return s3Service.getDiscoveryResult(service);
    }

    @GetMapping ("/s3-bucket-objects/{bucketName}")
    public ResponseEntity<String> getS3BucketObjects(@PathVariable String bucketName) {
        Job job = s3Service.intitlizeJob();
        s3Service.getS3BucketObjects(job, bucketName);
        return ResponseEntity.ok(job.getId());
    }

    @GetMapping("/s3-bucket-object-count/{bucketName}")
    public ResponseEntity<Integer> getS3BucketObjectCount(@PathVariable String bucketName) {
        return ResponseEntity.ok(s3Service.getS3BucketObjectCount(bucketName));
    }

    @GetMapping("/s3-bucket-object-like/{bucketName}")
    public ResponseEntity<List<String>> getS3BucketObjectLike(
            @PathVariable String bucketName,
            @RequestParam String pattern) {
        return ResponseEntity.ok(s3Service.getS3BucketObjectLike(bucketName, pattern));
    }

}
