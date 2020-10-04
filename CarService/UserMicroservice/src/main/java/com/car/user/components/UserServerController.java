package com.car.user.components;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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
  public ResponseEntity<User> getUser(@RequestHeader(name = "role") String role,
                                      @RequestHeader(name = "id") String myId,      // <-- Just to handle the "me" endpoint
                                      @PathVariable String userId) throws Exception {
    if (role.equalsIgnoreCase("ROLE_admin") || userId.equalsIgnoreCase("me")) {
      try {
        ObjectId userIdInt = new ObjectId(userId.equalsIgnoreCase("me") ?
                                            myId :
                                            userId);
        User user = services.findUser(userIdInt);
        if (user != null) {
          return ResponseEntity.ok().body(user);
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

  @GetMapping("/all")
  public ResponseEntity<Page<User>> getAll(@RequestHeader(name = "role") String role,
                                           @RequestHeader(name = "id") String myId,
                                           @RequestParam(name = "type", defaultValue = "customer") String type,
                                           @SortDefault.SortDefaults({
                                               @SortDefault(sort = "name", direction = Sort.Direction.ASC)
                                           }) Pageable pageable) {
    if (role.equalsIgnoreCase("ROLE_admin")) {
      Page<User> resultPage = services.getAllUsersOfType(type, pageable);
      return ResponseEntity.ok().body(resultPage);
    } else if (role.equalsIgnoreCase("ROLE_supervisor")) {
      Page<User> resultPage = services.getAllUsersOfType("technician", pageable);
      return ResponseEntity.ok().body(resultPage);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
