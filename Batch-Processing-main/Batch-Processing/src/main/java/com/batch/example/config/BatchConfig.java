package com.batch.example.config;

import com.batch.example.model.Products1;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Bean
    public Job jobBean(JobRepository jobRepository,
                       JobCompletionNotificationImpl listener,
                       Step steps) {
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(steps)
                .build();  // Ensure .preventRestart() is removed so the job is restartable
    }


    @Bean
    public Step steps(
            JobRepository jobRepository,
            DataSourceTransactionManager transactionManager,  // Updated with transactionManager
            ItemReader<Products1> reader,
            ItemProcessor<Products1, Products1> processor,
            ItemWriter<Products1> writer
    ) {
        return new StepBuilder("jobStep", jobRepository)
                .<Products1, Products1>chunk(25, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<Products1> reader() {
        return new FlatFileItemReaderBuilder<Products1>()
                .name("itemReader")
                .resource(new ClassPathResource("data.csv"))
                .delimited()
                .names("productId", "title", "description", "price", "discount")
                .targetType(Products1.class)
                .build();
    }

    @Bean
    public ItemProcessor<Products1, Products1> itemProcessor() {
        return new CustomItemProcessor();
    }

    @Bean
    public ItemWriter<Products1> itemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Products1>()
                .sql("insert into products1(product_id,title,description,price,discount,discounted_price)values(:productId, :title, :description, :price, :discount, :discountedPrice)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    // Add this transactionManager bean definition
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
