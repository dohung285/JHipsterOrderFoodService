package com.dohung.orderfood.web.rest.request;

import java.math.BigDecimal;
import java.util.List;

public class FoodRequestModel {

    private String name;

    private BigDecimal price;

    private Integer groupId;

    private Integer imageId;

    //    private Integer discountId;

    private String desciption;

    private List<ObjectFoodDetail> foodDetails;

    public FoodRequestModel() {}

    public List<ObjectFoodDetail> getFoodDetails() {
        return foodDetails;
    }

    public void setFoodDetails(List<ObjectFoodDetail> foodDetails) {
        this.foodDetails = foodDetails;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
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

    //    public Integer getDiscountId() {
    //        return discountId;
    //    }
    //
    //    public void setDiscountId(Integer discountId) {
    //        this.discountId = discountId;
    //    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    @Override
    public String toString() {
        return (
            "FoodRequestModel{" +
            "name='" +
            name +
            '\'' +
            ", price=" +
            price +
            ", groupId=" +
            groupId +
            ", desciption='" +
            desciption +
            '\'' +
            '}'
        );
    }
}
