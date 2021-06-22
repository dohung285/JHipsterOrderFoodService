package com.dohung.orderfood.web.rest.response;

public class CardResponseDto {

    private Integer id;

    private Integer foodId;

    private Integer amount;

    private String username;

    public CardResponseDto() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
        return "CardResponseDto{" + "id=" + id + ", foodId=" + foodId + ", amount=" + amount + ", username='" + username + '\'' + '}';
    }
}
