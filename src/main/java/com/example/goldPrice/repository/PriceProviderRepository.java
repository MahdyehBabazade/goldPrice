package com.example.goldPrice.repository;

import com.example.goldPrice.model.PriceProviders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceProviderRepository extends JpaRepository<PriceProviders, Long> {
    Optional<PriceProviders> findByName(String name);
}
