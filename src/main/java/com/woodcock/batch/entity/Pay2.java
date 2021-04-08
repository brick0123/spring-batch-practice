package com.woodcock.batch.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Pay2 {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long amount;
  private String txName;
  private LocalDateTime txDateTime;

  public Pay2(Long amount, String txName, LocalDateTime txDateTime) {
    this.amount = amount;
    this.txName = txName;
    this.txDateTime = txDateTime;
  }

  public Pay2(Long id, Long amount, String txName, LocalDateTime txDateTime) {
    this.id = id;
    this.amount = amount;
    this.txName = txName;
    this.txDateTime = txDateTime;
  }
}

