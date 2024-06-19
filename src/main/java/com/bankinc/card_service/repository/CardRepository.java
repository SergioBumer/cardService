package com.bankinc.card_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankinc.card_service.models.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, String>{

}
