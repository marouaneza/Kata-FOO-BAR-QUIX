package com.foo.bar.quix.kata.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/jobs")
public class JobLauncherController {
    private final JobLauncher jobLauncher;
    private final Job fooBarQuixJob;

    public JobLauncherController(JobLauncher jobLauncher, Job fooBarQuixJob) {
        this.jobLauncher = jobLauncher;
        this.fooBarQuixJob = fooBarQuixJob;
    }

    @PostMapping("/run")
    public ResponseEntity<String> runJob(@RequestParam(required = false) String inputFile){
        try {
            JobParametersBuilder parametersBuilder = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis());

            if(inputFile != null){
                parametersBuilder.addString("inputFile", inputFile);
            }

            JobExecution execution = jobLauncher.run(fooBarQuixJob, parametersBuilder.toJobParameters());

            return ResponseEntity.ok("Job lancé avec l'ID: " + execution.getJobId() +
                    ", statut : "+ execution.getStatus());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du lancement du job : " + e.getMessage());
        }
    }
}
