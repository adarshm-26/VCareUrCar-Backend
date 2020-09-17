package com.car.user.components;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,Integer>{
	User findByEmail(String email);
	User findById(int id);
}
