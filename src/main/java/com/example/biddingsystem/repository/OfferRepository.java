package com.example.biddingsystem.repository;

import com.example.biddingsystem.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    Offer findTopByItemIdOrderByAmountDesc(Long itemId);
}
