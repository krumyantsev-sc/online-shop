package com.scand.bookshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sale_stats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleStat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "stat_id")
  private Long statId;

  @Column(name = "amount_of_orders", nullable = false)
  private Integer amount;

  @Column(name = "sum", nullable = false)
  private Double sum;

  @Column(name = "date", nullable = false)
  private LocalDate date;
}
