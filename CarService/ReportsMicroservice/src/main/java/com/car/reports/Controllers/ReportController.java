package com.car.reports.Controllers;

import com.car.reports.Services.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ReportController {

  private ReportService service;

  ReportController(ReportService service) {
    this.service = service;
  }

  @GetMapping(value = "/{reportClass}", produces = MediaType.APPLICATION_PDF_VALUE)
  public void getCarDetails(HttpServletResponse response,
                            @RequestHeader(name = "role") String role,
                            @PathVariable("reportClass") String clazz) throws Exception {
    if (role.equals("ROLE_admin")) {
      service.generateReports(response, clazz);
    } else {
      response.sendError(HttpStatus.UNAUTHORIZED.value());
    }
  }
}
