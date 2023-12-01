package com.example.demo.repository;

import com.example.demo.domain.S3Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3BucketRepository extends JpaRepository<S3Bucket, String> {
}
