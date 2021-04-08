package com.woodcock.batch.config;

import com.woodcock.batch.entity.Pay;
import com.woodcock.batch.entity.Pay2;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaItemWriterJobConfig {

  private static final int CHUNK_SIZE = 10;

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory em;

  @Bean
  public Job jpaItemWriterJob() {
    return jobBuilderFactory.get("jpaItemWriterJob")
        .start(jpaItemWriterStep())
        .build();
  }

  @Bean
  public Step jpaItemWriterStep() {
    return stepBuilderFactory.get("jpaItemWriterStep")
        .<Pay, Pay2>chunk(CHUNK_SIZE)
        .reader(jpaItemWriterReader())
        .processor(jpaItemWriterProcessor())
        .writer(jpaItemWriter())
        .build();
  }

  @Bean
  public JpaPagingItemReader<Pay> jpaItemWriterReader() {
    return new JpaPagingItemReaderBuilder<Pay>()
        .name("jpaItemWriterReader")
        .entityManagerFactory(em)
        .pageSize(CHUNK_SIZE)
        .queryString("SELECT p FROM Pay p")
        .build();
  }

  @Bean
  public ItemProcessor<Pay, Pay2> jpaItemWriterProcessor() {
    return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
  }


  @Bean
  public JpaItemWriter<Pay2> jpaItemWriter() {
    JpaItemWriter<Pay2> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(em);
    return jpaItemWriter;
  }

}
