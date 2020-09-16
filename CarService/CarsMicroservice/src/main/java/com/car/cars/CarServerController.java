package com.car.cars;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CarServerController {

  private final CarServices carServices;

  public CarServerController(CarServices carServices) {
    this.carServices = carServices;
  }

  @GetMapping("/{userId}/{carId}")
  public Car getCarDetails(@RequestHeader(name = "role") String role,@PathVariable("userId") int userId,@PathVariable("carId") int carId) throws Exception {
    if(role.equalsIgnoreCase("ROLE_admin")) {
      Car car = null;
      car = carServices.getCarById(carId);
      if (car == null || car.getOwnerId() != userId) {
        throw new Exception("invalid car id or owner id");
      }
      return car;
    }throw new Exception("unauthorized");
  }

  @GetMapping("/{userId}")
  public List<Car> getUserCars(@RequestHeader(name = "role") String role,@PathVariable int userId) throws Exception {
    if(role.equalsIgnoreCase("ROLE_admin")) {
      List<Car> cars = null;
      cars = carServices.getCarsOfOwner(userId);
      if (cars == null) {
        throw new Exception("no cars found");
      }
      return cars;
    }throw new Exception("unauthorized");
  }



  @PostMapping("/add")
  public Car addCar(@RequestHeader(name = "role") String role,@RequestBody Car car) throws Exception {
    if(role.equalsIgnoreCase("ROLE_admin") || role.equalsIgnoreCase("ROLE_customer")){
      Car carObj=null;
      carObj= carServices.putCar(car);
      if(carObj==null){
        throw new Exception("something went wrong");
      }return carObj;
    }
    throw new Exception("unauthorized");

  }

  @PostMapping("/remove")
  public String removeCar(@RequestHeader(name = "role") String role,@RequestBody Car car) throws Exception {
    if (role.equalsIgnoreCase("ROLE_customer") || role.equalsIgnoreCase("ROLE_admin")) {
      carServices.removeCar(car);
      return "Success";
    }
    throw new Exception("unautorized");
  }

}
