package com.example.goldPrice.repository;

import com.example.goldPrice.model.Prices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Prices, Long> {

}
