package com.car.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.service.model.Technician;
import com.car.service.repository.TechnicianRepository;
@Service
public class TechnicianService {
	@Autowired
	private TechnicianRepository trepo;
	public Technician saveTechnician(Technician tech) {
		Technician techObj=null;
		techObj=trepo.save(tech);
		return techObj;
	}
	public List<Technician> allTechnicians(){
		return trepo.findAll();
	}
	public Technician getTechnician(String email,String password) {
		 Technician userObj=null;
		userObj=trepo.findByEmailAndPassword(email,password);
		return userObj;
	}
	public Technician getTechnicianByEmail(String email) {
		Technician obj=null;
		obj=trepo.findByEmail(email);
		return obj;
	}
}
