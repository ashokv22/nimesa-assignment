package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "s3_objects")
@NoArgsConstructor
@AllArgsConstructor
public class S3BucketObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String objectId;

    private String bucketName;

    private String fileName;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

}
