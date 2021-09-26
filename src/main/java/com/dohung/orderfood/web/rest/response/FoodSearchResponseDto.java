package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;

public class FoodSearchResponseDto {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String description;
    private Integer discountId;
    private Integer isDeleted;
    private Integer percent;
    private String path;

    public FoodSearchResponseDto() {}

    public FoodSearchResponseDto(
        Integer id,
        String name,
        BigDecimal price,
        String description,
        Integer discountId,
        Integer isDeleted,
        Integer percent,
        String path
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.discountId = discountId;
        this.isDeleted = isDeleted;
        this.percent = percent;
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return (
            "FoodSearchResponseDto{" +
            "id=" +
            id +
            ", name='" +
            name +
            '\'' +
            ", price=" +
            price +
            ", description='" +
            description +
            '\'' +
            ", discountId=" +
            discountId +
            ", isDeleted=" +
            isDeleted +
            ", path='" +
            path +
            '\'' +
            '}'
        );
    }
}
