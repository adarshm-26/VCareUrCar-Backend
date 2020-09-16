package com.car.cars;


import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarServices {

  public final CarRepository carRepository;

  public CarServices(CarRepository carRepository) {
    this.carRepository = carRepository;
  }

  //register car details of user
  public Car putCar(Car car) {
    return carRepository.save(car);
  }

  //get car bi id
  public Car getCarById(int id) {
    return carRepository.findById(id);
  }

  // list of all cars belongs to one user
  public List<Car> getCarsOfOwner(int ownerId) {
    return carRepository.findCarsByOwnerId(ownerId);
  }
  // delete car dtetails from user
  public void removeCar(Car car) {
    carRepository.delete(car);
  }
}
