package com.woodcock.batch.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private LocalDate hireDate;

  @ManyToOne
  @JoinColumn(name = "store_id", foreignKey = @ForeignKey(name = "FK_EMPLOYEE_STORE"))
  private Store store;

  public Employee(String name, LocalDate hireDate) {
    this.name = name;
    this.hireDate = hireDate;
  }

  public void updateStore(Store store){
    this.store = store;
  }
}