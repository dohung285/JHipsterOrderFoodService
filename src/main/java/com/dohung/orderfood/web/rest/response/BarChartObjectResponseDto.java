package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;

public class BarChartObjectResponseDto {

    private BigDecimal total;
    private Integer month;

    public BarChartObjectResponseDto(BigDecimal total, Integer month) {
        this.total = total;
        this.month = month;
    }

    public BarChartObjectResponseDto() {}

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "BarChartObjectResponseDto{" + "total=" + total + ", month=" + month + '}';
    }
}
