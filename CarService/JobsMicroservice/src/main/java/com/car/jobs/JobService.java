package com.car.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.json.Property;
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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.SignatureException;
import java.util.Date;
import java.util.Properties;

@Service
public class JobService {
	@Autowired
	private JobRepository jobrepo;
	@Autowired
	private MongoTemplate mongoTemplate;
	private Logger logger = LoggerFactory.getLogger(JobService.class);
	private RazorpayClient razorpayClient;
	private String RAZORPAY_SECRET = "kQPl746StDee83rTEdPk6tmF";
	private String RAZORPAY_KEY = "rzp_test_qO4Lpyz964OqSr";
	private String HMAC_ALGO_NAME = "HmacSHA256";

	JobService() {
		try {
			razorpayClient = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);
		} catch (RazorpayException e) {
			logger.error("Could not initialize RazorPay hence Payments cannot be processed due to {}", e.getMessage());
		}
	}
	
	public Job putJob(Job job) {
		logger.info("Saving new job with {} services", job.getServices().size());
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
		logger.info("Scheduling job {}" , job.getId().toString());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}

	public Job updateJobForServicing(Job job) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(job.getId()));
		Update update = new Update();
		update.set("status", job.getStatus());
		update.set("services", job.getServices());
		update.set("appointedDate", job.getAppointedDate());
		logger.info("Adding service logs for job {}", job.getId().toString());
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
		logger.info("Verifying services for job {}", job.getId().toString());
		return mongoTemplate.findAndModify(query,update,Job.class);
	}

	public Job getJob(ObjectId id){
		logger.info("Fetched details of job {}", id.toString());
		return jobrepo.findById(id).orElse(null);
	}

	public Page<Job> getJobsByCustomer(ObjectId id, Pageable pageable){
		logger.info("Fetched page {} of customer {}", pageable.getPageNumber(), id.toString());
		return jobrepo.findAllByCustomerId(id, pageable);
	}

	public Page<Job> getJobsBySupervisor(ObjectId id, Pageable pageable){
		logger.info("Fetched page {} of supervisor {}", pageable.getPageNumber(), id.toString());
		return jobrepo.findAllBySupervisorId(id, pageable);
	}

	public Page<Job> getJobsByStatus(String status, Pageable pageable) {
		logger.info("Fetched page {} having {} status", pageable.getPageNumber(), status);
		return jobrepo.findAllByStatus(status, pageable);
	}

	public Page<Job> getJobsByTechnician(ObjectId id, Pageable pageable){
		logger.info("Fetched page {} of technician {}", pageable.getPageNumber(), id.toString());
		return jobrepo.findAllByTechnicianId(id, pageable);
	}

	public Page<Job> getAllJobs(Pageable pageable){
		logger.info("Fetched page {} of all jobs", pageable.getPageNumber());
		return jobrepo.findAll(pageable);
	}

	public void deleteJob(ObjectId id){
		logger.warn("Removing job " + id.toString());
		jobrepo.deleteById(id);
	}

	public Properties initiatePayment(Properties paymentDetails) throws Exception {
		JSONObject requiredDetails = Property.toJSONObject(paymentDetails);
		requiredDetails.put("payment_capture", true);
		Bill unverifiedBill = new Bill();
		Query query = new Query();
		Update update = new Update();
		try {
			Order order = razorpayClient.Orders.create(requiredDetails);
			JSONObject jsonOrder = order.toJson();
			Properties resultReturn = new Properties();
			resultReturn.put("orderId", jsonOrder.get("id"));

			unverifiedBill.setOrderId((String) jsonOrder.get("id"));
			unverifiedBill.setJobId(new ObjectId(paymentDetails.getProperty("receipt")));
			unverifiedBill.setAmount(Integer.parseInt(paymentDetails.getProperty("amount")));

			query.addCriteria(Criteria.where("id").is(new ObjectId(paymentDetails.getProperty("receipt"))));
			update.set("bill", unverifiedBill);
			mongoTemplate.findAndModify(query, update, Job.class);
			return resultReturn;
		} catch (RazorpayException e) {
			logger.error("Failed to create order {} due to {}", requiredDetails.get("receipt"), e.getMessage());
			throw e;
		}
	}

	public boolean verifyPayment(Properties signatures) throws Exception {
		String result;
		Job job = getJob(new ObjectId(signatures.getProperty("receipt")));
		Query query = new Query();
		Update update = new Update();
		try {
			SecretKeySpec signingKey = new SecretKeySpec(RAZORPAY_SECRET.getBytes(), HMAC_ALGO_NAME);
			Mac mac = Mac.getInstance(HMAC_ALGO_NAME);
			mac.init(signingKey);

			String data = job.getBill().getOrderId() + "|" + signatures.getProperty("razorpay_payment_id");
			logger.info("Computing HMAC on {}", data);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			result = DatatypeConverter.printHexBinary(rawHmac).toLowerCase();

			if (result.equals(signatures.getProperty("razorpay_signature"))) {
				Bill bill = job.getBill();
				bill.setPaymentId(signatures.getProperty("razorpay_payment_id"));
				bill.setSignature(signatures.getProperty("razorpay_signature"));
				query.addCriteria(Criteria.where("id").is(job.getId()));
				update.set("bill", bill);
				update.set("status", "COMPLETED");
				update.set("paymentRecieveDate", new Date());
				mongoTemplate.findAndModify(query, update, Job.class);
				return true;
			} else {
				logger.error("Generated signature {} is not equal to recieved signature {}", result, signatures.getProperty("razorpay_signature"));
				return false;
			}
		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}
	}
}
