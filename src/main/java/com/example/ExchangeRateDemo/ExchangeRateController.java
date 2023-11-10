package com.example.ExchangeRateDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
public class ExchangeRateController {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateController.class);

    private final ExchangeRateService exchangeRateService;

    @Autowired
    private ExchangeRateApiClient exchangeRateApiClient;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/")
    public String showRates(Model model) {
        Map<String, Object> latestExchangeRate = exchangeRateApiClient.getLatestExchangeRate();
        model.addAttribute("exchangeRates", exchangeRateService.findAll());
        model.addAttribute("USD_NTD_Rate",latestExchangeRate.get("USD/NTD"));
        model.addAttribute("RMB_NTD_Rate",latestExchangeRate.get("RMB/NTD"));
        model.addAttribute("USD_RMB_Rate",latestExchangeRate.get("USD/RMB"));
        return "rates";
    }


}

