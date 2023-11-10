package com.example.ExchangeRateDemo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class ExchangeRateRestfulController {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateController.class);

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ExchangeRateApiClient exchangeRateApiClient;


    @PostMapping("/api/postRate")
    public ExchangeRate postExchangeRate(
            @RequestBody String currencyPair
                                       ) throws ParseException {
        Map<String, Object> exchangeRate = exchangeRateApiClient.getLatestExchangeRate();
        return exchangeRateService.save(exchangeRate, currencyPair);
    }

    // RESTful API 處裡更新匯率數據的請求
    @PutMapping("/api/rates/{id}")
    @ResponseBody
    public ExchangeRate updateExchangeRate(@PathVariable Long id, @RequestBody ExchangeRate exchangeRate) {
        return exchangeRateService.update(id, exchangeRate);
    }

    // RESTful API 處理删除匯率數據的请求
    @DeleteMapping("/api/rates/{id}")
    @ResponseBody
    public void deleteExchangeRate(@PathVariable Long id) {
        exchangeRateService.delete(id);
    }

    // RESTful API 處裡查詢所有匯率的請求
    @GetMapping("/api/rates")
    @ResponseBody
    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateService.findAll();
    }

    // RESTful API 處裡根據貨幣對查詢匯率數據的請求
    @GetMapping("/api/rates/{fromCurrency}/to/{toCurrency}")
    @ResponseBody
    public List<ExchangeRate> getExchangeRateByCurrencyPair(
            @PathVariable String fromCurrency,
            @PathVariable String toCurrency
    ) {
        String currencyPair = fromCurrency + "/" + toCurrency;
        return exchangeRateService.findByCurrencyPair(currencyPair)
                .orElseThrow(() -> new IllegalArgumentException("Invalid currency pair: " + currencyPair));
    }
}
