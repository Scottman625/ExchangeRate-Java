package com.example.ExchangeRateDemo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRates {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private Date date;
    private BigDecimal USD_NTD;
    private BigDecimal RMB_NTD;
    private BigDecimal USD_RMB;
    // ... other fields

    // Getters and setters for all fields

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getUSD_NTD() {
        return USD_NTD;
    }

    public void setUSD_NTD(BigDecimal USD_NTD) {
        this.USD_NTD = USD_NTD;
    }

    public BigDecimal getRMB_NTD() {
        return RMB_NTD;
    }

    public void setRMB_NTD(BigDecimal RMB_NTD) {
        this.RMB_NTD = RMB_NTD;
    }

    public BigDecimal getUSD_RMB() {
        return USD_RMB;
    }

    public void setUSD_RMB(BigDecimal USD_RMB) {
        this.USD_RMB = USD_RMB;
    }
}

