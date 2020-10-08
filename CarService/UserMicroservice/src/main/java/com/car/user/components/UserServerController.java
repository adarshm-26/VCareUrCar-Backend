package com.car.user.components;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserServerController {

  private final UserServices services;

  public UserServerController(UserServices services) {
    this.services = services;
  }

  // 1.registration user and admin
  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody User user) {
    if (user.getType().equals("customer")) {
      User savedUser = services.putUser(user);
      if (savedUser != null) {
        return ResponseEntity.ok("Registration ID: " + user.getId());
      } else {
        return ResponseEntity.badRequest().body("Unable to register");
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/registerEmployee")
  public ResponseEntity<String> registerEmployee(@RequestHeader(name = "role") String role,
                                                 @RequestBody User user) {
    if (role.equals("ROLE_admin")) {
      User savedUser = services.putUser(user);
      if (savedUser != null) {
        return ResponseEntity.ok().body("Registration ID: " + savedUser.getId());
      } else {
        return ResponseEntity.badRequest().body("Unable to register");
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/updatePassword")
  public ResponseEntity<String> updatePassword(@RequestHeader(name = "role") String role,
                                               @RequestHeader(name = "id") String myId,
                                               @RequestBody User user) {
    if (role.equals("ROLE_admin") || (user.getId().equals(new ObjectId(myId)))) {
      User savedUser = services.findUser(user.getId());
      services.removeUser(savedUser);
      savedUser.setPassword(user.getPassword());
      services.putUser(savedUser);
      return ResponseEntity.ok().body("Updated password");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  //for getting user details
  @GetMapping("/{userId}")
  public ResponseEntity<?> getUser(@RequestHeader(name = "role") String role,
                                      @RequestHeader(name = "id") String myId,      // <-- Just to handle the "me" endpoint
                                      @PathVariable String userId) {
    if (role.equals("ROLE_admin") || userId.equalsIgnoreCase("me")) {
      ObjectId userIdInt = new ObjectId(userId.equalsIgnoreCase("me") ?
                                          myId :
                                          userId);
      User user = services.findUser(userIdInt);
      if (user != null) {
        return ResponseEntity.ok().body(user);
      } else {
        return ResponseEntity.unprocessableEntity().build();
      }
    } else if (!role.equals("")){
      ObjectId userObjId = new ObjectId(userId);
      User user = services.findUser(userObjId);
      Map<String,String> map = new HashMap();
      map.put("name", user.getName());
      return ResponseEntity.ok().body(map);
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
    if (role.equals("ROLE_admin") ||
        (role.equals("ROLE_supervisor") && type.equalsIgnoreCase("technician"))) {
      Page<User> resultPage = services.getAllUsersOfType(type, pageable);
      return ResponseEntity.ok().body(resultPage);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
