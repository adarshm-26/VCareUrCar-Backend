package com.car.cars;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CarServices {

  public final CarRepository carRepository;
  private Logger logger = LoggerFactory.getLogger(CarServices.class);

  public CarServices(CarRepository carRepository) {
    this.carRepository = carRepository;
  }

  public Car putCar(Car car) {
    
    return carRepository.save(car);
  }

  public Car getCarById(ObjectId id) {
    logger.info("Fetched details of car " + id.toString());
    return carRepository.findById(id).orElse(null);
  }

  public Page<Car> getCarsOfOwner(ObjectId ownerId, Pageable pageable) {
    logger.info("Fetched page " + pageable.getPageNumber() + " of cars owned by " + ownerId.toString());
    return carRepository.findCarsByOwnerId(ownerId, pageable);
  }

  public void removeCar(ObjectId carId) {
    logger.info("Removing car " );
    carRepository.deleteById(carId);
  }
  public Page<Car> getAllCars(Pageable pageable){
    logger.info("Fetched all cars ");
    return carRepository.findAll(pageable);
  }
}
