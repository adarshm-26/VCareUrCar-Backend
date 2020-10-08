package com.car.cars;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarServerController {

  private final CarServices carServices;
  private final Logger logger = LoggerFactory.getLogger(CarServerController.class);

  public CarServerController(CarServices carServices) {
    this.carServices = carServices;
  }

  @GetMapping("/{carId}")
  public ResponseEntity<Car> getCarDetails(@RequestHeader(name = "role") String role,
                                           @RequestHeader(name = "id") String myId,
                                           @PathVariable("carId") String carId) throws Exception {
    logger.info("Fetching carId: " + carId + " for " + role);
    Car car = carServices.getCarById(new ObjectId(carId));
    if (!role.equalsIgnoreCase("ROLE_customer") ||
        role.equalsIgnoreCase("ROLE_customer") && car.getOwnerId().equals(new ObjectId(myId))) {
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
  public ResponseEntity<Page<Car>> getUserCars(@RequestHeader(name = "role") String role,
                                               @RequestHeader(name = "id") String myId,
                                               @PathVariable String userId,
                                               @SortDefault.SortDefaults({
                                                   @SortDefault(sort = "id", direction = Sort.Direction.DESC)
                                               }) Pageable pageable) throws Exception {
    if(role.equalsIgnoreCase("ROLE_admin") || userId.equalsIgnoreCase("my")) {
      ObjectId userIdInt = new ObjectId(userId.equalsIgnoreCase("my") ?
                                          myId :
                                          userId);
      Page<Car> cars = carServices.getCarsOfOwner(userIdInt, pageable);
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
        (role.equalsIgnoreCase("ROLE_customer") && car.getOwnerId().equals(new ObjectId(myId)))) {
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
        (role.equalsIgnoreCase("ROLE_admin") && car.getOwnerId().equals(new ObjectId(myId)))) {
      carServices.removeCar(car);
      return ResponseEntity.accepted().body("Removed car with id: " + car.getId());
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
