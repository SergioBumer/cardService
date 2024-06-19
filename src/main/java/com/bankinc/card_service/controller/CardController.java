package com.bankinc.card_service.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankinc.card_service.models.Card;
import com.bankinc.card_service.service.CardService;

@RestController
@RequestMapping("/card")
public class CardController {

	CardService cardService;

	public CardController(CardService cardService) {
		this.cardService = cardService;
	}

	@GetMapping("/{productId}/number")
	public ResponseEntity<Map<String, String>> createCard(@PathVariable int productId) {
		Map<String, String> responseObject = new HashMap<String, String>();
		String cardId = cardService.createCard(productId);
		if (cardId.equals("0")) {
			responseObject.put("error", "Is not a valid productId.");
			return new ResponseEntity<Map<String, String>>(responseObject, HttpStatus.BAD_REQUEST);
		}
		responseObject.put("cardId", cardId);
		return new ResponseEntity<Map<String, String>>(responseObject, HttpStatus.OK);
	}
}
