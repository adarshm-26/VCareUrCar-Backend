package com.car.user.components;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId id;

  private String name;

  private String type;

  @Indexed(name = "email_index", unique = true)
  private String email;

  private int age;

  private String phone;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "YYYY-MM-dd")
  private Date registerDate;

  private boolean enable;

  private int verificationcode;

  public boolean isEnable() {
    return enable;
  }

  public int getVerificationcode() {
    return verificationcode;
  }

  public void setVerificationcode(int verficationcode) {
    this.verificationcode = verficationcode;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
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
    this.password = password;
  }

  public void setType(String type) {
  	this.type = type;
	}

	public String getType() {
  	return this.type;
	}

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public Date getRegisterDate() {
    return registerDate;
  }

  public void setRegisterDate(Date registerDate) {
    this.registerDate = registerDate;
  }
}
