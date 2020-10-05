package com.car.cars;


import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends MongoRepository<Car, ObjectId> {

  Optional<Car> findById(ObjectId id);
  Page<Car> findCarsByOwnerId(ObjectId ownerId, Pageable pageable);
}
