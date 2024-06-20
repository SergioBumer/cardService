package com.bankinc.card_service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransactionAnulationDto {
	private String cardId;
	private String transactionId;

}
