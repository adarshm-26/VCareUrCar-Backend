package com.car.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class JobService {
	@Autowired
	private JobRepository jobrepo;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Job putJob(Job job) {
		return jobrepo.save(job);
	}

	public Job updateJobForScheduling(Job job) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(job.getId()));
		Update update = new Update();
		update.set("status", job.getStatus());
		update.set("acceptedDate", job.getAcceptedDate());
		update.set("technicianId", job.getTechnicianId());
		update.set("supervisorId", job.getSupervisorId());
		update.set("deadlineDate", job.getDeadlineDate());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}

	public Job updateJobForServicing(Job job) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(job.getId()));
		Update update = new Update();
		update.set("status", job.getStatus());
		update.set("services", job.getServices());
		update.set("appointedDate", job.getAppointedDate());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}

	public Job updateJobForVerification(Job job){
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(job.getId()));
		Update update = new Update();
		update.set("status", job.getStatus());
		update.set("services", job.getServices());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}

	public Job getJob(int id){
		return jobrepo.findById(id);
	}

	public List<Job> getJobsByCustomer(int id){
		return jobrepo.findAllByCustomerId(id);
	}

	public List<Job> getJobsBySupervisor(int id){
		return jobrepo.findAllBySupervisorId(id);
	}

	public List<Job> getJobsByTechnician(int id){
		return jobrepo.findAllByTechnicianId(id);
	}

	public List<Job> getAllJobs(){
		return jobrepo.findAll();
	}

	public void deleteJob(int id){
		Job job = jobrepo.findById(id);
		jobrepo.delete(job);
	}
}
