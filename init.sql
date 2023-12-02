truncate table job;
drop table ec2_instance;
drop table s3_bucket;
drop table s3_objects;


CREATE TABLE IF NOT EXISTS job (
    id VARCHAR(255) PRIMARY KEY,
    status VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS ec2_instance (
	id INT AUTO_INCREMENT PRIMARY KEY,
    instance_id VARCHAR(255),
    job_id VARCHAR(255),
    FOREIGN KEY (job_id) REFERENCES job (id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS s3_bucket (
	id INT AUTO_INCREMENT PRIMARY KEY,
    bucket_name VARCHAR(255),
    job_id VARCHAR(255),
    FOREIGN KEY (job_id) REFERENCES job (id) ON DELETE CASCADE
);

CREATE TABLE s3_objects (
	id INT AUTO_INCREMENT PRIMARY KEY,
    object_id VARCHAR(255),
    bucket_name VARCHAR(255),
    file_name VARCHAR(255),
    job_id VARCHAR(255),
    FOREIGN KEY (job_id) REFERENCES job (id)
);


SELECT @@sql_mode;
select * from job;
select * from ec2_instance ei;
select * from s3_bucket sb;
select * from s3_objects;


SHOW CREATE TABLE job;

delete from job; 