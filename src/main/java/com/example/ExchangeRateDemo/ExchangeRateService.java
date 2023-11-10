package com.example.ExchangeRateDemo;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public List<ExchangeRate> findAll() {

        return exchangeRateRepository.findAll();
    }

    public Optional<ExchangeRate> findById(Long id) {
        return exchangeRateRepository.findById(id);
    }
    public Optional<List<ExchangeRate>> findByCurrencyPair(String currencyPair) {
        return exchangeRateRepository.findAllByCurrencyPair(currencyPair);
    }

    public ExchangeRate save(Map<String,Object> exchangeRate,String currencyPair) throws ParseException {
        ExchangeRate newExchangeRate = new ExchangeRate();
        newExchangeRate.setCurrencyPair(currencyPair);
        BigDecimal rate = new BigDecimal((String) exchangeRate.get(currencyPair)); // Correct way to convert String to BigDecimal
        newExchangeRate.setRate((rate));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        newExchangeRate.setLastUpdated(formatter.parse((String) exchangeRate.get("Date")));
        return exchangeRateRepository.save(newExchangeRate);
    }
    public ExchangeRate update(Long id, ExchangeRate newExchangeRateData) {
        return exchangeRateRepository.findById(id).map(exchangeRate -> {
            exchangeRate.setCurrencyPair(newExchangeRateData.getCurrencyPair());
            exchangeRate.setRate(newExchangeRateData.getRate());
            exchangeRate.setLastUpdated(newExchangeRateData.getLastUpdated());
            return exchangeRateRepository.save(exchangeRate);
        }).orElseGet(() -> {
            newExchangeRateData.setId(id);
            return exchangeRateRepository.save(newExchangeRateData);
        });
    }

    public void delete(Long id) {
        exchangeRateRepository.deleteById(id);
    }
}