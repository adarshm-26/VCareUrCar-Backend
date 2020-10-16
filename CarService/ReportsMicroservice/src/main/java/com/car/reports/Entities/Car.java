package com.car.reports.Entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "Cars")
@CompoundIndex(name = "owner_car_index", def = "{'id':1, 'ownerId':1}", unique = true)
public class Car {

  @JsonSerialize(using = ToStringSerializer.class)
  @Id
  private ObjectId id;

  private String brand;

  private String model;

  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId ownerId;

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public ObjectId getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(ObjectId ownerId) {
    this.ownerId = ownerId;
  }
}
