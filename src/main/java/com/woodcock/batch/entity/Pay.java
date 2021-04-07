package com.woodcock.batch.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pay {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long amount;
  private String txName;
  private LocalDateTime txDateTime;

  public Pay(Long amount, String txName, String txDateTime) {
    this.amount = amount;
    this.txName = txName;
    this.txDateTime = convertStringToDateTime(txDateTime);
  }

  public Pay(Long id, Long amount, String txName, String txDateTime) {
    this.id = id;
    this.amount = amount;
    this.txName = txName;
    this.txDateTime = convertStringToDateTime(txDateTime);
  }

  private LocalDateTime convertStringToDateTime(String txDateTime) {
    return LocalDateTime.parse(txDateTime, FORMATTER);
  }
}
