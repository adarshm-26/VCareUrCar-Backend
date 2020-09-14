package com.car.jobs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document (collection = "Jobs")
public class Job {
  @Id
  private int id;
  private int carId;
  private boolean status;
  private Date bookingDate;
  private Date acceptedDate;
  private Date appointedDate;
  private Date paymentReceiveDate;
  private int supervisorId;
  private int technicianId;
  private BigDecimal estimatedCost;
  private int discount;
  private boolean isServiceCompleted;
  private String typeOfService;



  /* TODO -> Handle actual maintainenance services
   * I dont know how to handle this, I tried using a class but am facing
   * difficulties, lets discuss first and decide what to do with this
   * Until then, we will assume that a [JOB] means all 19 tasks listed
   * in the notes
   * */
//  private List<ServiceDetails> services;

}
