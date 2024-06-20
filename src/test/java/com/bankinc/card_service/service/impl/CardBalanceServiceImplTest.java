package com.bankinc.card_service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bankinc.card_service.dto.BalanceRechargeDto;
import com.bankinc.card_service.models.Card;
import com.bankinc.card_service.models.CardStatus;
import com.bankinc.card_service.repository.CardRepository;

@ExtendWith(MockitoExtension.class)
public class CardBalanceServiceImplTest<T> {

	@Mock
	CardRepository cardRepository;

	@InjectMocks
	CardBalanceServiceImpl<T> cardService;

	@Test
	void testCardNotFoundForBeingRecharged() {
		when(cardRepository.findById(anyString())).thenReturn(Optional.empty());
		BalanceRechargeDto balanceRechargeDto = BalanceRechargeDto.builder().cardId("1020307731398909").balance(20000D)
				.build();
		ResponseEntity<Map<String, String>> response = cardService.balanceRecharge(balanceRechargeDto);

		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void testCardBalanceCannotBeRecharged() {
		BalanceRechargeDto balanceRechargeDto = BalanceRechargeDto.builder().cardId("1020307731398909").balance(20000D)
				.build();
		Card retrievedCard = new Card();
		retrievedCard.setStatus(CardStatus.BLOCKED);
		when(cardRepository.findById(anyString())).thenReturn(Optional.of(retrievedCard));
		ResponseEntity<Map<String, String>> response = cardService.balanceRecharge(balanceRechargeDto);

		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testCardBalanceSuccessfullyRecharged() {
		Card retrievedCard = new Card();
		retrievedCard.setStatus(CardStatus.ACTIVE);
		retrievedCard.setBalance(0);
		BalanceRechargeDto balanceRechargeDto = BalanceRechargeDto.builder().cardId("1020307731398909").balance(20000D)
				.build();
		when(cardRepository.findById(anyString())).thenReturn(Optional.of(retrievedCard));
		ResponseEntity<Map<String, String>> response = cardService.balanceRecharge(balanceRechargeDto);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testCardNotFound() {
		when(cardRepository.findById(anyString())).thenReturn(Optional.empty());
		ResponseEntity<Map<String, String>> response = cardService.getCardBalance("1020307731398909");

		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void testCardBalanceSuccessfullyRetrieved() {
		Card retrievedCard = new Card();
		retrievedCard.setStatus(CardStatus.ACTIVE);
		retrievedCard.setBalance(20000D);

		when(cardRepository.findById(anyString())).thenReturn(Optional.of(retrievedCard));
		ResponseEntity<Map<String, String>> response = cardService.getCardBalance("1020307731398909");

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("20000.0", response.getBody().get("balance"));
	}

}
