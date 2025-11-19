package com.example.expensetracker.service;

import com.example.expensetracker.entity.User;
import com.example.expensetracker.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder encoder;

  public UserService(UserRepository userRepository, PasswordEncoder encoder) {
    this.userRepository = userRepository;
    this.encoder = encoder;
  }

  public User register(String username, String rawPassword, String fullName) {
    if (userRepository.existsByUsername(username)) {
      throw new RuntimeException("Username already exists");
    }
    User user = new User();
    user.setUsername(username);
    user.setPassword(encoder.encode(rawPassword));
    user.setFullName(fullName);
    return userRepository.save(user);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  // For Spring Security
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User u = userRepository.findByUsername(username).orElseThrow(() ->
        new UsernameNotFoundException("User not found"));
    return new org.springframework.security.core.userdetails.User(
        u.getUsername(),
        u.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority("USER"))
    );
  }

}
