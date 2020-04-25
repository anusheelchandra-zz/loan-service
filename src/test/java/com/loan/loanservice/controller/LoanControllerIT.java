package com.loan.loanservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan.loanservice.domain.LoanRequest;
import com.loan.loanservice.service.LoanPlanGeneratorService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanControllerIT {

  private static final String URI = "/generate-plan";

  @Autowired private MockMvc mvc;
  @Autowired private LoanPlanGeneratorService generatorService;
  @Autowired private ObjectMapper objectMapper;

  @Test
  public void shouldReturnSuccessfullResponseWithLoanPlan() throws Exception {
    LoanRequest request = getLoanRequest();
    String valueAsString =
        objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(request);

    mvc.perform(
            MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].principal", Matchers.is(5000.0)))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[0].initialOutstandingPrincipal", Matchers.is(5000)))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[0].borrowerPaymentAmount", Matchers.is(5020.83)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].interest", Matchers.is(20.83)))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[0].remainingOutstandingPrincipal", Matchers.is(0.0)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", Matchers.is("2020-05-01T00:00:01")));
  }

  @Test
  public void shouldThrowValidationExceptionWhenAmountIsZero() throws Exception {
    LoanRequest request = getLoanRequest();
    request.setLoanAmount(BigDecimal.ZERO);
    String valueAsString =
        objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(request);

    MvcResult mvcResult =
        mvc.perform(
                MockMvcRequestBuilders.post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();
    Assertions.assertThat(mvcResult.getResolvedException())
        .isInstanceOf(MethodArgumentNotValidException.class);
    Assertions.assertThat(mvcResult.getResolvedException().getMessage())
        .contains("loanAmount must be greater than or equal to 1");
  }

  @Test
  public void shouldThrowValidationExceptionWhenAmountIsNull() throws Exception {
    LoanRequest request = getLoanRequest();
    request.setLoanAmount(null);
    String valueAsString =
        objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(request);

    MvcResult mvcResult =
        mvc.perform(
                MockMvcRequestBuilders.post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();
    Assertions.assertThat(mvcResult.getResolvedException())
        .isInstanceOf(MethodArgumentNotValidException.class);
    Assertions.assertThat(mvcResult.getResolvedException().getMessage())
        .contains("[loanAmount]]; default message [must not be null]");
  }

  @Test
  public void shouldThrowValidationExceptionWhenNominalRateIsZero() throws Exception {
    LoanRequest request = getLoanRequest();
    request.setNominalRate(BigDecimal.ZERO);
    String valueAsString =
        objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(request);

    MvcResult mvcResult =
        mvc.perform(
                MockMvcRequestBuilders.post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();
    Assertions.assertThat(mvcResult.getResolvedException())
        .isInstanceOf(MethodArgumentNotValidException.class);
    Assertions.assertThat(mvcResult.getResolvedException().getMessage())
        .contains(
            "[nominalRate],1]; default message [nominalRate(annual) must be greater than or equal to 1]");
  }

  @Test
  public void shouldThrowValidationExceptionWhenNominalRateIsNull() throws Exception {
    LoanRequest request = getLoanRequest();
    request.setNominalRate(null);
    String valueAsString =
        objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(request);

    MvcResult mvcResult =
        mvc.perform(
                MockMvcRequestBuilders.post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();
    Assertions.assertThat(mvcResult.getResolvedException())
        .isInstanceOf(MethodArgumentNotValidException.class);
    Assertions.assertThat(mvcResult.getResolvedException().getMessage())
        .contains("[nominalRate]]; default message [must not be null]");
  }

  @Test
  public void shouldThrowValidationExceptionWhenDateIsInPast() throws Exception {
    LoanRequest request = getLoanRequest();
    request.setStartDate(LocalDateTime.now().minusDays(5));
    String valueAsString =
        objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(request);

    MvcResult mvcResult =
        mvc.perform(
                MockMvcRequestBuilders.post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();
    Assertions.assertThat(mvcResult.getResolvedException())
        .isInstanceOf(MethodArgumentNotValidException.class);
    Assertions.assertThat(mvcResult.getResolvedException().getMessage())
        .contains("[startDate]]; default message [must be a date in the present or in the future]");
  }

  @Test
  public void shouldThrowValidationExceptionWhenDateIsNull() throws Exception {
    LoanRequest request = getLoanRequest();
    request.setStartDate(null);
    String valueAsString =
        objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(request);

    MvcResult mvcResult =
        mvc.perform(
                MockMvcRequestBuilders.post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();
    Assertions.assertThat(mvcResult.getResolvedException())
        .isInstanceOf(MethodArgumentNotValidException.class);
    Assertions.assertThat(mvcResult.getResolvedException().getMessage())
        .contains("[startDate]]; default message [must not be null]");
  }

  @Test
  public void shouldThrowValidationExceptionWhenDurationIsZero() throws Exception {
    LoanRequest request = getLoanRequest();
    request.setDurationInMonth(0);
    String valueAsString =
        objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(request);

    MvcResult mvcResult =
        mvc.perform(
                MockMvcRequestBuilders.post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();
    Assertions.assertThat(mvcResult.getResolvedException())
        .isInstanceOf(MethodArgumentNotValidException.class);
    Assertions.assertThat(mvcResult.getResolvedException().getMessage())
        .contains("[durationInMonth must be greater than or equal to 1]");
  }

  private LoanRequest getLoanRequest() {
    return LoanRequest.builder()
        .nominalRate(new BigDecimal(5))
        .loanAmount(new BigDecimal(5000))
        .durationInMonth(1)
        .startDate(LocalDateTime.parse("2020-05-01T00:00:01"))
        .build();
  }
}
