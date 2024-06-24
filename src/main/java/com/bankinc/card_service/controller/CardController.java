package com.bankinc.card_service.controller;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankinc.card_service.dto.CardNumberDto;
import com.bankinc.card_service.service.CardService;

@RestController
@RequestMapping("/card")
@Tag(name = "Card Controller", description = "Card controller to create, activate and block debit and credit cards")
public class CardController {

	CardService<?> cardService;

	public CardController(CardService<?> cardService) {
		this.cardService = cardService;
	}

	@GetMapping("/{productId}/number")
	@Operation(summary = "Create a card", description = "Creates both a debit card (102030) or a credit card(152535) and return the cardId")
	public ResponseEntity<Map<String, String>> createCard(@PathVariable int productId) {
		Map<String, String> responseObject = new HashMap<>();
		String cardId = cardService.createCard(productId);
		if (cardId.equals("0")) {
			responseObject.put("error", "Is not a valid productId.");
			return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
		}
		responseObject.put("cardId", cardId);
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}

	@PostMapping("enroll")
	@Operation(summary = "Activate a card", description = "Activates an inactive card.")
	public ResponseEntity<Map<String, String>> enrollCard(@RequestBody CardNumberDto cardNumberDto) {
		return cardService.enrollCard(cardNumberDto.getCardId());	
	}
	
	@DeleteMapping("/{cardId}")
	@Operation(summary = "Block a card", description = "Block an active card.")
	public ResponseEntity<Map<String, String>> blockCard(@PathVariable String cardId) {
		return cardService.blockCard(cardId);	
	}
	
}
