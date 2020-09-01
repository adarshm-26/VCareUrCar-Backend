package com.car.service.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.car.service.model.User;
import com.car.service.service.UserService;

import java.util.List;


@RestController
public class CarServiceController {

  @Autowired
  private UserService uservice;

  @PostMapping("/register")
  public String register(@RequestBody User user) throws Exception {

    String email = user.getEmail();
    if (email != null && !"".equals(email)) {
      User obj = null;
      obj = uservice.getUserByEmail(email);
      if (obj != null) {
        throw new Exception("User already exists with " + email);
      }
    }

    // TODO -> Add validation of other fields

    User obj = uservice.saveUser(user);
    if (obj != null)
      return "registration success id: " + user.getId();
    else
      return "registration failed. try again!";
  }

  @PostMapping("/login")
  public String login(@RequestBody User user) throws Exception {

    String email = user.getEmail();
    String password = user.getPassword();

    // TODO -> Change string return to Status Codes ??

    if (email != null && password != null) {
      User userObj = uservice.getUserByEmail(email);
      if (userObj.getPassword().compareTo(password) == 0)
        return "Success";
      else
        return "Incorrect Password";
    }
    else
      return "Invalid user";
  }
}
