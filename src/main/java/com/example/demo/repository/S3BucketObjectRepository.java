package com.example.demo.repository;

import com.example.demo.domain.S3BucketObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface S3BucketObjectRepository extends JpaRepository<S3BucketObject, String> {
    int countByBucketName(String bucketName);

    @Query("SELECT s.fileName FROM S3BucketObject s WHERE s.bucketName = :bucketName AND s.fileName LIKE %:pattern%")
    List<String> findByBucketNameAndFileNamesLike(String bucketName, String pattern);

}
