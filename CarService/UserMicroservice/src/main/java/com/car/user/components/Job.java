package com.car.user.components;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {

  private int id;
  private int userId;
  private int carId;
  private boolean Status;
  private Date bookingDate;
  private int discount;
  private boolean isServiceCompleted;
  private String typeOfService;



}
