package com.loan.loanservice.controller;

import com.loan.loanservice.domain.LoanRequest;
import com.loan.loanservice.domain.MonthlyRepayment;
import com.loan.loanservice.service.LoanPlanGeneratorService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LoanControllerTest {

  @Mock private LoanPlanGeneratorService service;

  private LoanController loanController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    loanController = new LoanController(service);

    Mockito.when(service.generateLoanPlan(ArgumentMatchers.any()))
        .thenReturn(createLoanPlanResponse());
  }

  @Test
  public void shouldGenerateLoanPlan() {
    LoanRequest loanRequest = getLoanRequest();

    ResponseEntity<List<MonthlyRepayment>> responseEntity =
        loanController.generateLoanPlan(loanRequest);

    Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Assertions.assertThat(responseEntity.getBody()).isNotNull();
    Assertions.assertThat(responseEntity.getBody().size()).isEqualTo(1);
    Assertions.assertThat(responseEntity.getBody().get(0).getPrincipal())
        .isEqualTo(new BigDecimal(5000));
    Assertions.assertThat(responseEntity.getBody().get(0).getInitialOutstandingPrincipal())
        .isEqualTo(new BigDecimal(5000));
    Assertions.assertThat(responseEntity.getBody().get(0).getRemainingOutstandingPrincipal())
        .isEqualTo(BigDecimal.ZERO);
    Assertions.assertThat(responseEntity.getBody().get(0).getInterest())
        .isEqualTo(new BigDecimal("20.83"));
    Assertions.assertThat(responseEntity.getBody().get(0).getBorrowerPaymentAmount())
        .isEqualTo(new BigDecimal("5020.83"));
    Assertions.assertThat(responseEntity.getBody().get(0).getDate())
        .isEqualTo(LocalDateTime.parse("2020-05-01T00:00:01"));
  }

  private List<MonthlyRepayment> createLoanPlanResponse() {
    return Collections.singletonList(
        MonthlyRepayment.builder()
            .date(LocalDateTime.parse("2020-05-01T00:00:01"))
            .borrowerPaymentAmount(new BigDecimal("5020.83"))
            .interest(new BigDecimal("20.83"))
            .principal(new BigDecimal(5000))
            .initialOutstandingPrincipal(new BigDecimal(5000))
            .remainingOutstandingPrincipal(BigDecimal.ZERO)
            .build());
  }

  private LoanRequest getLoanRequest() {
    return LoanRequest.builder()
        .nominalRate(new BigDecimal(5))
        .loanAmount(new BigDecimal(5000))
        .durationInMonth(1)
        .startDate(LocalDateTime.now())
        .build();
  }
}
