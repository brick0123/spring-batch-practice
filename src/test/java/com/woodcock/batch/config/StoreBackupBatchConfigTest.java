package com.woodcock.batch.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.woodcock.batch.TestBatchConfig;
import com.woodcock.batch.entity.Employee;
import com.woodcock.batch.entity.Product;
import com.woodcock.batch.entity.Store;
import com.woodcock.batch.repository.StoreHistoryRepository;
import com.woodcock.batch.repository.StoreRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {StoreBackupBatchConfig.class, TestBatchConfig.class})
class StoreBackupBatchConfigTest {

  @Autowired
  JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  StoreRepository storeRepository;

  @Autowired
  StoreHistoryRepository storeHistoryRepository;

  @Test
  @Disabled("n+1트 테스트")
  void batch_test() throws Exception {
    // given
    Store store1 = new Store("서점", "서울시 강남구");
    store1.addProduct(new Product("책1_1", 10000L));
    store1.addProduct(new Product("책1_2", 20000L));
    store1.addEmployee(new Employee("직원1", LocalDate.now()));
    storeRepository.save(store1);

    Store store2 = new Store("서점2", "서울시 강남구");
    store2.addProduct(new Product("책2_1", 10000L));
    store2.addProduct(new Product("책2_2", 20000L));
    store2.addEmployee(new Employee("직원2", LocalDate.now()));
    storeRepository.save(store2);

    Store store3 = new Store("서점3", "서울시 강남구");
    store3.addProduct(new Product("책3_1", 10000L));
    store3.addProduct(new Product("책3_2", 20000L));
    store3.addEmployee(new Employee("직원3", LocalDate.now()));
    storeRepository.save(store3);

    // when
    JobParameters jobParameters = new JobParametersBuilder()
        .addString("address", "서울")
        .toJobParameters();

    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    // then
    assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);


  }

}