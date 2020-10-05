package com.car.cars;


import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public Car getCarById(ObjectId id) {
    return carRepository.findById(id).orElse(null);
  }

  // list of all cars belongs to one user
  public Page<Car> getCarsOfOwner(ObjectId ownerId, Pageable pageable) {
    return carRepository.findCarsByOwnerId(ownerId, pageable);
  }
  // delete car dtetails from user
  public void removeCar(Car car) {
    carRepository.delete(car);
  }
}
