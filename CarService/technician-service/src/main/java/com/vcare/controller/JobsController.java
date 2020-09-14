package com.vcare.controller;


import java.util.Arrays;
import java.util.List;

import com.vcare.model.repository.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vcare.service.JobsService;

@RestController
@RequestMapping("/jobs")
public class JobsController {
	
	@Autowired
	private JobsService jobService;

	
	@GetMapping("/getmyjob/{id}")
	public List<Job> getMyJob(@PathVariable int id) throws Exception {
		List<Job> arr= Arrays.asList(jobService.getJob(id));
		
		if(arr.size()==0) {
			throw new Exception("Hurray all jobs completed ");
		}
		return arr;
	}
	@PostMapping("/update/job")
	public ResponseEntity<Object> updateJob(@RequestBody Job job){
		Job obj=null;
		obj=jobService.updateJobFields(job);
		if(obj==null){
			return new ResponseEntity<>("failed to update fields", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("updated job ",HttpStatus.OK);

	}
	
}
