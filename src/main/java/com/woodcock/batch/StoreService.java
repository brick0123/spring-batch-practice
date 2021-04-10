package com.woodcock.batch;

import com.woodcock.batch.entity.Employee;
import com.woodcock.batch.entity.Product;
import com.woodcock.batch.entity.Store;
import com.woodcock.batch.repository.StoreRepository;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StoreService {

  private final StoreRepository storeRepository;

  @Transactional(readOnly = true)
  public long find() {
    List<Store> stores = storeRepository.findAll();
    long productSum = stores.stream()
        .map(Store::getProducts)
        .flatMap(Collection::stream)
        .mapToLong(Product::getPrice)
        .sum();

    stores.stream()
        .map(Store::getEmployees)
        .flatMap(Collection::stream)
        .map(Employee::getName)
        .collect(Collectors.toList());

    return productSum;
  }

}
