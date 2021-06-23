package com.dohung.orderfood.web.rest.request;

public class CreateDiscountFood {

    private Integer discountId;

    public CreateDiscountFood() {}

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    @Override
    public String toString() {
        return "CreateDiscountFood{" + "discountId=" + discountId + '}';
    }
}
