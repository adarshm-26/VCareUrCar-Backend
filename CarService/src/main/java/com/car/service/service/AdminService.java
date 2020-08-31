package com.car.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.service.model.Admin;
import com.car.service.repository.AdminRepository;
@Service
public class AdminService {
	@Autowired
	private AdminRepository arepo;
	
	public Admin saveAdmin(Admin admin) {
		Admin adminObj=null;
		adminObj=arepo.save(admin);
		return adminObj;
	}
	
	public Admin getAdmin(String email,String password) {
		Admin Obj=null;
		Obj=arepo.findByEmailAndPassword(email,password);
		return Obj;
	}
	
	public Admin getAdminByEmail(String email) {
		Admin admin=null;
		admin=arepo.findByEmail(email);
		return admin;
	}
}
