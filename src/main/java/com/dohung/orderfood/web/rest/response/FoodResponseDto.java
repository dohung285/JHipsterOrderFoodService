package com.dohung.orderfood.web.rest.response;

import com.dohung.orderfood.domain.FoodDetail;
import com.dohung.orderfood.web.rest.request.ObjectFoodDetail;
import java.math.BigDecimal;
import java.util.List;

public class FoodResponseDto {

    private Integer id;

    private String name;

    private BigDecimal price;

    private Integer groupId;

    private Integer imageId;

    private Integer discountId;

    private String desciption;

    private List<FoodDetailResponseDto> foodDetails;

    public FoodResponseDto() {}

    public List<FoodDetailResponseDto> getFoodDetails() {
        return foodDetails;
    }

    public void setFoodDetails(List<FoodDetailResponseDto> foodDetails) {
        this.foodDetails = foodDetails;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    @Override
    public String toString() {
        return (
            "FoodResponseDto{" +
            "id=" +
            id +
            ", name='" +
            name +
            '\'' +
            ", price=" +
            price +
            ", groupId=" +
            groupId +
            ", discountId=" +
            discountId +
            ", desciption='" +
            desciption +
            '\'' +
            '}'
        );
    }
}
