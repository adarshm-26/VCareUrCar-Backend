package com.car.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.car.service.model.Admin;

public interface AdminRepository extends MongoRepository<Admin,Integer>{

	public Admin findByEmailAndPassword(String email, String password);

	public Admin findByEmail(String email);

}
