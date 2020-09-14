package com.car.jobs;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface UserRepository extends MongoRepository<User,Integer>{
	public List<User> findAllByType(String type);
	public User findByEmail(String email);
}
