package com.car.user.components;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User,ObjectId>{

	User findByEmail(String email);

	Optional<User> findById(ObjectId id);

	Page<User> findAllByType(String type, Pageable pageable);

	Page<User> findAll(Pageable pageable);

	void deleteById(ObjectId id);
}
