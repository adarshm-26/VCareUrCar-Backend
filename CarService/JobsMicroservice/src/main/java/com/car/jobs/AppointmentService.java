package com.car.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {
	@Autowired
	private JobRepository jobRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Job regAppointment(Job job) {
		job.setStatus("BOOKED");
		return jobRepo.save(job);
	}

	public List<User> getTechnicians(String type){
		List<User> arr=userRepo.findAllByType(type);
		return arr;
	}

	public Job saveUserJob(Job job){
		return jobRepo.save(job);
	}

	public List<Job> getJobs(boolean status){
		return jobRepo.findAllByStatus(status);
	}

	public Job update(Job job){
		Query query=new Query();
		query.addCriteria(Criteria.where("id").is(job.getId()));
		Update update=new Update();
		update.set("status",job.isStatus());
		update.set("acceptedDate",job.getAcceptedDate());
		update.set("technicianId",job.getTechnicianId());
		update.set("supervisorId",job.getSupervisorId());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}

	public Job assignToTechnician(int techId,int supId,Job job){
		SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date=new Date();
		job.setAcceptedDate(date);
		job.setTechnicianId(techId);
		job.setStatus(true);
		job.setSupervisorId(supId);
		job=update(job);
		return job;
	}


	public List<Job> getJobTechId(int techId){
		return jobRepo.findAllByTechnicianId(techId);
	}

	public Job updateAndAssign(Job job) {
		Job obj=null;
		SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date=new Date();
		job.setAppointedDate(date);
		job.setServiceCompleted(true);
		obj=TechnicianUpdate(job);
		return obj;
	}

	private Job TechnicianUpdate(Job job) {
		Query query=new Query();
		query.addCriteria(Criteria.where("id").is(job.getId()));
		Update update=new Update();
		update.set("isServiceCompleted",job.isServiceCompleted());
		update.set("appointedDate",job.getAppointedDate());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}
	public Job getJob(int id){
		return jobRepo.findById(id);
	}

	private Job costUpdate(Job job){
		Query query=new Query();
		query.addCriteria(Criteria.where("id").is(job.getId()));
		Update update=new Update();
		update.set("estimatedCost",job.getEstimatedCost());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}
	public Job costUpdating(Job job ){
		Job obj=null;
		job.setEstimatedCost(new BigDecimal("12000"));
		obj=costUpdate(job);
		return obj;
	}
}
