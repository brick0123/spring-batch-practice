package com.woodcock.batch.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.batch.core.BatchStatus.COMPLETED;

import com.woodcock.batch.TestBatchConfig;
import com.woodcock.batch.entity.Pay;
import com.woodcock.batch.repository.PayRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {JpaPagingItemReaderConfig.class, TestBatchConfig.class})
class JpaPagingItemReaderConfigTest {

  @Autowired
  JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  PayRepository payRepository;

  @Test
  void t1() throws Exception {

    long amount = 0L;
    for (int i = 0; i < 5; i++) {
      amount += 1000L;
      payRepository.save(new Pay(amount, "tx", "2021-04-07 23:18:20"));
    }
    JobExecution jobExecution = jobLauncherTestUtils.launchJob();

    assertThat(jobExecution.getStatus()).isEqualTo(COMPLETED);
  }

}