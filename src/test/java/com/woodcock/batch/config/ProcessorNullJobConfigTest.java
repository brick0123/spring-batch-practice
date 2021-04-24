package com.woodcock.batch.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.woodcock.batch.TestBatchConfig;
import com.woodcock.batch.entity.Order;
import com.woodcock.batch.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class, IncreaseAmountJobConfig.class})
class ProcessorNullJobConfigTest {

  @Autowired
  JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  OrderRepository orderRepository;

  @Test
  void inset() throws Exception {
    for (int i = 0; i < 10; i++) {
      orderRepository.save(new Order(1000));
    }
    JobExecution jobExecution = jobLauncherTestUtils.launchJob();

    assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    assertThat(orderRepository.findTwoThousand().size()).isEqualTo(10);
  }

}