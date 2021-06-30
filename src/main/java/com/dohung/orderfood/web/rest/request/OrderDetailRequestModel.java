package com.dohung.orderfood.web.rest.request;

import java.math.BigDecimal;

public class OrderDetailRequestModel {

    private Integer amount;

    private BigDecimal money;

    public OrderDetailRequestModel() {}

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "OrderDetailRequestModel{" + "amount=" + amount + ", money=" + money + '}';
    }
}
