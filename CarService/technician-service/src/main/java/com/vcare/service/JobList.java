package com.vcare.service;

import com.vcare.model.repository.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobList {
    private List<Job> jobs;

}
