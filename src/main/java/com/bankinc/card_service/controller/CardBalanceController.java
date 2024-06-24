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

import com.bankinc.card_service.dto.BalanceRechargeDto;
import com.bankinc.card_service.service.CardBalanceService;

@RequestMapping("/card/balance")
@RestController
@Tag(name = "Card Balance Controller", description = "Card controller to manage balance in debit and credit cards")
public class CardBalanceController {
	CardBalanceService<?> cardBalanceService;
	public CardBalanceController(CardBalanceService<?> cardBalanceService) {
		this.cardBalanceService = cardBalanceService;
		// TODO Auto-generated constructor stub
	}

	@PostMapping
	@Operation(summary = "Recharge the card's balance", description = "Increases the value of the card's balance")
	public ResponseEntity<Map<String, String>> balanceRecharge(@RequestBody BalanceRechargeDto balanceRechargeDto) {
		return cardBalanceService.balanceRecharge(balanceRechargeDto);	
	}
	
	@GetMapping("/{cardId}")
	@Operation(summary = "Retrieve the card's available balance", description = "Return the card's balance")
	public ResponseEntity<Map<String, String>> getCardBalance(@PathVariable String cardId) {
		return cardBalanceService.getCardBalance(cardId);	
	}
}
