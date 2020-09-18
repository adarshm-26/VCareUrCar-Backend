package com.car.jobs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    private String name;
    private float cost;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
    private Date completedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
    private Date verifiedDate;
    private String work;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Date getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(Date verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }
}
