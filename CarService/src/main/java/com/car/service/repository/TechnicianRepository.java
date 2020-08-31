package com.car.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.car.service.model.Technician;

public interface TechnicianRepository extends MongoRepository<Technician,Integer>{
	public Technician findByEmailAndPassword(String email,String password);

	public Technician findByEmail(String email);
}
