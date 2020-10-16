package com.car.reports.Entities;

import org.bson.types.ObjectId;

public class Bill {

  private int amount;

  private ObjectId jobId;

  private String orderId;

  private String paymentId;

  private String signature;

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public ObjectId getJobId() {
    return jobId;
  }

  public void setJobId(ObjectId jobId) {
    this.jobId = jobId;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(String paymentId) {
    this.paymentId = paymentId;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }
}
