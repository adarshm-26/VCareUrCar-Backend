package com.car.cars;


import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CarRepository extends MongoRepository<Car,Integer> {

  public Car findById(int id);

  public List<Car> findCarsByOwnerId(int ownerId);
}
