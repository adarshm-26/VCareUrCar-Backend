package com.car.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class JobController {
	@Autowired
	private JobService service;

	//booking
	@PostMapping("/book")
	public Job registerAppointment(@RequestHeader(name = "role") String role,@RequestBody Job apt) throws Exception{
		if(role.equalsIgnoreCase("ROLE_customer") || role.equalsIgnoreCase("ROLE_admin")){
			Job aptObj=null;
			aptObj=service.regAppontment(apt);
			if(aptObj==null)
				throw new Exception("oops something went wrong");

			return aptObj;
		}
		throw new Exception("unauthorized");
	}

	//scheduling

	@PostMapping("/schedule")
	public ResponseEntity<Object> addJobToTech(@RequestHeader(name = "role") String role,@RequestBody Job job){
		if(role.equalsIgnoreCase("ROLE_supervisor") || role.equalsIgnoreCase("ROLE_admin")) {
			Job jobObj = null;

			jobObj = service.assignToTechnician(job);
			if (jobObj == null) {
				return new ResponseEntity<>("something went wrong ", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("updated successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>("unauthorized",HttpStatus.BAD_REQUEST);
	}
	//service
	@PostMapping("/service")
	public ResponseEntity<Object> techAdd(@RequestHeader(name = "role") String role,@RequestBody Job job){
		if(role.equalsIgnoreCase("ROLE_technician") || role.equalsIgnoreCase("ROLE_admin")) {
			Job jobObj = null;
			jobObj = service.techUpdate(job);
			if (jobObj == null) {
				return new ResponseEntity<>("failed to update", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("updated success fully", HttpStatus.OK);
		}
		return new ResponseEntity<>("unauthorized",HttpStatus.BAD_REQUEST);
	}

	//verify

	@PostMapping("/verify")
	public ResponseEntity<Object> verify(@RequestHeader(name = "role") String role,@RequestBody Job job){
		if(role.equalsIgnoreCase("ROLE_supervisor") || role.equalsIgnoreCase("ROLE_admin")) {
			Job jobObj = null;
			jobObj = service.verifyAndSet(job);
			if (jobObj == null) {
				return new ResponseEntity<>("something went wrong", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("verified and updated", HttpStatus.OK);
		}
		return new ResponseEntity<>("unauthorized",HttpStatus.BAD_REQUEST);
	}




	@GetMapping("/user/{userId}")
	public List<Job> getJobsByTechId(@RequestHeader(name = "role") String role,@PathVariable int userId) throws Exception {
		User user=null;
		user=service.getUser(userId);
		if(user==null){
			throw new Exception("user not found");
		}

		if(role.equalsIgnoreCase("ROLE_customer")){
			List<Job> jobs=service.getByUserId(userId);
			if(jobs.size()==0){
				throw new Exception("no jobs found");
			}return jobs;
		}
		else if(role.equalsIgnoreCase("ROLE_technician")){
			List<Job> jobs=service.getJobTechId(userId);
			if(jobs.size()==0){
				throw new Exception("no jobs found");
			}return jobs;
		}
		else if(role.equalsIgnoreCase("ROLE_supervisor")){
			List<Job> jobs=service.getJobsSup(userId);
			if(jobs.size()==0){
				throw new Exception("no jobs found");
			}return jobs;
		}
		return null;

	}

	@GetMapping("/{jobId}")
	public Job getJob(@PathVariable int jobId) throws Exception {
		Job job=null;
		job=service.getJob(jobId);
		if(job==null){
			throw new Exception("no job found");
		}
		return job;
	}

	@GetMapping("/all")
	public List<Job> getAll(@RequestHeader(name = "role") String role) throws Exception {
		if(role.equalsIgnoreCase("ROLE_admin")) {
			List<Job> jobs = service.getAllJobs();
			if (jobs.size() == 0) {
				throw new Exception("no jobs found");
			}
			return jobs;
		}
		throw new Exception("unauthorized");
	}

	@PostMapping("/remove")
	public ResponseEntity<Object> removeJob(@RequestHeader(name = "role") String role,@RequestBody Job job) throws Exception {
		if(role.equalsIgnoreCase("ROLE_admin")) {
			Job jobObj = null;
			jobObj = service.deleteJob(job.getId());
			if (jobObj != null) {
				return new ResponseEntity<>("failed to delete", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("deleted successfuly", HttpStatus.OK);
		}
		throw new Exception("unauthorized");
	}

}
