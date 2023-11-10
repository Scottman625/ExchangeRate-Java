package com.example.ExchangeRateDemo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    // Method to find ExchangeRate by currencyPair
    Optional<List<ExchangeRate>> findAllByCurrencyPair(String currencyPair);

    Optional<ExchangeRate> findById(Long id);
}

