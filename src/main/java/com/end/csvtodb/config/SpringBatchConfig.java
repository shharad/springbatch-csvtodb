package com.end.csvtodb.config;

import com.end.csvtodb.entity.RssService;
import com.end.csvtodb.repository.RssServiceRepository;
import com.end.csvtodb.util.AppProperties;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

    private RssServiceRepository rssServiceRepository;

    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;

    private AppProperties appProperties;
    //@Value("${files.input.path}")
    //String fileInputPath;

    @Bean
    public FlatFileItemReader<RssService> reader() {
        FlatFileItemReader<RssService> itemReader = new FlatFileItemReader<>();
        //itemReader.setResource(new FileSystemResource("src/main/resources/T_RSS_SERVICE_TST.csv"));
        itemReader.setResource(new FileSystemResource(appProperties.getFileInputPath() + File.separator + "T_RSS_SERVICE_TST.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());

        return itemReader;
    }

    private LineMapper<RssService> lineMapper() {
        DefaultLineMapper<RssService> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("SERVICE_ID", "SERVICE_NAME", "SERVICE_TYPE", "SOURCE_SYSTEM", "STATUS", "REMARK");

        BeanWrapperFieldSetMapper<RssService> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(RssService.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public RssServiceProcessor processor() {
        return new RssServiceProcessor();
    }

    @Bean
    public RepositoryItemWriter<RssService> writer() {

        RepositoryItemWriter<RssService> writer = new RepositoryItemWriter<>();
        writer.setRepository(rssServiceRepository);
        writer.setMethodName("save");

        return writer;
    }

    @Bean
    public Step step1() {

        return new StepBuilder("csv-step", jobRepository)
                .<RssService, RssService>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob() {

        return new JobBuilder("import-data-from-CSV-to-DB", jobRepository)
                .flow(step1())
                .end().build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(10); // 10 Threads will execute in parallel
        return simpleAsyncTaskExecutor;
    }


}
