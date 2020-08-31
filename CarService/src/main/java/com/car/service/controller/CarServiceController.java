package com.car.service.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.car.service.model.Admin;
import com.car.service.model.Supervisor;
import com.car.service.model.Technician;
import com.car.service.model.User;
import com.car.service.service.AdminService;
import com.car.service.service.SupervisorService;
import com.car.service.service.TechnicianService;
import com.car.service.service.UserService;


@RestController
public class CarServiceController {
	@Autowired
	private UserService uservice;
	@Autowired
	private AdminService aservice;
	@Autowired
	private TechnicianService tservice;
	@Autowired
	private SupervisorService sservice;
	@PostMapping("/reguser")
	public String regUser(@RequestBody User user) throws Exception {
		String tempEmail=user.getEmail();
		if(tempEmail!=null && !"".equals(tempEmail)) {
			User obj=null;
			obj=uservice.getUserByEmail(tempEmail);
			if(obj!=null) {
				throw new Exception("User already exists with "+tempEmail);
			}
		}
		User obj=null;
		
		obj=uservice.saveUser(user);
		if(obj !=null)
		return "registration success id: "+user.getId();
		else
			return "registration failed. try again!";
	}
	
	@PostMapping("userlogin")
	public String userLogin(@RequestBody User user) throws Exception {
		String tempEmail=user.getEmail();
		String tempPassword=user.getPassword();
		User userObj=null;
		if(tempEmail!=null && tempPassword!=null) {
		userObj=uservice.getUser(tempEmail, tempPassword);
		}
		if(userObj==null) {
			throw new Exception("oops.. check your email or password");
		}
		return "login success";
		
	}
	
	@PostMapping("/adminlogin")
	public String adminLogin(@RequestBody Admin admin) throws Exception {
		String email=admin.getEmail();
		String password=admin.getPassword();
		
		Admin obj=null;
		if(email!=null && password!=null) {
			obj=aservice.getAdmin(email, password);
		}
		if(obj==null) {
			throw new Exception("oops.. check your email or password");
		}
		return "login success";
	}
	
	@PostMapping("/adminreg")
	public String adminRegister(@RequestBody Admin admin) throws Exception {
		String email=admin.getEmail();
		Admin obj=null;
		if(email!=null && !"".equals(email)) {
			obj=aservice.getAdminByEmail(email);
		}
		if(obj!=null) {
			throw new Exception("Admin already exists");
		}
		obj=aservice.saveAdmin(admin);
		if(obj!=null)
		return "registration success";
		else
			return "registration failed try again";
	}
	@PostMapping("/techlogin")
	public String techLogin(@RequestBody Technician tech) throws Exception {
		String email=tech.getEmail();
		String password=tech.getPassword();
		Technician obj=null;
		if(email!=null && password!=null) {
			obj=tservice.getTechnician(email, password);
			if(obj==null) {
				throw new Exception("check user name or password");
			}
		}
		
		return "login succcess ";
	}
	
	@PostMapping("/techreg")
	public String techReg(@RequestBody Technician tech) throws Exception {
		String email=tech.getEmail();
		Technician obj=null;
		if(email!=null && !"".equals(email)) {
			obj=tservice.getTechnicianByEmail(email);
			if(obj!=null) {
				throw new Exception("user already exists");
			}
		}
		obj=tservice.saveTechnician(tech);
		if(obj!=null)
		return "registration success id: "+tech.getId();
		else
			return "registration failed try again later";
	}
	
	@PostMapping("/suplogin")
	public String supLogin(@RequestBody Supervisor sv) {
		Supervisor obj=null;
		String email=sv.getEmail();
		String password=sv.getPassword();
		obj=sservice.getSupervisor(email, password);
		if(obj!=null)
		return "login success";
		else
			return "oops.. check your email or password";
	}
	
	@PostMapping("/supreg")
	public String supRegister(@RequestBody Supervisor sv) throws Exception {
		Supervisor obj=null;
		String email=sv.getEmail();
		if(email!=null && !"".equals(email)) {
			obj=sservice.getSupervisorByEmail(email);
		}
		if(obj!=null) {
			throw new Exception("user already exists");
		}
		
		obj=sservice.saveSupervisor(sv);
		return "registration success id: "+sv.getId();
	}
}
