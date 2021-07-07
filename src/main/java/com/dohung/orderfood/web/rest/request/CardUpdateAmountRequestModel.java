package com.dohung.orderfood.web.rest.request;

public class CardUpdateAmountRequestModel {

    private Integer amount;

    public CardUpdateAmountRequestModel() {}

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CardUpdateAmountRequestModel{" + "amount=" + amount + '}';
    }
}
