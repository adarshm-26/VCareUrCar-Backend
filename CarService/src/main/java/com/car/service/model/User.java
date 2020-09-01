package com.car.service.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "User")
public class User {
  @MongoId
  private long id;
  private String name;
  private String type;
  private String email;
  private String phone;
  private String password;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {

  	// TODO -> Add hashing and salting here

    this.password = password;
  }

  public void setType(String type) {
  	this.type = type;
	}

	public String getType() {
  	return this.type;
	}
}
