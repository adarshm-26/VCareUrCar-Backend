package com.car.jobs;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
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
		if (job.getServices().stream().allMatch(service -> service.getVerifiedDate() != null))
			job.setStatus("VERIFIED");
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(job.getId()));
		Update update = new Update();
		update.set("status", job.getStatus());
		update.set("services", job.getServices());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}

	public Job getJob(ObjectId id){
		return jobrepo.findById(id).orElse(null);
	}

	public Page<Job> getJobsByCustomer(ObjectId id, Pageable pageable){
		return jobrepo.findAllByCustomerId(id, pageable);
	}

	public Page<Job> getJobsBySupervisor(ObjectId id, Pageable pageable){
		return jobrepo.findAllBySupervisorId(id, pageable);
	}

	public Page<Job> getJobsByStatus(String status, Pageable pageable) {
		return jobrepo.findAllByStatus(status, pageable);
	}

	public Page<Job> getJobsByTechnician(ObjectId id, Pageable pageable){
		return jobrepo.findAllByTechnicianId(id, pageable);
	}

	public Page<Job> getAllJobs(Pageable pageable){
		return jobrepo.findAll(pageable);
	}

	public void deleteJob(ObjectId id){
		Job job = getJob(id);
		jobrepo.delete(job);
	}
}
