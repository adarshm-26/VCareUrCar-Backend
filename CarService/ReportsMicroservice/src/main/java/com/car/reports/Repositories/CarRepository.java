package com.car.reports.Repositories;

import com.car.reports.Entities.Car;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CarRepository extends MongoRepository<Car, ObjectId> {

  Optional<Car> findById(ObjectId id);

  Page<Car> findCarsByOwnerId(ObjectId ownerId, Pageable pageable);

  void deleteById(ObjectId id);
}
