package com.loan.loanservice.domain;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyRepayment {

    @ApiModelProperty(notes = "Borrower payment amount", required =true)
    private BigDecimal borrowerPaymentAmount;

    @ApiModelProperty(notes = "Payment Date", required =true)
    private LocalDateTime date;

    @ApiModelProperty(notes = "Interest charged", required =true)
    private BigDecimal interest;

    @ApiModelProperty(notes = "Principal amount", required =true)
    private BigDecimal principal;

    @ApiModelProperty(notes = "Initial outstanding principal amount", required =true)
    private BigDecimal initialOutstandingPrincipal;

    @ApiModelProperty(notes = "Remaining outstanding principal amount", required =true)
    private BigDecimal remainingOutstandingPrincipal;
}
