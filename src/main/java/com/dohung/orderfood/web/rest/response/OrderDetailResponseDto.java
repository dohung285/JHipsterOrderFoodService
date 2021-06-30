package com.dohung.orderfood.web.rest.response;

import com.dohung.orderfood.domain.OrderIdentity;
import java.math.BigDecimal;

public class OrderDetailResponseDto {

    private OrderIdentity id;

    private Integer amount;

    private BigDecimal money;

    public OrderDetailResponseDto() {}

    public OrderIdentity getId() {
        return id;
    }

    public void setId(OrderIdentity id) {
        this.id = id;
    }

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
        return "OrderDetailResponseDto{" + "id=" + id + ", amount=" + amount + ", money=" + money + '}';
    }
}
