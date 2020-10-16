package com.car.reports.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document (collection = "Jobs")
public class Job {
  @JsonSerialize(using = ToStringSerializer.class)
  @Id
  private ObjectId id;

  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId carId;

  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId customerId;

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

  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId supervisorId;

  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId technicianId;

  private int discount;

  private List<Service> services;

  private Bill bill;

  public Bill getBill() {
    return bill;
  }

  public void setBill(Bill bill) {
    this.bill = bill;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public ObjectId getCarId() {
    return carId;
  }

  public void setCarId(ObjectId carId) {
    this.carId = carId;
  }

  public ObjectId getCustomerId() {
    return customerId;
  }

  public void setCustomerId(ObjectId customerId) {
    this.customerId = customerId;
  }

  public String getStatus() {
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

  public ObjectId getSupervisorId() {
    return supervisorId;
  }

  public void setSupervisorId(ObjectId supervisorId) {
    this.supervisorId = supervisorId;
  }

  public ObjectId getTechnicianId() {
    return technicianId;
  }

  public void setTechnicianId(ObjectId technicianId) {
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
