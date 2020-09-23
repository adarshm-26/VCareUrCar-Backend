package com.car.cars;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CarServerController {

  private final CarServices carServices;

  public CarServerController(CarServices carServices) {
    this.carServices = carServices;
  }

  @GetMapping("/{carId}")
  public ResponseEntity<Car> getCarDetails(@RequestHeader(name = "role") String role,
                                           @RequestHeader(name = "id") String myId,
                                           @PathVariable("carId") int carId) throws Exception {
    int userIdInt = Integer.parseInt(myId);
    Car car = carServices.getCarById(carId);
    if (role.equalsIgnoreCase("ROLE_admin") ||
        role.equalsIgnoreCase("ROLE_customer") && car.getOwnerId() == userIdInt) {
        if (car != null) {
          return ResponseEntity.accepted().body(car);
        } else {
          return ResponseEntity.badRequest().build();
        }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @GetMapping("/byUser/{userId}")
  public ResponseEntity<List<Car>> getUserCars(@RequestHeader(name = "role") String role,
                               @RequestHeader(name = "id") String myId,
                               @PathVariable String userId) throws Exception {
    if(role.equalsIgnoreCase("ROLE_admin") || userId.equalsIgnoreCase("my")) {
      int userIdInt = Integer.parseInt(userId.equalsIgnoreCase("my") ?
                                          myId :
                                          userId);
      List<Car> cars = carServices.getCarsOfOwner(userIdInt);
      if (cars != null) {
        return ResponseEntity.accepted().body(cars);
      } else {
        return ResponseEntity.unprocessableEntity().build();
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/add")
  public ResponseEntity<String> addCar(@RequestHeader(name = "role") String role,
                                       @RequestHeader(name = "id") String myId,
                                       @RequestBody Car car) throws Exception {
    if(role.equalsIgnoreCase("ROLE_admin") ||
        (role.equalsIgnoreCase("ROLE_customer") && car.getOwnerId() == Integer.parseInt(myId))) {
      Car carObj = carServices.putCar(car);
      if(carObj != null){
        return ResponseEntity.accepted().body("Added car with id: " + carObj.getId());
      } else {
        return ResponseEntity.badRequest().build();
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/remove")
  public ResponseEntity<String> removeCar(@RequestHeader(name = "role") String role,
                                          @RequestHeader(name = "id") String myId,
                                          @RequestBody Car car) throws Exception {
    if (role.equalsIgnoreCase("ROLE_customer") ||
        (role.equalsIgnoreCase("ROLE_admin") && car.getOwnerId() == Integer.parseInt(myId))) {
      carServices.removeCar(car);
      return ResponseEntity.accepted().body("Removed car with id: " + car.getId());
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
