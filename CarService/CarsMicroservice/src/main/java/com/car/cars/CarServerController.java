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
import java.util.Properties;

@RestController
public class CarServerController {

  private final CarServices carServices;

  public CarServerController(CarServices carServices) {
    this.carServices = carServices;
  }

  @GetMapping("/{carId}")
  public ResponseEntity<Car> getCarDetails(@RequestHeader(name = "role") String role,
                                           @RequestHeader(name = "id") String myId,
                                           @PathVariable("carId") String carId) throws Exception {
    Car car = carServices.getCarById(new ObjectId(carId));
    if (!role.equalsIgnoreCase("ROLE_customer") ||
        role.equalsIgnoreCase("ROLE_customer") && car.getOwnerId().equals(new ObjectId(myId))) {
        if (car != null) {
          return ResponseEntity.ok().body(car);
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
        return ResponseEntity.ok().body(cars);
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
        return ResponseEntity.ok().body("Added car with id: " + carObj.getId());
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
                                          @RequestBody Properties carProps) throws Exception {
    ObjectId carObjId = new ObjectId(carProps.getProperty("id"));
    if (role.equalsIgnoreCase("ROLE_customer") ||
        (role.equalsIgnoreCase("ROLE_admin") && carObjId.equals(new ObjectId(myId)))) {
      carServices.removeCar(carObjId);
      return ResponseEntity.ok().body("Removed car with id: " + carObjId.toString());
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
  @GetMapping("/allcars")
  public ResponseEntity<Page<Car>> getAllCars(@RequestHeader(name = "role") String role,
                                               @RequestHeader(name = "id") String myId,
                                               @SortDefault.SortDefaults({
                                                       @SortDefault(sort = "id", direction = Sort.Direction.DESC)
                                               }) Pageable pageable) throws Exception {
    if(!role.equalsIgnoreCase("ROLE_customer") ) {
      ObjectId userIdInt = new ObjectId(myId);
      Page<Car> cars = carServices.getAllCars();
      if (cars != null) {
        return ResponseEntity.ok().body(cars);
      } else {
        return ResponseEntity.unprocessableEntity().build();
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
