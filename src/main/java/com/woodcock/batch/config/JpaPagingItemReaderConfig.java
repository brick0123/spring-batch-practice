package com.woodcock.batch.config;

import com.woodcock.batch.entity.Pay;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JpaPagingItemReaderConfig {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory em;

  private static final String JOB_NAME = "jpaPagingItemReaderJob";
  private static final int CHUNK_SIZE = 10;

  @Bean
  public Job jpaPagingItemReaderJob() {
    return jobBuilderFactory.get(JOB_NAME)
        .start(jpaPagingItemReaderStep())
        .build();
  }

  @Bean
  public Step jpaPagingItemReaderStep() {
    return stepBuilderFactory.get("jpaPagingItemReaderStep")
        .<Pay, Pay>chunk(CHUNK_SIZE)
        .reader(jpaPagingItemReader())
        .writer(jpaPagingItemWriter())
        .build();
  }

  @Bean
  public JpaPagingItemReader<Pay> jpaPagingItemReader() {
    return new JpaPagingItemReaderBuilder<Pay>()
        .name("jpaPagingItemReader")
        .entityManagerFactory(em)
        .pageSize(CHUNK_SIZE)
        .queryString("SELECT p FROM Pay p WHERE amount >= 2000")
        .build();
  }

  private ItemWriter<Pay> jpaPagingItemWriter() {
    return list -> {
      for (Pay pay: list) {
        log.info("Current Pay={}", pay);
      }
    };
  }


}
