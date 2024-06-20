package com.bankinc.card_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.bankinc.card_service.service.TransactionService;

@WebMvcTest(TransactionController.class)
@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest<T> {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	TransactionService transactionService;

	@InjectMocks
	TransactionController transactionController;

	@Test
	void testBalanceSuccessfullyRecharged() throws Exception {
		Map<String, String> responseBody = new HashMap<String, String>();
		responseBody.put("transactionId", "1af90edc-82d5-41df-9a07-118e8dc5ee73");
		when(transactionService.createPurchase(any()))
				.thenReturn(new ResponseEntity<Map<String, String>>(responseBody, HttpStatus.OK));
		String requestBody = "{\"cardId\": \"1234567890123456\", \"price\": 20000.0}";
		this.mockMvc.perform(post("/transaction/purchase").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isOk());
	}
	
	@Test
	void testRetrieveTransaction() throws Exception {
		Map<String, Object> responseBody = new HashMap<String, Object>();
		responseBody.put("transactionId", "1af90edc-82d5-41df-9a07-118e8dc5ee73");
		when(transactionService.getTransaction(anyString()))
				.thenReturn(new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK));
		this.mockMvc.perform(get("/transaction/1af90edc-82d5-41df-9a07-118e8dc5ee73").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	void testBalanceCancelTransaction() throws Exception {
		when(transactionService.createPurchase(any()))
				.thenReturn(new ResponseEntity(HttpStatus.OK));
		String requestBody = "{\"cardId\": \"1234567890123456\", \"transactionId\": \"1af90edc-82d5-41df-9a07-118e8dc5ee73\"}";
		this.mockMvc.perform(post("/transaction/anulation").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isOk());
	}
}
