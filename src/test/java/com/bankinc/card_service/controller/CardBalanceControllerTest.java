package com.bankinc.card_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import com.bankinc.card_service.service.CardBalanceService;
import com.bankinc.card_service.service.CardService;

@WebMvcTest(CardBalanceController.class)
@ExtendWith(MockitoExtension.class)
public class CardBalanceControllerTest<T> {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	CardBalanceService<T> cardService;

	@InjectMocks
	CardController cardController;

	@Test
	void testBalanceSuccessfullyRecharged() throws Exception {
		when(cardService.balanceRecharge(any())).thenReturn(new ResponseEntity<Map<String, String>>(HttpStatus.OK));
		String requestBody = "{\"cardId\": \"1234567890123456\", \"balance\": 20000.0}";
		this.mockMvc.perform(post("/card/balance").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isOk());
	}

	@Test
	void testBalanceSuccessfullyRetrieved() throws Exception {
		var response = new HashMap<String, String>();
		response.put("balance", "20000.0");
		when(cardService.getCardBalance(anyString()))
				.thenReturn(new ResponseEntity<Map<String, String>>(response, HttpStatus.OK));
		this.mockMvc.perform(get("/card/balance/102030")).andExpect(status().isOk())
				.andExpect(jsonPath("$.balance").value("20000.0"));
	}
}
