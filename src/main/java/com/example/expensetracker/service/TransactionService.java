package com.example.expensetracker.service;

import com.example.expensetracker.entity.*;
import com.example.expensetracker.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;

  public TransactionService(TransactionRepository transactionRepository,
      UserRepository userRepository,
      CategoryRepository categoryRepository) {
    this.transactionRepository = transactionRepository;
    this.userRepository = userRepository;
    this.categoryRepository = categoryRepository;
  }

  public Transaction addTransaction(String username, Transaction tx) {
    User user = userRepository.findByUsername(username).orElseThrow();
    tx.setUser(user);
    if (tx.getDate() == null) tx.setDate(LocalDate.now());
    return transactionRepository.save(tx);
  }

  public List<Transaction> getTransactionsForUser(String username) {
    User user = userRepository.findByUsername(username).orElseThrow();
    return transactionRepository.findByUserOrderByDateDesc(user);
  }

  // update & delete...
  public void deleteTransaction(Long id, String username) {
    Transaction tx = transactionRepository.findById(id).orElseThrow();
    if (!tx.getUser().getUsername().equals(username)) throw new RuntimeException("Not allowed");
    transactionRepository.deleteById(id);
  }
}
