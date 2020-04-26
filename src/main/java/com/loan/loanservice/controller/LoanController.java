package com.loan.loanservice.controller;

import com.loan.loanservice.domain.LoanRequest;
import com.loan.loanservice.domain.MonthlyRepayment;
import com.loan.loanservice.service.LoanPlanGeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Api(value = "Loan Controller", description = "Operation related to Loan Plan Generation API")
public class LoanController {

  private final LoanPlanGeneratorService loanPlanGeneratorService;

  @PostMapping(
      value = "/generate-plan",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Loan Plan Generator")
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Plan Created")})
  public ResponseEntity<List<MonthlyRepayment>> generateLoanPlan(
      @RequestBody @Valid LoanRequest loanRequest) {
    List<MonthlyRepayment> monthlyRepayments =
        loanPlanGeneratorService.generateLoanPlan(loanRequest);
    return new ResponseEntity<List<MonthlyRepayment>>(monthlyRepayments, HttpStatus.CREATED);
  }
}
