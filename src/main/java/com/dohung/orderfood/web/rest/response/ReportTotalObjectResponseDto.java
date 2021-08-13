package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ReportTotalObjectResponseDto {

    private Integer id;
    private String name;
    private BigInteger month;
    private BigDecimal total;

    public ReportTotalObjectResponseDto() {}

    public ReportTotalObjectResponseDto(Integer id, String name, BigInteger month, BigDecimal total) {
        this.id = id;
        this.name = name;
        this.month = month;
        this.total = total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getMonth() {
        return month;
    }

    public void setMonth(BigInteger month) {
        this.month = month;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ReportTotalObjectResponseDto{" + "id=" + id + ", name='" + name + '\'' + ", month=" + month + ", total=" + total + '}';
    }
}
