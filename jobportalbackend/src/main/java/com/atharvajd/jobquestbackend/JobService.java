package com.atharvajd.jobquestbackend;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    public List<JobApplication> allJobs() {
        return jobRepository.findAll();
    }

    public Optional<JobApplication> singleJob(ObjectId id) {
        return jobRepository.findById(id);
    }

    public JobApplication createJob(JobApplication job) {
        return jobRepository.insert(job);
    }

    public JobApplication deleteJob(ObjectId id) {
        JobApplication job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
        jobRepository.delete(job);
        return job;
    }
}
