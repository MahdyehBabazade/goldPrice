package com.example.goldPrice.repository;

import com.example.goldPrice.model.TalaseaPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TalaseaRepository extends JpaRepository<TalaseaPrice, Long> {

}
