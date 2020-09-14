package com.car.user.components;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServices {
  @Autowired
  private RestTemplate template;
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
  public Optional<User> findUser(int id) {
    return userRepository.findById(id);
  }
  public ResponseEntity<Object> saveAppointment(Job request, int id) {
    request.setUserId(id);
    SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date=new Date();
    request.setBookingDate(date);
    request.setStatus(false);
    Job appointment=null;
    appointment=template.postForObject( "http://APPOINTMENT-SERVICE/bookAppointment", request, Job.class);
    if(appointment==null)
      return new ResponseEntity<>("failed to register",HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>("success", HttpStatus.OK);
  }
}
