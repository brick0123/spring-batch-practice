package com.woodcock.batch.config;

import com.woodcock.batch.entity.Order;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UpdateJobConfig {

  private static final String JOB_NAME = "orderJob";
  private static final int CHUNK_SIZE = 10;

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory em;

  @Bean
  public Job setup() {
    return jobBuilderFactory.get(JOB_NAME)
        .start(orderStep())
        .build();
  }

  @Bean
  @JobScope
  public Step orderStep() {
    return stepBuilderFactory.get("orderStep")
        .<Order, Order>chunk(CHUNK_SIZE)
        .reader(orderReader())
        .writer(orderWriter())
        .build();
  }

  @Bean
  public JpaPagingItemReader<? extends Order> orderReader() {
    return new JpaPagingItemReaderBuilder<Order>()
        .name("orderReader")
        .entityManagerFactory(em)
        .pageSize(CHUNK_SIZE)
        .queryString("SELECT o FROM Order o")
        .build();
  }


  @Bean
  public ItemWriter<Order> orderWriter() {
    return list -> {
      for (Order order : list) {
        order.increaseThousand();
      }
    };
  }
}
