package com.bankinc.card_service.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
public class Card {
	@Id
	private String cardId;
	private String ownerFirstName;
	private String ownerLastName;
	private LocalDate expirationDate;
	private double balance;
	@Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ACTIVE', 'INACTIVE', 'BLOCKED', 'EXPIRED')")
	private CardStatus status;
}
