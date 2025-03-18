package com.atharvajd.jobquestbackend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JobQuestBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobQuestBackendApplication.class, args);
    }
}
