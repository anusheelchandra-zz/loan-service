package com.loan.loanservice.domain;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {

  @NotNull(message = "Loan duration must not null")
  @Min(value = 1, message = "durationInMonth must be greater than or equal to 1")
  @ApiModelProperty(
      notes = "Loan duration in months and must be  greater than or equal to 1",
      required = true)
  private Integer durationInMonth;

  @NotNull(message = "NominalRate(annual) must not null")
  @Min(value = 1, message = "nominalRate(annual) must be greater than or equal to 1")
  @ApiModelProperty(notes = "Nominal(annual)interest rate", required = true)
  private BigDecimal nominalRate;

  @NotNull(message = "Loan amount must not null")
  @Min(value = 1, message = "loanAmount must be greater than or equal to 1")
  @ApiModelProperty(notes = "Loan amount and  greater than or equal to 1", required = true)
  private BigDecimal loanAmount;

  @NotNull(message = "Start date must not null")
  @Future
  @ApiModelProperty(notes = "Loan start date and must be in future", required = true)
  private LocalDateTime startDate;
}
