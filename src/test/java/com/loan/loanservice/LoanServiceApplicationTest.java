package com.loan.loanservice;

import com.loan.loanservice.controller.LoanController;
import com.loan.loanservice.service.LoanPlanGeneratorService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoanServiceApplicationTest {

  @Autowired private LoanController loanController;

  @Autowired private LoanPlanGeneratorService generatorService;

  @Test
  public void shouldLoadContext() {
    Assertions.assertThat(loanController).isNotNull();
    Assertions.assertThat(generatorService).isNotNull();
  }
}
