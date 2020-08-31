package com.car.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.car.service.model.User;


public interface UserRepository extends MongoRepository<User,Integer>{
	public User findByEmailAndPassword(String email,String password);

	public User findByEmail(String email);
}
