package com.bankinc.card_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CardNumberDto {
	@JsonProperty("cardId")
	private String cardId;
}
