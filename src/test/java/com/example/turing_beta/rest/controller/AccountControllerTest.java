package com.example.turing_beta.rest.controller;

import com.example.turing_beta.repos.AccountRepo;
import com.example.turing_beta.rest.dto.account.NewAccountRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(value = "/sql/delete-currency-and-account.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/add-currency.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void itShouldAddNewAccount() throws Exception {
        //Given a new account request
        String name = "Test account";
        NewAccountRequest request = NewAccountRequest.builder()
                .name(name)
                .amount(BigDecimal.valueOf(1240.66))
                .currencyId(1L)
                .build();

        //When
        ResultActions postNewAccountActions = mockMvc.perform(post("/api/v1/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(javaToJson(request)));

        //Then
        postNewAccountActions.andExpect(status().is2xxSuccessful());
        assertThat(accountRepo.findByName(name))
                .isPresent()
                .hasValueSatisfying(acc -> {
                    assertThat(acc.getName()).isEqualTo(request.getName());
                    assertThat(acc.getAmount()).isEqualTo(request.getAmount());
                    assertThat(acc.getCurrency().getId()).isEqualTo(request.getCurrencyId());
                });
    }

    private String javaToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed Java to JSON");
            return null;
        }
    }
}