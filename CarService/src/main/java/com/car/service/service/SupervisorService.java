package com.car.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.service.model.Supervisor;
import com.car.service.repository.SupervisorRepository;
@Service
public class SupervisorService {
	
	@Autowired
	private SupervisorRepository srepo;
	public Supervisor saveSupervisor(Supervisor sv) {
		Supervisor svObj=null;
		svObj=srepo.save(sv);
		return svObj;
	}
	
	public List<Supervisor> allSupervisors(){
		return srepo.findAll();
	}
	
	public Supervisor getSupervisor(String email,String password) {
		Supervisor userObj=null;
		userObj=srepo.findByEmailAndPassword(email,password);
		return userObj;
	}
	public Supervisor getSupervisorByEmail(String email) {
		Supervisor obj=null;
		obj=srepo.findByEmail(email);
		return obj;
	}
}
