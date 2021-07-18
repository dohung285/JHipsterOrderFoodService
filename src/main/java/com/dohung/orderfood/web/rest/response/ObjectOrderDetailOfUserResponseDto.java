package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;

public class ObjectOrderDetailOfUserResponseDto {

    private Integer orderId;
    private Integer foodId;
    private String foodName;
    private Integer amount;
    private BigDecimal money;
    private String pathImage;

    public ObjectOrderDetailOfUserResponseDto() {}

    public ObjectOrderDetailOfUserResponseDto(
        Integer orderId,
        Integer foodId,
        String foodName,
        Integer amount,
        BigDecimal money,
        String pathImage
    ) {
        this.orderId = orderId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.amount = amount;
        this.money = money;
        this.pathImage = pathImage;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
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

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    @Override
    public String toString() {
        return (
            "ObjectOrderDetailOfUserResponseDto{" +
            "orderId=" +
            orderId +
            ", foodId=" +
            foodId +
            ", foodName='" +
            foodName +
            '\'' +
            ", amount=" +
            amount +
            ", money=" +
            money +
            ", pathImage='" +
            pathImage +
            '\'' +
            '}'
        );
    }
}
