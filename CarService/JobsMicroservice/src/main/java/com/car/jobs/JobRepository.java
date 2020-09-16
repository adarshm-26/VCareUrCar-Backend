package com.car.jobs;


import com.car.jobs.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobRepository extends MongoRepository<Job,Integer> {
  public Job findById(int id);


  public List<Job> findAllByTechnicianId(int techId);


  List<Job> findAllByCustomerId(int id);

  List<Job> findAllBySupervisorId(int id);
}
