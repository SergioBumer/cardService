package com.bankinc.card_service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bankinc.card_service.models.Card;
import com.bankinc.card_service.models.CardStatus;
import com.bankinc.card_service.repository.CardRepository;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {
	private final int DEBIT_CARD_ID = 102030;
	private final int CREDIT_CARD_ID = 152535;
	
	@Mock
	CardRepository cardRepository;
	
	@InjectMocks
	CardServiceImpl cardService;
	
	@Test
	void testReturnDebitCard() {
		when(cardRepository.findById(anyString())).thenReturn(Optional.empty());
		String cardId = cardService.createCard(DEBIT_CARD_ID);
        String regex = "^"+DEBIT_CARD_ID+"\\d{10}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cardId);
        assertTrue(matcher.matches());
		
	}
	
	@Test
	void testReturnCreditCard() {
		when(cardRepository.findById(anyString())).thenReturn(Optional.empty());
		String cardId = cardService.createCard(CREDIT_CARD_ID);
		String regex = "^"+CREDIT_CARD_ID+"\\d{10}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cardId);
        assertTrue(matcher.matches());
	}
	
	@Test
	void testProductIdNotValid() {
		String cardId = cardService.createCard(122131);
		assertNotNull(cardId);
		assertEquals("0", cardId);
	}
	
	@Test
	void testCardNotFoundForEnroll() {
		when(cardRepository.findById(anyString())).thenReturn(Optional.empty());
		ResponseEntity<Map<String, String>> response = cardService.enrollCard("1020307731398909");
		
		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testCardCantBeActivated() {
		Card retrievedCard = new Card();
		retrievedCard.setStatus(CardStatus.ACTIVE);
		when(cardRepository.findById(anyString())).thenReturn(Optional.of(retrievedCard));
		ResponseEntity<Map<String, String>> response = cardService.enrollCard("1020307731398909");
		
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	void testCardSuccessfullyActivated() {
		Card retrievedCard = new Card();
		retrievedCard.setStatus(CardStatus.INACTIVE);
		when(cardRepository.findById(anyString())).thenReturn(Optional.of(retrievedCard));
		ResponseEntity<Map<String, String>> response = cardService.enrollCard("1020307731398909");
		
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testCardNotFoundForBlock() {
		when(cardRepository.findById(anyString())).thenReturn(Optional.empty());
		ResponseEntity<Map<String, String>> response = cardService.blockCard("1020307731398909");
		
		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testCardAlreadyBlocked() {
		Card retrievedCard = new Card();
		retrievedCard.setStatus(CardStatus.BLOCKED);
		when(cardRepository.findById(anyString())).thenReturn(Optional.of(retrievedCard));
		ResponseEntity<Map<String, String>> response = cardService.blockCard("1020307731398909");
		
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	void testCardSuccessfullyBlocked() {
		Card retrievedCard = new Card();
		retrievedCard.setStatus(CardStatus.ACTIVE);
		when(cardRepository.findById(anyString())).thenReturn(Optional.of(retrievedCard));
		ResponseEntity<Map<String, String>> response = cardService.blockCard("1020307731398909");
		
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
