package com.example.bankingapi;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class BankingApiIntegrationTest {
 @Autowired MockMvc mockMvc;
 @Autowired ObjectMapper objectMapper;
 private long newAccount() throws Exception {
  MvcResult r=mockMvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON)
   .content("{\"name\":\"Test User\"}")).andExpect(status().isOk()).andReturn();
  JsonNode j=objectMapper.readTree(r.getResponse().getContentAsString()); return j.get("id").asLong();
 }
 @Test void TC01_createAccount() throws Exception {
  mockMvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Rahul\"}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.id").isNumber()).andExpect(jsonPath("$.name").value("Rahul")).andExpect(jsonPath("$.balance").value(0));
 }
 @Test void TC02_positiveDeposit() throws Exception {
  long id=newAccount();
  mockMvc.perform(post("/api/accounts/"+id+"/deposit").contentType(MediaType.APPLICATION_JSON).content("{\"amount\":5000}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.balance").value(5000));
 }
 @Test void TC03_withdrawal() throws Exception {
  long id=newAccount();
  mockMvc.perform(post("/api/accounts/"+id+"/deposit").contentType(MediaType.APPLICATION_JSON).content("{\"amount\":5000}")).andExpect(status().isOk());
  mockMvc.perform(post("/api/accounts/"+id+"/withdraw").contentType(MediaType.APPLICATION_JSON).content("{\"amount\":1000}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.balance").value(4000));
 }
 @Test void TC04_getBalance() throws Exception {
  long id=newAccount();
  mockMvc.perform(post("/api/accounts/"+id+"/deposit").contentType(MediaType.APPLICATION_JSON).content("{\"amount\":2500}")).andExpect(status().isOk());
  mockMvc.perform(get("/api/accounts/"+id+"/balance")).andExpect(status().isOk()).andExpect(content().string("2500"));
 }
 @Test void TC05_negativeDeposit() throws Exception {
  long id=newAccount();
  mockMvc.perform(post("/api/accounts/"+id+"/deposit").contentType(MediaType.APPLICATION_JSON).content("{\"amount\":-500}")).andExpect(status().isBadRequest());
 }
 @Test void TC06_zeroDeposit() throws Exception {
  long id=newAccount();
  mockMvc.perform(post("/api/accounts/"+id+"/deposit").contentType(MediaType.APPLICATION_JSON).content("{\"amount\":0}")).andExpect(status().isBadRequest());
 }
 @Test void TC07_insufficientFunds() throws Exception {
  long id=newAccount();
  mockMvc.perform(post("/api/accounts/"+id+"/withdraw").contentType(MediaType.APPLICATION_JSON).content("{\"amount\":10000}")).andExpect(status().isBadRequest());
 }
 @Test void TC08_unknownAccount() throws Exception {
  mockMvc.perform(get("/api/accounts/999999/balance")).andExpect(status().isNotFound());
 }
 @Test void TC09_missingAmount() throws Exception {
  long id=newAccount();
  mockMvc.perform(post("/api/accounts/"+id+"/deposit").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
 }
 @Test void TC10_blankName() throws Exception {
  mockMvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"   \"}")).andExpect(status().isBadRequest());
 }
}
