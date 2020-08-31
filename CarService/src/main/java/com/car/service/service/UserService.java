package com.car.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.service.model.User;
import com.car.service.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository urepo;
	public User saveUser(User user) {
		User userObj=null;
		userObj=urepo.save(user);
		return userObj;
	}
	public User getUser(String email,String password) {
		User userObj=null;
		userObj=urepo.findByEmailAndPassword(email,password);
		return userObj;
	}
	public List<User> allUsers(){
		return urepo.findAll();
	}
	public User getUserByEmail(String email) {
		User user=null;
		user=urepo.findByEmail(email);
		return user;
	}
}
