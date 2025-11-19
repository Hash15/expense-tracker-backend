package com.example.expensetracker.dto;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AuthRequest {
  private String username;
  private String password;
}
