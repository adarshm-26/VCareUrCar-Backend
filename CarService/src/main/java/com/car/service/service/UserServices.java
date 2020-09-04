package com.car.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.service.model.User;
import com.car.service.repository.UserRepository;

@Service
public class UserServices {

  @Autowired
  private UserRepository userRepository;

  // Save a User in the database
  public User putUser(User user) {
    return userRepository.save(user);
  }

  // Fetch User details using email
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  // Fetch All
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  // Remove a User from database
  public void removeUser(User user) {
    userRepository.delete(user);
  }
}
