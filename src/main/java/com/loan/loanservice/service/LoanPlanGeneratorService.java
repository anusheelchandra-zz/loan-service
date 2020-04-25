package com.loan.loanservice.service;

import com.loan.loanservice.domain.LoanRequest;
import com.loan.loanservice.domain.MonthlyRepayment;
import com.loan.loanservice.utils.InterestCalculationUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class LoanPlanGeneratorService {

  public List<MonthlyRepayment> generateLoanPlan(LoanRequest loanRequest) {
    sanityCheckLoanRequest(loanRequest);
    List<MonthlyRepayment> monthlyRepaymentList = new ArrayList<>();

    BigDecimal annuity = InterestCalculationUtil.calculateAnnuity(loanRequest);
    BigDecimal loanAmount = loanRequest.getLoanAmount();
    BigDecimal nominalInterestRate = getNominalRate(loanRequest);
    BigDecimal calculatedInterest =
        InterestCalculationUtil.calculateInterest(loanAmount, nominalInterestRate);
    BigDecimal principal = evaluatePrincipal(annuity, calculatedInterest, loanAmount);

    MonthlyRepayment firstMonthlyRepayment =
        MonthlyRepayment.builder()
            .borrowerPaymentAmount(principal.add(calculatedInterest))
            .date(loanRequest.getStartDate())
            .initialOutstandingPrincipal(loanAmount)
            .interest(calculatedInterest)
            .principal(principal)
            .remainingOutstandingPrincipal(loanAmount.subtract(principal))
            .build();

    monthlyRepaymentList.add(firstMonthlyRepayment);

    generateMonthlyRepayments(
        monthlyRepaymentList, firstMonthlyRepayment, nominalInterestRate, annuity);

    return monthlyRepaymentList;
  }

  private void sanityCheckLoanRequest(LoanRequest loanRequest) {
    if (loanRequest.getNominalRate() != null
        && loanRequest.getDurationInMonth() != null
        && loanRequest.getLoanAmount() != null
        && loanRequest.getStartDate() != null) {
      return ;
    }
    throw new ValidationException("Invalid Loan Request. One or more mandatory parameters are null.");
  }

  private BigDecimal getNominalRate(LoanRequest loanRequest) {
    return loanRequest.getNominalRate().divide(new BigDecimal(100), 20, RoundingMode.HALF_DOWN);
  }

  private void generateMonthlyRepayments(
      List<MonthlyRepayment> monthlyRepaymentList,
      MonthlyRepayment payment,
      BigDecimal nominalRate,
      BigDecimal annuity) {
    if (payment.getRemainingOutstandingPrincipal().doubleValue() > 0) {
      BigDecimal initialOutstandingPrincipal = payment.getRemainingOutstandingPrincipal();
      BigDecimal calculateInterest =
          InterestCalculationUtil.calculateInterest(initialOutstandingPrincipal, nominalRate);
      BigDecimal principal =
          evaluatePrincipal(annuity, calculateInterest, initialOutstandingPrincipal);
      BigDecimal borrowerPaymentAmount = calculateInterest.add(principal);

      MonthlyRepayment newMonthlyPayment =
          MonthlyRepayment.builder()
              .date(payment.getDate().plusMonths(1))
              .interest(calculateInterest)
              .initialOutstandingPrincipal(initialOutstandingPrincipal)
              .borrowerPaymentAmount(borrowerPaymentAmount)
              .remainingOutstandingPrincipal(initialOutstandingPrincipal.subtract(principal))
              .build();

      sanityCheckAndUpdatePrincipal(calculateInterest, principal, newMonthlyPayment);

      monthlyRepaymentList.add(newMonthlyPayment);

      generateMonthlyRepayments(monthlyRepaymentList, newMonthlyPayment, nominalRate, annuity);
    }
  }

  private BigDecimal evaluatePrincipal(
      BigDecimal annuity, BigDecimal calculatedInterest, BigDecimal initialOutstandingPrincipal) {
    BigDecimal principal = annuity.subtract(calculatedInterest);
    return calculatedInterest.compareTo(initialOutstandingPrincipal) > 0
        ? initialOutstandingPrincipal
        : principal;
  }

  private void sanityCheckAndUpdatePrincipal(
      BigDecimal calculateInterest, BigDecimal principal, MonthlyRepayment newMonthlyPayment) {
    BigDecimal remainingOutstandingPrincipal =
        newMonthlyPayment.getInitialOutstandingPrincipal().subtract(principal);

    if (remainingOutstandingPrincipal.compareTo(BigDecimal.ZERO) <= 0) {
      principal = principal.add(newMonthlyPayment.getRemainingOutstandingPrincipal());
      newMonthlyPayment.setBorrowerPaymentAmount(
          newMonthlyPayment
              .getBorrowerPaymentAmount()
              .add(newMonthlyPayment.getRemainingOutstandingPrincipal()));
      newMonthlyPayment.setRemainingOutstandingPrincipal(BigDecimal.ZERO);
    }

    BigDecimal diffPrincipal =
        newMonthlyPayment.getRemainingOutstandingPrincipal().subtract(principal);

    if (diffPrincipal.compareTo(BigDecimal.ZERO) < 0) {
      principal = principal.add(newMonthlyPayment.getRemainingOutstandingPrincipal());
      newMonthlyPayment.setBorrowerPaymentAmount(calculateInterest.add(principal));
      newMonthlyPayment.setRemainingOutstandingPrincipal(BigDecimal.ZERO);
    }

    newMonthlyPayment.setPrincipal(principal);
  }
}
