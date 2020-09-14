package com.car.user.components;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@RestController
@CrossOrigin

public class UserServerController {

  private final UserServices services;

  public UserServerController(UserServices services) {
    this.services = services;
  }

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

  @GetMapping("/testSecure")
  public List<User> getAll(@RequestHeader(name = "role") String role) throws Exception {
    if (role.equalsIgnoreCase("ROLE_customer"))
      return services.getAllUsers();
    else
      return new LinkedList<>();
  }

  //this method will interact with appointment-service
  @PostMapping("/bookAppointment/{id}/carservice")
  public ResponseEntity<Object> appointmentRequest(@PathVariable("id") int id, @RequestBody Job request) {
    if(services.findUser(id)==null) {
      return new ResponseEntity<>("register to continue", HttpStatus.BAD_REQUEST);
    }

    return services.saveAppointment(request,id);


  }
}
