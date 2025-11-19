package com.example.expensetracker.util;

import com.example.expensetracker.entity.*;
import com.example.expensetracker.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

  private final UserRepository userRepo;
  private final CategoryRepository categoryRepo;
  private final TransactionRepository txRepo;
  private final PasswordEncoder encoder;

  public DataLoader(UserRepository userRepo, CategoryRepository categoryRepo, TransactionRepository txRepo, PasswordEncoder encoder) {
    this.userRepo = userRepo;
    this.categoryRepo = categoryRepo;
    this.txRepo = txRepo;
    this.encoder = encoder;
  }

  @Override
  public void run(String... args) throws Exception {
    // create demo user if not present
    if (!userRepo.existsByUsername("demo")) {
      User u = new User();
      u.setUsername("demo");
      u.setPassword(encoder.encode("demo123"));
      u.setFullName("Demo User");
      userRepo.save(u);

      Category food = categoryRepo.save(new Category(null, "Food"));
      Category salary = categoryRepo.save(new Category(null, "Salary"));

      txRepo.save(new Transaction(null, "Lunch", -250.0, LocalDate.now().minusDays(1), food, u));
      txRepo.save(new Transaction(null, "Salary", 50000.0, LocalDate.now().minusDays(3), salary, u));
    }
  }
}
