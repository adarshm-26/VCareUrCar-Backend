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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCarId() {
    return carId;
  }

  public void setCarId(int carId) {
    this.carId = carId;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public Date getBookingDate() {
    return bookingDate;
  }

  public void setBookingDate(Date bookingDate) {
    this.bookingDate = bookingDate;
  }

  public Date getAcceptedDate() {
    return acceptedDate;
  }

  public void setAcceptedDate(Date acceptedDate) {
    this.acceptedDate = acceptedDate;
  }

  public Date getAppointedDate() {
    return appointedDate;
  }

  public void setAppointedDate(Date appointedDate) {
    this.appointedDate = appointedDate;
  }

  public Date getPaymentReceiveDate() {
    return paymentReceiveDate;
  }

  public void setPaymentReceiveDate(Date paymentReceiveDate) {
    this.paymentReceiveDate = paymentReceiveDate;
  }

  public int getSupervisorId() {
    return supervisorId;
  }

  public void setSupervisorId(int supervisorId) {
    this.supervisorId = supervisorId;
  }

  public int getTechnicianId() {
    return technicianId;
  }

  public void setTechnicianId(int technicianId) {
    this.technicianId = technicianId;
  }

  public BigDecimal getEstimatedCost() {
    return estimatedCost;
  }

  public void setEstimatedCost(BigDecimal estimatedCost) {
    this.estimatedCost = estimatedCost;
  }

  public int getDiscount() {
    return discount;
  }

  public void setDiscount(int discount) {
    this.discount = discount;
  }

  public boolean isServiceCompleted() {
    return isServiceCompleted;
  }

  public void setServiceCompleted(boolean serviceCompleted) {
    isServiceCompleted = serviceCompleted;
  }

  public String getTypeOfService() {
    return typeOfService;
  }

  public void setTypeOfService(String typeOfService) {
    this.typeOfService = typeOfService;
  }
}
