package com.car.jobs;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface JobRepository extends MongoRepository<Job, ObjectId> {
  Optional<Job> findById(ObjectId id);

  Page<Job> findAllByTechnicianId(ObjectId techId, Pageable pageable);

  Page<Job> findAllByCustomerId(ObjectId id, Pageable pageable);

  Page<Job> findAllBySupervisorId(ObjectId id, Pageable pageable);

  Page<Job> findAllByStatus(String status, Pageable pageable);
}
