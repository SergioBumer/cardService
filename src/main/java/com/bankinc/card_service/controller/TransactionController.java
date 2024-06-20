package com.bankinc.card_service.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankinc.card_service.dto.PurchaseDTO;
import com.bankinc.card_service.service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
	
	TransactionService transactionService;
	
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@PostMapping("/purchase")
	public ResponseEntity<Map<String, String>> makeAPurchase(@RequestBody PurchaseDTO purchaseDTO) {
		return transactionService.createPurchase(purchaseDTO);	
	}
	
	@GetMapping("/{transactionId}")
	public ResponseEntity<?> getTransaction(@PathVariable String transactionId) {
		return transactionService.getTransaction(transactionId);
		
	}
}
