package com.car.service.CarsMicroservice;

import com.car.service.CarsMicroservice.Car;
import com.car.service.CarsMicroservice.CarServices;
import com.car.service.UserMicroservice.UserServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CarServerController {

  private final CarServices carServices;
  private final UserServices userServices;

  public CarServerController(CarServices carServices, UserServices userServices) {
    this.carServices = carServices;
    this.userServices = userServices;
  }

  @GetMapping("/getAll/{email}")
  public List<Car> getCarsOfOwner(@PathVariable("email") String ownerEmail) {
    return carServices.getCarsOfOwner(userServices.getUserByEmail(ownerEmail));
  }

  @PostMapping("/add")
  public Car addCar(@RequestBody Car car) {
    return carServices.putCar(car);
  }

  @PostMapping("/remove")
  public String removeCar(@RequestBody Car car) {
    carServices.removeCar(car);
    return "Success";
  }
}
