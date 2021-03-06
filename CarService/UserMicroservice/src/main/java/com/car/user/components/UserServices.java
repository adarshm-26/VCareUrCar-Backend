package com.car.user.components;

import java.util.Date;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
  @Autowired
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  @Autowired
  private MongoTemplate mongoTemplate;
  private Logger logger = LoggerFactory.getLogger(UserServices.class);

  public UserServices(UserRepository userRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  public User putUser(User user) {
    logger.info("Saving details of user {}", user.getEmail());
    user.setRegisterDate(new Date());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public User getUserByEmail(String email) {
    logger.info("Fetching details of user using email");
    return userRepository.findByEmail(email);
  }

  public Page<User> getAllUsers(Pageable pageable) {
    logger.info("Fetching page {} of all users", pageable.getPageNumber());
    return userRepository.findAll(pageable);
  }

  public Page<User> getAllUsersOfType(String type, Pageable pageable) {
    logger.info("Fetching page {} of {} type", pageable.getPageNumber(), type);
    return userRepository.findAllByType(type, pageable);
  }

  public User updatePassword(ObjectId id, String password) {
    Query query = new Query();
    query.addCriteria(Criteria.where("id").is(id));
    Update update = new Update();
    update.set("password", passwordEncoder.encode(password));
    logger.info("Updating password for {}", id.toString());
    return mongoTemplate.findAndModify(query, update, User.class);
  }

  public void removeUser(ObjectId id) {
    logger.info("Removing user {}", id.toString());
    userRepository.deleteById(id);
  }

  public User findUser(ObjectId id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      logger.info("Fetched user {}", user.get().getId().toString());
      return user.get();
    } else {
      logger.info("User with {} does not exist", id.toString());
      return null;
    }
  }

  public User updateCode(int code,ObjectId id){
    Query query = new Query();
    query.addCriteria(Criteria.where("id").is(id));
    Update update = new Update();
    update.set("verificationcode", code);
    logger.info("Updating verification to user", id.toString());
    return mongoTemplate.findAndModify(query, update, User.class);
  }
  public User updateEnable(ObjectId id){
    Query query = new Query();
    query.addCriteria(Criteria.where("id").is(id));
    Update update = new Update();
    update.set("enable", true);
    logger.info("Updating verification to user", id.toString());
    return mongoTemplate.findAndModify(query, update, User.class);
  }
}
