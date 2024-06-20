package com.bankinc.card_service.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@Getter
@AllArgsConstructor
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String transactionId;
	private String cardId;
	private double price;
	private LocalDate purchaseDate;
	@Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ACTIVE', 'CANCELED')")
	private TransactionStatus transactionStatus;
	
	public Transaction(){}

	
	
	
}
