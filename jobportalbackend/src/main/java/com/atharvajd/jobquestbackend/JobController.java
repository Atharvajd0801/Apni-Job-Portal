package com.atharvajd.jobquestbackend;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/jobs")
@CrossOrigin(origins = "http://localhost:8080")
public class JobController {
    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<List<JobApplication>> getAllJobs() {
        return new ResponseEntity<List<JobApplication>>(jobService.allJobs(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<JobApplication>> getSingleJob(@PathVariable ObjectId id) {
        return new ResponseEntity<Optional<JobApplication>>(jobService.singleJob(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<JobApplication> createJob(@RequestBody JobApplication job) {
        return new ResponseEntity<JobApplication>(jobService.createJob(job), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JobApplication> deleteJob(@PathVariable String id) {
        ObjectId jobId = new ObjectId(id);
        return new ResponseEntity<JobApplication>(jobService.deleteJob(jobId), HttpStatus.NO_CONTENT);
    }
}
