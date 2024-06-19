package com.bankinc.card_service.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
}
