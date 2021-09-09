package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;

public class ObjectChildOrderDetailResponse {

    private Integer orderId;
    private Integer foodId;
    private String foodName;
    private Integer amount;
    private BigDecimal price;
    private String image;

    public ObjectChildOrderDetailResponse() {}

    public ObjectChildOrderDetailResponse(
        Integer orderId,
        Integer foodId,
        String foodName,
        Integer amount,
        BigDecimal price,
        String image
    ) {
        this.orderId = orderId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.amount = amount;
        this.price = price;
        this.image = image;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return (
            "ObjectChildOrderDetailResponse{" +
            "orderId=" +
            orderId +
            ", foodId=" +
            foodId +
            ", foodName='" +
            foodName +
            '\'' +
            ", amount=" +
            amount +
            ", price=" +
            price +
            ", image='" +
            image +
            '\'' +
            '}'
        );
    }
}
