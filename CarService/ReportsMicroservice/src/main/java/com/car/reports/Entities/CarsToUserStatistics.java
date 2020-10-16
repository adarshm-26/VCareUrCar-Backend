package com.car.reports.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CarsToUserStatistics {

  public int count;
  @Id
  public int id;

  CarsToUserStatistics(int id, int count) {
    this.id = id;
    this.count = count;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Stat : [count:" + count + " ,id:" + id + "]";
  }
}