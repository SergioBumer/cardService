package com.bankinc.card_service.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
public class Card {
	@Id
	@Getter
	private String cardId;
	private String ownerFirstName;
	private String ownerLastName;
	@Getter
	private LocalDateTime expirationDate;
	@Getter
	private double balance;
	@Getter
	@Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ACTIVE', 'INACTIVE', 'BLOCKED', 'EXPIRED')")
	private CardStatus status;
}
