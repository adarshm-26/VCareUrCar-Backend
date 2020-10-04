package com.car.user.components;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public Page<User> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  public Page<User> getAllUsersOfType(String type, Pageable pageable) {
    return userRepository.findAllByType(type, pageable);
  }

  // Remove a User from database
  public void removeUser(User user) {
    userRepository.delete(user);
  }

  //get user by id
  public User findUser(ObjectId id) {
    Optional<User> user = userRepository.findById(id);
    return user.orElse(null);
  }

}
