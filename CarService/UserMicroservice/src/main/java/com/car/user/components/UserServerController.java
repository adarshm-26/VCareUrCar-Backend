package com.car.user.components;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin

public class UserServerController {

  private final UserServices services;

  public UserServerController(UserServices services) {
    this.services = services;
  }

  // 1.registration user and admin
  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody User user) throws Exception {

      String email = user.getEmail();
      if (email != null && !"".equals(email)) {
        User obj = services.getUserByEmail(email);
        if (obj != null) {
          throw new Exception("User already exists with " + email);
        }
      }

      // TODO -> Add validation of other fields

      User obj = services.putUser(user);
      if (obj != null) {
        return ResponseEntity.ok("registration success id: " + user.getId());
      }
      return ResponseEntity.badRequest().body("registration failed. try again!");

  }

  //for getting user details

  @GetMapping("/{userId}")
  public Optional<User> getUser(@RequestHeader(name = "role") String role ,@PathVariable int userId) throws Exception {
    if(role.equalsIgnoreCase("ROLE_admin")) {

      Optional<User> userObj = null;
      userObj = services.findUser(userId);
      if (userObj == null) {
        throw new Exception("user not exists");
      }
      return userObj;
    }else
      throw new Exception("unauthorized");
  }

  @GetMapping("/testSecure")
  public List<User> getAll(@RequestHeader(name = "role") String role) throws Exception {
    if (role.equalsIgnoreCase("ROLE_customer"))
      return services.getAllUsers();
    else
      return new LinkedList<>();
  }


}
