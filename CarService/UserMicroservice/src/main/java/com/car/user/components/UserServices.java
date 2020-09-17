package com.car.user.components;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
  @Autowired
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public UserServices(UserRepository userRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  // Save a User in the database
  public User putUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
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

  //get user by id
  public User findUser(int id) {
    return userRepository.findById(id);
  }

}
