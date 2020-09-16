package com.car.jobs;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class JobService {
	@Autowired
	private JobRepository jobrepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RestTemplate template;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Job regAppontment(Job appointment) {

		return jobrepo.save(appointment);

	}

	
	public List<User> getTechnicians(String type){
		List<User> arr=userRepo.findAllByType(type);
		return arr;
	}
	public Job saveUerJob(Job job){
		return jobrepo.save(job);
	}


	private Job update(Job job){
		Query query=new Query();
		query.addCriteria(Criteria.where("id").is(job.getId()));
		Update update=new Update();
		update.set("status",job.isStatus());
		update.set("acceptedDate",job.getAcceptedDate());
		update.set("technicianId",job.getTechnicianId());
		update.set("supervisorId",job.getSupervisorId());
		update.set("deadlineDate",job.getDeadlineDate());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}
	public Job assignToTechnician(Job job){
		job=update(job);
		return jobrepo.findById(job.getId());
	}


	public List<Job> getJobTechId(int techId){
		return jobrepo.findAllByTechnicianId(techId);
	}

	public Job techUpdate(Job job) {
		Job prevServices=jobrepo.findById(job.getId());
		List<com.car.jobs.Service> preServices=prevServices.getServices();
		List<com.car.jobs.Service> currService=job.getServices();
		for (int i=0;i<currService.size();i++){
			String work=currService.get(i).getWork();
			Date completed=currService.get(i).getCompletedDate();
			preServices.get(i).setCompletedDate(completed);
			preServices.get(i).setWork(work);
		}
		job.setServices(preServices);
		job=technicianUpdate(job);
		return jobrepo.findById(job.getId());
	}
//
	private Job technicianUpdate(Job job) {
		Query query=new Query();
		query.addCriteria(Criteria.where("id").is(job.getId()));
		Update update=new Update();
		update.set("status",job.isStatus());
		update.set("services",job.getServices());
		update.set("appointedDate",job.getAppointedDate());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}
	public Job getJob(int id){
		return jobrepo.findById(id);
	}

	private Job verifyAndUpdate(Job job){
		Query query=new Query();
		query.addCriteria(Criteria.where("id").is(job.getId()));
		Update update=new Update();
		update.set("status",job.isStatus());
		update.set("services",job.getServices());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}

	public Job verifyAndSet(Job job){
		Job prevServices=jobrepo.findById(job.getId());
		List<com.car.jobs.Service> preServices=prevServices.getServices();
		List<com.car.jobs.Service> currService=job.getServices();
		for (int i=0;i<currService.size();i++){
			Date verified=currService.get(i).getVerifiedDate();
			preServices.get(i).setVerifiedDate(verified);
		}
		job.setServices(preServices);
		job=verifyAndUpdate(job);
		return jobrepo.findById(job.getId());
	}

	public List<Job> getByUserId(int id){
		return jobrepo.findAllByCustomerId(id);
	}

	public User getUser(int id){
		return userRepo.findById(id);
	}

	public List<Job> getJobsSup(int id){
		return jobrepo.findAllBySupervisorId(id);
	}

	public List<Job> getAllJobs(){
		return jobrepo.findAll();
	}

	public Job deleteJob(int id){
		Job job=jobrepo.findById(id);
		jobrepo.delete(job);
		return jobrepo.findById(id);

	}

}
