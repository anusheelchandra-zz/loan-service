package com.loan.loanservice.utils;

import com.loan.loanservice.domain.LoanRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class InterestCalculationUtilTest {

  @Test
  public void shouldCalculateInterest() {
    BigDecimal interest = InterestCalculationUtil.calculateInterest(new BigDecimal(5000), new BigDecimal(0.05));
    Assertions.assertThat(interest).isEqualTo(20.83);
  }

  @Test
  public void shouldCalculateAnnuity() {
    BigDecimal annuity = InterestCalculationUtil.calculateAnnuity(getLoanRequest());
    Assertions.assertThat(annuity).isEqualTo(219.36);
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