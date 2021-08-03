package com.dohung.orderfood.web.rest.request;

import java.util.List;

public class CreateDiscountFood {

    private Integer discountId;

    private List<Integer> foodIds;

    public CreateDiscountFood() {}

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public List<Integer> getFoodIds() {
        return foodIds;
    }

    public void setFoodIds(List<Integer> foodIds) {
        this.foodIds = foodIds;
    }

    @Override
    public String toString() {
        return "CreateDiscountFood{" + "discountId=" + discountId + ", foodIds=" + foodIds + '}';
    }
}
