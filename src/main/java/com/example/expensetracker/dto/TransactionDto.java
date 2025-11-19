package com.example.expensetracker.dto;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TransactionDto {
  private Long id;
  private String description;
  private Double amount;
  private LocalDate date;
  private Long categoryId;
  private String categoryName;
}
