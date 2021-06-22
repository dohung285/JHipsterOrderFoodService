package com.dohung.orderfood.web.rest.request;

public class CardRequestModel {

    private Integer foodId;

    private Integer amount;

    private String username;

    public CardRequestModel() {}

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "CardRequestModel{" + "foodId=" + foodId + ", amount=" + amount + ", username='" + username + '\'' + '}';
    }
}
