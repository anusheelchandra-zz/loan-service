package com.loan.loanservice.service;

import com.loan.loanservice.domain.LoanRequest;
import com.loan.loanservice.domain.MonthlyRepayment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.ValidationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoanPlanGeneratorServiceTest {

  private LoanPlanGeneratorService planGeneratorService;

  @BeforeEach
  public void setup() {
    planGeneratorService = new LoanPlanGeneratorService();
  }

  @Test
  public void shouldSuccessfullyGenerateLoanPlan() {
    LoanRequest loanRequest = getLoanRequest();
    List<MonthlyRepayment> repaymentList = planGeneratorService.generateLoanPlan(loanRequest);
    Assertions.assertThat(repaymentList).isNotEmpty();
    Assertions.assertThat(repaymentList.size()).isEqualTo(24);

    MonthlyRepayment firstMonthlyRepayment = repaymentList.get(0);
    MonthlyRepayment lastMonthlyRepayment = repaymentList.get(23);

    Assertions.assertThat(firstMonthlyRepayment.getPrincipal())
        .isEqualTo(BigDecimal.valueOf(198.53));
    Assertions.assertThat(firstMonthlyRepayment.getInterest()).isEqualTo(BigDecimal.valueOf(20.83));
    Assertions.assertThat(firstMonthlyRepayment.getRemainingOutstandingPrincipal())
        .isEqualTo(BigDecimal.valueOf(4801.47));
    Assertions.assertThat(firstMonthlyRepayment.getBorrowerPaymentAmount())
        .isEqualTo(BigDecimal.valueOf(219.36));
    Assertions.assertThat(firstMonthlyRepayment.getInitialOutstandingPrincipal())
        .isEqualTo(BigDecimal.valueOf(5000));
    Assertions.assertThat(firstMonthlyRepayment.getDate())
        .isEqualTo(LocalDateTime.parse("2020-05-01T00:00:01"));

    Assertions.assertThat(lastMonthlyRepayment.getPrincipal())
        .isEqualTo(BigDecimal.valueOf(218.37));
    Assertions.assertThat(lastMonthlyRepayment.getInterest()).isEqualTo(BigDecimal.valueOf(0.91));
    Assertions.assertThat(lastMonthlyRepayment.getRemainingOutstandingPrincipal())
        .isEqualTo(BigDecimal.ZERO);
    Assertions.assertThat(lastMonthlyRepayment.getBorrowerPaymentAmount())
        .isEqualTo(BigDecimal.valueOf(219.28));
    Assertions.assertThat(lastMonthlyRepayment.getInitialOutstandingPrincipal())
        .isEqualTo(BigDecimal.valueOf(218.37));
    Assertions.assertThat(lastMonthlyRepayment.getDate())
        .isEqualTo(LocalDateTime.parse("2022-04-01T00:00:01"));
  }

  @Test
  public void shouldThrowValidationExceptionWhenAmountIsNull() {
    LoanRequest loanRequest = getLoanRequest();
    loanRequest.setLoanAmount(null);
    ValidationException exception =
        org.junit.jupiter.api.Assertions.assertThrows(
            ValidationException.class, () -> planGeneratorService.generateLoanPlan(loanRequest));
    Assertions.assertThat(exception.getMessage())
        .isEqualTo("Invalid Loan Request. One or more mandatory parameters are null.");
  }

  @Test
  public void shouldThrowValidationExceptionWhenDurationIsNull() {
    LoanRequest loanRequest = getLoanRequest();
    loanRequest.setDurationInMonth(null);
    ValidationException exception =
        org.junit.jupiter.api.Assertions.assertThrows(
            ValidationException.class, () -> planGeneratorService.generateLoanPlan(loanRequest));
    Assertions.assertThat(exception.getMessage())
        .isEqualTo("Invalid Loan Request. One or more mandatory parameters are null.");
  }

  @Test
  public void shouldThrowValidationExceptionWhenDateIsNull() {
    LoanRequest loanRequest = getLoanRequest();
    loanRequest.setStartDate(null);
    ValidationException exception =
        org.junit.jupiter.api.Assertions.assertThrows(
            ValidationException.class, () -> planGeneratorService.generateLoanPlan(loanRequest));
    Assertions.assertThat(exception.getMessage())
        .isEqualTo("Invalid Loan Request. One or more mandatory parameters are null.");
  }

  @Test
  public void shouldThrowValidationExceptionWhenNominalRateIsNull() {
    LoanRequest loanRequest = getLoanRequest();
    loanRequest.setNominalRate(null);
    ValidationException exception =
        org.junit.jupiter.api.Assertions.assertThrows(
            ValidationException.class, () -> planGeneratorService.generateLoanPlan(loanRequest));
    Assertions.assertThat(exception.getMessage())
        .isEqualTo("Invalid Loan Request. One or more mandatory parameters are null.");
  }

  @Test
  public void shouldThrowValidationExceptionWhenAllLoanParamtersAreNull() {
    LoanRequest loanRequest = new LoanRequest();
    ValidationException exception =
        org.junit.jupiter.api.Assertions.assertThrows(
            ValidationException.class, () -> planGeneratorService.generateLoanPlan(loanRequest));
    Assertions.assertThat(exception.getMessage())
        .isEqualTo("Invalid Loan Request. One or more mandatory parameters are null.");
  }

  private LoanRequest getLoanRequest() {
    return LoanRequest.builder()
        .nominalRate(new BigDecimal(5))
        .loanAmount(new BigDecimal(5000))
        .durationInMonth(24)
        .startDate(LocalDateTime.parse("2020-05-01T00:00:01"))
        .build();
  }
}
