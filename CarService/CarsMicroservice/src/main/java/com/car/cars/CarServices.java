package com.car.service.CarsMicroservice;

import com.car.service.CarsMicroservice.Car;
import com.car.service.UserMicroservice.User;
import com.car.service.CarsMicroservice.CarRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarServices {

  public final CarRepository carRepository;

  public CarServices(CarRepository carRepository) {
    this.carRepository = carRepository;
  }

  public Car putCar(Car car) {
    return carRepository.save(car);
  }

  public Car getCarById(int id) {
    return carRepository.findById(id);
  }

  public List<Car> getCarsOfOwner(User user) {
    return carRepository.findCarsByOwnerId(user.getId());
  }

  public void removeCar(Car car) {
    carRepository.delete(car);
  }
}
