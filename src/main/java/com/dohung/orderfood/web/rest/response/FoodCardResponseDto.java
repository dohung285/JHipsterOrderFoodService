package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;

public class FoodCardResponseDto {

    private Integer cardId;

    private Integer foodId;

    private String name;

    private BigDecimal price;

    private Integer amount;

    private String imagePath;

    private Integer percent;

    public FoodCardResponseDto() {}

    public FoodCardResponseDto(
        Integer cardId,
        Integer foodId,
        String name,
        BigDecimal price,
        Integer amount,
        String imagePath,
        Integer percent
    ) {
        this.cardId = cardId;
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.imagePath = imagePath;
        this.percent = percent;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return (
            "FoodCardResponseDto{" +
            "cardId=" +
            cardId +
            ", foodId=" +
            foodId +
            ", name='" +
            name +
            '\'' +
            ", price=" +
            price +
            ", amount=" +
            amount +
            ", imagePath='" +
            imagePath +
            '\'' +
            ", percent=" +
            percent +
            '}'
        );
    }
}
