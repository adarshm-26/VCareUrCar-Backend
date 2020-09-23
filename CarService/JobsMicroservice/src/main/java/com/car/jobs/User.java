package com.car.jobs;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
public class User {
  @Id
  private int id;
  private String name;
  private String type;
  private String email;
  private String phone;
  private String password;

  public long getId() {
    return id;
  }

  public void setId(int id) {
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