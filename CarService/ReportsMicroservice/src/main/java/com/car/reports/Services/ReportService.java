package com.car.reports.Services;

import com.car.reports.Entities.Car;
import com.car.reports.Entities.CarsToUserStatistics;
import com.car.reports.Entities.Job;
import com.car.reports.Entities.User;
import com.car.reports.Repositories.CarRepository;
import com.car.reports.Repositories.JobRepository;
import com.car.reports.Repositories.UserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class ReportService {

  private Logger logger = LoggerFactory.getLogger(ReportService.class);
  private final UserRepository userRepository;
  private CarRepository carRepository;
  private JobRepository jobRepository;
  private MongoTemplate mongoTemplate;

  public ReportService(UserRepository userRepository,
                       CarRepository carRepository,
                       JobRepository jobRepository,
                       MongoTemplate mongoTemplate) {
    this.userRepository = userRepository;
    this.carRepository = carRepository;
    this.jobRepository = jobRepository;
    this.mongoTemplate = mongoTemplate;
  }

  public List<User> getUsersForReport() {
    return userRepository.findAll();
  }

  public List<CarsToUserStatistics> getCarsForReport() {
    GroupOperation groupByOwnerIds = group("ownerId").count().as("cars");
    GroupOperation groupByCarCount = group("cars").count().as("count");
    Aggregation aggregation = newAggregation(
        groupByOwnerIds,
        groupByCarCount);
    AggregationResults<CarsToUserStatistics> result = mongoTemplate
        .aggregate(aggregation, "Cars", CarsToUserStatistics.class);
    logger.info("Got following cars : {}", result.getMappedResults().toString());
    return result.getMappedResults();
  }

  public List<Job> getJobForReport() {
    return jobRepository.findAll();
  }

  public void generateReports(HttpServletResponse response, String reportClass)
      throws IOException, JRException {

    File file = ResourceUtils.getFile("classpath:" + reportClass + ".jrxml");
    JasperReport report = JasperCompileManager.compileReport(file.getAbsolutePath());

    JRBeanCollectionDataSource dataSource;
    switch (reportClass) {
      case "Users":
        dataSource = new JRBeanCollectionDataSource(getUsersForReport());
        break;
      case "Cars":
        dataSource = new JRBeanCollectionDataSource(getCarsForReport());
        break;
      case "Jobs":
        dataSource = new JRBeanCollectionDataSource(getJobForReport());
        break;
      default:
        dataSource = new JRBeanCollectionDataSource(Collections.emptyList());
        break;
    }

    JasperPrint printReport = JasperFillManager.fillReport(report, new HashMap<>(), dataSource);
    streamReport(response, JasperExportManager.exportReportToPdf(printReport), reportClass);
  }

  protected void streamReport(HttpServletResponse response, byte[] data, String name)
      throws IOException {

    response.setContentType("application/pdf");
    response.setHeader("Content-disposition", "attachment; filename=" + name);
    response.setContentLength(data.length);

    response.getOutputStream().write(data);
    response.getOutputStream().flush();
  }
}
