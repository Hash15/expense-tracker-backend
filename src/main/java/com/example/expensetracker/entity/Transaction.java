package com.example.expensetracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Transaction {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String description;
  private Double amount; // positive for income, negative for expense
  private LocalDate date;

  @ManyToOne
  private Category category;

  @ManyToOne
  private User user;
}
