package com.woodcock.batch.repository;

import com.woodcock.batch.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Long> {

}
