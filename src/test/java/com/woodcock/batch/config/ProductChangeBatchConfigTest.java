package com.woodcock.batch.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.woodcock.batch.TestBatchConfig;
import com.woodcock.batch.entity.Order;
import com.woodcock.batch.entity.Product;
import com.woodcock.batch.repository.OrderRepository;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {ProductChangeBatchConfig.class, TestBatchConfig.class})
class ProductChangeBatchConfigTest {

  @Autowired
  JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  EntityManager em;

  @Test
  void update() throws Exception {

    for(int i = 0; i < 10; i++) {
      Product product = new Product(1000);

      Order order = new Order(1000);
      order.updateProduct(product);
      orderRepository.save(order);
    }
    
    JobExecution jobExecution = jobLauncherTestUtils.launchJob();

    List<Order> orders = orderRepository.findAll();

    assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

    for (Order order : orders) {
      System.out.println("order.getAmount() = " + order.getAmount());
      System.out.println("order.getProduct().getPrice() = " + order.getProduct().getPrice());
    }
  }

}