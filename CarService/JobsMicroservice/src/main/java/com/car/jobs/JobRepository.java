package com.car.jobs;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobRepository extends MongoRepository<Job,Integer> {
  public Job findById(int id);

  public List<Job> findAllByStatus(boolean b);

  public List<Job> findAllByTechnicianId(int techId);
}
