package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ec2_instance")
@NoArgsConstructor
@AllArgsConstructor
public class EC2Instance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instanceId;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

}
