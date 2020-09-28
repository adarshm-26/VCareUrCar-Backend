package com.car.user.components;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;

@RestController
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
      User obj = services.putUser(user);
      if (obj != null) {
        return ResponseEntity.ok("Registration ID: " + user.getId());
      }
      return ResponseEntity.badRequest().body("Unable to register");
  }

  //for getting user details
  @GetMapping("/{userId}")
  public ResponseEntity<User> getUser(@RequestHeader(name = "role") String role ,
                                      @RequestHeader(name = "id") String myId,      // <-- Just to handle the "me" endpoint
                                      @PathVariable String userId) throws Exception {
    if (role.equalsIgnoreCase("ROLE_admin") || userId.equalsIgnoreCase("me")) {
      try {
        int userIdInt = Integer.parseInt(userId.equalsIgnoreCase("me") ?
                                            myId :
                                            userId);
        User user = services.findUser(userIdInt);
        if (user != null) {
          return ResponseEntity.accepted().body(user);
        } else {
          return ResponseEntity.unprocessableEntity().build();
        }
      } catch (NumberFormatException e) {
        return ResponseEntity.unprocessableEntity().build();
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  // TODO -> To implement correctly
  @GetMapping("/testSecure")
  public List<User> getAll(@RequestHeader(name = "role") String role) throws Exception {
    if (role.equalsIgnoreCase("ROLE_customer"))
      return services.getAllUsers();
    else
      return new LinkedList<>();
  }
}
