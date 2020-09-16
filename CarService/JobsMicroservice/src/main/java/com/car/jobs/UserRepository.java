package com.car.jobs;


import com.car.jobs.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface UserRepository extends MongoRepository<User,Integer>{
	public List<User> findAllByType(String type);
	User findById(int id);
	public User findByEmail(String email);
}
