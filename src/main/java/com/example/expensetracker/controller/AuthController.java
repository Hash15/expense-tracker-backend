package com.example.expensetracker.controller;

import com.example.expensetracker.config.JwtTokenUtil;
import com.example.expensetracker.dto.*;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.service.UserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserService userService;
  private final AuthenticationManager authManager;
  private final JwtTokenUtil jwtTokenUtil;

  public AuthController(UserService userService, AuthenticationManager authManager, JwtTokenUtil jwtTokenUtil) {
    this.userService = userService;
    this.authManager = authManager;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
    User created = userService.register(
        req.getUsername(),
        req.getPassword(),
        req.getFullName()
    );

    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    response.put("message", "User registered successfully!");

    Map<String, Object> userData = new HashMap<>();
    userData.put("username", created.getUsername());
    userData.put("fullName", created.getFullName());
    userData.put("id", created.getId()); // if you have id

    response.put("user", userData);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequest req) {
    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
    String token = jwtTokenUtil.generateToken(req.getUsername());
    return ResponseEntity.ok(new AuthResponse(token));
  }
}
