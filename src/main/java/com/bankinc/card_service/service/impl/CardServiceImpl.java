package com.bankinc.card_service.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankinc.card_service.models.Card;
import com.bankinc.card_service.models.CardStatus;
import com.bankinc.card_service.repository.CardRepository;
import com.bankinc.card_service.service.CardService;

@Service
public class CardServiceImpl implements CardService {
    private final int DEBIT_CARD_ID = 102030;
    private final int CREDIT_CARD_ID = 152535;

    CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public String createCard(int productId) {
        if (!(productId == DEBIT_CARD_ID || productId == CREDIT_CARD_ID)) {
            return "0";
        }

        String cardId = generateCardId(productId);

        while (cardRepository.findById(cardId).isPresent()) {
            cardId = generateCardId(productId);
        }

        LocalDateTime newDate = LocalDateTime.now().plusYears(3);
        Card card = new Card();
        card.setCardId(cardId);
        card.setOwnerFirstName("");
        card.setOwnerLastName("");
        card.setStatus(CardStatus.INACTIVE);
        card.setBalance(0d);
        card.setExpirationDate(newDate);

        cardRepository.save(card);
        return cardId;
    }

    private String generateCardId(int productId) {
        Random random = new Random();
        long randomTenDigitNumber = (long) (1000000000L + random.nextDouble() * 9000000000L);
        return productId + String.valueOf(randomTenDigitNumber);

    }

    @Override
    public ResponseEntity<Map<String, String>> enrollCard(String cardId) {
        ResponseEntity<Map<String, String>> response;
        Map<String, String> responseBody = new HashMap();
        Card card = cardRepository.findById(cardId).orElse(null);

        if (card == null) {
            responseBody.put("error", "This card doesn't exists.");
            response = new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        } else if (!card.getStatus().equals(CardStatus.INACTIVE)) {
            responseBody.put("error", "This card can not be activated.");
            response = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        } else {
            card.setStatus(CardStatus.ACTIVE);
            response = new ResponseEntity<>(HttpStatus.OK);
            cardRepository.save(card);
        }

        return response;
    }

    @Override
    public ResponseEntity<Map<String, String>> blockCard(String cardId) {
        // TODO Auto-generated method stub
        ResponseEntity<Map<String, String>> response;
        Map<String, String> responseBody = new HashMap();
        Card card = cardRepository.findById(cardId).orElse(null);

        if (card == null) {
            responseBody.put("error", "This card doesn't exists.");
            response = new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        } else if (CardStatus.BLOCKED.equals(card.getStatus())) {
            responseBody.put("error", "This card is already blocked.");
            response = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        } else {
            card.setStatus(CardStatus.BLOCKED);
            response = new ResponseEntity<>(HttpStatus.OK);
            cardRepository.save(card);
        }

        return response;
    }

}
