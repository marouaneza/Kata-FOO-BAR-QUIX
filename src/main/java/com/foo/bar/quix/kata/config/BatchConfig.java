package com.foo.bar.quix.kata.config;

import com.foo.bar.quix.kata.service.FooBarQuixService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    private static final Logger log = LoggerFactory.getLogger(BatchConfig.class);

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private FooBarQuixService fooBarQuixService;

    private ApplicationContext applicationContext;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, FooBarQuixService fooBarQuixService, ApplicationContext applicationContext) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;

        this.fooBarQuixService = fooBarQuixService;
        this.applicationContext = applicationContext;
    }

    /**
     * Reader : lit les lignes du fichier input.txt
     */
    @Bean
    @StepScope
    public FlatFileItemReader<Integer> reader(@Value("#{jobParameters['inputFile']}") String inputFile){
        log.info("Debut de lecture du fichier input.txt");
        Resource resource;
        if(inputFile == null || inputFile.isBlank()){
            inputFile = applicationContext.getEnvironment().getProperty("app.input.file", "classpath:input.txt");
        }

        if(inputFile.startsWith("classpath:")){
            resource = new ClassPathResource(inputFile.replace("classpath:", ""));
        }else {
            resource = new FileSystemResource(inputFile);
        }

        return new FlatFileItemReaderBuilder<Integer>()
                .name("numberItemReader")
                .resource(resource)
                .lineMapper((line, lineNumber) -> Integer.parseInt(line.trim()))
                .build();
    }

    /**
     * Processor : applique la transformation FooBarQuix
     */
    @Bean
    public ItemProcessor<Integer, String> processor(){
        log.info("Debut de transformation");
        return number -> number+ " " + fooBarQuixService.transform(number);
    }

    /**
     * Writer : écrit dans output.txt
     */
    @Bean
    @StepScope
    public FlatFileItemWriter<String> writer(@Value("${app.output.file:${java.io.tmpdir}output.txt}") String outputFile) {
        log.info("Debut d'ecriture dans le fichier output.txt");
        log.info(outputFile);
        return new FlatFileItemWriterBuilder<String>()
                .name("numberItemWriter")
                .resource(new FileSystemResource(outputFile))
                .append(false)
                .lineAggregator(item -> item)
                .build();
    }

    /**
     * Step
     */
    @Bean
    public Step fooBarQuixStep(FlatFileItemReader<Integer> reader, FlatFileItemWriter<String> writer, ItemProcessor<Integer, String> processor){
        return new StepBuilder("fooBarQuixStep", jobRepository)
                .<Integer, String>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    /**
     * Job
     */
    @Bean
    public Job fooBarQuixJob(FlatFileItemReader<Integer> reader, FlatFileItemWriter<String> writer, ItemProcessor<Integer, String> processor){
        return  new JobBuilder("fooBarQuixJob",jobRepository)
                .start(fooBarQuixStep(reader, writer,processor ))
                .build();
    }
}
