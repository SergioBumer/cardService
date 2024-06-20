package com.bankinc.card_service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bankinc.card_service.dto.PurchaseDTO;
import com.bankinc.card_service.dto.TransactionAnulationDto;
import com.bankinc.card_service.models.Card;
import com.bankinc.card_service.models.CardStatus;
import com.bankinc.card_service.models.Transaction;
import com.bankinc.card_service.models.TransactionStatus;
import com.bankinc.card_service.repository.CardRepository;
import com.bankinc.card_service.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {
	
	@Mock
	CardRepository cardRepository;
	
	@Mock
	TransactionRepository transactionRepository;
	
	@InjectMocks
	TransactionServiceImpl transactionServiceImpl;
	
	static PurchaseDTO purchaseDTO;
	static TransactionAnulationDto transactionAnulationDto;
	
	@BeforeAll
	static void setup() {
		purchaseDTO = PurchaseDTO.builder().cardId("1020307731398909").price(20000D).build();
		transactionAnulationDto = TransactionAnulationDto.builder().cardId("1020301234567890").transactionId("be81556b-9b54-4091-aff6-a9c3c6decf54").build();
	}
	
	@Test
	void testCardNotFound() {
		
		when(cardRepository.findById(anyString())).thenReturn(Optional.empty());
		ResponseEntity<Map<String, String>> response = transactionServiceImpl.createPurchase(purchaseDTO);
		
		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testCardNotAvailableForPurchases() {
		Card retrievedCard = new Card();
		retrievedCard.setStatus(CardStatus.BLOCKED);
		when(cardRepository.findById(anyString())).thenReturn(Optional.of(retrievedCard));
		ResponseEntity<Map<String, String>> response = transactionServiceImpl.createPurchase(purchaseDTO);
		
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This card is not available for purchases.", response.getBody().get("error"));
	}
	
	@Test
	void testCardIsUnderFunded() {
		Card retrievedCard = new Card();
		retrievedCard.setStatus(CardStatus.ACTIVE);
		retrievedCard.setBalance(10000);
		when(cardRepository.findById(anyString())).thenReturn(Optional.of(retrievedCard));
		ResponseEntity<Map<String, String>> response = transactionServiceImpl.createPurchase(purchaseDTO);
		
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Your card balance is lower than the purchase price.", response.getBody().get("error"));
	}
	
	@Test
	void testCardIsExpired() {
		Card retrievedCard = new Card();
		retrievedCard.setStatus(CardStatus.ACTIVE);
		retrievedCard.setBalance(100000);
		LocalDate expirationDate = LocalDate.now().minusYears(5);
		retrievedCard.setExpirationDate(expirationDate);
		when(cardRepository.findById(anyString())).thenReturn(Optional.of(retrievedCard));
		ResponseEntity<Map<String, String>> response = transactionServiceImpl.createPurchase(purchaseDTO);
		
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Your card is currently in EXPIRED status.", response.getBody().get("error"));
	}
	
	@Test
	void testTransactionIsCompleted() {
		Card retrievedCard = new Card();
		
		LocalDate expirationDate = LocalDate.now().plusYears(10);
		retrievedCard.setStatus(CardStatus.ACTIVE);
		retrievedCard.setExpirationDate(expirationDate);
		retrievedCard.setBalance(30000);
		when(cardRepository.findById(anyString())).thenReturn(Optional.of(retrievedCard));
		Transaction transaction = Transaction.builder().transactionId("be81556b-9b54-4091-aff6-a9c3c6decf54").build();
		when(transactionRepository.save(any())).thenReturn(transaction);
		ResponseEntity<Map<String, String>> response = transactionServiceImpl.createPurchase(purchaseDTO);
		
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("be81556b-9b54-4091-aff6-a9c3c6decf54", response.getBody().get("transactionId"));
	}
	
	@Test
	void testTransactionNotRetrieved() {
		when(transactionRepository.findById(anyString())).thenReturn(Optional.empty());
		
		ResponseEntity<Map<String, Object>> response = transactionServiceImpl.getTransaction("be81556b-9b54-4091-aff6-a9c3c6decf54");
		
		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testTransactionSuccessfullyRetrieved() {
		Transaction transaction = Transaction.builder().transactionId("be81556b-9b54-4091-aff6-a9c3c6decf54").build();
		when(transactionRepository.findById(anyString())).thenReturn(Optional.of(transaction));
		
		ResponseEntity<Map<String, Object>> response = transactionServiceImpl.getTransaction("be81556b-9b54-4091-aff6-a9c3c6decf54");
		
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	
	@Test
	void testTransactionToBeCancelledNotRetrieved() {
		when(transactionRepository.findById(anyString())).thenReturn(Optional.empty());
		
		ResponseEntity<Map<String, String>> response = transactionServiceImpl.cancelTransaction(transactionAnulationDto);
		
		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testTransactionToBeCancelledNotActive() {
		Transaction transaction = Transaction.builder().transactionId("be81556b-9b54-4091-aff6-a9c3c6decf54").transactionStatus(TransactionStatus.CANCELLED).build();
		when(transactionRepository.findById(anyString())).thenReturn(Optional.of(transaction));
		
		ResponseEntity<Map<String, String>> response = transactionServiceImpl.cancelTransaction(transactionAnulationDto);
		
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	void testTransactionToBeCancelledFailingBecauseOfPurchaseDate() {
		Transaction transaction = Transaction.builder().transactionId("be81556b-9b54-4091-aff6-a9c3c6decf54").transactionStatus(TransactionStatus.CANCELLED).purchaseDate(LocalDate.now().minusDays(20)).build();
		when(transactionRepository.findById(anyString())).thenReturn(Optional.of(transaction));
		
		ResponseEntity<Map<String, String>> response = transactionServiceImpl.cancelTransaction(transactionAnulationDto);
		
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	void testTransactionToBeCancelledIsSuccessful() {
		Card retrievedCard = new Card();
		
		LocalDate expirationDate = LocalDate.now().plusYears(10);
		retrievedCard.setStatus(CardStatus.ACTIVE);
		retrievedCard.setExpirationDate(expirationDate);
		retrievedCard.setBalance(30000);
		when(cardRepository.findById(anyString())).thenReturn(Optional.of(retrievedCard));
		Transaction transaction = Transaction.builder().transactionId("be81556b-9b54-4091-aff6-a9c3c6decf54").price(20000).purchaseDate(LocalDate.now().plusDays(1)).transactionStatus(TransactionStatus.ACTIVE).build();
		when(transactionRepository.findById(anyString())).thenReturn(Optional.of(transaction));
		when(transactionRepository.findById(anyString())).thenReturn(Optional.of(transaction));
		
		ResponseEntity<Map<String, String>> response = transactionServiceImpl.cancelTransaction(transactionAnulationDto);
		
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
