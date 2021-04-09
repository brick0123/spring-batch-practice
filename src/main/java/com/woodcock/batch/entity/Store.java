package com.woodcock.batch.entity;

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String address;

  @OneToMany(mappedBy = "store", cascade = ALL)
  private List<Product> products = new ArrayList<>();

  @OneToMany(mappedBy = "store", cascade = ALL)
  private List<Employee> employees = new ArrayList<>();

  public Store(String name, String address) {
    this.name = name;
    this.address = address;
  }

  public void addProduct(Product product){
    this.products.add(product);
    product.updateStore(this);
  }

  public void addEmployee(Employee employee){
    this.employees.add(employee);
    employee.updateStore(this);
  }
}