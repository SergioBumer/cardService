package com.bankinc.card_service.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankinc.card_service.dto.PurchaseDTO;
import com.bankinc.card_service.models.Card;
import com.bankinc.card_service.models.CardStatus;
import com.bankinc.card_service.models.Transaction;
import com.bankinc.card_service.models.TransactionStatus;
import com.bankinc.card_service.repository.CardRepository;
import com.bankinc.card_service.repository.TransactionRepository;
import com.bankinc.card_service.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.json.JsonMapper.Builder;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class TransactionServiceImpl implements TransactionService {

	CardRepository cardRepository;
	TransactionRepository transactionRepository;

	public TransactionServiceImpl(CardRepository cardRepository, TransactionRepository transactionRepository) {
		this.cardRepository = cardRepository;
		this.transactionRepository = transactionRepository;
	}

	@Override
	public ResponseEntity<Map<String, String>> createPurchase(PurchaseDTO purchaseDTO) {
		// TODO Auto-generated method stub
		ResponseEntity<Map<String, String>> response;
		Map<String, String> responseBody = new HashMap();
		Card card = cardRepository.findById(purchaseDTO.getCardId()).orElse(null);
		LocalDate purchaseDate = LocalDate.now();
		if (card == null) {
			responseBody.put("error", "This card doesn't exists.");
			return new ResponseEntity<Map<String, String>>(responseBody, HttpStatus.NOT_FOUND);
		} else if (!CardStatus.ACTIVE.equals(card.getStatus())) {
			responseBody.put("error", "This card is not available for purchases.");
			return new ResponseEntity<Map<String, String>>(responseBody, HttpStatus.BAD_REQUEST);
		} else if (underfunded(card, purchaseDTO.getPrice())) {
			responseBody.put("error", "Your card balance is lower than the purchase price.");
			return new ResponseEntity<Map<String, String>>(responseBody, HttpStatus.BAD_REQUEST);
		} else if (purchaseDate.isAfter(card.getExpirationDate())) {
			setCardStatusToExpired(card);
			responseBody.put("error", "Your card is currently in EXPIRED status.");
			return new ResponseEntity<Map<String, String>>(responseBody, HttpStatus.BAD_REQUEST);
		}

		String transactionId = createTransaction(card, purchaseDate, purchaseDTO.getPrice());

		updateCardBalance(card, purchaseDTO.getPrice());

		responseBody.put("transactionId", transactionId);
		response = new ResponseEntity<Map<String, String>>(responseBody, HttpStatus.OK);

		return response;
	}

	private boolean underfunded(Card card, double price) {
		return card.getBalance() < price;
	}

	private void updateCardBalance(Card card, double price) {
		card.setBalance(card.getBalance() - price);
		cardRepository.save(card);
	}

	private String createTransaction(Card card, LocalDate purchaseDate, double price) {
		Transaction transaction = Transaction.builder().cardId(card.getCardId()).price(price).purchaseDate(purchaseDate)
				.transactionStatus(TransactionStatus.ACTIVE).build();
		Transaction savedTransaction = transactionRepository.save(transaction);
		return String.valueOf(savedTransaction.getTransactionId());
	}

	private void setCardStatusToExpired(Card card) {
		card.setStatus(CardStatus.EXPIRED);
		cardRepository.save(card);
	}

	@Override
	public ResponseEntity<Map<String, Object>> getTransaction(String transactionId) {
		// TODO Auto-generated method stub
		Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
		System.out.println(transaction);
		if(transaction == null) {
			Map<String, Object> responseBody = new HashMap<String, Object>();
			responseBody.put("error", "This transactionId doesn't exists.");
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
		}
		JsonMapper mapObject = JsonMapper.builder()
			    .addModule(new JavaTimeModule()).build();
		Map < String, Object > mapObj = mapObject.convertValue(transaction, Map.class);
		return new ResponseEntity<Map<String, Object>>(mapObj, HttpStatus.OK);
	}

}
