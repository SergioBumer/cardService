package com.bankinc.card_service.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankinc.card_service.dto.PurchaseDto;
import com.bankinc.card_service.dto.TransactionAnulationDto;
import com.bankinc.card_service.models.Card;
import com.bankinc.card_service.models.CardStatus;
import com.bankinc.card_service.models.Transaction;
import com.bankinc.card_service.models.TransactionStatus;
import com.bankinc.card_service.repository.CardRepository;
import com.bankinc.card_service.repository.TransactionRepository;
import com.bankinc.card_service.service.TransactionService;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
	public ResponseEntity<Map<String, String>> createPurchase(PurchaseDto purchaseDTO) {
		// TODO Auto-generated method stub
		ResponseEntity<Map<String, String>> response;
		Map<String, String> responseBody = new HashMap<>();
		Card card = cardRepository.findById(purchaseDTO.getCardId()).orElse(null);
		LocalDateTime purchaseDate = LocalDateTime.now();
		if (card == null) {
			responseBody.put("error", "This card doesn't exists.");
			return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
		} else if (!CardStatus.ACTIVE.equals(card.getStatus())) {
			responseBody.put("error", "This card is not available for purchases.");
			return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
		} else if (underfunded(card, purchaseDTO.getPrice())) {
			responseBody.put("error", "Your card balance is lower than the purchase price.");
			return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
		} else if (purchaseDate.isAfter(card.getExpirationDate())) {
			setCardStatusToExpired(card);
			responseBody.put("error", "Your card is currently in EXPIRED status.");
			return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
		}

		String transactionId = createTransaction(card, purchaseDate, purchaseDTO.getPrice());

		dicreaseCardBalance(card, purchaseDTO.getPrice());

		responseBody.put("transactionId", transactionId);
		response = new ResponseEntity<>(responseBody, HttpStatus.OK);

		return response;
	}

	private boolean underfunded(Card card, double price) {
		return card.getBalance() < price;
	}

	private void dicreaseCardBalance(Card card, double price) {
		card.setBalance(card.getBalance() - price);
		cardRepository.save(card);
	}
	
	private void increaseCardBalance(Card card, double price) {
		card.setBalance(card.getBalance() + price);
		cardRepository.save(card);
	}

	private String createTransaction(Card card, LocalDateTime purchaseDate, double price) {
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
	public ResponseEntity getTransaction(String transactionId) {
		// TODO Auto-generated method stub
		Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
		System.out.println(transaction);
		if(transaction == null) {
			Map<String, Object> responseBody = new HashMap<String, Object>();
			responseBody.put("error", "This transactionId doesn't exists.");
			return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
		}
		JsonMapper mapObject = JsonMapper.builder()
			    .addModule(new JavaTimeModule()).build();
		Map mapObj = mapObject.convertValue(transaction, Map.class);
		return new ResponseEntity<>(mapObj, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Map<String, String>> cancelTransaction(TransactionAnulationDto transactionAnulationDto) {
		Transaction transaction = transactionRepository.findById(transactionAnulationDto.getTransactionId()).orElse(null);
		Map<String, String> responseBody = new HashMap<String, String>();
		if(transaction == null) {
			responseBody.put("error", "This transactionId doesn't exists.");
			return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
		} 
		
		if(!transaction.getTransactionStatus().equals(TransactionStatus.ACTIVE)) {
			responseBody.put("error", "This transactionId is not active.");
			return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
		}

		LocalDateTime todayMinus24Hours = LocalDateTime.now().minusDays(1);
		
		if(transaction.getPurchaseDate().isBefore(todayMinus24Hours)) {
			responseBody.put("error", "This transaction cannot be cancelled because of the time window.");
			return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
		}
		
		transaction.setTransactionStatus(TransactionStatus.CANCELED);
		transactionRepository.save(transaction);
		
		Card card = cardRepository.findById(transactionAnulationDto.getCardId()).get();
		increaseCardBalance(card, transaction.getPrice());
		cardRepository.save(card);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
