package com.example.expensetracker.controller;

import com.example.expensetracker.dto.TransactionDto;
import com.example.expensetracker.entity.*;
import com.example.expensetracker.repository.CategoryRepository;
import com.example.expensetracker.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
  private final TransactionService txService;
  private final CategoryRepository categoryRepository;

  public TransactionController(TransactionService txService, CategoryRepository categoryRepository) {
    this.txService = txService;
    this.categoryRepository = categoryRepository;
  }

  @GetMapping
  public List<TransactionDto> list(Authentication auth) {
    String username = auth.getName();
    List<Transaction> txs = txService.getTransactionsForUser(username);
    return txs.stream().map(this::toDto).collect(Collectors.toList());
  }

  @PostMapping
  public TransactionDto add(@RequestBody TransactionDto dto, Authentication auth) {
    Transaction tx = new Transaction();
    tx.setDescription(dto.getDescription());
    tx.setAmount(dto.getAmount());
    tx.setDate(dto.getDate() == null ? java.time.LocalDate.now() : dto.getDate());
    if (dto.getCategoryId() != null) {
      Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
      tx.setCategory(category);
    }
    Transaction saved = txService.addTransaction(auth.getName(), tx);
    return toDto(saved);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
    txService.deleteTransaction(id, auth.getName());
    return ResponseEntity.ok().build();
  }

  private TransactionDto toDto(Transaction t) {
    TransactionDto d = new TransactionDto();
    d.setId(t.getId());
    d.setAmount(t.getAmount());
    d.setDescription(t.getDescription());
    d.setDate(t.getDate());
    if (t.getCategory() != null) {
      d.setCategoryId(t.getCategory().getId());
      d.setCategoryName(t.getCategory().getName());
    }
    return d;
  }
}
