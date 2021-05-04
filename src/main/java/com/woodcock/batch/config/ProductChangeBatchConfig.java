package com.woodcock.batch.config;

import com.woodcock.batch.entity.Order;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ProductChangeBatchConfig {

  private static final String JOB_NAME = "processorNullBatch";
  private static final String BEAN_PREFIX = JOB_NAME + "_";

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory em;


  @Value("${chunkSize:1000}")
  private int chunkSize;

  @Bean(JOB_NAME)
  public Job job() {
    return jobBuilderFactory.get(JOB_NAME)
        .start(step())
        .build();
  }

  @Bean(BEAN_PREFIX)
  public Step step() {
    return stepBuilderFactory.get(BEAN_PREFIX + "step")
        .<Order, Order>chunk(chunkSize)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .build();
  }

  @Bean(BEAN_PREFIX + "reader")
  public ItemReader<Order> reader() {
    return new JpaPagingItemReaderBuilder<Order>()
        .name(BEAN_PREFIX + "reader")
        .entityManagerFactory(em)
        .pageSize(chunkSize)
        .queryString("SELECT o FROM Order o")
        .build();
  }

  private ItemProcessor<Order, Order> processor() {
    return order -> {
      order.increaseThousand();
      order.getProduct().increaseThousand();
      return order;
    };
  }

  private ItemWriter<Order> writer() {
    JpaItemWriter<Order> writer = new JpaItemWriter<>();
    writer.setEntityManagerFactory(em);
    return writer;
  }

}
