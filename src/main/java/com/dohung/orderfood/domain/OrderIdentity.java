package com.dohung.orderfood.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class OrderIdentity implements Serializable {

    private Integer foodId;
    private Integer orderId;

    public OrderIdentity() {}

    public OrderIdentity(Integer foodId, Integer orderId) {
        this.foodId = foodId;
        this.orderId = orderId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderIdentity that = (OrderIdentity) o;
        return Objects.equals(foodId, that.foodId) && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodId, orderId);
    }

    @Override
    public String toString() {
        return "OrderIdentity{" + "foodId=" + foodId + ", orderId=" + orderId + '}';
    }
}
