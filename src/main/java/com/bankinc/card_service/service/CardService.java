package com.bankinc.card_service.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.bankinc.card_service.dto.CardNumberDto;

public interface CardService<T> {

	String createCard(int productId);

	ResponseEntity<Map<String, String>> enrollCard(String cardId);

	ResponseEntity<Map<String, String>> blockCard(String cardId);
}
