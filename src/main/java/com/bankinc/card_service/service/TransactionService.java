package com.bankinc.card_service.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.bankinc.card_service.dto.PurchaseDTO;
import com.bankinc.card_service.dto.TransactionAnulationDto;

public interface TransactionService {
	ResponseEntity<Map<String, String>> createPurchase(PurchaseDTO purchaseDTO);

	ResponseEntity<Map<String, Object>> getTransaction(String transactionId);

	ResponseEntity<Map<String, String>> cancelTransaction(TransactionAnulationDto transactionAnulationDto);
}
