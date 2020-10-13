package com.car.user.components;

import com.car.user.components.emailservice.EmailService;
import com.car.user.components.emailservice.MailRequest;
import com.car.user.components.emailservice.MailResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserServerController {
  @Autowired
  private UserServices services;

  @Autowired
  private EmailService emailService;



  @PostMapping("/validatemail")
  public ResponseEntity<String> validateEmail(@RequestHeader(name = "id") String myId,@RequestBody User user){
    User user1=null;
    user1=services.findUser(new ObjectId(myId));
    if(user1!=null){
      if(user.getVerificationcode()==user1.getVerificationcode()){
        services.updateEnable(new ObjectId(myId));
        return ResponseEntity.ok("verified Registration ID: " + user1.getId());
      }else{
        return ResponseEntity.badRequest().body("entered wrong");
      }
    }else{
      return ResponseEntity.badRequest().body("user not found"+user1.getEmail());
    }
  }

  @PostMapping("/sendmail")
  public ResponseEntity<String> sendEmail(@RequestHeader(name = "id") String myId){
    User user=null;
    user=services.findUser(new ObjectId(myId));
    if(user!=null) {
      Map<String, Object> model = new HashMap<>();
	  int code=(int)Math.floor(Math.random()*100000);
	  user=services.updateCode(code,new ObjectId(myId));
      MailRequest mailRequest = new MailRequest();
      mailRequest.setName("vcareurcar");
      mailRequest.setFrom("vacareurcar@gmail.com");
      mailRequest.setSubject("verify your email");
      mailRequest.setTo(user.getEmail());
      model.put("Name", mailRequest.getName());
      model.put("code", ""+code);
      model.put("location", "team vcareurcar");

      MailResponse mailResponse = emailService.sendEmail(mailRequest, model);
      return ResponseEntity.ok("sent mail to email ID: " + user.getEmail());
    }else{
      return ResponseEntity.badRequest().body("user not found");
    }
  }

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
      services.updatePassword(user.getId(), user.getPassword());
      return ResponseEntity.ok().body("Updated password");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  //for getting user details
  @GetMapping("/{userId}")
  public ResponseEntity<?> getUser(@RequestHeader(name = "role") String role,
                                   @RequestHeader(name = "id") String myId,
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
    } else if (!role.equals("ROLE_customer")){
      ObjectId userObjId = new ObjectId(userId);
      User user = services.findUser(userObjId);
      Map<String,String> map = new HashMap<>();
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

  @PostMapping("/remove")
  public ResponseEntity<String> removeUser(@RequestHeader(name = "role") String role,
                                           @RequestBody String id) {
    if (role.equals("ROLE_admin")) {
      services.removeUser(new ObjectId(id));
      return ResponseEntity.ok().body("Successfully removed user");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
