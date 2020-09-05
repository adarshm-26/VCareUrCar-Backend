package com.car.service.repository;

import com.car.service.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarRepository extends MongoRepository<Car,Integer> {
  public Car findById(int id);
}
