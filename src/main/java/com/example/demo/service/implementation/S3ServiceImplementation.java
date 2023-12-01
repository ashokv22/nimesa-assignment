package com.example.demo.service.implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.demo.domain.EC2Instance;
import com.example.demo.domain.Job;
import com.example.demo.domain.S3Bucket;
import com.example.demo.domain.S3BucketObject;
import com.example.demo.domain.enums.JobStatus;
import com.example.demo.repository.Ec2InstanceRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.S3BucketObjectRepository;
import com.example.demo.repository.S3BucketRepository;
import com.example.demo.service.S3Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
public class S3ServiceImplementation implements S3Service {

    @Autowired
    private AmazonEC2 amazonEC2;

    @Autowired private AmazonS3 amazonS3;

    @Autowired private JobRepository jobRepository;

    @Autowired private Ec2InstanceRepository ec2InstanceRepository;

    @Autowired private S3BucketRepository s3BucketRepository;

    @Autowired private S3BucketObjectRepository s3BucketObjectRepository;

    @Override
    public Job intitlizeJob() {
        // Generate a unique JobId for this discovery job
        String jobId = UUID.randomUUID().toString();
        Job job = new Job(jobId, JobStatus.IN_PROGRESS);
        jobRepository.save(job);
        return job;
    }

    /**
     * Asynchronously discovers EC2 instances and S3 buckets.
     *
     * @param job      The Job entity to update during the discovery process.
     * @param services The list of services to discover (e.g., "EC2", "S3").
     */
    @Async
    @Override
    public void discoverServices(Job job, List<String> services) {
        // Update the job status
        job.setStatus(JobStatus.IN_PROGRESS); // Set the initial status
        jobRepository.save(job);

        try {
            List<EC2Instance> ec2InstanceList = new ArrayList<>();
            List<S3Bucket> s3BucketList = new ArrayList<>();

            // Discover EC2 instances
            List<Instance> ec2Instances = amazonEC2.describeInstances()
                    .getReservations()
                    .stream()
                    .flatMap(reservation -> reservation.getInstances().stream())
                    .toList();

            // Persist EC2 instances in the database
            ec2Instances.forEach(instance -> {
                EC2Instance ec2Instance = new EC2Instance();
                ec2Instance.setInstanceId(instance.getInstanceId());
                ec2Instance.setJob(job);
                ec2InstanceList.add(ec2Instance);
            });
            // Saving list all together to reduce the table insertions calls
            ec2InstanceRepository.saveAllAndFlush(ec2InstanceList);

            // Discover S3 buckets
            List<Bucket> s3Buckets = amazonS3.listBuckets();

            // Persist S3 buckets in the database
            s3Buckets.forEach(bucket -> {
                S3Bucket s3Bucket = new S3Bucket();
                s3Bucket.setBucketName(bucket.getName());
                s3Bucket.setJob(job);
                s3BucketList.add(s3Bucket);
            });
            s3BucketRepository.saveAllAndFlush(s3BucketList);
            job.setStatus(JobStatus.SUCCESS);
        } catch (AmazonServiceException e) {
            log.error("Discovery job failed: " + e.getMessage());
            job.setStatus(JobStatus.FAILED);
        } finally {
            jobRepository.save(job);
        }
    }

    @Override
    public CompletableFuture<String> getJobResult(String jobId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            return CompletableFuture.completedFuture(job.getStatus().name());
        }
        return CompletableFuture.completedFuture("Job Not Found");
    }

    @Override
    public CompletableFuture<List<String>> getDiscoveryResult(String service) {
        if (service.equalsIgnoreCase("s3")) {
            List<S3Bucket> buckets = s3BucketRepository.findAll();
            List<String> bucketNames = buckets.stream().map(S3Bucket::getBucketName).toList();
            return CompletableFuture.completedFuture(bucketNames);
        } else if (service.equalsIgnoreCase("EC2")) {
            List<EC2Instance> instances = ec2InstanceRepository.findAll();
            List<String> instanceIds = instances.stream().map(EC2Instance::getInstanceId).toList();
            return CompletableFuture.completedFuture(instanceIds);
        }
        return CompletableFuture.completedFuture(new ArrayList<>());
    }

    @Async
    @Override
    public void getS3BucketObjects(Job job, String bucketName) {
        // Update the job status
        job.setStatus(JobStatus.IN_PROGRESS);
        jobRepository.save(job);

        // Start the asynchronous task
        try {
            // Discover S3 objects
            List<S3ObjectSummary> objectSummaries = amazonS3.listObjects(bucketName)
                    .getObjectSummaries();

            // Persist S3 object names in the database with the associated JobId
            objectSummaries.forEach(objectSummary -> {
                S3BucketObject s3BucketObject = new S3BucketObject();
                s3BucketObject.setObjectId(objectSummary.getKey());
                s3BucketObject.setBucketName(bucketName);
                s3BucketObject.setFileName(objectSummary.getKey());
                s3BucketObject.setJob(job);
                s3BucketObjectRepository.save(s3BucketObject);
            });

            // Update the job status to 'Success' upon successful completion
            job.setStatus(JobStatus.SUCCESS);
            jobRepository.save(job);
        } catch (Exception e) {
            log.error("S3 discovery job failed: " + e.getMessage());
            // Update the job status to 'Failed' upon failure
            job.setStatus(JobStatus.FAILED);
            jobRepository.save(job);
        }
    }

    @Override
    public Integer getS3BucketObjectCount(String bucketName) {
        int bucketObjectsCount = s3BucketObjectRepository.countByBucketName(bucketName);
        log.info("S3 Objects count for bucket: " + bucketName + " is: " + bucketObjectsCount);
        return bucketObjectsCount;
    }

    @Override
    public List<String> getS3BucketObjectLike(String bucketName, String pattern) {
        return s3BucketObjectRepository.findByBucketNameAndFileNamesLike(bucketName, pattern);
    }

}
