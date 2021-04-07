package com.woodcock.batch.repository;

import com.woodcock.batch.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {


  @Query("select o from Order o where o.amount = 2000")
  List<Order> findTwoThousand();
}
