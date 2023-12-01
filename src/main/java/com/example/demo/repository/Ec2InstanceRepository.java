package com.example.demo.repository;

import com.example.demo.domain.EC2Instance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Ec2InstanceRepository extends JpaRepository<EC2Instance, String> {
}
