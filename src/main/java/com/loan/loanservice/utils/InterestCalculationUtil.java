package com.loan.loanservice.utils;

import com.loan.loanservice.domain.LoanRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InterestCalculationUtil {

    public BigDecimal calculateInterest(BigDecimal currentValue, BigDecimal nominalRate) {
        // interest = (nominal rate * days in months * amount) / days in an year.
        BigDecimal top = nominalRate.multiply(new BigDecimal(30)).multiply(currentValue);
        return top.divide(new BigDecimal(360), 2, RoundingMode.HALF_DOWN);
    }

    public BigDecimal calculateAnnuity(LoanRequest loanRequest) {
        BigDecimal nominalMonthlyRate =
            loanRequest.getNominalRate().divide(new BigDecimal(100 * 12), 20, RoundingMode.HALF_DOWN);
        Integer duration = loanRequest.getDurationInMonth();

        BigDecimal top = nominalMonthlyRate.multiply(loanRequest.getLoanAmount());
        BigDecimal bottom = BigDecimal.ONE.subtract(calculateRatePower(nominalMonthlyRate, duration));
        return top.divide(bottom, 2, RoundingMode.HALF_DOWN);
    }

    private BigDecimal calculateRatePower(BigDecimal monthlyInterestRate, int duration) {
        BigDecimal onePlusMonthlyInterestRate = BigDecimal.ONE.add(monthlyInterestRate);
        return BigDecimal.ONE.divide(onePlusMonthlyInterestRate.pow(duration), 20, RoundingMode.HALF_DOWN);
    }
}
