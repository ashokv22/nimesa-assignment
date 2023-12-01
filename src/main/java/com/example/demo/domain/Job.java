package com.example.demo.domain;

import com.example.demo.domain.enums.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "job")
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

}
