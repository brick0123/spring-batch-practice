package com.woodcock.batch;

import static org.assertj.core.api.Assertions.assertThat;

import com.woodcock.batch.entity.Employee;
import com.woodcock.batch.entity.Product;
import com.woodcock.batch.entity.Store;
import com.woodcock.batch.repository.StoreRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StoreServiceTest {

  @Autowired
  StoreRepository storeRepository;

  @Autowired
  StoreService storeService;

  @Test
  @DisplayName("N+1 확인")
  void batch_size() {
    // given
    Store store1 = new Store("서점", "서울시 강남구");
    store1.addProduct(new Product("책1_1", 10000L));
    store1.addProduct(new Product("책1_2", 20000L));
    store1.addEmployee(new Employee("직원1", LocalDate.now()));
    store1.addEmployee(new Employee("직원2", LocalDate.now()));
    storeRepository.save(store1);

    Store store2 = new Store("서점2", "서울시 강남구");
    store2.addProduct(new Product("책2_1", 10000L));
    store2.addProduct(new Product("책2_2", 20000L));
    store2.addEmployee(new Employee("직원2_1", LocalDate.now()));
    store2.addEmployee(new Employee("직원2_2", LocalDate.now()));
    storeRepository.save(store2);

    // when
    long sum = storeService.find();

    // then
    assertThat(sum).isEqualTo(60000L);
  }

}