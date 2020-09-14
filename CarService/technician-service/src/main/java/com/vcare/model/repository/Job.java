package com.vcare.model.repository;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

  private int id;
  private int carId;
  private int userId;
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


}
