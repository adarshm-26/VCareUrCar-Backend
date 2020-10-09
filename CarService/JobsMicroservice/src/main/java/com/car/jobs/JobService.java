package com.car.jobs;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class JobService {
	@Autowired
	private JobRepository jobrepo;
	@Autowired
	private MongoTemplate mongoTemplate;
	private Logger logger = LoggerFactory.getLogger(JobService.class);
	
	public Job putJob(Job job) {
		logger.info("Saving new job " + job.getId().toString());
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
		logger.info("Scheduling job " + job.getId().toString());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}

	public Job updateJobForServicing(Job job) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(job.getId()));
		Update update = new Update();
		update.set("status", job.getStatus());
		update.set("services", job.getServices());
		update.set("appointedDate", job.getAppointedDate());
		logger.info("Adding service logs for job " + job.getId().toString());
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
		logger.info("Verifying services for job " + job.getId().toString());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}

	public Job getJob(ObjectId id){
		logger.info("Fetched details of job " + id.toString());
		return jobrepo.findById(id).orElse(null);
	}

	public Page<Job> getJobsByCustomer(ObjectId id, Pageable pageable){
		logger.info("Fetched page " + pageable.getPageNumber() + " of customer " + id.toString());
		return jobrepo.findAllByCustomerId(id, pageable);
	}

	public Page<Job> getJobsBySupervisor(ObjectId id, Pageable pageable){
		logger.info("Fetched page " + pageable.getPageNumber() + " of supervisor " + id.toString());
		return jobrepo.findAllBySupervisorId(id, pageable);
	}

	public Page<Job> getJobsByStatus(String status, Pageable pageable) {
		logger.info("Fetched page " + pageable.getPageNumber() + " having " + status + " status");
		return jobrepo.findAllByStatus(status, pageable);
	}

	public Page<Job> getJobsByTechnician(ObjectId id, Pageable pageable){
		logger.info("Fetched page " + pageable.getPageNumber() + " of technician " + id.toString());
		return jobrepo.findAllByTechnicianId(id, pageable);
	}

	public Page<Job> getAllJobs(Pageable pageable){
		logger.info("Fetched page " + pageable.getPageNumber() + " of all jobs");
		return jobrepo.findAll(pageable);
	}

	public void deleteJob(ObjectId id){
		logger.warn("Removing job " + id.toString());
		jobrepo.deleteById(id);
	}
}
