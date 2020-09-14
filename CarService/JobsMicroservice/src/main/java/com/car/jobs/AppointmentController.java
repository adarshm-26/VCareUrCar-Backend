package com.car.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController

public class AppointmentController {
	@Autowired
	private AppointmentService service;

	//this will be called by user-service
	@PostMapping("/bookAppointment")
	public Job registerAppointment( @RequestBody Job apt) throws Exception{
		Job aptObj=null;
		aptObj=service.regAppontment(apt);
		if(aptObj==null)
			throw new Exception("oops something went wrong");
		
		return aptObj;
	}
	@PostMapping("/addJobToTech/{supId}/{techId}")
	public Job addJobToTech(@PathVariable("techId") int techId, @PathVariable("supId") int supId , @RequestBody Job job){
		
		return service.assignToTechnician(techId,supId, job);
	}
	@GetMapping("/getAllTechnicians")
	public List<User> getTechniciansAvl() throws Exception{
		List<User> arr=service.getTechnicians("tech");
		if(arr.size()==0) {
			throw new Exception("technicians on leave");
		}
		return arr;
	}
	@GetMapping("/getAllAppointments")
	public List<Job> getAllJobs() throws Exception {
		Job obj=null;
		List<Job> arr=service.getJobs(false);
		if(arr.size()==0){
			throw new Exception("no jobs found");
		}
		return arr;
	}
	//this method will be called by technician
	@GetMapping("/getMyJob/{techId}")
	public List<Job> getJobsByTechId(@PathVariable int techId){
		List<Job> obj=service.getJobTechId(techId);
		return obj;

	}
	//this method will be called by technician -server
	@PostMapping("/assignToJob")
	public Job updateJobFields(@RequestBody Job job){
		return service.updateAndAssign(job);
	}

	@PostMapping("/estimating/cost/{id}")
	public BigDecimal estimatedCost(@PathVariable("id") int id) throws Exception {
		Job job=service.getJob(id);
		if(job!=null && job.isServiceCompleted()==false){
			throw new Exception("my technicians are still working");
		}
		job=service.costUpdating(job);
		if(job.getDiscount()!=0) {
			BigDecimal diff;
			BigDecimal totalCost = job.getEstimatedCost();
			BigDecimal discount = new BigDecimal(String.valueOf(job.getDiscount()));
			diff=totalCost.subtract(discount);
			return diff;
		}
		return job.getEstimatedCost();
	}
	
}
