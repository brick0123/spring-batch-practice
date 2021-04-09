package com.woodcock.batch.config;

import com.woodcock.batch.entity.Store;
import com.woodcock.batch.entity.StoreHistory;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
//@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
public class StoreBackupBatchConfig {

  public static final String JOB_NAME = "storeBackupBatch";
  private static final String STEP_NAME = JOB_NAME+"Step";

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory em;

  @Value("${chunkSize:1000}")
  private int chunkSize;

  private static String ADDRESS_PARAM = null;

  @Bean
  public Job job() {
    return jobBuilderFactory.get(JOB_NAME)
        .start(step())
        .build();
  }

  @Bean
  @JobScope
  public Step step() {
    return stepBuilderFactory.get(STEP_NAME)
        .<Store, StoreHistory>chunk(chunkSize)
        .reader(reader(ADDRESS_PARAM))
        .processor(processor())
        .writer(writer())
        .build();
  }
  @Bean
  @StepScope
  public JpaPagingItemReader<Store> reader (
      @Value("#{jobParameters[address]}") String address) {

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("address", address+"%");

    JpaPagingItemReader<Store> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(em);
    reader.setQueryString("select s From Store s where s.address like :address");
    reader.setParameterValues(parameters);
    reader.setPageSize(chunkSize);

    return reader;
  }

  public ItemProcessor<Store, StoreHistory> processor() {
    return item -> new StoreHistory(item, item.getProducts(), item.getEmployees());
  }

  public JpaItemWriter<StoreHistory> writer() {
    JpaItemWriter<StoreHistory> writer = new JpaItemWriter<>();
    writer.setEntityManagerFactory(em);
    return writer;
  }

}
