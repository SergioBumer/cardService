package com.bankinc.card_service.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.bankinc.card_service.service.CardService;

@WebMvcTest(CardController.class)
@ExtendWith(MockitoExtension.class)
public class CardControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	CardService cardService;

	@InjectMocks
	CardController cardController;
	
	@Test
	void testSuccesfullyCreateANewCard() throws Exception {
		when(cardService.createCard(anyInt())).thenReturn("1020301234567890");
		this.mockMvc.perform(get("/card/102030/number")).andExpect(status().isOk());
	}
	
	@Test
	void testFailedCreateANewCard() throws Exception {
		when(cardService.createCard(anyInt())).thenReturn("0");
		this.mockMvc.perform(get("/card/100000/number")).andExpect(status().isBadRequest());
	}
	
	@Test
	void testSuccesfulCardActivation() throws Exception {
		when(cardService.enrollCard(anyString())).thenReturn(new ResponseEntity<Map<String, String>>(HttpStatus.OK));
		String requestBody = "{\"cardId\": \"1234567890123456\"}";
		this.mockMvc.perform(post("/card/enroll").contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isOk());
	}
	
	@Test
	void testSuccesfulCardBlock() throws Exception {
		when(cardService.blockCard(anyString())).thenReturn(new ResponseEntity<Map<String, String>>(HttpStatus.OK));
		this.mockMvc.perform(delete("/card/102030")).andExpect(status().isOk());
	}
}
