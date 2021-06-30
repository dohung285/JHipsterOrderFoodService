package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;

public class ObjectOrderDetail {

    private Integer foodId;
    private Integer amount;
    private BigDecimal money;

    public ObjectOrderDetail(Integer foodId, Integer amount, BigDecimal money) {
        this.foodId = foodId;
        this.amount = amount;
        this.money = money;
    }

    public ObjectOrderDetail() {}

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
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
        return "ObjectOrderDetail{" + "foodId=" + foodId + ", amount=" + amount + ", money=" + money + '}';
    }
}
