package com.car.auth.components;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,Integer>{
	public User findByEmail(String email);
}
