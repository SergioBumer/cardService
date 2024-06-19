package com.bankinc.card_service.service.impl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankinc.card_service.dto.CardNumberDto;
import com.bankinc.card_service.models.Card;
import com.bankinc.card_service.models.CardStatus;
import com.bankinc.card_service.repository.CardRepository;
import com.bankinc.card_service.service.CardService;

@Service
public class CardServiceImpl implements CardService {
	private final int DEBIT_CARD_ID = 102030;
	private final int CREDIT_CARD_ID = 152535;
	
	CardRepository cardRepository;
	
	public CardServiceImpl(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	@Override
	public String createCard(int productId) {
		if (!(productId == DEBIT_CARD_ID || productId == CREDIT_CARD_ID)) {
			return "0";
		}

		String cardId = generateCardId(productId);
		
		while(cardRepository.findById(cardId).isPresent()) {
			cardId = generateCardId(productId);
		}

		// Add 3 years to the current date
		LocalDate newDate = LocalDate.now().plusYears(3);
		Card card = new Card();
		card.setCardId(cardId);
		card.setOwnerFirstName("");
		card.setOwnerLastName("");
		card.setStatus(CardStatus.INACTIVE);
		card.setBalance(0d);
		card.setExpirationDate(newDate);
		
		cardRepository.save(card);
		return cardId;
	}

	private String generateCardId(int productId) {
		Random random = new Random();
		long randomTenDigitNumber = (long) (1000000000L + random.nextDouble() * 9000000000L);
		return String.valueOf(productId) + String.valueOf(randomTenDigitNumber);

	}

	@Override
	public ResponseEntity<Map<String, String>> enrollCard(String cardId) {
		// TODO Auto-generated method stub
		ResponseEntity<Map<String, String>> response;
		Map<String, String> responseBody = new HashMap();
		Card card = cardRepository.findById(cardId).orElse(null);
		
		CardStatus[] notValidCardStatus = {CardStatus.ACTIVE, CardStatus.BLOCKED, CardStatus.EXPIRED};
		
		if (card == null) {
			responseBody.put("error", "This card doesn't exists.");
			response = new ResponseEntity<Map<String,String>>(responseBody, HttpStatus.NOT_FOUND);
		} else if (Arrays.asList(notValidCardStatus).contains(card.getStatus())) {
			responseBody.put("error", "This card can not be activated.");
			response = new ResponseEntity<Map<String,String>>(responseBody, HttpStatus.BAD_REQUEST);
		} else {
			card.setStatus(CardStatus.ACTIVE);
			response = new ResponseEntity<Map<String,String>>(HttpStatus.OK);
			cardRepository.save(card);
		}
		
		return response;
	}
	
	@Override
	public ResponseEntity<Map<String, String>> blockCard(String cardId) {
		// TODO Auto-generated method stub
		ResponseEntity<Map<String, String>> response;
		Map<String, String> responseBody = new HashMap();
		Card card = cardRepository.findById(cardId).orElse(null);
		
		
		if (card == null) {
			responseBody.put("error", "This card doesn't exists.");
			response = new ResponseEntity<Map<String,String>>(responseBody, HttpStatus.NOT_FOUND);
		} else if (CardStatus.BLOCKED.equals(card.getStatus())) {
			responseBody.put("error", "This card is already blocked.");
			response = new ResponseEntity<Map<String,String>>(responseBody, HttpStatus.BAD_REQUEST);
		} else {
			card.setStatus(CardStatus.BLOCKED);
			response = new ResponseEntity<Map<String,String>>(HttpStatus.OK);
			cardRepository.save(card);
		}
		
		return response;
	}

}
