package com.bankinc.card_service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
