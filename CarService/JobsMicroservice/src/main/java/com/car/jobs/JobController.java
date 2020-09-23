package com.car.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController

public class JobController {

	@Autowired
	private JobService service;

	//booking
	@PostMapping("/book")
	public ResponseEntity<Job> registerAppointment(@RequestHeader(name = "role") String role,
																								 @RequestHeader(name = "id") String myId,
																								 @RequestBody Job job) {
		if ((role.equalsIgnoreCase("ROLE_customer") && Integer.parseInt(myId) == job.getCustomerId()) ||
				role.equalsIgnoreCase("ROLE_admin")) {
			if (service.getJob(job.getId()) != null)
				return ResponseEntity.badRequest().build();
			job.setStatus("BOOKED");
			job.setBookingDate(new Date());
			Job savedJob = service.putJob(job);
			if (savedJob != null) {
				return ResponseEntity.ok(savedJob);
			} else {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	//scheduling
	@PostMapping("/schedule")
	public ResponseEntity<Job> acceptAndScheduleJob(@RequestHeader(name = "role") String role,
																									 @RequestHeader(name = "id") String myId,
																									 @RequestBody Job job) {
		if ((role.equalsIgnoreCase("ROLE_supervisor") && Integer.parseInt(myId) == job.getSupervisorId()) ||
				role.equalsIgnoreCase("ROLE_admin")) {
			if (!service.getJob(job.getId()).getStatus().equalsIgnoreCase("BOOKED"))						// To prevent updating more than once
				return ResponseEntity.badRequest().build();
			job.setStatus("SCHEDULED");
			job.setAcceptedDate(new Date());
			Job jobObj = service.updateJobForScheduling(job);
			if (jobObj != null) {
				return ResponseEntity.ok(jobObj);
			} else {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	//service
	@PostMapping("/service")
	public ResponseEntity<Job> addServiceLogs(@RequestHeader(name = "role") String role,
																						@RequestHeader(name = "id") String myId,
																						@RequestBody Job job) {
		if ((role.equalsIgnoreCase("ROLE_technician") && Integer.parseInt(myId) == job.getTechnicianId()) ||
				role.equalsIgnoreCase("ROLE_admin")) {

			Job curJob = service.getJob(job.getId());
			String curStatus = curJob.getStatus();
			if (!(curStatus.equalsIgnoreCase("SCHEDULED") ||
					curStatus.equalsIgnoreCase("UNDER_SERVICE")))
				return ResponseEntity.badRequest().build();
			job.setStatus("UNDER_SERVICE");

			if (curJob.getAppointedDate() == null)
				job.setAppointedDate(new Date());

			job.setServices(getUpdatedServices(curJob, job));

			Job jobObj = service.updateJobForServicing(job);
			if (jobObj != null) {
				return ResponseEntity.ok(jobObj);
			} else {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	//verify
	@PostMapping("/verify")
	public ResponseEntity<Job> verifyServiceLogs(@RequestHeader(name = "role") String role,
																		@RequestHeader(name = "id") String myId,
																		@RequestBody Job job) {
		if ((role.equalsIgnoreCase("ROLE_supervisor") && Integer.parseInt(myId) == job.getSupervisorId()) ||
				role.equalsIgnoreCase("ROLE_admin")) {

			if (!service.getJob(job.getId()).getStatus().equalsIgnoreCase("UNDER_SERVICE"))
				return ResponseEntity.badRequest().build();
			job.setStatus("VERIFIED");

			job.setServices(getUpdatedServices(service.getJob(job.getId()), job));

			Job jobObj = service.updateJobForVerification(job);
			if (jobObj != null) {
				return ResponseEntity.ok(jobObj);
			} else {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@GetMapping("/byUser/{userId}")
	public ResponseEntity<List<Job>> getJobsByUser(@RequestHeader(name = "role") String role,
																	 @RequestHeader(name = "id") String myId,
																	 @PathVariable String userId) throws Exception {
		if (userId.equalsIgnoreCase("my") ||
				role.equalsIgnoreCase("ROLE_admin")) {
			int userIdInt = Integer.parseInt(userId.equalsIgnoreCase("my") ?
																					 myId :
																					 userId);
			List<Job> jobs;
			switch (role) {
				case "ROLE_customer":
				case "ROLE_admin":
					jobs = service.getJobsByCustomer(userIdInt);
					break;
				case "ROLE_supervisor":
					jobs = service.getJobsBySupervisor(userIdInt);
					break;
				case "ROLE_technician":
					jobs = service.getJobsByTechnician(userIdInt);
					break;
				default:
					return ResponseEntity.badRequest().build();
			}
			return ResponseEntity.ok(jobs);        // Send list even if it is empty
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@GetMapping("/{jobId}")
	public ResponseEntity<Job> getJob(@RequestHeader(name = "role") String role,
																		@RequestHeader(name = "id") String myId,
																		@PathVariable int jobId) throws Exception {
		Job job = service.getJob(jobId);
		if (job != null) {
			switch (role) {
				case "ROLE_customer":
					if (Integer.parseInt(myId) == job.getCustomerId())
						return ResponseEntity.ok(job);
					else
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				case "ROLE_supervisor":
					if (Integer.parseInt(myId) == job.getSupervisorId())
						return ResponseEntity.ok(job);
					else
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				case "ROLE_technician":
					if (Integer.parseInt(myId) == job.getTechnicianId())
						return ResponseEntity.ok(job);
					else
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				case "ROLE_admin":
					return ResponseEntity.ok(job);
				default:
					return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@GetMapping("/all")
	public ResponseEntity<List<Job>> getAll(@RequestHeader(name = "role") String role) throws Exception {
		if (role.equalsIgnoreCase("ROLE_admin")) {
			List<Job> jobs = service.getAllJobs();
			return ResponseEntity.ok(jobs);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@PostMapping("/remove")
	public ResponseEntity<String> removeJob(@RequestHeader(name = "role") String role,
																					@RequestBody Job job) throws Exception {
		if (role.equalsIgnoreCase("ROLE_admin")) {
			service.deleteJob(job.getId());
			return ResponseEntity.ok("Deleted Successfully");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	public List<Service> getUpdatedServices(Job oldJob, Job newJob) {

		final HashMap<String, Service> curServices = new HashMap<>();
		oldJob.getServices().forEach(service -> curServices.put(service.getName(), service));

		for (Service service : newJob.getServices()) {
			if (curServices.get(service.getName()).getCompletedDate() == null
				&& service.getCompletedDate() != null)
				curServices.put(service.getName(), service);

			else if (curServices.get(service.getName()).getCompletedDate() != null
				&& curServices.get(service.getName()).getVerifiedDate() == null
				&& service.getVerifiedDate() != null)
				curServices.put(service.getName(), service);
		}
		return new ArrayList<>(curServices.values());
	}
}