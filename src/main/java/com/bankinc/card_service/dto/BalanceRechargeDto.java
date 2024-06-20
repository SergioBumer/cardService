package com.bankinc.card_service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BalanceRechargeDto {
	private String cardId;
	private double balance; 
}
