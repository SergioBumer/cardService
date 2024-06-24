package com.bankinc.card_service.controller;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankinc.card_service.dto.PurchaseDto;
import com.bankinc.card_service.dto.TransactionAnulationDto;
import com.bankinc.card_service.service.TransactionService;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Transaction Controller", description = "Transaction controller to create, retrieve and cancel transactions with cards")
public class TransactionController {
	
	TransactionService transactionService;
	
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@PostMapping("/purchase")
	@Operation(summary = "Create a transaction", description = "Creates a new purchase and returns the transaction Id.")

	public ResponseEntity<Map<String, String>> makeAPurchase(@RequestBody PurchaseDto purchaseDTO) {
		return transactionService.createPurchase(purchaseDTO);	
	}

	@GetMapping("/{transactionId}")
	@Operation(summary = "Query a transaction", description = "Queries a transaction information.")
	public ResponseEntity<?> getTransaction(@PathVariable String transactionId) {
		return transactionService.getTransaction(transactionId);
		
	}
	
	@PostMapping("/anulation")
	@Operation(summary = "Cancel a transaction", description = "Cancel an active transaction and increase the card balance.")
	public ResponseEntity<Map<String, String>> makeAPurchase(@RequestBody TransactionAnulationDto transactionAnulationDto) {
		return transactionService.cancelTransaction(transactionAnulationDto);	
	}
}
