package com.bankinc.card_service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseDTO {
	private String cardId;
	private double price;
}
