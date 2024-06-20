package com.bankinc.card_service.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.bankinc.card_service.dto.BalanceRechargeDto;
import com.bankinc.card_service.dto.CardNumberDto;

public interface CardBalanceService<T> {

	ResponseEntity<Map<String, String>> balanceRecharge(BalanceRechargeDto balanceRechargeDto);

	ResponseEntity<Map<String, String>> getCardBalance(String cardId);
}
