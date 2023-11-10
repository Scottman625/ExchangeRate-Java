package com.example.ExchangeRateDemo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ExchangeRateApiClient {

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateApiClient.class);

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String,Object> getLatestExchangeRate() {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl("https://openapi.taifex.com.tw/v1/DailyForeignExchangeRates")
                .queryParam("currencyPair", "{currencyPair}")
                .encode()
                .toUriString();

        logger.info("urlTemplate: " + urlTemplate);
//
//        Map<String, String> params = new HashMap<>();
//        params.put("currencyPair", currencyPair);

        // 調用API
        URI url = UriComponentsBuilder.fromUriString(urlTemplate).build().toUri();





        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());

            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);


//        List<ExchangeRate> exchangeRateList = new ArrayList<>();
//            ExchangeRate exchangeRate = new ExchangeRate();
//            exchangeRate.setCurrencyPair(currencyPair);
            if (response.getStatusCode().is2xxSuccessful()) {
                // Assuming the latest rate is the first element in the array
                byte[] bytes = response.getBody();
                Resource resource = new ByteArrayResource(bytes);
                String jsonString = new String(bytes, StandardCharsets.UTF_8); // 假设编码为UTF-8


                ObjectMapper mapper = new ObjectMapper();

                try {
                    List<Map<String, Object>> list = mapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>(){});
                    System.out.println(list.getClass().getName()); // 输出转换后的列表
                     Map<String,Object> latestRates = list.get(list.size()-1);
//                    BigDecimal rate = new BigDecimal((String) latestRates.get(currencyPair)); // Correct way to convert String to BigDecimal
//
//                    exchangeRate.setRate((rate));
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//                    exchangeRate.setLastUpdated(formatter.parse((String) latestRates.get("Date")));
//                    exchangeRateRepository.save(exchangeRate);
                        return latestRates;
                } catch (Exception e) {
                    e.printStackTrace(); // 處理潛在的解析錯誤
                }

            }
            return null;
    }
}

