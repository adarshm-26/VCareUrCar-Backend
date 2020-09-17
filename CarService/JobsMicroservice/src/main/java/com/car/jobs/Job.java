package com.car.jobs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document (collection = "Jobs")
public class Job {
  @Id
  private int id;
  private int carId;
  private int customerId;
  private String status;
  @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
  private Date bookingDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
  private Date acceptedDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
  private Date appointedDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
  private Date paymentReceiveDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
  private Date deadlineDate;
  private int supervisorId;
  private int technicianId;
  private int discount;
  private List<Service> services;

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

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String isStatus() {
    return status;
  }

  public void setStatus(String status) {
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

  public Date getDeadlineDate() {
    return deadlineDate;
  }

  public void setDeadlineDate(Date deadlineDate) {
    this.deadlineDate = deadlineDate;
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

  public int getDiscount() {
    return discount;
  }

  public void setDiscount(int discount) {
    this.discount = discount;
  }

  public List<Service> getServices() {
    return services;
  }

  public void setServices(List<Service> services) {
    this.services = services;
  }
}
