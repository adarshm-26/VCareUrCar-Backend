package com.car.service.repository;

import com.car.service.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<Job,Integer> {
  public Job findById(int id);
}
