package com.bankinc.card_service.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankinc.card_service.dto.BalanceRechargeDto;
import com.bankinc.card_service.models.Card;
import com.bankinc.card_service.models.CardStatus;
import com.bankinc.card_service.repository.CardRepository;
import com.bankinc.card_service.service.CardBalanceService;

@Service
public class CardBalanceServiceImpl<T> implements CardBalanceService<T> {

	CardRepository cardRepository;

	public CardBalanceServiceImpl(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	@Override
	public ResponseEntity<Map<String, String>> balanceRecharge(BalanceRechargeDto balanceRechargeDto) {
		// TODO Auto-generated method stub
		ResponseEntity<Map<String, String>> response;
		Map<String, String> responseBody = new HashMap();
		Card card = cardRepository.findById(balanceRechargeDto.getCardId()).orElse(null);

		if (card == null) {
			responseBody.put("error", "This card doesn't exists.");
			response = new ResponseEntity<Map<String, String>>(responseBody, HttpStatus.NOT_FOUND);
		} else if (!card.getStatus().equals(CardStatus.ACTIVE)) {
			responseBody.put("error", "This card's balance can't be recharged.");
			response = new ResponseEntity<Map<String, String>>(responseBody, HttpStatus.BAD_REQUEST);
		} else {
			card.setBalance(card.getBalance() + balanceRechargeDto.getBalance());
			response = new ResponseEntity<Map<String, String>>(HttpStatus.OK);
			cardRepository.save(card);
		}

		return response;
	}

	@Override
	public ResponseEntity<Map<String, String>> getCardBalance(String cardId) {
		// TODO Auto-generated method stub
		ResponseEntity<Map<String, String>> response;
		Map<String, String> responseBody = new HashMap();
		Card card = cardRepository.findById(cardId).orElse(null);

		if (card == null) {
			responseBody.put("error", "This card doesn't exists.");
			response = new ResponseEntity<Map<String, String>>(responseBody, HttpStatus.NOT_FOUND);
		} else {
			responseBody.put("balance", String.valueOf(card.getBalance()));
			response = new ResponseEntity<Map<String, String>>(responseBody, HttpStatus.OK);
		}

		return response;
	}

}
