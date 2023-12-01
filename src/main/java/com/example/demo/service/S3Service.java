package com.example.demo.service;

import com.example.demo.domain.Job;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface S3Service {

    Job intitlizeJob();

    void discoverServices(Job job, List<String> services);

    CompletableFuture<String> getJobResult(String jobId);

    CompletableFuture<List<String>> getDiscoveryResult(String service);

    void getS3BucketObjects(Job job, String bucketName);

    Integer getS3BucketObjectCount(String bucketName);

    List<String> getS3BucketObjectLike(String bucketName, String pattern);

}
