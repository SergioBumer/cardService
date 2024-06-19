package com.bankinc.card_service.models;

import java.time.LocalDate;
import java.util.Date;

import com.bankinc.card_service.CardStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Setter
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
