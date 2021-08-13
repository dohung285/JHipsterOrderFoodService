package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PieChartObjectResponseDto {

    private String name;
    private BigInteger number;

    public PieChartObjectResponseDto() {}

    public PieChartObjectResponseDto(String name) {
        this.name = name;
    }

    public PieChartObjectResponseDto(BigInteger number) {
        this.number = number;
    }

    public PieChartObjectResponseDto(String name, BigInteger number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getNumber() {
        return number;
    }

    public void setNumber(BigInteger number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "PieChartObjectResponseDto{" + "name='" + name + '\'' + ", number=" + number + '}';
    }
}
