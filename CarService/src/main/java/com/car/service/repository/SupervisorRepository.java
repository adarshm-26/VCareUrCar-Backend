package com.car.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.car.service.model.Supervisor;

public interface SupervisorRepository extends MongoRepository<Supervisor,Integer>{

	public Supervisor findByEmailAndPassword(String email, String password);

	public Supervisor findByEmail(String email);

}
