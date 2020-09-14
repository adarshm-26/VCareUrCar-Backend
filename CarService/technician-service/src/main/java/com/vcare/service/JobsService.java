package com.vcare.service;



import com.vcare.model.repository.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

@Service
public class JobsService {


	@Autowired
	private RestTemplate restTemplate;
	

	public Job[] getJob(int techId) {
		ResponseEntity<Job[]> response=
		restTemplate.getForEntity("http://APPOINTMENT-SERVICE/appointment/getMyJob/"+techId,Job[].class);

		return response.getBody();
	}

	public Job updateJobFields(Job job){
		Job obj=null;
		obj=restTemplate.postForObject("http://APPOINTMENT-SERVICE/appointment/assignToJob",job,Job.class);
		return obj;
	}
}
