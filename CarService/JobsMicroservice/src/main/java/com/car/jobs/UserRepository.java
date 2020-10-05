package com.car.jobs;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId>{
	List<User> findAllByType(String type);
	Optional<User> findById(ObjectId id);
	User findByEmail(String email);
}
